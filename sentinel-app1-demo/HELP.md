# 接入dashboard方式

# 客户端接入

* [适配接入文档参考](https://sentinelguard.io/zh-cn/docs/open-source-framework-integrations.html)
* [配置文档参考](https://sentinelguard.io/zh-cn/docs/startup-configuration.html)

```
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-transport-simple-http</artifactId>
     <version>1.8.6</version>
</dependency>

```

启动时加入 JVM 参数 -Dcsp.sentinel.dashboard.server=localhost:8080 指定控制台地址和端口。若启动多个应用，则需要通过
-Dcsp.sentinel.api.port=xxxx 指定客户端监控 API 的端口（默认是 8719）。

# 监控端点(https://github.com/alibaba/Sentinel/wiki/实时监控#实时查询)

## dashboardClient

* [查看NodeSelectorSlot内存结构](http://localhost:8719/tree?type=root)
* [查看ClusterBuilderSlot内存结构](http://localhost:8719/origin?id=caller)
* [查看资源的实时统计信息](http://localhost:8719/cnode?id=resourceName)
* [查看所有生效的限流规则](localhost:8719/getRules?type=flow)
* [查看所有生效的留空规则 ](localhost:8719/getRules?type=flow)type=flow 以 JSON 格式返回现有的限流规则，degrade
  返回现有生效的降级规则列表，system 则返回系统保护规则。
* [查看资源规则放行情况](http://localhost:8719/cnode?id=com.example.sentinelapp1demo.service.UserServiceImpl.findByUser)

```
idx id   thread  pass  blocked   success  total Rt   1m-pass   1m-block   1m-all   exeption
2   abc647 0     46     0           46     46   1       2763      0         2763     0
其中：

thread： 代表当前处理该资源的线程数；
pass： 代表一秒内到来到的请求；
blocked： 代表一秒内被流量控制的请求数量；
success： 代表一秒内成功处理完的请求；
total： 代表到一秒内到来的请求以及被阻止的请求总和；
RT： 代表一秒内该资源的平均响应时间；
1m-pass： 则是一分钟内到来的请求；
1m-block： 则是一分钟内被阻止的请求；
1m-all： 则是一分钟内到来的请求和被阻止的请求的总和；
exception： 则是一秒内业务本身异常的总和。
```