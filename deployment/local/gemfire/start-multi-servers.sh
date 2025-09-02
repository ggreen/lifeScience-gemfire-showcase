cd $GEMFIRE_HOME/bin
rm -rf lflocator*
rm -rf lfserver*

$GEMFIRE_HOME/bin/gfsh -e "start locator --name=lflocator1 --port=10334 --max-heap=250m --initial-heap=250m --J=-Dgemfire.prometheus.metrics.emission=Default --J=-Dgemfire.prometheus.metrics.port=7777 --J=-Dgemfire.prometheus.metrics.host=localhost --J=-Dgemfire.prometheus.metrics.interval=15s --bind-address=127.0.0.1 --J=-Dgemfire.prometheus.metrics.port=7777 --J=-Dgemfire.enable-statistics=true --J=-Dgemfire.-–statistic-archive-file=locator.gfs --J=-Dgemfire.enable-time-statistics=true"

curl http://localhost:7777/metrics
$GEMFIRE_HOME/bin/gfsh -e "connect" -e "configure pdx --read-serialized=false --disk-store --auto-serializable-classes=.*"
$GEMFIRE_HOME/bin/gfsh -e "start server --name=lfserver1 --max-heap=500m --initial-heap=500m --locators=localhost[10334] --server-port=1880 --J=-Dgemfire.prometheus.metrics.emission=Default --J=-Dgemfire.prometheus.metrics.port=7778 --J=-Dgemfire.prometheus.metrics.host=localhost --J=-Dgemfire.prometheus.metrics.interval=15s --bind-address=127.0.0.1  --http-service-port=8590  --J=-Dgemfire.enable-statistics=true --J=-Dgemfire.-–statistic-archive-file=lfserver1.gfs --J=-Dgemfire.enable-time-statistics=true"

$GEMFIRE_HOME/bin/gfsh -e "start server --name=lfserver2 --max-heap=500m --initial-heap=500m --locators=localhost[10334] --server-port=1881 --J=-Dgemfire.prometheus.metrics.emission=Default --J=-Dgemfire.prometheus.metrics.port=7798 --J=-Dgemfire.prometheus.metrics.host=localhost --J=-Dgemfire.prometheus.metrics.interval=15s --bind-address=127.0.0.1  --http-service-port=8591  --J=-Dgemfire.enable-statistics=true --J=-Dgemfire.-–statistic-archive-file=lfserver2.gfs --J=-Dgemfire.enable-time-statistics=true"
$GEMFIRE_HOME/bin/gfsh -e "start server --name=lfserver3 --max-heap=500m --initial-heap=500m --locators=localhost[10334] --server-port=1882 --J=-Dgemfire.prometheus.metrics.emission=Default --J=-Dgemfire.prometheus.metrics.port=7799 --J=-Dgemfire.prometheus.metrics.host=localhost --J=-Dgemfire.prometheus.metrics.interval=15s --bind-address=127.0.0.1  --http-service-port=8592  --J=-Dgemfire.enable-statistics=true --J=-Dgemfire.-–statistic-archive-file=lfserver3.gfs --J=-Dgemfire.enable-time-statistics=true"

$GEMFIRE_HOME/bin/gfsh -e "connect" -e "create region --skip-if-exists=true --name=DrugPricingInfo --type=PARTITION_REDUNDANT_PERSISTENT --enable-statistics=true"

$GEMFIRE_HOME/bin/gfsh -e "connect" -e "create region --skip-if-exists=true --name=PlanPricingRule --type=PARTITION_REDUNDANT_PERSISTENT --enable-statistics=true --colocated-with=DrugPricingInfo  --partition-resolver=org.apache.geode.cache.util.StringPrefixPartitionResolver"

## simpleIndex uses default Lucene StandardAnalyzer
$GEMFIRE_HOME/bin/gfsh -e "connect" -e "create lucene index --name=drugInfoNotesIndex --region=DrugPricingInfo --field=notes"


$GEMFIRE_HOME/bin/gfsh -e "connect" -e "create region --skip-if-exists=true --name=example-search-region --type=PARTITION --enable-statistics=true"


$GEMFIRE_HOME/bin/gfsh -e "connect" -e "create region --skip-if-exists=true --name=AffordableEvent --type=PARTITION_REDUNDANT_PERSISTENT"

#Number of seconds for expiration = 1 hour
$GEMFIRE_HOME/bin/gfsh -e "connect" -e "create region --skip-if-exists=true --name=Paging --type=PARTITION  --eviction-entry-count=10000 --eviction-action=local-destroy --entry-time-to-live-expiration=3600 --enable-statistics=true"




