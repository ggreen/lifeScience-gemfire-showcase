package spring.gemfire.showcase.account;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import showcase.life.science.gemfire.pharmacy.pricing.domains.*;
import spring.gemfire.showcase.account.function.GetPricing;

import java.util.List;
import java.util.Set;

@ClientCacheApplication(subscriptionEnabled = true)
//@EnableSecurity
@Configuration
@EnablePdx(serializerBeanName = "serializer")
@EnableGemfireRepositories
@EnableGemfireFunctionExecutions(basePackageClasses = GetPricing.class)
public class GemFireConf
{
    @Bean("serializer")
    ReflectionBasedAutoSerializer serializer()
    {
        return new ReflectionBasedAutoSerializer(".*");
    }
    @Bean("DrugPricingInfo")
            ClientRegionFactoryBean<String, DrugPricingInfo> drugPricingInfo (ClientCache gemFireCache)
    {
        var factory = new ClientRegionFactoryBean<String, DrugPricingInfo>();
        factory.setCache(gemFireCache);
        factory.setDataPolicy(DataPolicy.EMPTY);
        return factory;
    }

    @Bean("PlanPricingRule")
    ClientRegionFactoryBean<String, PlanPricingRule> planPricingRule (ClientCache gemFireCache)
    {
        var factory = new ClientRegionFactoryBean<String, PlanPricingRule>();
        factory.setCache(gemFireCache);
        factory.setDataPolicy(DataPolicy.EMPTY);
        return factory;
    }

    //
    @Bean("AffordableEvent")
    ClientRegionFactoryBean<String, AffordableEvent> affordableEvent (ClientCache gemFireCache)
    {
        var factory = new ClientRegionFactoryBean<String, AffordableEvent>();
        factory.setCache(gemFireCache);
        factory.setDataPolicy(DataPolicy.EMPTY);
        return factory;
    }

    @Bean
    GetPricing getPricing()
    {
        return request -> {

            ResultCollector<PricingRequest, List<PricingResult>> results = FunctionService.onRegion(ClientCacheFactory.getAnyInstance().getRegion("DrugPricingInfo"))
                    .setArguments(request)
                    .withFilter(Set.of(request.getNdc()))
                    .execute("getPricing");

            var responses = results.getResult();

            if(responses == null || responses.isEmpty())
                return null;

            return results.getResult().iterator().next();
        };
    }
}
