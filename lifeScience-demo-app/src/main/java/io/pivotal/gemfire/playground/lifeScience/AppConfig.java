package io.pivotal.gemfire.playground.lifeScience;

import java.util.Collections;
import java.util.Properties;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.data.gemfire.listener.ContinuousQueryDefinition;
import org.springframework.data.gemfire.listener.ContinuousQueryListenerContainer;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import io.pivotal.gemfire.playground.lifeScience.listener.PatientVisitListener;

@EnableAutoConfiguration
@ClientCacheApplication(name = "DataGemFireApplication", logLevel = "error")
@EnableGemfireRepositories
@EnablePdx(serializerBeanName = "pdxSerializer")
@EnableEntityDefinedRegions(basePackages = "io.pivotal.gemfire.playground.orders.domain")
@EnableSecurity
@Configuration
public class AppConfig
{	
	@Bean
	public Properties gemfireProperties() {

		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", "test");
		gemfireProperties.setProperty("mcast-port", "0");

		return gemfireProperties;
	}
	
	/**
	 * 
	 * @param gemfireProperties the GemFire properties (see https://gemfire.docs.pivotal.io/geode/reference/topics/gemfire_properties.html)
	 * @return Client cache
	 * @throws Exception
	 */
	@Bean
	@Autowired
	public ClientCache gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties)
			throws Exception {
		
		//"io.pivotal.gemfire.playground.lifeScience.domain.*"

		ClientCache gemfireCache = new ClientCacheFactory(gemfireProperties)
		.setPoolSubscriptionEnabled(true)
		.addPoolLocator("localhost", 10000)
		.setPdxSerializer(
		new ReflectionBasedAutoSerializer(".*"))
		.create();
		
		return gemfireCache;
	}//------------------------------------------------
	
	@Bean
	@Autowired
	public ClientRegionFactory<?, ?> proxyClientRegionFactory(@Qualifier("gemfireCache") 
	ClientCache gemfireCache)
	{
		return gemfireCache.createClientRegionFactory(ClientRegionShortcut.PROXY);
		
	}
	
	
	@Bean
	@Autowired
	public GemfireTemplate patientVisitTemplate(@Qualifier("PatientVisit") Region<?,?> patientVisitRegion)
	{
		return new GemfireTemplate(patientVisitRegion);
	}

	@Bean
	@Autowired
	public PatientVisitListener patientistener()
	{
		return new PatientVisitListener();
	}

	@Bean
	@Autowired
	public ContinuousQueryListenerContainer patientVisitListenerContainer(PatientVisitListener patientVisitListener, ClientCache gemfireCache)
	{
		ContinuousQueryListenerContainer container = new ContinuousQueryListenerContainer();
		container.setCache(gemfireCache);

		String query  = "SELECT * FROM /PatientVisit where status = 'COMPLETE'";
		ContinuousQueryDefinition cqd = new ContinuousQueryDefinition(query,patientVisitListener);
		
		container.setQueryListeners(Collections.singleton(cqd));
		
		return container;
	}
}
