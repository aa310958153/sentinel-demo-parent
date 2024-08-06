# sentinel 日志
https://github.com/alibaba/Sentinel/wiki/日志
# 文档合集
* [官方demo合集](https://github.com/alibaba/Sentinel/tree/master/sentinel-demo)
* [dashboard使用](https://sentinelguard.io/zh-cn/docs/dashboard.html)
* [基本原理](https://sentinelguard.io/zh-cn/docs/basic-implementation.html)
* [流量控制](https://sentinelguard.io/zh-cn/docs/flow-control.html)
* [规则说明](https://sentinelguard.io/zh-cn/docs/basic-api-resource-rule.html)
* [动态规则支持](https://github.com/alibaba/Sentinel/wiki/动态规则扩展)
* [集群限流](https://sentinelguard.io/zh-cn/docs/cluster-flow-control.html)
* [注解支持文档](https://sentinelguard.io/zh-cn/docs/annotation-support.html)
* [参数配置](https://github.com/alibaba/Sentinel/wiki/启动配置项#日志相关配置项)
* [生产环境是sentinel](https://github.com/alibaba/Sentinel/wiki/在生产环境中使用-Sentinel)
* [推模式规则改造](https://github.com/alibaba/Sentinel/wiki/Sentinel-控制台（集群流控管理）#规则配置)
* [秒级监控](https://github.com/alibaba/Sentinel/wiki/日志#秒级监控日志)
# 部署sentinel控制台
* [文档参考](https://sentinelguard.io/zh-cn/docs/dashboard.html)
```
java  -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard  -jar sentinel-dashboard-1.8.8.jar
```
-Dcsp.sentinel.dashboard.server=localhost:8080 表示上报自己

访问地址:http://localhost:8080/#/login
账号/密码:sentinel/sentinel

日志排查 如果未注册到dashboard

```
${user.home}/logs/csp/sentinel-record.log.xxx
```