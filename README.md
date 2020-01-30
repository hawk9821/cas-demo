# cas-demo 学习心得
#### 关于CAS的文档请到[CAS Home](https://apereo.github.io/cas/5.3.x/index.html) 查阅
![JDK](https://img.shields.io/badge/JDK-1.8%2B-green.svg) ![CAS](https://img.shields.io/badge/CAS-5.3-green.svg) ![MAVEN](https://img.shields.io/badge/MAVEN-3.2.2-green.svg)

## 项目模块
- `CAS-server`集成`jdbc`,并对密码加盐`salt`,再进行`MD5`加密验证
- `cas-clientA`基于`Springboot`搭建cas客户端
- `cas-clientB`基于`Servlet + SpringMVC`搭建cas客户端
- `cas-clientC`基于`Servlet`搭建cas客户端