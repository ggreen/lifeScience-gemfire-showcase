# Overview

The following project provides coding example for basic GemFire client API features, along with Spring Data GemFire in Spring Boot Applications.


Connect thru gfsh

	gfsh>connect --locator=localhost[10000]
	
SetUp

	gfsh>create lucene index --region=/PatientVisit --name=notesIndex --field=notes 

	gfsh>create region --name=PatientVisit --type=PARTITION

Start Spring App

	java -jar target/lifeScience-demo-app-0.0.1-SNAPSHOT.jar
 
 **Create Data with REST endpoints**

	http://localhost:8080/create

	{
	   "id": "test01",
	   "visitName": "Annual-checkup",
	  "location": "Hospital",
	  "status": "STARTED"
	}

**Query in gfsh**

	gfsh>query --query="select * from /PatientVisit"

**Create Data with REST endpoints**
	
	http://localhost:8080/create

	{
	   "id": "test02",
	   "visitName": "Annual-checkup",
	  "location": "Hospital",
	  "status": "STARTED",
	  "patient": 
	   { 
	      "id" : "01",
	      "name" : "Nyla"
	   }
	}
**Query in gfsh**

	query --query="select id, patient from /PatientVisit"
	query --query="select id, patient from /PatientVisit order by id"

**Update Records**

	http://localhost:8080/savePatientVisit
	
	{
	   "id": "test01",
	   "visitName": "Annual-checkup",
	  "location": "Hospital",
	  "status": "IN-PROGRESS"
	}

**Query in gfsh**

	query --query="select * from /PatientVisit"


**Application Based Queries**

	http://localhost:8080/select

	POST Body
	
	select * from /PatientVisit where status != 'IN-PROGRESS'

**Events/Continuous Queries**

	POST http://localhost:8080/savePatientVisit
	
	{
	   "id": "test01",
	   "visitName": "Annual-checkup",
	  "location": "Hospital",
	  "status": "COMPLETE"
	}


	POST http://localhost:8080/create

	{
	   "id": "test03",
	   "visitName": "Payment",
	  "location": "Hospital",
	  "status": "COMPLETE"
	}


	{
	   "id": "test04",
	   "visitName": "Payment",
	  "location": "Hospital",
	  "status": "COMPLETE",
	  "notes" : "It was a beautiful day for a visit at pivotal"
	}

**Text based search**

	gfsh>search lucene --name=notesIndex --region=/PatientVisit --queryString="pivotal" --defaultField=notes


Pulse**

	http://localhost:17070/pulse





