# wfengine
1、启动

```shell
cd ~/development/dev_software/zookeeper-3.4.9/bin
sudo ./zkServer.sh start
```

2、启动rocketmq

在rocketmq-all-4.3.2/distribution/target/apache-rocketmq/conf/broker.conf 中 加入 

```
brokerIP1=127.0.0.1 //你的公网IP
```

运行Name Server

```shell
cd /Users/wunsiang/development/dev_software/rocketmq-all-4.3.2/distribution/target/apache-rocketmq
nohup ./bin/mqnamesrv -n 127.0.0.1:9876 &
tail -f ~/logs/rocketmqlogs/namesrv.log
```

运行Broker

```shell
cd /Users/wunsiang/development/dev_software/rocketmq-all-4.3.2/distribution/target/apache-rocketmq
nohup sh bin/mqbroker -n 127.0.0.1:9876 -c conf/broker.conf autoCreateTopicEnable=true &
tail -f ~/logs/rocketmqlogs/broker.log 
```

运行console
```shell
cd /Users/wunsiang/development/dev_software/rocketmq-externals-master/rocketmq-console/
java -jar target/rocketmq-console-ng-1.0.1.jar
//dubbo-admin
cd /Users/wunsiang/development/dev_software/dubbo-admin
mvn --projects dubbo-admin-server spring-boot:run
```
运行redis
```shell script
brew services start redis@3.2
```
3、结束

Rocketmq Shutdown Servers

```shell
cd /Users/wunsiang/development/dev_software/rocketmq-all-4.3.2/distribution/target/apache-rocketmq
sh bin/mqshutdown broker
sh bin/mqshutdown namesrv
```

zookeeper

```
cd ~/development/dev_software/zookeeper-3.4.9/bin
./zkServer.sh stop
```


