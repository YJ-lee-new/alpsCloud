#apls cloud 搭建日记 ----   Yujie.lee 2019-11-29
Springboot:2.1.1.RELEASE ---> v1.0.0 升级到  Springboot:2.1.6.RELEASE 
Springcloud:Greenwich.RELEASE ---> v1.1.0 升级到  Greenwich.SR2

#eureka
开启安全认证:security.
2.X以上会默认开启csrf验证，会导致客户端无法联上

#负载均衡 Ribbon & feign

#熔断:hystrix

#网关 zuul -->  后续有时间再升级到springcloud gateway  
 ---> v1.1.0 升级到 gateway并实现智能动=态网关



# Redis作为缓存管理
redis version 3.2
   进入cmd界面，直接运行命令redis-server.exe redis.windows.conf。如果报错，依次执行第一条指令：redis-cli.exe，第二条指令：shutdown第三条指令：exit
  
Redis 设置 redis.windows.conf：
屏蔽  127.0.0.1 redis.windows.conf --> #bind 127.0.0.1 
设置密码/屏蔽密码: protected-mode yes
设置 持久化的问题，可通过stop-writes-on-bgsave-error值设置为no即可避免这种问题  ：stop-writes-on-bgsave-error no

#日志管理：logback
--->v1.0.0实现log动态管理,由alps-platform-log统一提供服务

#链路追踪:ZIPKIN
SpringBoot2.X以后版本不再支持自已建zipKin Service.
https://github.com/openzipkin/zipkin下载新版
或者https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/下载jar包
 直接运行即可,默认端口是9411:java -jar zipkin-server-2.12.9-exec.jar 也可用如下配置命名启动
java -jar  zipkin-server-2.12.9-exec.jar --zipkin.collector.rabbitmq.addresses=10.202.86.138 --STORAGE_TYPE=mysql --MYSQL_DB=alps_platform  --MYSQL_HOST=10.202.86.138 --MYSQL_TCP_PORT=3306 --MYSQL_USER=root --MYSQL_PASS=liyujie


  

#进程 与线程的管理 Sleuth + RabbitMQ
因RabbitMQ是Erlang语言开发的,所以需要先安装安装Erlang
下载：https://www.erlang.org/downloads

然后再下载RabbitMQ
下载地址:http://www.rabbitmq.com/download.html
 若是安装启动之后无法访问:http://localhost:15672/ 打开RabbitMQ Server的开始菜单安装目录
选择RabbitMQ Command Prompt 命令行并打开，输入
rabbitmq-plugins enable rabbitmq_management安装插件
再启动sever
若再报:4369端口被占用,请先解决端口占用问题:netstat -aon|findstr "4369" tasklist|findstr "10004                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            "
若安装插件报错，需要把C:\Users\账号下胡的.erlang.cookie Copy同步到C:\Windows\System32\config下

-----特别说明： 
我是从官网上面下载的最新的rabbitmq-server-3.8.1.exe。 然后是各种无法启动，研究了半天，终于可以正常启动了。然后又发现无法访问http://localhost:15672/。
又是各种尝试解决，又浪费了半天无果。然后又是重新卸载，重装，甚至怀疑用的云桌面有问题，又换电脑重装，反正能想的办法都想到了，把官方的文档也研究了遍。实在无办法！无只能想是不是当前的Release的版本本身有Bug。
然后去网上下了之前的版本：rabbitmq-server-3.7.5.exe 
安装后输入指令激活插件：rabbitmq-plugins.bat enable rabbitmq_management
重启服务器：net stop RabbitMQ && net start RabbitMQ
一次性搞定，从下载到启动不到2分钟。
然后心里就.......！！！  顿时对这个RabbitMQ没好感了，等有时间了一定第一个就把它给换了,换成 RocketMQ或者ActiveMQ！

然后重新启动zipKin.

当需要与RabbitMQ配合时，启动命令：

java -jar zipkin-server-2.12.2-exec.jar --zipkin.collector.rabbitmq.addresses=localhost

