# Overview

The following project provides coding example for basic GemFire client API features, along with Spring Data GemFire in Spring Boot Applications.


	create lucene index --region=/PatientVisit --name=notesIndex --field=notes 

	create region --name=PatientVisit --type=PARTITION


http://localhost:8080/create

{
   "id": "test01",
   "visitName": "Annual-checkup",
  "location": "Hospital",
  "status": "STARTED"
}

query --query="select * from /PatientVisit"

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

query --query="select id, patient from /PatientVisit"
query --query="select id, patient from /PatientVisit" order by id

** Update Records

http://localhost:8080/savePatientVisit

{
   "id": "test01",
   "visitName": "Annual-checkup",
  "location": "Hospital",
  "status": "IN-PROGRESS"
}

query --query="select * from /PatientVisit"


**Application Based Queries**

http://localhost:8080/select

select * from /PatientVisit where status != 'IN-PROGRESS'

**Events/Continous Queries**

http://localhost:8080/savePatientVisit

{
   "id": "test01",
   "visitName": "Annual-checkup",
  "location": "Hospital",
  "status": "COMPLETE"
}


http://localhost:8080/create

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

search lucene --name=notesIndex --region=/PatientVisit --queryString="pivotal" --defaultField=notes



