package io.pivotal.gemfire.playground.lifeScience.domain;

public class Patient  
{
	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	private String id;
	private String name;
}