但是神奇般的报错了.提示连接超时.首先想到的是否是版本的问题 ,查了下有可能:Zipkin最新2.12.9版从2.12.6开始有个较大的更新，迁移使用了Armeria HTTP引擎。
所以降到了zipkin-server-2.12.2-exec.jar.但还是报错.弄了一晚上.然后又把RabbitMQ换到rabbitmq-server-3.7.8.exe
还是行,后面又参考https://www.cnblogs.com/amylis_chen/p/6286104.html重新配了一遍环境变量.还是不行.

关机一晚上,第二天开机启动,还是行,然后再次启动,又可以了.泪奔! 网上一查,发现rabbitmq有各种奇葩无解的事,哎.
然后分别查看http://localhost:15672/#/queues 下面会多出一个zipkin的对例,表示成功.


#在zipkin中使用RabbitMQ
使用如下命名启动:RABBIT_ADDRESSES=loclhost java -jar zipkin.jar 或者  java -jar zipkin-server-2.12.9-exec.jar --zipkin.collector.rabbitmq.addresses=10.202.86.138
而连接到 RabbitMQ 的用户名和密码就用默认的 guest 默认访问页面为: http://10.202.86.138:15672 

属性                                                                                                                                    	环境变量	                                                               描述
zipkin.collector.rabbitmq.concurrency	RABBIT_CONCURRENCY	并发消费者数量，默认为 1
zipkin.collector.rabbitmq.connection-timeout	RABBIT_CONNECTION_TIMEOUT	建立连接时的超时时间，默认为 60000 毫秒，即 1 分钟
zipkin.collector.rabbitmq.queue	RABBIT_QUEUE	从中获取 span 信息的队列，默认为 zipkin
zipkin.collector.rabbitmq.uri	RABBIT_URI	符合 RabbitMQ URI 规范 的 URI，例如 amqp://user:pass@host:10000/vhost
		
如果设置了 URI，则以下属性将被忽略。		
		
属性	          环境变量                                                                                                                     	 描述
zipkin.collector.rabbitmq.addresses	RABBIT_ADDRESSES	用逗号分隔的 RabbitMQ 地址列表，例如 localhost:5672,localhost:5673
zipkin.collector.rabbitmq.password	RABBIT_PASSWORD	连接到 RabbitMQ 时使用的密码，默认为 guest
zipkin.collector.rabbitmq.username	RABBIT_USER	连接到 RabbitMQ 时使用的用户名，默认为 guest
zipkin.collector.rabbitmq.virtual-host	RABBIT_VIRTUAL_HOST	使用的 RabbitMQ virtual host，默认为 /
zipkin.collector.rabbitmq.use-ssl	RABBIT_USE_SSL	设置为 true 则用 SSL 的方式与 RabbitMQ 建立链接


#关于分布式配置及部署
因工作电脑只有3G多内存，实在是跑不动了(Doker最低要求也需要4G，真的是无力吐槽了)。
且目前业务暂不会用到此部分，而且未来大概率也不会上公有云。LoadingBlance方案团队有高手，故暂时不实现，在以后有条件时再加上。

#关于配置中心Config
---- 待完成
#关于Jenkins实现Spring Cloud自动化部署
 巧妇难为无米之炊啊，没有服务器！没有服务器！没有服务器！
 好不容易坑蒙拐骗了一台服务器，配置还行，超过8核16G内存，150G硬盘。想着，可以在上面虚拟几台架想来了。然后开始准备，后面才发现，别人给的也是一台VM！ 尴尬了。
