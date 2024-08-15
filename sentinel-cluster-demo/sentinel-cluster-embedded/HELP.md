# 集成nacos-client的demo
```
docker run --env MODE=standalone --name nacos --restart=always  -d -p 8848:8848 nacos/nacos-server:1.2.0
http://127.0.0.1:8848/nacos


java  -Dproject.name=clusterapp -Dserver.port=9081 -Dcsp.sentinel.dashboard.server=localhost:8080 -jar sentinel-cluster-embedded-0.0.1-SNAPSHOT.jar 
java  -Dproject.name=clusterapp -Dserver.port=9082 -Dcsp.sentinel.dashboard.server=localhost:8080 -jar sentinel-cluster-embedded-0.0.1-SNAPSHOT.jar 
java  -Dproject.name=clusterapp -Dserver.port=9083 -Dcsp.sentinel.dashboard.server=localhost:8080 -jar sentinel-cluster-embedded-0.0.1-SNAPSHOT.jar 


```

```
[
    {
        "app": "clusterapp",
        "clusterConfig": {//集群相关配置
            "acquireRefuseStrategy": 0,
            "clientOfflineTime": 2000,
            "fallbackToLocalWhenFail": true,
            "flowId": 2,
            "resourceTimeout": 2000,
            "resourceTimeoutStrategy": 0,
            "sampleCount": 10,
            "strategy": 0,
            "thresholdType": 1,
            "windowIntervalMs": 1000
        },
        "clusterMode": true, //表示集群配置
        "controlBehavior": 0,
        "count": 3.0,
        "gmtCreate": 1723113895361,
        "gmtModified": 1723113895361,
        "grade": 1,
        "id": 2,
        "ip": "10.4.1.125", //tokenServer
        "limitApp": "default",
        "port": 8720,//tokenServier 端口
        "resource": "/helloSentinelResource",
        "strategy": 0
    }
]
```