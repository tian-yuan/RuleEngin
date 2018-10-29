### rule engine 

#### kafka config

kafka consumer config
```json
{
    "brokerAddress": "localhost:9092",
    "group": "rule-engine",
    "topics": "topic",
    "autoCommit": true,
    "maxPollCount": 1000,
    "maxPollInterval": 2000,
    "maxQps": 1000
}
```

#### rule config
```json
{
    "action": {
        "kafka": {
            "brokers": "localhost:9092",
            "topic": "dest-topic"
        }
    },
    "npnsIotSqlVersion": "2018-10-18",
    "ruleDisabled": true,
    "sql": "select * from topic"
}
```

> kafka consumer topic should be equal to sql database, as the above example, the topic should be "topic"

#### compile jar

mvn package -Dmaven.test.skip=true

#### build docker image
mvn package dockerfile:build -Dmaven.test.skip=true

#### run jar
java -Denv=DEV -Ddev_meta=http://localhost:8080/ -jar ./target/push-rule-engine-1.0-SNAPSHOT.jar

