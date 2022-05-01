# 一、MybatisPlus

> [Mybatis-Plus快速入门_小蜗牛耶的博客-CSDN博客_mybatis-plus](https://blog.csdn.net/qq_45714272/article/details/123013268)
>
> 回顾可看我写的这篇文章
>
> 下文用boot代替springboot
>
> mp代替mybatisplus



**下面是快速用boot+mp快速开发一套后端的CRUD接口，之后用swagger进行测试**

## 1.1 boot+mp开发套路

这一套东西就是写接口对吧，所以==写接口也就是写业务类，写业务类有个小口诀==

1. 建表写sql
2. 定义实体类
3. dao与mapper
4. service和impl
5. controller



那么我们用==boot写微服务模块==也有个小套路

1. 建module
2. 改pom
3. 写yml
4. 主启动
5. 业务类

**业务类对标我们上面的顺序**



下面我严格遵守我的套路来写

## 1.2 建module

建立新项目的时候可以用spring initializr初始化boot项目，也可以用maven的方式创建。这里我采用maven的方式来创建

![image-20220415093644304](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415093644304.png)



## 1.3 改pom

> pom出现的问题，跟网络环境有很大关系。
>
> pom如果不断出错，可以更换网络或者删掉本地仓库中的包，重下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.caq</groupId>
    <artifactId>mybatisplusdemo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>mybatisplusdemo</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <scope>provided</scope>
            <version>2.7.0</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.7.0</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.0.5</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```



## 1.4 yml

> 这里我采用mysql8.0以上的驱动，配置url到时候?后面的值必须要加
>
> 驱动的名称和mysql5的也不一样记得区分

```yml
#端口号
server:
  port: 8003
#服务名

spring:
  application:
    name: service-edu
  #返回json的全局时间格式
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  datasource:
    username: root
    password: root
    url: jdbc:mysql://localhost:3306/mybatisplus?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8
    driver-class-name: com.mysql.cj.jdbc.Driver
  profiles:
    active: dev

# mybatis日志
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

```



## 1.5 主启动

```java
package com.caq.mybatisplusdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MybatisplusdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisplusdemoApplication.class, args);
    }

}
```



## 1.6 业务类

下面就开始写业务类了，业务类也遵守我们的步骤来写



### 1.6.1 建表写sql

```sql
CREATE TABLE user01
(
    id int(20) NOT NULL COMMENT '主键ID' ,
    name01 VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age01 INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email01 VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);


DELETE FROM user01;
INSERT INTO user01 (id, name01, age01, email01) VALUES
(1, 'Jone', 18, 'test1@baomidou.com'),
(2, 'Jack', 20, 'test2@baomidou.com'),
(3, 'Tom', 28, 'test3@baomidou.com'),
(4, 'Sandy', 21, 'test4@baomidou.com'),
(5, 'Billie', 24, 'test5@baomidou.com');
```



### 1.6.2 实体类、dao、service

**这里我们采用mp的插件，一键完成创建**



==安装插件==

![image-20220415095650610](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415095650610.png)



==idea中连接数据库==

![image-20220415095707544](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415095707544.png)



==右键你要一键生成实体类，dao，service的表格generator即可==

![image-20220415095756678](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415095756678.png)



==生成说明==

![image-20220415100707303](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415100707303.png)



![image-20220415100857091](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415100857091.png)



![image-20220415101005965](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415101005965.png)





==service接口说明==

![image-20220415103131974](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415103131974.png)



==那么我们是不是要实现这么多方法呢？==

当然不用，mp给我们定义好了一个IService的实现类，我们只需要实现类继承它并实现接口即可

![image-20220415103354040](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415103354040.png)





### 1.6.3 controller

> 调用service，service调用mapper（dao）
>
> 开发controller，service，dao的过程就叫开发接口

为了前后端更好的沟通，我们可以定义一个统一的返回类

#### 统一返回类

**状态码定义**

```java
package com.caq.commonutils;

public interface ResultCode {
    //状态码：成功
    public static Integer SUCCESS = 20000;
    //状态码：失败
    public static Integer ERROR = 20001;
}
```

**统一返回类型R**

```java
package com.caq.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 结果类
 */
@Data
public class R<T> {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<>();

