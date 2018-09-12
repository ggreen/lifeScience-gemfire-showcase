package io.pivotal.gemfire.playground.lifeScience;
import static org.junit.Assert.*;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.cache.client.ClientCache;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.gemfire.GemfireTemplate;
import gedi.solutions.geode.qa.GUnit;
import io.pivotal.gemfire.playground.lifeScience.AppConfig;
import io.pivotal.gemfire.playground.lifeScience.GemfireDemoApplication;
import io.pivotal.gemfire.playground.lifeScience.dao.PatientVisitCrudRepository;
import io.pivotal.gemfire.playground.lifeScience.domain.PatientVisit;
import nyla.solutions.core.patterns.jmx.JMX;


/**
 * <pre>
 * JUNIT test that does not us the Spring Run as annotations.
 * 
 * This will use the AnnotationConfigApplicationContext to 
 * manual create the Spring context.
 * 
 * </pre>
 * @author Gregory Green
 *
 */
public class GemfireAnnotationConfigApplicationContextTests {
	
	/**
	 * Spring Boot application that will be manually wired 
	 * and will not used the Spring JUNIT runner.
	 */
	static GemfireDemoApplication app;
	
	/**
	 * The manually created Spring Context
	 */
	static AnnotationConfigApplicationContext ctx;

	
	static GemfireTemplate t;
	/**
	 * <pre>
	 * This start using an simple classed that wraps 
	 * Gfsh and JMX calls to the GemFire cluster members.
	 * 
	 * 
	 * See https://github.com/nyla-solutions/gedi-geode/blob/master/gedi-geode-extensions-core/src/main/java/gedi/solutions/geode/qa/GUnit.java
	 * </pre>
	 * @throws Exception when unknown error occurs
	 */
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUp()
	throws Exception
	{
		//starts a locator(name="locator") and cache server(name="server") uses the default GemFire ports
		gunit.startCluster();
		
		/**
		 * Establish a JMX connection to the locator listening of the default JMX port.
		 */
		try(JMX jmx = JMX.connect("localhost",1099))
		{
			//this is a option step to demonstrate the ability to wait for a member to become available.
			gunit.waitForMemberStart("locator",jmx);
			gunit.waitForMemberStart("server",jmx);
		}
		
		//Create regions on server
		gunit.createRegion("PatientVisit", RegionShortcut.PARTITION);
		
		//Manually wire the application object
		ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		
		app = new GemfireDemoApplication();
		
		//Note these step will normally be done by the Spring JUNIT Runner
		app.patientVisitRepos = ctx.getBean(PatientVisitCrudRepository.class);
		app.patientVisitTemplate = ctx.getBean("patientVisitTemplate",GemfireTemplate.class);
		
		app.patientVisitRegion = ctx.getBean("PatientVisit",Region.class);
		
		app.patientVisitRepos = ctx.getBean(PatientVisitCrudRepository.class);
		
	}//------------------------------------------------
	/**
	 * Shutdown the cluster and close
	 * @throws Exception when unknown error occurs
	 */
	@AfterClass
	public static void shutdown()
	throws Exception
	{
		try{gunit.shutdown(); } catch(Exception e) {}
		
		try{ctx.close(); } catch(Exception e) {}
	}//------------------------------------------------
	/**
	 * Simple test to confirm the client cache is autowired.
	 */
	@Test
	public void contextLoads() {
		
		
		ClientCache cache = ctx.getBean("gemfireCache",ClientCache.class);
		assertNotNull(cache);
	}//------------------------------------------------
	/**
	 * Core test to use the CRUD repository and GemFire Template
	 * @throws Exception when an unknown error occurs
	 */
	@Test
	public void testSaveAcccount() throws Exception
	{
		PatientVisit patientVisit = new PatientVisit();
		patientVisit.setId("1");
		patientVisit.setVisitName("Testing name");
		
		//use the save account
		assertTrue(app.savePatientVisit(patientVisit));
		
		assertEquals(patientVisit,app.patientVisitTemplate.get(patientVisit.getId()));
		assertEquals(patientVisit,app.patientVisitRepos.findById(patientVisit.getId()).get());
	}//------------------------------------------------
	
	private static GUnit gunit = new GUnit();
}
