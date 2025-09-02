package spring.gemfire.showcase.account;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.client.ClientCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import showcase.life.science.gemfire.pharmacy.pricing.domains.DrugPricingInfo;
import showcase.life.science.gemfire.pharmacy.pricing.domains.PlanPricingRule;

@ClientCacheApplication
//@EnableSecurity
@Configuration
@EnablePdx
@EnableGemfireRepositories
public class GemFireConf
{
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
}
