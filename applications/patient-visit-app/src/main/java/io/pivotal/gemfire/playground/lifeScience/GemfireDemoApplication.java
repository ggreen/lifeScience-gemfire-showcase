package io.pivotal.gemfire.playground.lifeScience;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import gedi.solutions.geode.io.Querier;
import io.pivotal.gemfire.playground.lifeScience.dao.PatientVisitCrudRepository;
import io.pivotal.gemfire.playground.lifeScience.domain.PatientVisit;

@Controller
@EnableAutoConfiguration
@SpringBootApplication
@ClientCacheApplication(name = "DataGemFireApplication", logLevel = "error")
@EnableGemfireRepositories
@EnablePdx(serializerBeanName = "pdxSerializer")
@EnableEntityDefinedRegions(basePackages = "io.pivotal.gemfire.playground.lifeScience.domain")
@EnableSecurity
public class GemfireDemoApplication {


	@Resource(name="PatientVisit")
	Region<String, PatientVisit> patientVisitRegion;
	
	@Resource
	GemfireTemplate patientVisitTemplate;
	
	
	@Autowired
	PatientVisitCrudRepository patientVisitRepos;
	
	
	// HTTP Mappings
	@GetMapping("/patientVisit/{id}")
    @ResponseBody
    public PatientVisit findById(@PathVariable String id) {
        return patientVisitRepos.findById(id).get();
    }
	
	@PostMapping("/savePatientVisit")
	@ResponseBody
	public boolean savePatientVisit(@RequestBody PatientVisit patientVisit)
	{
		this.patientVisitRepos.save(patientVisit);
		return true;
	}
	
	
	@GetMapping("/patientVisit/{patientId}")
    @ResponseBody
    public List<PatientVisit> findByPatientId(@PathVariable int patientId) {
        return patientVisitRepos.findByPatientId(patientId);
    }
	
	
	
	@PostMapping("/select")
	@ResponseBody
	public Collection<PatientVisit> executeQuery(@RequestBody String query)
	{
		return Querier.query(query);
	}//------------------------------------------------
	
	@PostMapping("/create")
	@ResponseBody
	public boolean create(@RequestBody PatientVisit patientVisit)
	{
		this.patientVisitTemplate.create(patientVisit.getId(), patientVisit);
		return true;
	}//------------------------------------------------
	
	
	
	@PostMapping("/batch")
	@ResponseBody
	public int save(@RequestBody ArrayList<PatientVisit> patientVisits)
	{
		this.patientVisitRepos.saveAll(patientVisits);
		
		
		return patientVisits.size();
	}//------------------------------------------------
	
	
	 @RequestMapping("/")
	    @ResponseBody
	    String home() {
	        return "Demo";
	    }
	 
	public static void main(String[] args) {
		SpringApplication.run(GemfireDemoApplication.class, args);
	}
}
