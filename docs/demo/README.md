# Demo

## Pre-steps

- Start GemFire Console
- Start Promethesus


---

Start Cluster

```shell
./deployment/local/gemfire/start-multi-servers.sh
```

Deploy Get Pricing Function

```shell
$GEMFIRE_HOME/bin/gfsh -e "connect" -e "deploy --jar=$PWD/components/adjudication-pricing-function/target/adjudication-pricing-function-0.0.1-SNAPSHOT.jar" 
```

Start Service Spring App


```shell
java -jar applications/pharmacy-service/target/pharmacy-service-0.0.1-SNAPSHOT.jar
```

Open Spring Apps

```shell
open http://localhost:6001
```

Get Pricing - empty results

```properties
ndc=0001
planId=Plan-A
```

```shell
curl -X 'GET' \
  'http://localhost:6001/pharmacy/pricing/0001/Plan-A' \
  -H 'accept: */*'
```


Save Drug Info

```json
{
  "id": "0001",
  "awp": 120.00,
  "notes": "Example life saving drug"
}
```

```shell
curl -X 'POST' \
  'http://localhost:6001/pharmacy/drug/pricing/info' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "0001",
  "awp": 120.00,
  "notes": "Example life saving drug"
}'
```


```shell
curl -X 'POST' \
  'http://localhost:6001/pharmacy/drug/pricing/info' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "0002",
  "awp": 75.00,
  "notes": "Example drug 2"
}'
```

```shell
curl -X 'POST' \
  'http://localhost:6001/pharmacy/drug/pricing/info' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "0003",
  "awp": 25.00,
  "notes": "Example drug 3"
}'
```


Add Pharmacy plan pricing


```shell
curl -X 'POST' \
  'http://localhost:6001/pharmacy/plan/pricing' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "planId": "PLAN-A",
  "ndc": "0001",
  "awpDiscount": 0.20,
  "dispensingFee": 1.50,
  "copay": 10.00
}'
```


Adding Plan


```shell
curl -X 'POST' \
  'http://localhost:6001/pharmacy/plan/pricing' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "planId": "PLAN-B",
  "ndc": "0001",
  "awpDiscount": 0.15,
  "dispensingFee": 1.00,
  "copay": 20.00
}'
```


```shell
curl -X 'POST' \
  'http://localhost:6001/pharmacy/plan/pricing' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "planId": "PLAN-IM",
  "ndc": "0002",
  "awpDiscount": 0.25,
  "dispensingFee": 1.50,
  "copay": 50.00
}'
```

```shell
curl -X 'POST' \
  'http://localhost:6001/pharmacy/plan/pricing' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "planId": "PLAN-IM",
  "ndc": "0003",
  "awpDiscount": 0.37,
  "dispensingFee": 1.70,
  "copay": 57.00
}'
```

```shell
curl -X 'POST' \
  'http://localhost:6001/pharmacy/plan/pricing' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "planId": "PLAN-B",
  "ndc": "0001",
  "awpDiscount": 0.15,
  "dispensingFee": 1.00,
  "copay": 20.00
}'
```

Access Gfsh

```shell
$GEMFIRE_HOME/bin/gfsh
```


Type help

```shell
help
```


Connect to cluster

```gfsh
connect
```

List members

```gfsh
list members
```

List clients
```shell
list clients
```

Query Data in gfsh

```gfsh
query --query="select * from /DrugPricingInfo"
```

```gfsh
query --query="select * from /PlanPricingRule"
```



List functions in gfsh

```gfsh
list functions
```


Get price

```properties
ndc="0001"
planId="PLAN-A"
```

Get price

```properties
ndc="0001"
planId="PLAN-B"
```


```shell
curl  -X 'GET' \
  'http://localhost:6001/pharmacy/pricing/0002/PLAN-IM' \
  -H 'accept: */*'
  curl  -X 'GET' \
  'http://localhost:6001/pharmacy/pricing/0002/PLAN-IM' \
  -H 'accept: */*'
```

```shell
curl  -X 'GET' \
  'http://localhost:6001/pharmacy/pricing/0001/PLAN-B' \
  -H 'accept: */*'
```

```shell
curl  -X 'GET' \
  'http://localhost:6001/pharmacy/pricing/0003/PLAN-IM' \
  -H 'accept: */*'
```


Show logs in gfsh


```gfsh
show log --member=lfserver1
```


```gfsh
show log --member=lfserver2
```

```gfsh
show log --member=lfserver3
```


