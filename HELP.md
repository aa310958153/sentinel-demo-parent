# 文档合集
* [官方demo合集](https://github.com/alibaba/Sentinel/tree/master/sentinel-demo)
* [基本原理](https://sentinelguard.io/zh-cn/docs/basic-implementation.html)
* [流量控制](https://sentinelguard.io/zh-cn/docs/flow-control.html)
* [规则说明](https://sentinelguard.io/zh-cn/docs/basic-api-resource-rule.html)
* [动态规则支持](https://github.com/alibaba/Sentinel/wiki/动态规则扩展)
* [集群限流](https://sentinelguard.io/zh-cn/docs/cluster-flow-control.html)
# 部署sentinel控制台
* [文档参考](https://sentinelguard.io/zh-cn/docs/dashboard.html)
```
java  -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard  -jar sentinel-dashboard-1.8.8.jar
```
-Dcsp.sentinel.dashboard.server=localhost:8080 表示上报自己

访问地址:http://localhost:8080/#/login
账号/密码:sentinel/sentinel
