package io.pivotal.gemfire.playground.lifeScience.dao;

import java.util.List;

import org.springframework.data.gemfire.repository.GemfireRepository;

import io.pivotal.gemfire.playground.lifeScience.domain.PatientVisit;

public interface PatientVisitCrudRepository extends GemfireRepository<PatientVisit, String>
{
	List<PatientVisit> findByPatientId(int patientId);
}
