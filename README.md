# 权限过滤器

## 前言

在实际项目开发中，我们经常会遇到对某些用户进行权限的管理，而如果我们对每一个资源和请求都写上权限判断的代码，这对我们后期维护和代码移植上都会造成很大的困扰，所以以此我们对权限管理部分封装部分细节，方便大部分人能够直接引入调用。

对此我们在Spring的AOP模式或者在传统java的动态代理上对其设计了一套权限过滤器插件，以注解方式来完成对权限的管理

## 介绍

### 安装

1、将Permission-Filter.jar包导入到项目库里，在web.xml配置容器启动器

```xml
<!--配置权限容器-->
<context-param>
    <param-name>filterFactoriesListenerClass</param-name>
    <param-value>启动类的具体位置(com.*.*.java)</param-value>
</context-param>
<listener>
    <listener-class>com.tjrac.filter.context.ContextLoaderListener</listener-class>
</listener>
```

2、使用@FilterInjection注解获得权限容器的对象，必须为static类型的变量

```java
@FilterInjection
static public PermissionFactories permissionFactories;

@Resource
PermissionDao permissionDao;

// 是否为第一次初始化
static boolean init = false;

/**
 * 初始化所有权限
 */
@PostConstruct
public void permissionsInit(){
    if (!init) {
        System.out.println("初始化权限对象");
        List<Permission> permissions = permissionDao.queryAll();
        for (Permission permission : permissions){
            try {
                permissionFactories.writePermission(permission);
            } catch (KeyRepeatException | KeyNullException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        permissionFactories.setConfigStrict(true);
    }
    init = true;
}
```
### 入口

#### 切点入口

我们构建了一个抽象函数ProxyControl.java，通过继承该类，用spring注解来进行切点和切面的定义

```java
@Aspect
@Component
public class ProxyControlImpl extends ProxyControl {

    /**
     * 设置切点,扫描com.tjrac.generalpermissions.service包下的所有类
     */
    @Pointcut("execution(public * com.tjrac.generalpermissions.service.*.*(..))")
    public void proxyControlService() {}


    @Override
    @Around(value = "proxyControlService() && @annotation(permissionControl)")
    public Object permissionControl(ProceedingJoinPoint pjp, PermissionControl permissionControl) throws Throwable {
        return super.permissionControl(pjp, permissionControl);
    }

    @Override
    @Around(value = "proxyControlService() && @annotation(parameterFilter)")
    public Object parameterFilter(ProceedingJoinPoint pjp, ParameterFilter parameterFilter) throws Throwable {
        return super.parameterFilter(pjp, parameterFilter);
    }
}
```

#### 拦截器入口

此入口依赖于SpringMVC，我们构建了HandlerInterceptor拦截器抽象类，通过继承去实现permissionIsEmpty和handler两个方法进行对请求地址的拦截处理，permissionIsEmpty是在没有权限对象1时候执行的操作，handler是权限不足的时候执行的操作

```java
public class MyFilter extends HandlerInterceptor {

    /**
     * 权限为空时处理
     */
    @Override
    public void permissionIsEmpty(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect("/error/nologin.html");
    }

    /**
     * 权限不通过时处理
     */
    @Override
    public void handler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect("/error/inaccessible.html");
    }

}
```

此外，我们还需要在springMVC的配置文件里加入拦截器的配置，如下

```xml
<!-- 配置拦截器 -->
<mvc:interceptors>
    <mvc:interceptor>
        <mvc:mapping path="/manage/**" />
        <bean class="com.tjrac.generalpermissions.filter.MyFilter" />
    </mvc:interceptor>
</mvc:interceptors>
```

### 拦截方法

#### 静态资源

配置静态资源的拦截器，继承插件的HandlerInterceptor类进行对无权限时的处理，访问到固定资源

```xml
<mvc:interceptor>
    <mvc:mapping path="/resources/**" />
    <bean class="com.tjrac.generalpermissions.filter.JPGFilter" />
</mvc:interceptor>
```

root账号访问：

![](\img\yanshi1.jpg)

无权限用户访问：

![](\img\yanshi2.jpg)

#### 业务方法拦截

在业务方法上添加@PermissionControl("权限名")，既可以对此方法拦截，权限不通过将会抛出PermissionDeniedException的异常(参考下面异常表)

```java
@PermissionControl("添加用户")
@Override
public String addUser(User user) throws MsgException {
    // TODO 添加用户
}
```

