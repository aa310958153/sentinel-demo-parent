# 集成nacos-client的demo

```
docker run --env MODE=standalone --name nacos --restart=always  -d -p 8848:8848 nacos/nacos-server:v2.2.0


http://127.0.0.1:8848/nacos
nacos/nacos
注意 nacos有缓存 如果读取失败则读取缓存
 C:\Users\user\nacos\config\fixed-localhost_8848_nacos\snapshot
 cd ~\nacos\config\fixed-localhost_8848_nacos\snapshot
```