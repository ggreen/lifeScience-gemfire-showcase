package io.pivotal.gemfire.playground.lifeScience.dao;

import java.util.List;

import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.stereotype.Service;

import io.pivotal.gemfire.playground.lifeScience.domain.PatientVisit;
@Service
public interface PatientVisitCrudRepository extends GemfireRepository<PatientVisit, String>
{
	List<PatientVisit> findByPatientId(int patientId);
}