演示如下：

![](\img\yanshi3.jpg)

#### 业务方法参数拦截

在业务方法上添加@ParameterFilter注解可以对业务方法的参数进行筛选验证，不通过时抛出PermissionDeniedException异常

```java
@PermissionControl("查询用户")
@ParameterFilter(param = {@Param(index = 0, sessionKey = SessionPermission.QUERY_SINGLEUSER_RANGE)})
@Override
public User queryByUsername(String username) throws MsgException {
    // TODO 查询用户
}
```

```java
if (user.getUsername().equals("root")){
    // ROOT用户可以查询所有的用户
    session.setAttribute(SessionPermission.QUERY_SINGLEUSER_RANGE, userService.queryAllListParam());
} else {
    // 其他用户只能查询自己
    session.setAttribute(SessionPermission.QUERY_SINGLEUSER_RANGE, new ObjectParam(user.getUsername()));
}
```

演示如下：

![](\img\yanshi4.jpg)

![](\img\yanshi5.jpg)

### 关于权限构建

#### Java实体类设置

角色实体类实现VerificationRole接口，权限实体实现Verification接口即可

#### 实例项目

![](\img\yanshi6.jpg)

![](\img\yanshi7.jpg)

## 插件结构

### 实体类（entity）

#### 角色表

```java
public class Role {
    Integer id;     // 角色编号
    String name;    // 角色名称
    String desc;    // 角色描述
    Permit[] permits;   // 拥有的权限表
}
```

#### 权限表

```java
public class Permission {
    Integer id;     // 权限编号
    String name;    // 权限名称
    String desc;    // 权限描述
}
```

### 角色权限容器（RoleFactories）

### 传统servlet方式

### Spring方式

#### 相关依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${spring.version}</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
    <version>${spring.version}</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>${spring.version}</version>
</dependency>
```

## 检验方式

基于RBAC模型，我们开放了角色检验和参数检验的两个接口

### 权限角色(VerificationRole)

#### 接口实现

```java
public class Role implements VerificationRole {
    Integer id;     // 角色编号
    String name;    // 角色名称
    String desc;    // 角色描述
    List<Permission> permissions;   // 拥有的权限表

    /**
     * 验证权限
     * @param permission 权限
     */
    @Override
    public boolean verification(Permission permission) {
        for (Permission p : permissions){
            if (p.equals(permission))
                return true;
        }
        return false;
    }
}
```

#### 示例源码

```java
@PermissionControl("插入分组")
public String insert(Group group) throws MsgException {
	// 具体实现
}
```

### 参数验证(VerificationParam)

#### 接口实现

```java
public class ObjectParam implements VerificationParam {
    Object object;

    public ObjectParam(Object object) {
        this.object = object;
    }

    @Override
    public boolean verification(Object o) {
        return o.equals(object);
    }
}
```

```java
public class ListParam implements VerificationParam {
    List<Object> list;

    public ListParam(List<Object> list) {
        this.list = list;
    }

    @Override
    public boolean verification(Object o) {
        for (Object l : list){
            if (l.equals(o)){
                return true;
            }
        }
        return false;
    }
}
```

#### 示例源码

```java
@ParameterFilter(param = {@Param(index = 0, sessionKey = SessionPermission.PERMISSION_USER_ID)})
public List<Person> queryAllByUserId(int userId) {
    // 具体实现
}
```

```java
class Login(HttpSession session){
    if (/*登陆成功*/){
        session.setAttribute(SessionPermission.PERMISSION_USER_ID, new ObjectParam(user.getUser_id()));
    }
}
```

### 接口说明

```java
public interface VerificationParam {
    boolean verification(Object o);
}
```

```java
public interface VerificationRole {
    boolean verification(Permission permission);
}
```

不管是（**权限角色**[VerificationRole]）的接口还是（**参数验证**[VerificationParam]）的接口，我们只需要实现它的**verification**方法即可，整个过滤器只需要根据注解信息取调用接口的这个方法进行权限的判断，如果权限不通过会抛出**PermissionDeniedException**异常

### 抛出异常说明

| 异常                      | 说明                                         |
| ------------------------- | -------------------------------------------- |
| FilterException           | 本插件异常的基类，继承于Exception异常类      |
| KeyNullException          | 写入权限池时键值为空，或者查询是匹配键值为空 |
| KeyRepeatException        | 写入权限池时键值重复                         |
| PermissionDeniedException | 权限验证失败                                 |