找不到CentOS,转换用Windows做服务器吧，找几台开发PC开始吧。
具体搭建可以参考文档《alpsCloud jenkinst部署说明V1.0.0》

	#!/bin/bash 
	 
	#export BUILD_ID=dontKillMe这一句很重要，这样指定了，项目启动之后才不会被Jenkins杀掉。
	export BUILD_ID=dontKillMe
	 
	#指定最后编译好的jar存放的位置 即是发布目录
	#jar_path=D:/jenkins/workspace/target
	jar_path=D:/alps/target
	 
	#获取运行编译好的进程ID，便于我们在重新部署项目的时候先杀掉以前的进程
	#pid1=$(pgrep -f alps-server.jar)
	#pid2=$(pgrep -f alps-oauth-uaa-admin.jar)
	#pid3=$(pgrep -f alps-oauth-server.jar)
	#pid4=$(pgrep -f alps-gateway.jar)
	#pid5=$(pgrep -f alps-system-admin.jar)
	#pid6=$(pgrep -f alps-file.jar)
	
	
	 #获取运行编译好的进程ID，便于我们在重新部署项目的时候先杀掉以前的进程
	PID1=ps -ef | grep alps-server.jar| grep -v grep | awk '{print $2}'
	PID2=ps -ef | grep alps-gateway.jar| grep -v grep | awk '{print $2}'
	PID3=ps -ef | grep alps-oauth-uaa-admin.jar| grep -v grep | awk '{print $2}'
	PID4=ps -ef | grep alps-oauth-server.jar| grep -v grep | awk '{print $2}'
	PID5=ps -ef | grep alps-system-admin.jar| grep -v grep | awk '{print $2}'
	PID6=ps -ef | grep alps-file.jar| grep -v grep | awk '{print $2}'

    
    echo ${PID1} ${PID2} ${PID3} ${PID4} ${PID5} ${PID6}
    
    if [ -z "$PID1" ]
	then
	    echo alps-server is already stopped
	else
	    echo kill $PID1
	    kill -9 $PID1
	fi
	
	 if [ -z "$PID2" ]
	then
	    echo alps-gateway is already stopped
	else
	    echo kill $PID2
	    kill -9 $PID2
	fi
	
		 if [ -z "$PID3" ]
	then
	    echo alps-oauth-uaa-admin is already stopped
	else
	    echo kill $PID3
	    kill -9 $PID3
	fi
	
			 if [ -z "$PID4" ]
	then
	    echo alps-oauth-server is already stopped
	else
	    echo kill $PID4
	    kill -9 $PID4
	fi
	
			 if [ -z "$PID5" ]
	then
	    echo alps-system-admin is already stopped
	else
	    echo kill $PID5
	    kill -9 $PID6
	fi
	
				 if [ -z "$PID6" ]
	then
	    echo alps-file is already stopped
	else
	    echo kill $PID6
	    kill -9 $PID6
	fi
	 
	 
	
	 
	#依次启动子服务
	sleep 30
	nohup java -Xmx512m -jar ${jar_path}/alps-server.jar out.file 2>&1 &
	sleep 30
	nohup java -Xmx512m -jar ${jar_path}/alps-gateway.jar out.file 2>&1 &
	sleep 30
	nohup java -Xmx512m -jar ${jar_path}/alps-system-admin.jar out.file  2>&1 &
	sleep 30
	nohup java -Xmx512m -jar ${jar_path}/alps-file.jar out.file 2>&1 &
	sleep 30
	nohup java -Xmx512m -jar ${jar_path}/alps-oauth-uaa-admin.jar out.file  2>&1 &
	sleep 30
	nohup java -Xmx512m -jar ${jar_path}/alps-oauth-server.jar out.file 2>&1 &
	 
	 
	#关闭上次启动的子服务
	#kill ${pid1} ${pid2} ${pid3} ${pid4} ${pid5} ${pid6}

#Windos jenkins Server上面收集jar包批处理命令

	 
			echo "先删除D:/jenkins/workspace/target下面的文件"
			::del /f /s /q D:\jenkins\workspace\alps\target\*.*
	        md D:\jenkins\workspace\alps\target
	        SET target_path=D:\jenkins\workspace\alps\target\
	        
	        echo "jar包路径"
	        SET jar_path_server=D:\jenkins\workspace\alps\alps-server\target\alps-server.jar
			SET jar_path_auth_admin=D:\jenkins\workspace\alps\alps-oauth\alps-oauth-uaa-admin\target\alps-oauth-uaa-admin.jar
			SET jar_path_auth_server=D:\jenkins\workspace\alps\alps-oauth\alps-oauth-server\target\alps-oauth-server.jar
			SET jar_path_gateway=D:\jenkins\workspace\alps\alps-gateway\target\alps-gateway.jar
			SET jar_path_system=D:\jenkins\workspace\alps\alps-system\alps-system-admin\target\alps-system-admin.jar
			SET jar_path_file=D:\jenkins\workspace\alps\alps-file\target\alps-file.jar
			SET jar_path_zuul=D:\jenkins\workspace\alps\alps-zuul\target\alps-zuul.jar
	
			echo.
			echo "复制 所有jar包到D:\jenkins\workspace\target\ 目录下"
			copy /y %jar_path_server% %target_path%
			copy /y %jar_path_auth_admin% %target_path%
			copy /y %jar_path_auth_server% %target_path%
			copy /y %jar_path_gateway% %target_path%
			copy /y %jar_path_system% %target_path%
			copy /y %jar_path_file% %target_path%
			copy /y %jar_path_zuul% %target_path%

