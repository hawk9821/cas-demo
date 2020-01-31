# cas-demo 学习心得
#### 关于CAS的文档请到[CAS Home](https://apereo.github.io/cas/5.3.x/index.html) 查阅
![JDK](https://img.shields.io/badge/JDK-1.8%2B-green.svg) ![CAS](https://img.shields.io/badge/CAS-5.3-green.svg) ![MAVEN](https://img.shields.io/badge/MAVEN-3.2.2-green.svg) ![TOMCAT](https://img.shields.io/badge/TOMCAT-8.5.50-green.svg)
## 项目模块
- `cas-server`集成`jdbc`,并对密码`salt`登录验证
- `cas-clientA`基于`Springboot`搭建cas客户端
- `cas-clientB`基于`Servlet + SpringMVC`搭建cas客户端
- `cas-clientC`基于`Servlet`搭建cas客户端
### 一、工程配置
#### 工程为maven工程`root  pom.xml` 如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.hawk</groupId>
    <artifactId>cas-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>cas-server</module>
        <module>cas-clientA</module>
        <module>cas-clientB</module>
        <module>cas-clientC</module>
    </modules>

</project>
```
#### mvn编译打包，需要下载相关依赖
![images](https://github.com/hawk9821/cas-demo/blob/master/doc/casdemo1.jpg)
#### 配置tomcat
##### `cas-server`
![images](https://github.com/hawk9821/cas-demo/blob/master/doc/casserver1.jpg)
![images](https://github.com/hawk9821/cas-demo/blob/master/doc/casserver2.jpg)
##### `cas-clientB`
![images](https://github.com/hawk9821/cas-demo/blob/master/doc/casclient1.jpg)
![images](https://github.com/hawk9821/cas-demo/blob/master/doc/casclient2.jpg)
##### `cas-clientC`同`cas-clientB`

### 二、cas-server
### 1.域名映射
#### 修改host文件，添加服务端域名及客户端域名
```bash
127.0.0.1      cas.server.com
127.0.0.1      app1.cas.com
127.0.0.1      app2.cas.com
127.0.0.1      app3.cas.com
```
### 2.本地配置tomcat通过https访问
#### 生成keystore
```bash
keytool -genkey -alias tomcat -keyalg RSA -validity 3650 -keystore server.keystore
```
![images](https://github.com/hawk9821/cas-demo/blob/master/doc/certificate1.jpg)
-alias `tomcat` ：表示秘钥库的别名是tomcat，实际操作都用别名识别，所以这个参数很重要。

-validity `3650` ： 表示证书有效期10年。
秘钥库口令 : `changeit` 。

名字与姓氏输入服务器域名,其它一路回车，最后如果显示正确 输入 `y` 就行了。 tomcat秘钥口令与秘钥库相同
#### 根据keystore生成crt文件
```bash
keytool -export -alias tomcat -file server.cer -keystore server.keystore -validity 3650 -storepass changeit
```
![images](https://github.com/hawk9821/cas-demo/blob/master/doc/certificate2.jpg)
#### 导入证书到jdk
```bash
keytool -delete -alias tomcat -keystore "C:\Program Files\Java\jdk1.8.0_171\jre\lib\security\cacerts" -storepass changeit

keytool -import -keystore "C:\Program Files\Java\jdk1.8.0_171\jre\lib\security\cacerts" -file server.cer -alias tomcat -storepass changeit
```

![images](https://github.com/hawk9821/cas-demo/blob/master/doc/certificate4.jpg)
#### 检查证书是否正确导入
```bash
keytool -list -v -alias tomcat -keystore "C:\Program Files\Java\jdk1.8.0_171\jre\lib\security\cacerts" -storepass changeit
```
![images](https://github.com/hawk9821/cas-demo/blob/master/doc/certificate5.jpg)
#### 修改tomcat的配置文件`apache-tomcat-8.5.50\conf\server.xml`
添加以下内容：
```xml
<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
           maxThreads="200" SSLEnabled="true" scheme="https"
           secure="true" clientAuth="false" sslProtocol="TLS"
           keystoreFile="G:\WorkSpace\cas-demo\cas-server\src\main\resources\server.keystore"
           keystorePass="changeit"/>
```
#### chrome浏览器添加信任证书
设置 > 高级 > 证书管理 > 导入`server.cer`
![images](https://github.com/hawk9821/cas-demo/blob/master/doc/certificate6.jpg)

### 3.`application.properties`配置
```xml
##
# https证书配置
#
server.ssl.enabled=true
server.ssl.key-store=classpath:server.keystore
server.ssl.key-store-password=changeit
server.ssl.key-password=changeit
server.ssl.keyAlias=tomcat
```
```xml
##
# 注册客户端
#
cas.serviceRegistry.initFromJson=true
cas.serviceRegistry.watcherEnabled=true
cas.serviceRegistry.schedule.repeatInterval=120000
cas.serviceRegistry.schedule.startDelay=15000
cas.serviceRegistry.managementType=DEFAULT
cas.serviceRegistry.json.location=classpath:/services
cas.logout.followServiceRedirects=true
```
```xml
##
# jdbc验证配置
#
cas.authn.jdbc.query[0].sql=SELECT * FROM user_info WHERE username =?
#那一个字段作为密码字段
cas.authn.jdbc.query[0].fieldPassword=password
#指定过期字段，1为过期，若过期需要修改密码
cas.authn.jdbc.query[0].fieldExpired=expired
#为不可用字段段，1为不可用，
cas.authn.jdbc.query[0].fieldDisabled=disabled
#配置数据库连接
cas.authn.jdbc.query[0].url=jdbc:mysql://localhost:3306/cas?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false
cas.authn.jdbc.query[0].dialect=org.hibernate.dialect.MySQLDialect
#数据库用户名
cas.authn.jdbc.query[0].user=root
#数据库密码
cas.authn.jdbc.query[0].password=root
#mysql驱动
cas.authn.jdbc.query[0].driverClass=com.mysql.jdbc.Driver
#配置加密策略 简单MD5加密
cas.authn.jdbc.query[0].passwordEncoder.type=DEFAULT
cas.authn.jdbc.query[0].passwordEncoder.characterEncoding=UTF-8
cas.authn.jdbc.query[0].passwordEncoder.encodingAlgorithm=MD5
```
```xml
##
# 对密码进行盐值处理再加密
#
#加密迭代次数
cas.authn.jdbc.encode[0].numberOfIterations=10
#该列名的值可替代上面的值，但对密码加密时必须取该值进行处理
cas.authn.jdbc.encode[0].numberOfIterationsFieldName=
#盐值固定列
cas.authn.jdbc.encode[0].saltFieldName=salt
#静态盐值
cas.authn.jdbc.encode[0].staticSalt=.
cas.authn.jdbc.encode[0].sql=SELECT * FROM user_info WHERE username =?
#对处理盐值后的算法
cas.authn.jdbc.encode[0].algorithmName=MD5
cas.authn.jdbc.encode[0].passwordFieldName=password
cas.authn.jdbc.encode[0].expiredFieldName=expired
cas.authn.jdbc.encode[0].disabledFieldName=disabled
#数据库连接
cas.authn.jdbc.encode[0].url=jdbc:mysql://127.0.0.1:3306/cas?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false
cas.authn.jdbc.encode[0].dialect=org.hibernate.dialect.MySQL5Dialect
cas.authn.jdbc.encode[0].driverClass=com.mysql.jdbc.Driver
cas.authn.jdbc.encode[0].user=root
cas.authn.jdbc.encode[0].password=root
```
#### cas默认只支持HTTPS协议，如下配置开启HTTP协议支持
`application.properties`增加如下配置
```xml
##
# 配置开启http协议支持
#
cas.server.http.port=8080
cas.server.http.protocol=org.apache.coyote.http11.Http11NioProtocol
cas.server.http.enabled=true
cas.server.http.attributes.attributeName=attributeValue
cas.tgc.secure=false
```
修改resources\services目录下的`HTTPSandIMAPS-10000001.json`,内如如下：
```xml
{
  "@class" : "org.apereo.cas.services.RegexRegisteredService",
  "serviceId" : "^(https|http|imaps)://.*",
  "name" : "CAS SERVER",
  "id" : 10000001,
  "description" : "This service definition authorizes all application urls that support HTTPS,HTTP and IMAPS protocols.",
  "evaluationOrder" : 10000
}
```
