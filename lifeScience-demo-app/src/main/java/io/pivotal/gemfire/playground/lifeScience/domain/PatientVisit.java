package io.pivotal.gemfire.playground.lifeScience.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region("PatientVisit")
public class PatientVisit
{
	@Id
	private String id;
	
	private String createUserId;
	private String updateUserId;
	private String visitName;
	private String status;
	private String location;
	private String notes;
	
	private Patient patient;

	
	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the createUserId
	 */
	public String getCreateUserId()
	{
		return createUserId;
	}
	/**
	 * @return the updateUserId
	 */
	public String getUpdateUserId()
	{
		return updateUserId;
	}
	
	
	/**
	 * @param createUserId the createUserId to set
	 */
	public void setCreateUserId(String createUserId)
	{
		this.createUserId = createUserId;
	}

	/**
	 * @param updateUserId the updateUserId to set
	 */
	public void setUpdateUserId(String updateUserId)
	{
		this.updateUserId = updateUserId;
	}
	
	/**
	 * @return the patientId
	 */
	public String getPatientId()
	{
		if(this.patient == null)
			return null;
		
		return this.patient.getId();
	}
	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(String patientId)
	{
		if(this.patient == null)
			this.patient = new Patient();
		
		this.patient.setId(patientId);
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * @return the location
	 */
	public String getLocation()
	{
		return location;
	}
	/**
	 * @return the patient
	 */
	public Patient getPatient()
	{
		return patient;
	}
	/**
	 * @return the notes
	 */
	public String getNotes()
	{
		return notes;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location)
	{
		this.location = location;
	}
	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient)
	{
		this.patient = patient;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes)
	{
		this.notes = notes;
	}
	/**
	 * @return the visitName
	 */
	public String getVisitName()
	{
		return visitName;
	}
	/**
	 * @param visitName the visitName to set
	 */
	public void setVisitName(String visitName)
	{
		this.visitName = visitName;
	}
}
