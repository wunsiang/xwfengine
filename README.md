# wfengine
1、启动

```shell
cd ~/ThirdSoft/zookeeper-3.4.9/bin
./zkServer.sh start
```

2、启动rocketmq

在rocketmq-all-4.3.2/distribution/target/apache-rocketmq/conf/broker.conf 中 加入 

```
brokerIP1=192.168.0.163 //你的公网IP
```

运行Name Server

```shell
cd /Users/wunsiang/ThirdSoft/rocketmq-all-4.3.2/distribution/target/apache-rocketmq
nohup ./bin/mqnamesrv -n 192.168.0.163:9876 &
tail -f ~/logs/rocketmqlogs/namesrv.log
```

运行Broker

```shell
nohup sh bin/mqbroker -n 192.168.0.163:9876 -c conf/broker.conf autoCreateTopicEnable=true &
tail -f ~/logs/rocketmqlogs/broker.log 
```

3、结束

Rocketmq Shutdown Servers

```shell
cd /Users/wunsiang/ThirdSoft/rocketmq-all-4.3.2/distribution/target/apache-rocketmq
sh bin/mqshutdown broker
sh bin/mqshutdown namesrv
```

zookeeper

```
cd ~/ThirdSoft/zookeeper-3.4.9/bin
./zkServer.sh stop
```


