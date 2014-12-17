java \
  -jar automated-testing.jar \
  -c be.iminds.ilabt.jfed.lowlevel.api.test.TestAggregateManager3 \
  --authorities-file conf/cli.authorities \
  -p conf/cli.localhost.properties

open $(ls -t|head -n1)/result.html