    //私有的构造器
    private R() {}

    //成功的静态方法
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("您的操作成功啦(*^▽^*)");
        return r;
    }

    //失败的静态方法
    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("您的操作失败啦(⊙︿⊙)");
        return r;
    }

    //the follow methods all return this，链式编程
    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
```



#### crud接口

> @Api 用在类上，说明该类的作用。
>
> @ApiOperation 用在 Controller 里的方法上，说明方法的作用，每一个接口的定义。
>
> 
>
> @PathVariable 映射 URL 绑定的[占位符](https://so.csdn.net/so/search?q=占位符&spm=1001.2101.3001.7020)
> 通过 @PathVariable 可以将 URL 中占位符参数绑定到控制器处理方法的入参中:URL 中的 {xxx} 占位符可以
>
> 若方法参数名称和需要绑定的url中变量名称一致时,可以简写
>
> public User getUser(@PathVariable("name") String name)
>
> public User getUser(@PathVariable String name)

```java
package com.caq.mybatisplusdemo.controller;

import com.caq.mybatisplusdemo.commonutils.R;
import com.caq.mybatisplusdemo.domain.User;
import com.caq.mybatisplusdemo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Api("crud测试")
@RestController
@RequestMapping("/testMP/user")
public class crudDemo {

    @Autowired
    UserService userService;

