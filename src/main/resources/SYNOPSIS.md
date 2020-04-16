#aplsCloud 简介 ----   Yujie.lee 2019-11-04

#技术栈

                                            前端
            vue            bootstrap             layui         node



                                       服务端
            SpringBoot               Zuul                    Feign
           Hystrix                  Eureka                  Ribbon
           ZipKin                   Redis                   Sleuth
           Sleuth                   RabbitMQ                logback
           security                 Shiro                   Thymeleaf
           MyBatis                  Drools                  Druid
           Fastjson                 Pagehleper              Mapper
           swagger2                 docker                  Config
           Jenkins                  Nginx                   Zookeeper
           gateway                  mybatisplus             oauth2


                                      数据存储
           Mysql                    Sql-Server              Oracle  
          MongoDB       

#项目结构
>   alps ---------------------------------------------------- 主项目
<br>alps-file ---------------------------------------------- 文件服务
<br>alps-lbs  ---------------------------------------------  负载均衡服务（可忽略）
<br>alps-lbs-feign ----------------------------------------- feign（可忽略）
<br>alps-lbs-ribbon ---------------------------------------- ribbon（可忽略）
<br>alps-provider ------------------------------------------ 服务提供者
<br>alps-provider-ace -------------------------------------- ACE服务提供
<br>alps-provider-app -------------------------------------- 移动服务提供
<br>alps-provider-report ----------------------------------- 报表服务提供
<br>alps-provider-system ----------------------------------- 共用权限服务提供
<br>alps-server -------------------------------------------- 注册中心服务
<br>alps-tools --------------------------------------------- 工具服务主目录
<br>alps-tools-quartz -------------------------------------- 排程Job服务提供
<br>alps-web ----------------------------------------------- web主目录
<br>alps-web-admin ----------------------------------------- web admin主目录
<br>alps-web-common ---------------------------------------- common 共用主目录
<br>alps-web-framework --------------------------------------web framework 共用主目录
<br>alps-web-system ---------------------------------------- system 共用主目录(可忽略)
<br>alps-zuul ---------------------------------------------- 网关服务，负载均衡服务

#权限认证方案一：ZUUL + shiro + Redis   实现以下功能：

1.外部请求统一从网关zuul进入，内部服务相互调用不经过zuul，但接口需要要校验权限(或者不需要权限)-----或者用分布式Session。

2.cloud和shiro结合，达到单点登录，和集中一个服务完成权限管理，其他业务服务不需要关注权限如何实现

3.其他服务依然可以控制权限细粒度到接口，如在接口上使用@RequirePermisson等注解，方便开发      
  

#权限认证方案二：gateway + Security + oauth2 + RBAC + Redis   实现以下功能：

1.外部请求统一从网关gateway进入，内部服务相互调用不经过gateway，但接口需要要校验权限(或者不需要权限)。

2.提供统一权限认证中心,SSO.

3.其他服务依然可以控制权限细粒度到接口，如在接口上使用@RequirePermisson等注解，方便开发        

                          