#window 应用服务器上面开启关闭程式批处理命令.

            cd D://alps/target  
			@set port=8888
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%
			
						
			cd D://alps/target 
			@set port=9200
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%

			
			cd D://alps/target
			@set port=8500
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%
			
						
			cd D://alps/target 
			@set port=8600
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%
			
						
			cd D://alps/target 
			@set port=8009
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%
			
			cd D://alps/target 
			
			@echo#---- 原本想用choice /t 15 /d y /n每个服务启动后等待一段时间再启动下一个,在Windows中单独执行是OK的,但是通过jenkins远程调用会一直等待.
            
            start cmd /c java -jar alps-server.jar
           
            start cmd /c java -jar alps-gateway.jar
            
            start cmd /c java -jar alps-oauth-uaa-admin.jar
            
            start cmd /c java -jar alps-oauth-server.jar
            
            start cmd /c java alps-system-admin.jar
           
            
            exit 0
         
                 
          --------------------alps-server.bat---------------
			
            cd D://alps/target  
			@set port=8888
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%
			echo "启动alps-server"
			start cmd /c alps-server.jar --server.port=%port%	
			 exit 0
			----------------------------alps-gateway.bat---------------------	
			
			cd D://alps/target 
			@set port=9200
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%
			echo "启动alps-gateway"
			start cmd /c alps-gateway.jar --server.port=%port%	
			 exit 0
			-----------------------------alps-oauth-uaa-admin.bat--------------------	
			
			cd D://alps/target
			@set port=8500
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%
			echo "启动alps-oauth-uaa-admin"
			start cmd /c alps-oauth-uaa-admin.jar --server.port=%port%		
			 exit 0
			----------------------------alps-oauth-server.bat------------------	
			
			cd D://alps/target 
			@set port=8600
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%
			echo "启动alps-oauth-server"
			start cmd /c alps-oauth-server.jar --server.port=%port%	
			 exit 0
			----------------------------alps-system-admin.bat-----------------	
			
			cd D://alps/target
			@set port=8009
			@echo %port%
			
			for /f "tokens=5" %%i in ('netstat -aon ^| findstr %port%') do (
			    set n=%%i
			    goto js
			)
			:js
			taskkill /f /pid %n%
			echo "启动alps-system-admin"
			start cmd /c alps-system-admin.jar --server.port=%port%					
            exit 0

#关于使用Zookeeper/Nginx实现集群
---- 待完成

#关于前端
前端选型是用Vue 还是继续沿用大家比较熟悉的前端框架：Bootstrap /LayUI,待团队决定。
 --->后续会由vue提供统一前端.
#关于原来SpringBoots已实现的web务业是否全部调整？
目前所实现的功能，大部分可独立自为一个服务运行，故无需要全部转换成Springcloud 前后分享架构。
共用部分是否转换，或是怎么转换，等讨论确认。


#JWT:
keytool -genkey -v -alias slserver -keyalg RSA -keysize 1024 -keypass 123456 -dname "cn=alps,ou=itss,o=soft,l=wuhan,st=hubie,c=cn" -keystore D:\alps.jks -storepass 123456 -validity 3650



#springboot 2.1.3之前与AMQP有Bug，升级到2.1.6版本
As there is a bug in Spring AMQP, which will be fixed in Release 2.1.3 Issue 