    @ApiOperation("增加用户")
    @PostMapping("save")
    public R saveUser(@RequestBody User user){
        boolean save = userService.save(user);
        if (save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation("查看所有用户")
    @GetMapping("list")
    public R listUser(){
        List<User> list = userService.list(null);
        return R.ok().data("items",list);
    }

    @ApiOperation("查看某个用户")
    @GetMapping("getByIdUser")
    public R getByIdUser(@PathVariable String id){
        User user = userService.getById(id);
        return R.ok().data("user",user);
    }


    @ApiOperation("按ID删除user")
    @DeleteMapping("delete")
    public R removeUser(@ApiParam(name = "id",value = "讲师ID",required = true)@PathVariable String id){
        boolean delete = userService.removeById(id);
        if (delete){
            return R.ok();
        }else {
            return R.error();
        }
    }

    @ApiOperation("按ID更改user")
    @PostMapping
    public R updateUser(@RequestBody User user){
        boolean update = userService.updateById(user);
        if (update){
            return R.ok();
        }else {
            return R.error();
        }
    }
}
```

**好，让我们先来介绍下Swagger**



# 二、Swagger

还是一样的套路，开局三连问

## 2.1 是什么？

**一款接口测试工具**



## 2.2 有什么好处？

对于后端开发人员来说

- 不用再手写WiKi接口拼大量的参数，避免手写错误
- 对代码侵入性低，采用全注解的方式，开发简单
- 方法参数名修改、增加、减少参数都可以直接生效，不用手动维护
- 缺点：增加了开发成本，写接口还得再写一套参数配置

对于前端开发来说

- 后端只需要定义好接口，会自动生成文档，接口功能、参数一目了然
- 联调方便，如果出问题，直接测试接口，实时检查参数和返回值,就可以快速定位是前端还是后端的问题

对于测试

- 对于某些没有前端界面UI的功能，可以用它来测试接口
- 操作简单，不用了解具体代码就可以操作
- 操作简单，不用了解具体代码就可以操作



## 2.3 怎么用？

> 引入swagger的依赖
>
> 目前推荐使用2.7.0版本,因为2.6.0版本有bug

### 2.3.1 引入依赖

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <scope>provided</scope>
    <version>2.7.0</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.7.0</version>
</dependency>
```



### 2.3.2 springBoot整合swagger

```java
@Configuration
@MapperScan("com.caq.mybatisplusdemo.mapper")
@EnableSwagger2
public class MpConfig {
    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }


    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("mp测试")
                .description("本文档描述了mp接口定义")
                .version("1.0")
                .contact(new Contact("java", "http://java.com", "534215342@qq.com"))
                .build();
    }
}
```



## 2.4 swagger的注解

**@Api()**：用在请求的类上，表示对类的说明，也代表了这个类是swagger2的资源

参数：

```text
tags：说明该类的作用，参数是个数组，可以填多个。
value="该参数没什么意义，在UI界面上不显示，所以不用配置"
description = "用户基本信息操作"
```

**@ApiOperation()**：用于方法，表示一个http请求访问该方法的操作

参数：

```text
value="方法的用途和作用"    
notes="方法的注意事项和备注"    
tags：说明该方法的作用，参数是个数组，可以填多个。
格式：tags={"作用1","作用2"} 
（在这里建议不使用这个参数，会使界面看上去有点乱，前两个常用）
```

**@ApiModel()**：用于响应实体类上，用于说明实体作用

参数：

```text
description="描述实体的作用"  
```

**@ApiModelProperty**：用在属性上，描述实体类的属性

参数：

```text
value="用户名"  描述参数的意义
name="name"    参数的变量名
required=true     参数是否必选
```

**@ApiImplicitParams**：用在请求的方法上，包含多@ApiImplicitParam

**@ApiImplicitParam**：用于方法，表示单独的请求参数

参数：

```text
name="参数ming" 
value="参数说明" 
dataType="数据类型" 
paramType="query" 表示参数放在哪里
    · header 请求参数的获取：@RequestHeader
    · query   请求参数的获取：@RequestParam
    · path（用于restful接口） 请求参数的获取：@PathVariable
    · body（不常用）
    · form（不常用） 
defaultValue="参数的默认值"
required="true" 表示参数是否必须传
```

**@ApiParam()**：用于方法，参数，字段说明 表示对参数的要求和说明

参数：

```text
name="参数名称"
value="参数的简要说明"
defaultValue="参数默认值"
required="true" 表示属性是否必填，默认为false
```

**@ApiResponses**：用于请求的方法上，根据响应码表示不同响应

一个@ApiResponses包含多个@ApiResponse

**@ApiResponse**：用在请求的方法上，表示不同的响应

**参数**：

```text
code="404"    表示响应码(int型)，可自定义
message="状态码对应的响应信息"   
```

**@ApiIgnore()**：用于类或者方法上，不被显示在页面上

**@Profile({"dev", "test"})**：用于配置类上，表示只对开发和测试环境有用



## 2.5 使用swagger需要注意的问题

- 对于只有一个HttpServletRequest参数的方法,如果参数小于5个，推荐使用 @ApiImplicitParams的方式单独封装每一个参数；如果参数大于5个，采用定义一个对象去封装所有参数的属性，然后使用@APiParam的方式
- **默认的访问地址：**==ip:port/swagger-ui.html#/==,但是在shiro中,会拦截所有的请求，必须加上默认访问路径（比如项目中，就是ip:port/context/swagger-ui.html#/），然后登陆后才可以看到
- 在GET请求中，参数在Body体里面,不能使用@RequestBody。在POST请求，可以使用@RequestBody和@RequestParam，如果使用@RequestBody，对于参数转化的配置必须统一
- controller必须指定请求类型，否则swagger会把所有的类型(6种)都生成出来
- ==swagger在生产环境不能对外暴露==,可以使用@Profile({“dev”, “prod”,“pre”})指定可以使用的环境





# 三、实战

下面我们就开始用Swagger来测试我们写的接口

## 3.1 MybatisPlus补充

### 3.1.1 主键生成策略

下面是我对实体类字段进行的设置，

这样设置的话我的主键在每次创建新用户的时候都会自动填充为分布式全局唯一ID 字符串类型

```java
private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "讲师ID")
/**
 * 分布式应用时，我们需要生成分布式ID，可以选择使用@TableId(type=IdType.ID_WORKER)，数据库中的主键为：
 * IdType包括以下几类：
 * AUTO : 数据库主键自增
 * INPUT: 用户自行输入
 * ID_WORKER: 分布式全局唯一ID， 长整型
 * UUID: 32位UUID字符串
 * NONE: 无状态
 * ID_WORKER_STR: 分布式全局唯一ID 字符串类型
 */
@TableId(value = "id", type = IdType.ID_WORKER_STR)
private String id;
```



![image-20220407211824966](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220407211824966.png)



### 3.1.2 自动填充详解

> 自动填充一般应用在数据库创建时间或修改时间字段

[自动填充功能官网]([自动填充功能 | MyBatis-Plus (baomidou.com)](https://baomidou.com/pages/4c6bcf/))

==定义字段==

```java
@ApiModelProperty(value = "创建时间")
@TableField(fill= FieldFill.INSERT)
private Date gmtCreate;

@ApiModelProperty(value = "更新时间")
@TableField(fill=FieldFill.INSERT_UPDATE)
private Date gmtModified;
```



==配置类==

```java
package com.caq.servicebase.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入的时候填充创建和修改字段
    @Override
    public void insertFill(MetaObject metaObject) {
        //属性名称，不是字段名称
        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
    
//修改的时候填充修改字段
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
}
```



==测试自动填充==

![](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220408144537196.png)



### 3.1.3 事物

==什么是事物？==

一组逻辑上的操作，要么都成功，要么都失败



==如果不考虑事物隔离性，参数读问题？==

- 脏读（事物A读取到了事物B修改未提交的数据）
- 不可重复读（事物A读取两次数据，在这个过程中事物B修改了数据，导致事物A两次读取的数据不一样）
- 幻读（事物B读取两次数据，在这个过程事物A新增了符合事物B读取的数据，后一次读取的数据和前一次是不一样的这就是幻读）



### 3.1.4 锁

锁是针对数据冲突的解决方案

==悲观锁==

正如其名，它指的是对数据被外界修改持保守（悲观），因此在整个数据处理过程中，将数据处于锁定状态。**悲观锁的实现，往往依靠数据库提供的锁机制**



==乐观锁==

相对悲观锁而言，乐观锁假设认为数据一般情况下不会有冲突，所以在数据进行提交更新的时候，才会正式对数据的冲突与否进行检测，如果发现了冲突，则让返回用户错误的信息，让用户决定如何去做。**乐观锁的实现方式一般是记录数据版本**



**乐观锁的实现方式**

- 取出记录，获取当前Version
- 更新时，带上这个version
- 执行更新时，set version = newVersion where version = oldVersion
- 如果version不对，就更新失败



![image-20220408160411414](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220408160411414.png)



## 3.2 Swagger测试

**登录swaggerUI**

==ip:prot/swagger-ui.html==

![image-20220409162626568](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409162626568.png)





### 3.2.1 删除功能测试

> 逻辑删除我们是用Mp中的插件来实现的
>
> 所以在mp的配置类中添加逻辑删除插件即可
>
> - 插入: 不作限制
> - 查找: 追加 where 条件过滤掉已删除数据,且使用 wrapper.entity 生成的 where 条件会忽略该字段
> - 更新: 追加 where 条件防止更新到已删除数据,且使用 wrapper.entity 生成的 where 条件会忽略该字段
> - 删除: 转变为 更新
>
> 例如:
>
> - 删除: `update user set deleted=1 where id = 1 and deleted=0`
> - 查找: `select id,name,deleted from user where deleted=0`

在开发中，我们一般做逻辑删除

**所谓逻辑删除不是真正的删除，而是在逻辑上删除不是在数据库中删除**

==步骤一==

```java
//逻辑删除插件
@Bean
public ISqlInjector sqlInjector() {
    return new LogicSqlInjector();
}
```

==步骤二==

**实体类字段上加上`@TableLogic`注解**

```java
@TableLogic
private Integer deleted;
```



![image-20220409163156257](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409163156257.png)





### 3.2.2 讲师分页功能

分页功能我们也是用Mp中的插件来实现的

所以在mp的配置类中添加分页插件即可

==步骤一==

**分页插件**

```java
//分页插件
@Bean
public PaginationInterceptor paginationInterceptor() {
    return new PaginationInterceptor();
}
```



==步骤二==

**分页Controller方法**

```java
 @ApiOperation("分页查询讲师功能")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(@PathVariable long current,
                         @PathVariable long limit) {

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //调用方法实现分页
        //调用方法的时候，底层封装，把分页所有数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher, null);

        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();

        Map<String, Object> map = new HashMap();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);
//        两种方式都可以
//        return R.ok().data("total",total).data("rows",records);
    }
```

![img](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/20210907162939748.png)





![image-20220409205459668](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409205459668.png)



### 3.2.3 分页查询和多条件查询

> @requestbody注解的作用是使用json传递数据，并把json封装到对应对象里面
>
> 面试题补充：
>
> 你经常用springboot中的那些注解？
>
> @RequestBody、@ResponseBody、@PathVariable
>
> 前者是以json格式传递数据
>
> 后者是返回json格式数据



![image-20220409202513916](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409202513916.png)



==步骤一==

创建查询对象

```java
package com.caq.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeacherQuery {

//    private Long current;
//    private Long limit;
    @ApiModelProperty(value = "教师名称,模糊查询")
    private String name;

    @ApiModelProperty(value = "头衔 1普通讲师 2高级讲师 3超级讲师")
    private Integer level;

    @ApiModelProperty(value = "查询开始时间", example = "2019-01-01 10:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换
//    private Date begin;

    @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
    private String end;
//    private Date end;
}
```







==步骤二==

**分页查询和多条件查询接口**

TeacherQuery的属性根据前端需要的查询条件来设置
@RequestBody(required = false)表示增加参数TeacherQuery teacherQuery，非必选

```java
@ApiOperation("分页查询和多条件查询")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
//        多条件组合查询
        Integer level = teacherQuery.getLevel();
        String name = teacherQuery.getName();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.eq("gmt_modified", end);
        }

        //排序，新创建的在后面
        wrapper.orderByDesc("gmt_create");

//        调用方法实现条件查询分页
        teacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }
```

==当然我们也可以写条件表达式的形式==

**service**

```java
public interface EduTeacherService extends IService<EduTeacher> {
 
    IPage<EduTeacher> pageList(Long current, Long limit, TeacherQuery teacherQuery);
}
```



**impl**

```java
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherDao, EduTeacher> implements EduTeacherService {
 
 
    public IPage<EduTeacher> pageList(Long current, Long limit, TeacherQuery teacherQuery) {
        Integer level = teacherQuery.getLevel();
        String name = teacherQuery.getName();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        LambdaQueryWrapper<EduTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(level!=null, EduTeacher::getLevel,level)
                .like(StringUtils.isNotBlank(name),EduTeacher::getName,name)
                .ge(begin!=null,EduTeacher::getGmtCreate,begin)
                .le(end!=null, EduTeacher::getGmtModified,end);
        return this.page(new Page<>(current,limit), queryWrapper);
    }
}
```



### 3.2.3 添加讲师

```java
@PostMapping("addTeacher")
public R addTeacher(@RequestBody EduTeacher eduTeacher){
    boolean save = teacherService.save(eduTeacher);
    if (save){
        return R.ok();
    }else {
        return R.error();
    }
}
```



![image-20220409215249849](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409215249849.png)





### 3.2.4 查询讲师

```java
@GetMapping("getTeacher/{id}")
public R getTeacher(@PathVariable String id){
    EduTeacher eduTeacher = teacherService.getById(id);
    return R.ok().data("teacher",eduTeacher);
}
```



![image-20220409220142226](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409220142226.png)





### 3.2.5 修改讲师

> 我们修改讲师用的传入的参数是一个讲师对象，讲师对象里必须有id,因为我们修改讲师用的是id

```java
//讲师修改功能
@PostMapping("updateTeacher")
public R updateTeacher(@RequestBody EduTeacher eduTeacher){
    boolean flag = teacherService.updateById(eduTeacher);
    if (flag){
        return R.ok();
    }else {
        return R.error();
    }
}
```



![image-20220409220105963](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409220105963.png)





## 3.3 异常处理

==全局异常==

```java
//全局异常处理,当遇见Exception异常的时候调用error方法
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回数据(它不在controller中所以要加上ResponseBody注解)
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行了全局异常处理....");
    }
```

![image-20220409222957614](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409222957614.png)



### 3.3.1 特定异常处理

> 特定和全局异常怎么选择呢？
>
> 先找特定异常，如果没有则找全局异常

```java
//    特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody//返回数据(它不在controller中所以要加上ResponseBody注解)
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行了ArithmeticException异常处理....");
    }
```

![image-20220409223726085](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409223726085.png)





### 3.3.2 自定义异常处理

1. 创建自定义异常类继承RuntimeException，写异常属性
2. 在统一异常类添加规则
3. 执行自定义异常

```java
第一步、
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Guliexception extends RuntimeException {
    private Integer code;//状态码
    private String msg;//异常信息
}

第二步、
//    自定义异常处理
@ExceptionHandler(Guliexception.class)
@ResponseBody//返回数据(它不在controller中所以要加上ResponseBody注解)
public R error(Guliexception e){
    e.printStackTrace();
    //这一套链式调用记得多debug
    return R.error().code(e.getCode()).message(e.getMsg());
}

第三步、
//模拟一个异常
try {
    int i = 10/0;
} catch (Exception e) {
    /**执行自定义异常
             * 传入的参数是自己写的异常类的构造方法的参数，这样能让代码更通用
             */
    throw new Guliexception(20001,"执行了自定义异常处理.....");
}
```



测试

![image-20220409225409840](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409225409840.png)





## 3.4 统一日志处理

spring boot内部使用Logback作为日志实现的框架。

Logback和log4j非常相似，如果你对log4j很熟悉，那对logback很快就会得心应手。

logback相对于log4j的一些优点：https://blog.csdn.net/caisini_vc/article/details/48551287



### 3.4.1 配置logback日志

删除 application.yml 中的日志配置

resources 中创建 `logback-spring.xml`，名字固定的，不建议改

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="10 seconds">
    <!-- 日志级别从低到高分为TRACE < DEBUG < INFO < WARN < ERROR < FATAL，如果设
    置为WARN，则低于WARN的信息都不会输出 -->
    <!-- scan:当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值
    为true -->
    <!-- scanPeriod:设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认
    单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。 -->
    <!-- debug:当此属性设置为true时，将打印出logback内部日志信息，实时查
    看logback运行状态。默认值为false。 -->
    <contextName>logback</contextName>
    <!-- name的值是变量的名称，value的值时变量定义的值。通过定义的值会被插入
    到logger上下文中。定义变量后，可以使“${}”来使用变量。 -->
    <!--日志输出在文件夹的哪个位置-->
    <property name="log.path" value="D:\JavaStudy\gulixueyuan\logback"/>
 
    <!-- 彩色日志 -->
    <!-- 配置格式变量：CONSOLE_LOG_PATTERN 彩色日志格式 -->
    <!-- magenta:洋红 -->
    <!-- boldMagenta:粗红-->
    <!-- cyan:青色 -->
    <!-- white:白色 -->
    <!-- magenta:洋红 -->
    <property name="CONSOLE_LOG_PATTERN"
              value="%yellow(%date{yyyy-MM-dd HH:mm:ss}) |%highlight(%-5level)
|%blue(%thread) |%blue(%file:%line) |%green(%logger) |%cyan(%msg%n)"/>
    <!--输出到控制台-->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <!--此日志appender是为开发使用，只配置最底级别，控制台输出的日志级别是大于或
        等于此级别的日志信息-->
        <!-- 例如：如果此处配置了INFO级别，则后面其他位置即使配置了DEBUG级别的日
        志，也不会被输出 -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>INFO</level>
        </filter>
        <encoder>
            <Pattern>${CONSOLE_LOG_PATTERN}</Pattern>
            <!-- 设置字符集 -->
            <charset>UTF-8</charset>
        </encoder>
    </appender>
    <!--输出到文件-->
    <!-- 时间滚动输出 level为 INFO 日志 -->
    <appender name="INFO_FILE"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 正在记录的日志文件的路径及文件名 -->
        <file>${log.path}/log_info.log</file>
        <!--日志文件输出格式-->
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level
                %logger{50} - %msg%n
            </pattern>
            <charset>UTF-8</charset>
        </encoder>
        <!-- 日志记录器的滚动策略，按日期，按大小记录 -->
        <rollingPolicy
                class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- 每天日志归档路径以及格式 -->
            <fileNamePattern>${log.path}/info/log-info-%d{yyyy-MM-
                dd}.%i.log
            </fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy
                    class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!--日志文件保留天数-->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
        <!-- 此日志文件只记录info级别的 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>
 
    <!-- 时间滚动输出 level为 WARN 日志 -->
    <appender name="WARN_FILE"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 正在记录的日志文件的路径及文件名 -->
        <file>${log.path}/log_warn.log</file>
        <!--日志文件输出格式-->
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level
                %logger{50} - %msg%n
            </pattern>
            <charset>UTF-8</charset> <!-- 此处设置字符集 -->
        </encoder>
        <!-- 日志记录器的滚动策略，按日期，按大小记录 -->
        <rollingPolicy
                class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${log.path}/warn/log-warn-%d{yyyy-MM-
                dd}.%i.log
            </fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy
                    class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!--日志文件保留天数-->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
        <!-- 此日志文件只记录warn级别的 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>warn</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>
 
    <!-- 时间滚动输出 level为 ERROR 日志 -->
    <appender name="ERROR_FILE"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 正在记录的日志文件的路径及文件名 -->
        <file>${log.path}/log_error.log</file>
        <!--日志文件输出格式-->
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level
                %logger{50} - %msg%n
            </pattern>
            <charset>UTF-8</charset> <!-- 此处设置字符集 -->
        </encoder>
        <!-- 日志记录器的滚动策略，按日期，按大小记录 -->
        <rollingPolicy
                class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${log.path}/error/log-error-%d{yyyy-MM-
                dd}.%i.log
            </fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy
                    class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!--日志文件保留天数-->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
        <!-- 此日志文件只记录ERROR级别的 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>
 
    <!--
    <logger>用来设置某一个包或者具体的某一个类的日志打印级别、以及指
    定<appender>。
    <logger>仅有一个name属性，
    一个可选的level和一个可选的addtivity属性。
    name:用来指定受此logger约束的某一个包或者具体的某一个类。
    level:用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL
    和 OFF，
    如果未设置此属性，那么当前logger将会继承上级的级别。
    -->
    <!--
    使用mybatis的时候，sql语句是debug下才会打印，而这里我们只配置了info，所以想
    要查看sql语句的话，有以下两种操作：
        第一种把<root level="INFO">改成<root level="DEBUG">这样就会打印sql，不过
            这样日志那边会出现很多其他消息
        第二种就是单独给mapper下目录配置DEBUG模式，代码如下，这样配置sql语句会打
            印，其他还是正常DEBUG级别：
-->
 
    <!--开发环境:打印控制台-->
    <springProfile name="dev">
        <!--可以输出项目中的debug日志，包括mybatis的sql日志-->
        <logger name="com.guli" level="INFO"/>
        <!--
        root节点是必选节点，用来指定最基础的日志输出级别，只有一个level属性
        level:用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR,
        ALL 和 OFF，默认是DEBUG
        可以包含零个或多个appender元素。
        -->
        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
            <appender-ref ref="INFO_FILE"/>
            <appender-ref ref="WARN_FILE"/>
            <appender-ref ref="ERROR_FILE"/>
        </root>
    </springProfile>
    <!--生产环境:输出到文件-->
    <springProfile name="pro">
        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
            <appender-ref ref="DEBUG_FILE"/>
            <appender-ref ref="INFO_FILE"/>
            <appender-ref ref="ERROR_FILE"/>
            <appender-ref ref="WARN_FILE"/>
        </root>
    </springProfile>
</configuration>
```

### 3.4.2 将错误日志输出到文件

GlobalExceptionHandler.java 中

类上添加注解

![img](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/20210907180122271.png)



![image-20220415180920369](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220415180920369.png)

![image-20220409235356895](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220409235356895.png)





![image-20220410000036332](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220410000036332.png)








# 四、Bug记录

## userMapper爆红

![image-20220407204123176](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220407204123176.png)





## MP自动填充时，修改时间和创建时间同步更改

不勾选根据当前时间戳更新即可

![image-20220408143910110](https://typora-1259403628.cos.ap-nanjing.myqcloud.com/image-20220408143910110.png)



# 五、文章参考

[齐全的swagger注解介绍 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/49996147)

[后端 API 接口文档 Swagger 使用指南 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/98560871#:~:text=一：swagger是什么？ Swagger是一款RESTFUL接口的文档在线自动生成%2B功能测试功能软件。,Swagger是一个规范和完整的框架%2C用于生成、描述、调用和可视化RESTful风格的Web服务。 目标是使客户端和文件系统作为服务器以同样的速度来更新文件的方法%2C参数和模型紧密集成到服务器。)
