# 集成nacos-client的demo
```
docker run --env MODE=standalone --name nacos --restart=always  -d -p 8848:8848 nacos/nacos-server:1.2.0
http://127.0.0.1:8848/nacos
```