Full Text Search in Gfsh

```gfsh
search lucene --defaultField=notes --name=drugInfoNotesIndex --region=/DrugPricingInfo --queryString="lfe saving"
```


----

# GemFire Management Console

Query by ranges of values

```sql
select * from /DrugPricingInfo where awp > 20 and awp < 70
```

Select Fields

```sql
select distinct planPricing.ndc, planPricing.awpDiscount from /PlanPricingRule where planPricing.awpDiscount < 1
```


Complex Group By with max, min, averages

```sql
select planPricing.planId, 
    max(planPricing.awpDiscount) as max_discount, 
    min(planPricing.awpDiscount) as avg_discount , 
      avg(planPricing.awpDiscount)  as min_discount 
from /PlanPricingRule 
where  planPricing.copay < 100
group by planPricing.planId 
```

-- 

Continuous Query

```shell
curl -X 'POST' \
  'http://localhost:6001/pharmacy/plan/pricing' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "planId": "PLAN-NICE",
  "ndc": "0001",
  "awpDiscount": 0.55,
  "dispensingFee": 1.00,
  "copay": 1.00
}'
```


Gfsh/GMC query

```shell
select * from AffordableEvent
```


---

## Resiliency

Kill Cache Server 1

```shell
 gfpid=`ps -ef | grep lfserver1 | grep -v grep | awk '{print $2}'`
 kill -9 $gfpid
```

Test

-  View Server  List in GMC or gfsh
- Select data in GMC/gfsh

Start Back server 1

```shell
cd $GEMFIRE_HOME/bin
$GEMFIRE_HOME/bin/gfsh -e "start server --name=lfserver1 --max-heap=500m --initial-heap=500m --locators=localhost[10334] --server-port=1880 --J=-Dgemfire.prometheus.metrics.emission=Default --J=-Dgemfire.prometheus.metrics.port=7778 --J=-Dgemfire.prometheus.metrics.host=localhost --J=-Dgemfire.prometheus.metrics.interval=15s --bind-address=127.0.0.1  --http-service-port=8590  --J=-Dgemfire.enable-statistics=true --J=-Dgemfire.-–statistic-archive-file=lfserver1.gfs --J=-Dgemfire.enable-time-statistics=true"
```

Test

- View Server  List in GMC or gfsh
- Select data in GMC/gfsh


Kill Cache Server 2

```shell
 gfpid=`ps -ef | grep lfserver2 | grep -v grep | awk '{print $2}'`
 kill -9 $gfpid
```

Test

- View Server  List in GMC or gfsh
- Select data in GMC/gfsh



Start Back server 2

```shell
cd $GEMFIRE_HOME/bin
$GEMFIRE_HOME/bin/gfsh -e "start server --name=lfserver2 --max-heap=500m --initial-heap=500m --locators=localhost[10334] --server-port=1881 --J=-Dgemfire.prometheus.metrics.emission=Default --J=-Dgemfire.prometheus.metrics.port=7798 --J=-Dgemfire.prometheus.metrics.host=localhost --J=-Dgemfire.prometheus.metrics.interval=15s --bind-address=127.0.0.1  --http-service-port=8591  --J=-Dgemfire.enable-statistics=true --J=-Dgemfire.-–statistic-archive-file=lfserver2.gfs --J=-Dgemfire.enable-time-statistics=true"
```

Test

- View Server  List in GMC or gfsh
- Select data in GMC/gfsh


Kill Cache Server 3

```shell
 gfpid=`ps -ef | grep lfserver3 | grep -v grep | awk '{print $2}'`
 kill -9 $gfpid
```

Test

- View Server  List in GMC or gfsh
- Select data in GMC/gfsh


Start Server 3

```shell
cd $GEMFIRE_HOME/bin
$GEMFIRE_HOME/bin/gfsh -e "start server --name=lfserver3 --max-heap=500m --initial-heap=500m --locators=localhost[10334] --server-port=1882 --J=-Dgemfire.prometheus.metrics.emission=Default --J=-Dgemfire.prometheus.metrics.port=7799 --J=-Dgemfire.prometheus.metrics.host=localhost --J=-Dgemfire.prometheus.metrics.interval=15s --bind-address=127.0.0.1  --http-service-port=8592  --J=-Dgemfire.enable-statistics=true --J=-Dgemfire.-–statistic-archive-file=lfserver3.gfs --J=-Dgemfire.enable-time-statistics=true"
```



---

