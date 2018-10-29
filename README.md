### rule engine 

#### 编译可执行 jar

mvn package -Dmaven.test.skip=true

#### 编译 docker 镜像
mvn package dockerfile:build -Dmaven.test.skip=true

#### 运行可执行 jar
java -Denv=DEV -Ddev_meta=http://localhost:8080/ -jar ./target/push-rule-engine-1.0-SNAPSHOT.jar

