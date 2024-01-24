# Nginx安装配置

## nginx安装配置笔记

```
1.操作系统的选择，centos7
2.下载安装nginx的方式
rpm包安装
yum工具自动安装
获取源代码，手动编译安装nginx（指定安装路径，额外的开启nginx第三方的功能）
3.现在了nginx编译安装，那么就得解决好linux的编译开发环境，gcc make编译工具
使用yum工具之前，必须配置好阿里云的yum源
步骤1: 确保有wget命令
yum install wget -y
步骤2:
mkdir /etc/yum.repos.d/repobak  
mv /etc/yum.repos.d/* /etc/yum.repos.d/repobak
#如上的操作，就是使得在yum仓库的一层目录，没有任何repo文件，因此就实现了linux此时没有yum源了，此时没有yum源，需要你再配置一个新的repo仓库文件
可以访问阿里云的镜像站：https://developer.aliyun.com/mirror/
用如下命令生成新的yum源，其实也就是下载了一个新的repo文件
wget -O /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-7.repo
wget -O /etc/yum.repos.d/epel.repo https://mirrors.aliyun.com/repo/epel-7.repo
#清空旧的yum缓存
yum clean all
#生成新的阿里云的yum缓存，加速下次下载
yum makecache
#在配置了阿里云的yum源之后，来安装如下编译工具
yum install -y gcc gcc-c++ autoconf automake make
#安装使用nginx还得安装nginx所需的一些第三方系统库的支持，比如nginx的静态资源压缩功能所需的gzip，lib库，nginx需要支持URL重写所需的pcre库，perl开发的依赖库，以及nginx搭建加密站点https所需的openssl依赖库等
yum install zlib zlib-devel openssl openssl-devel pcre pcre-devel wget httpd-tools vim
#安装完毕nginx所需的基础依赖库，还得检查系统的防火墙是否关闭，selinux，yum配置，网络情况等等
执行如下命令检查
iptables -L
getenforce
ping baidu.com
```

## 编译安装nginx

```
1.下载程序源代码，从nginx官网nginx.org下载的代码是源nginx代码
#进到/opt目录执行wget下载
wget http://nginx.org/download/nginx-1.23.4.tar.gz
还可以获取淘宝的nginx代码
http://tengine.taobao.org/	#淘宝nginx官网
wget http://tengine.taobao.org/download/tengine-2.3.3.tar.gz
2.解压缩淘宝nginx或者官网nginx源代码都行，自行选择
tar -zxvf tengine-2.3.3.tar.gz
2.1 拷贝nginx的配置文件语法高亮，发给vim的插件目录
[root@basic ~]# mkdir -p ~/.vim
[root@basic ~]# cp -r /opt/tengine-2.3.3/contrib/vim/* ~/.vim/

3、解压缩nginx源代码后，进入源代码的目录，准备开始编译安装
进入源代码目录后，查看目录下有哪些内容
total 420
-rw-rw-r--  1 root root    931 Mar 29  2021 AUTHORS.te
drwxrwxr-x  6 root root   4096 Mar 29  2021 auto	检测系统模块依赖信息
-rw-rw-r--  1 root root 302927 Mar 29  2021 CHANGES	存放nginx的变化记录日志
-rw-rw-r--  1 root root  27289 Mar 29  2021 CHANGES.cn
-rw-rw-r--  1 root root  34831 Mar 29  2021 CHANGES.te
drwxrwxr-x  2 root root   4096 Mar 29  2021 conf	存放nginx主配置文件的目录
-rwxrwxr-x  1 root root   2502 Mar 29  2021 configure	可执行的脚本，用于释放编译文件的定制脚本
drwxrwxr-x  4 root root   4096 Mar 29  2021 contrib	提供了vim插件，让配置文件颜色区分，更友好
drwxrwxr-x  4 root root   4096 Mar 29  2021 docs
drwxrwxr-x  2 root root   4096 Mar 29  2021 html	存放了标准的html资源文件
-rw-rw-r--  1 root root   1715 Mar 29  2021 LICENSE
drwxrwxr-x  2 root root   4096 Mar 29  2021 man
drwxrwxr-x 26 root root   4096 Mar 29  2021 modules
drwxrwxr-x  3 root root   4096 Mar 29  2021 packages
-rw-rw-r--  1 root root   3450 Mar 29  2021 README.markdown
drwxrwxr-x 10 root root   4096 Mar 29  2021 src	存放了nginx源代码的目录
drwxrwxr-x  4 root root   4096 Mar 29  2021 tests
-rw-rw-r--  1 root root     43 Mar 29  2021 THANKS.te

4.开始准备编译三部曲
第一曲：进入软件源代码目录，执行编译脚本文件，如制定安装路径，以及开启额外功能等
首先，查看编译脚本的信息
./configure  --help	#查看编译帮助信息
#编译先需要安装
yum -y install openssl openssl-devel
yum -y install pcre-devel
执行编译脚本文件，释放makefile等信息
./configure --prefix=/opt/tngx232 --with-http_ssl_module --with-http_flv_module --with-http_gzip_static_module --with-http_stub_status_module --with-threads --with-file-aio

第二曲：（直接开始下一步安装）输入make指令
第三曲：（如果点击开始安装）make install
5.查看安装后的nginx目录
total 16
drwxr-xr-x 2 root root 4096 May  8 10:59 conf	存放nginx的配置文件，如nginx.conf
drwxr-xr-x 2 root root 4096 May  8 10:59 html	存放nginx的网页根目录文件，存放站点的静态文件数据
drwxr-xr-x 2 root root 4096 May  8 10:59 logs	存放nginx的各种日志目录
drwxr-xr-x 2 root root 4096 May  8 10:59 sbin	存放该软件的可执行文件
6.若是直接 执行nginx命令,默认是代表启动nginx进程
[root@basic tngx232]# nginx
-bash: nginx: command not found
#出现了如上的问题，如何解决？
配置PATH变量，把nginx的sbin目录加入到PATH当中
6.1 编写一个nginx的专属环境变量，创建一个脚本文件
[root@basic tngx232]# cat /etc/profile.d/nginx.sh 
export	PATH="$PATH:/opt/tngx232/sbin/"
7.退出当前会话，重新登录，系统默认加载/etc/profile.d下所有的环境变量文件
exit
#退出会话后重新登陆后，查看nginx的环境变量是否被添加了
[root@basic ~]# echo $PATH
/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/opt/tngx232/sbin/:/usr/java/default/bin:/opt/bigdata/hadoop-2.6.5/bin:/opt/bigdata/hadoop-2.6.5/sbin:/opt/mashibing/redis5/bin:/root/bin
8.此时可以快捷的使用nginx各种指令了
nginx	#首次直接输入nginx，表示启动该进程，如果你再次启动则报错,端口被占用
nginx -s stop	#可以停止nginx后，再次启动nginx服务，用于重新加载nginx配置
# 如果不想重启nginx，直接重新加载配置文件，nginx提供了一个reload功能，能够在不重启nginx服务下，直接重新读取配置文件功能
nginx -s reload	#平滑重启

9.此时检查nginx的编译安装信息
nginx -V
[root@basic ~]# nginx -V
Tengine version: Tengine/2.3.3
nginx version: nginx/1.18.0
built by gcc 4.4.7 20120313 (Red Hat 4.4.7-23) (GCC) 
built with OpenSSL 1.0.1e-fips 11 Feb 2013
TLS SNI support enabled
configure arguments: --prefix=/opt/tngx232 --with-http_ssl_module --with-http_flv_module --with-http_gzip_static_module --with-http_stub_status_module --with-threads --with-file-aio
```

你再电脑上安装qq的过程

1.去qq官网下载安装包

2.在本地找到安装包，双击运行

3.默认的情况，双击安装包运行后，会立即出现指定安装路径的画面（如同linux源码安装的，第一曲）

4、指定完了安装路径，直接下一步，，，下一步即可。

5.最终qq安装完毕，去第三步指定的安装路径，去寻找qq的可执行文件即可，如qq.exe（发送快捷方式到桌面）

6.最终可以在桌面很方便的使用qq程序啦（转化linux平台的动作，就是配置PATH环境变量）

## Nginx配置文件语法

- nginx.conf是由指令和指令块组成

- 每行语句都是有分号结束，指令和参数之间是由空格分割的

- 指令块可以由大括号{}组织多条语句

- nginx.com使用#号标识注释符

- nginx支持用$变量名 支持此语法

- nginx支持include语句，组合多个配置文件

- nginx部分指令支持正则表达式，如rewrite重写指令

  

## Nginx命令行

```
1.nginxqi启停的指令 -s参数，指的是给nginx进程发送某种信号
nginx #初次启动，直接输入nginx，如启动后再执行该命令，就会提示端口冲突（停止nginx，再启动）
nginx -s stop 	#停止nginx进程
nginx -s reload #平滑重启，利用reload可以在不重启nginx进程的情况下，重新读取配置文件
2.查看nginx的帮助信息
which nginx #找一下nginx的PATH路径在哪
[root@basic conf]# nginx -h
Tengine version: Tengine/2.3.3
nginx version: nginx/1.18.0
Usage: nginx [-?hvVtTq] [-s signal] [-c filename] [-p prefix] [-g directives]

Options:
  -?,-h         : this help		#输出nginx的帮助信息
  -v            : show version and exit		#列出nginx的版本号
  -V            : show version and configure options then exit	#列出显示版本和编译参数信息
  -t            : test configuration and exit	#检查nginx的配置文件，语法是否正常
  -T            : test configuration, dump it and exit #同是检查配置，然后输出配置信息
  -q            : suppress non-error messages during configuration testing #在检测配置文件期间屏蔽非错误信息
  # -s 给nginx主进程发送一个主信号，分别有stop停止运行，quit，优雅停止，reload重读配置文件，reopen重新记录nginx日志
  -s signal     : send signal to a master process: stop, quit, reopen, reload
  -p prefix     : set prefix path (default: /opt/tngx232/) # 设置nginx目录前缀
  -c filename   : set configuration file (default: conf/nginx.conf) #指定配置文件启动
  -g directives : set global directives out of configuration file #覆盖设置一些默认参数

  -m            : show all modules and exit	#列出nginx所有支持的模块
  -l            : show all directives and exit
```

## nginx命令行案例

### 配置文件重读

```
1.检查当前的nginx进程
ps -ef|grep nginx |grep -v grep	#去掉跟grep相关的进程
2.修改nginx.conf 修改配置参数
3.重新加载nginx配置
nginx -s reload
```

#### nginx-master信号传递

```
1.master主进程是不处理请求的，而是分配请求发给worker进程，主进程负责重启，热加载，热部署等等
2.master是根据nginx.conf中，worker_process定义启动时创建的工作进程数
3.当worker运行后，master就处于一个等待的状态，等待用户的请求来临，或者系统信号
4.系统管理员可以发送kill指令，或者nginx -s 信号，这样的形式操控nginx
```

nginx信号集

```
nginx -s  对应的信号功能如下
参数		信号		含义
stop	  TERM     强制关闭
null      INT	   强制关闭整个nginx服务
quit	  QUIT     优雅的关闭整个服务
reopen    USR1     重新打开日志记录
rload	  HUB      重新读取配置文件，并且优雅的退出旧的work
```

### nginx 热部署功能

nginx作为一个优秀的web服务器，优秀的反向代理服务器，并且nginx也支持高可用的特性，nginx还支持热部署的特点

热部署的特点：在不重启或者关闭进程的情况下，新的应用直接替换旧的应用

```
更换nginx的二进制命令版本
热部署大致流程
1.备份旧的程序 二进制文件 备份nginx命令，/opt/tngx232/sbin/nginx
2.编译安装新的二进制文件，覆盖旧的二进制文件（再装一个版本的nginx，且替换旧的nginx命令）
3.发送USR2信号发给旧的master进程
4.发送WINCH信号给旧的master进程
5.发送QUIT信号给旧的master进程
```

环境准备

```
1.准备旧的nginx程序版本
[root@basic conf]# nginx -v
Tengine version: Tengine/2.3.3
nginx version: nginx/1.18.0
2.准备一个新的nginx程序版本
wget http://tengine.taobao.org/download/tengine-2.2.0.tar.gz
```

#### nginx热部署操作

nginx工作模式是master-worker（包工头---干活工人）

刚才所说的nginx支持reload重载，仅仅是nginx的master进程，在检查配置文件正常之后，正确则更新，错误则返回异常，正确的情况下也不会更改已经建立的worker，只会等待worker处理完毕请求之后，杀死旧的worker，然后再重新的配置文件中，运行重新的worker（一旦更换了配置文件，reload master主进程，那么手底下的工人也会被换一批了）

nginx还提供了热部署功能，特点是：在不影响用户体验下，进行软件版本升级，也就是不主动的杀死worker，就能够更换软件的二进制命令

```
1.检查当前机器环境的nginx版本
[root@basic conf]# nginx -v
Tengine version: Tengine/2.3.3
nginx version: nginx/1.18.0

2.备份旧的二进制命令
[root@basic sbin]# mv nginx  nginx.232
3.检查旧的二进制命令的编译参数
[root@basic sbin]# nginx.232 -V
Tengine version: Tengine/2.3.3
nginx version: nginx/1.18.0
built by gcc 4.4.7 20120313 (Red Hat 4.4.7-23) (GCC) 
built with OpenSSL 1.0.1e-fips 11 Feb 2013
TLS SNI support enabled
configure arguments: --prefix=/opt/tngx232 --with-http_ssl_module --with-http_flv_module --with-http_gzip_static_module --with-http_stub_status_module --with-threads --with-file-aio
4.下载编译安装新版本的nginx
wget http://tengine.taobao.org/download/tengine-2.2.0.tar.gz
tar -zxvf tengine-2.2.0.tar.gz
进行编译安装
cd tengine-2.2.0
# 新版本的nginx编译参数和旧的保持一致
./configure --prefix=/opt/tngx232 --with-http_ssl_module --with-http_flv_module --with-http_gzip_static_module --with-http_stub_status_module --with-threads --with-file-aio
#编译三部曲，后两步
make && make install

5.检查一下新版的nginx信息，发现此时已经有2个版本的nginx命令
[root@basic tngx232]# cd sbin/
[root@basic sbin]# ll
total 13160
-rwxr-xr-x 1 root root   16654 May  8 15:38 dso_tool
-rwxr-xr-x 1 root root 6655628 May  8 15:38 nginx
-rwxr-xr-x 1 root root 6798529 May  8 10:59 nginx.232
6.再次检查当前系统的nginx状态
#通过pid ppid可以验证 worker process是由master process创建
[root@basic sbin]# ps -ef|grep nginx
root       9439      1  0 11:06 ?        00:00:00 nginx: master process /opt/tngx232/sbin/nginx
nobody     9440   9439  0 11:06 ?        00:00:00 nginx: worker process  
root      17450  14054  0 15:41 pts/0    00:00:00 grep nginx

7.此时发送一个USR2信号给旧的master process，作用是使得nginx旧的版本停止用户请求，并且切换nginx版本
#执行如下命令，给旧的nginx发送信号
[root@basic sbin]# kill -USR2 `cat /opt/tngx232/logs/nginx.pid`
当执行完毕上述的命令，nginx-master旧的，首先会重命名它的pid文件，然后添加上.oldbin后缀，然后然后会再启动一个新的master主进程，以及worker，使用的是新版本的nginx二进制命令，此时新的nginx就能够自动的接收用户发来的请求，过渡到新的nginx-worker工作进程上，因此实现了一个平滑过渡
8.此时再次检查新的nginx进程状态
[root@basic sbin]# ps -ef|grep nginx
root       9439      1  0 11:06 ?        00:00:00 nginx: master process /opt/tngx232/sbin/nginx
nobody     9440   9439  0 11:06 ?        00:00:00 nginx: worker process  
root      17454   9439  0 15:48 ?        00:00:00 nginx: master process /opt/tngx232/sbin/nginx
nobody    17455  17454  0 15:48 ?        00:00:00 nginx: worker process  
root      17457  14054  0 15:48 pts/0    00:00:00 grep nginx
再检查一下，新的pid文件信息
[root@basic sbin]# cd ../logs/
[root@basic logs]# ll
total 16
-rw-r--r-- 1 root root 177 May  8 15:25 access.log
-rw-r--r-- 1 root root  76 May  8 15:48 error.log
-rw-r--r-- 1 root root   6 May  8 15:48 nginx.pid
-rw-r--r-- 1 root root   5 May  8 11:06 nginx.pid.oldbin
[root@basic logs]# cat nginx.pid.oldbin 
9439
[root@basic logs]# cat nginx.pid
17454
9.此时发送WINCH信号，给旧的master进程，让旧的master优雅的退出
[root@basic logs]# cat nginx.pid
17454
[root@basic logs]# kill -WINCH `cat /opt/tngx232/logs/nginx.pid.oldbin`
[root@basic logs]# ps -ef|grep nginx
root       9439      1  0 11:06 ?        00:00:00 nginx: master process /opt/tngx232/sbin/nginx
root      17454   9439  0 15:48 ?        00:00:00 nginx: master process /opt/tngx232/sbin/nginx
nobody    17455  17454  0 15:48 ?        00:00:00 nginx: worker process  
root      17474  14054  0 16:03 pts/0    00:00:00 grep nginx
10.此时如果你觉得nginx服务一切正常，就可以干掉旧的master主进程了
[root@basic logs]# ps -ef|grep nginx
root       9439      1  0 11:06 ?        00:00:00 nginx: master process /opt/tngx232/sbin/nginx
root      17454   9439  0 15:48 ?        00:00:00 nginx: master process /opt/tngx232/sbin/nginx
nobody    17455  17454  0 15:48 ?        00:00:00 nginx: worker process  
root      17474  14054  0 16:03 pts/0    00:00:00 grep nginx
[root@basic logs]# kill 9439
[root@basic logs]# ps -ef|grep nginx
root      17454      1  0 15:48 ?        00:00:00 nginx: master process /opt/tngx232/sbin/nginx
nobody    17455  17454  0 15:48 ?        00:00:00 nginx: worker process  
root      17476  14054  0 16:05 pts/0    00:00:00 grep nginx
11.此时nginx版本热部署 热更换实验就结束了~
```

#### Nginx日志切割

日志切割是线上很常见的操作，能够控制单个日志文件的大小，便于对日志进行管理

```
1.针对nginx的访客日志进行切割
[root@basic logs]# ll -h
total 61M
-rw-r--r-- 1 root root 20M May  8 16:22 access.log
-rw-r--r-- 1 root root 42M May  8 16:22 error.log
-rw-r--r-- 1 root root   6 May  8 15:48 nginx.pid
2.给日志文件重命名，注意使用mv命令
[root@basic logs]# mv access.log "access.log_$(date +"%Y-%m-%d")"
3.发送信号给nginx主进程，给他发送一个重新打开的信号，让nginx生成新的日志文件
nginx -s reopen # 这个命令等同于kill -USR1 `cat nginx.pid`
4.注意在以上的nginx重命名日志切割链，不要着急立即对文件修改，而是要注意等待几秒钟，因为nginx的工作模式特点，master下发指令给worker去干活，刚发指令的时候只是一个标记，当业务量很大的时候，这个修改操作可能会有点慢，不会立即生效，
5.在生产环境下，日志切割主要是以定时任务的形式来操作
编写一个定时日志切割的脚本
#!/bin/bash
# 脚本写入crontab, 每天0点执行; 这是一个nginx日志切割脚本
# nginx日志存放点
logs_path="/opt/tngx232/logs/"
mkdir -p ${logs_path}$(date -d yesterday +"%Y")/$(date -d yesterday +"%m")
mv ${logs_path}access.log ${logs_path}$(date -d yesterday +"%Y")/$(date -d yesterday +"%m")/access_$(date -d yesterday +"%Y-%m-%d").log
# 也能换成 nginx -s reopen
kill -USR1 `cat /opt/tngx232/logs/nginx.pid`
6.把该脚本的执行，加入crontab 每天0点执行
crontab -e #打开定时任务
0 0 * * * /bin/bash /myscripts/cut_nginx_log.sh
```

### Nginx虚拟主机

虚拟主机指的就是一个独立的站点配置，是nginx默认支持的一个功能，它能狗有自己 独立的域名，独立的ip，独立的端口配置，能够配置完整的www服务，例如网站搭建，ftp服务搭建，邮件服务器代理等等。并且nginx支持多虚拟主机，可以在一台机器上，同时运行多个网站的功能，

ginx的多虚拟主机，可以基于

- 多域名的形式
- 多ip的形式
- 多端口形式

利用虚拟主机的功能，就不用为了运行一个网站，而单独的配置一个nginx服务器，或者单独的再运行一组nginx进程

利用nginx的多虚拟主机配置，我们就可以基于一条服务器，一个nginx进程，实现多个站点的配置

## nginx单虚拟主机的配置

### nginx静态网站搭建

```
nginx.conf

user www;
#nginx核心功能块
http{

	#在http{}标签里面就可以定义虚拟主机
	#nginx.conf中server{}虚拟主机标签的定义，默认加载顺序是自上而下的匹配规则（如果没有其他规则定义的情	   况，如基于域名的匹配，基于端口的匹配）
	#第一个虚拟主机站点
	server{
	
	}
	# 在平级关系内，编写第二个，第三个......第N个server{}就是代表配置多个虚拟主机
	#一个server{}标签就可以理解为一个网站
	server{
	
	}
	#编写第三个网站的配置
 	server{
	
	}   
}
#配置文件虚拟主机介绍如下，贴出来的只是一部分的修改，请理解后进行配置：
    #nginx.conf通过server关键字来定义虚拟主机
    #nginx.conf支持编写多个server{}标签
    #nginx.conf第一个虚拟主机站点配置如下
    server {
        #定义虚拟主机站点的端口号，也是用户访问网站的入口
        listen       80;
	# 填写虚拟主机的域名配置，没有域名可以写 localhost 或者_ 也行
        server_name  localhost;
	# server_name www.change.cc;
        # 给nginx定义网站的编码
        charset utf-8;
        
        #access_log  logs/host.access.log  main;
        #access_log  "pipe:rollback logs/host.access_log interval=1d baknum=7 maxsize=2G"  main;
        # nginx的路径匹配规则
        # 如下的规则是最低匹配，任何的nginx请求，都会进入如下location的配置，去它所定义的目录中寻找资料
        location / {
	    # root关键词 是定义网页根目录的，这个html是以nginx安装的路径为相对
            root   html;
	    # index关键词 是定义nginx的首页文件名字，默认找那个文件
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }
```

## 修改虚拟主机站点目录

```
1.修改nginx默认站点配置，nginx.conf 修改server{}标签内容
2.创建新的网页站点资料
2.改了配置文件，一定要重新加载nginx，读取配置
```

## Nginx静态资源压缩

nginx支持gzip压缩功能，经过gzip压缩的页面，图片，动态图这类的静态文件，能够压缩为原本的30%甚至更小，用户访问网站的体验会好很多

```
1.首先准备好nginx服务器，以及配置好虚拟主机站点，再准备好一些静态数据
2.此时在没有开启gzip压缩的情况下，访问该txt文本，发现加载速度很慢
3.开启nginx的gzip压缩功能，只需要在http{}配置中，打开如下参数即可
	gzip on;
	gzip_http_version 1.1;
	gzip_comp_level 4;
	gzip_types text/plain application/javascript application/x-javascript text/css 		     application/xml text/javascript application/x-httpd-php image/jpeg image/gif 	           image/png;

```

## Nginx基于IP的多虚拟主机

v server

环境的准备

```
准备好linux机器，以及配置好3个ip地址
#给网络设备添加别名，绑定多个ip
ifconfig ens33:1 192.168.145 netmask 255.255.255.0 broadcast 192.168.178.255 up
ifconfig ens33:2 192.168.146 netmask 255.255.255.0 broadcast 192.168.178.255 up

准备好的ip地址信息如下
ifconfig | grep "inet 192"
```

添加nginx的配置，添加多个server{}标签，让nginx支持基于ip的多虚拟主机，返回多个站点内容

```
1.给nginx添加include包含语法，让其他目录下的配置文件参数，导入到nginx.conf中，这样的写法，能够让nginx每一个配置文件，看起来简洁，更清晰

修改nginx.conf,在http{}标签中的最后一行，添加如下参数
include extra/*.conf
2.在extra目录下，添加多个基于ip的虚拟主机配置

可以像如下规则的形式，来编写nginx.conf配置文件
第一个基于ip的虚拟主机，当192.168.178.110请求到来，让nginx去/www/110文件夹下寻找资料
		   listen 192.168.178.110:80
		   server_name localhost;
	       location / {
            	root  	/www/110;
            	index  index.html index.htm;
        }
3.在添加192.168.178.145虚拟主机的配置
vim extra/145.conf 添加如下代码
server {
listen 192.168.178.145:80
server_name _;
location / {
	root /www/145;
	index index.html;
}
}
4.编辑146.conf，添加如下代码
vim extra/146.conf 添加如下代码
server {
listen 192.168.178.146:80
server_name _;
location / {
	root /www/146;
	index index.html;
}
}
5.检查nginx语法是否正确
[root@basic conf]# nginx -t
nginx: the configuration file /opt/tngx232/conf/nginx.conf syntax is ok
nginx: configuration file /opt/tngx232/conf/nginx.conf test is successful
6.重新加载nginx配置
nginx -s reload
7.准备3个基于ip的站点内容
mkdir -p /www/{110,145,156}
echo "i'm 110,hello man." > /www/110/index.html
echo "i'm 145,hello man." > /www/145/index.html
echo "i'm 146,hello man." > /www/146/index.html
8.先在linux本地测试多ip的虚拟主机
curl 192.168.178.110
curl 192.168.178.145
curl 192.168.178.146
9.使用客户端的浏览器查看效果
```

## Nginx基于多域名的虚拟主机配置

基于多ip的虚拟主机，用的还是不多的，还可能造成ip不足等问题。一般如果没有特殊需求，用的更多，且更方便的是基于多域名的虚拟主机

前提使用条件，要么配置DNS服务器，将你想用的域名解析到对应的ip

使用本地hosts文件，进行本地测试访问

多域名的配置结合nginx，就实现多虚拟主机的访问，解决了可能ip不足的问题

```
1.环境准备，先在你的客户端本地，修改hosts文件信息
masos /etc/hosts 文件里面改
windows C盘里面的hosts文件，搜索引擎搜一下即可知道 hosts.txt
2.修改hosts文件，添加如下信息,注意这里是客户端本地添加的域名
192.168.178.110		learn.nginx.com		
192.168.178.110 	learn_nginx_cc.com	
192.168.178.110		learn_nginx_yy.com	
3.修改服务器的nginx配置
第一个域名的虚拟主机
server{
	listen 80;
	server_name learn_nginx.com;
	location / {
		root /www/learn_nginx;
		index index.html index.htm;
	}
}
第二个域名的虚拟主机
vim extra/learn_nginx_cc.conf
server{
	listen 80;
	server_name learn_nginx_cc.com;
	location / {
		root /www/learn_nginx_cc;
		index index.html index.htm;
	}
}
第三个域名的虚拟主机
vim extra/learn_nginx_yy.conf
server{
	listen 80;
	server_name learn_nginx_yy.com;
	location / {
		root /www/learn_nginx_yy;
		index index.html index.htm;
	}
}
4.进行nginx重启
nginx -s stop
[root@basic conf]# nginx -t
nginx: the configuration file /opt/tngx232/conf/nginx.conf syntax is ok
nginx: configuration file /opt/tngx232/conf/nginx.conf test is successful
nginx
6.生成三个站点虚拟主机的首页内容
mkdir -p /www/{learn_nginx,learn_nginx_cc,learn_nginx_yy}
echo "welcome ....." > /www/learn_nginx/index.html
echo "welcome .....ccccc" > /www/learn_nginx_cc/index.html
echo "welcome .....yyyyy" > /www/146/learn_nginx_yy.html
7.通过客户端浏览器，分别访问3个域名，查看站点内容
```



## Nginx基于多端口的虚拟主机配置

```
只需要修改nginx.conf中
server{}标签里面定义的
listen端口，参数即可，实现不同端口，进行的虚拟主机匹配
192.168.178.110:80
192.168.178.110:85
192.168.178.110:90
基于不同的端口，来定义为到不同的server，虚拟主机的配置--
```

## Nginx多虚拟主机日志定义

不同ip 

不同端口

不同域名

​	都能够访问到一台计算机配置，nginx的多虚拟主机的功能配置，利用虚拟主机可以在一台服务器上，运行多个站点配置

针对每一个虚拟主机都配置好access.log更方便更清晰的对每个虚拟主机进行访客信息管理

```
#第一个虚拟主机的配置
vim learn_nginx_cc.conf
access_log logs/learn_nginx_cc.log;
#第二个虚拟主机的配置
vim learn_nginx_yy.conf
access_log logs/learn_nginx_yy.log;
nginx -t
nginx -s reload

#此时就生成了针对不同的虚拟主机站点的日志文件

```

## Nginx访客日志

日志对于程序员是很重要的，可以用于问题排错，记录程序运行的状态，一个好的日志配置，能够给予运维人员，开发人员精准的问题定位功能。

Nginx开启日志功能只需要在nginx.conf里面找到log_format参数，定义日志的格式，以及定义日志的存储位置，以及日志的格式，路径，缓存大小等等。

可以定义在全局配置中

```
nginx.conf配置日志功能的代码如下
  include       mime.types;
    default_type  application/octet-stream;
	#定义日志的内容格式（记录内容的详细程度）
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';
	#定义存储的路径
    access_log  logs/cc_access.log  main;
    #access_log  "pipe:rollback logs/access_log interval=1d baknum=7 maxsize=2G"  main;

```

nginx的访客日志内容如下

```
192.168.205.15 - - [11/May/2023:15:21:01 +0800] "GET / HTTP/1.1" 200 555 "-" "curl/7.19.7 (x86_64-redhat-linux-gnu) libcurl/7.19.7 NSS/3.14.0.0 zlib/1.2.3 libidn/1.18 libssh2/1.4.2" "-"
$remote_addr 记录访客的客户端ip地址
$remote_user 记录远程客户端的访客用户名
$time_local 记录访问的时间和地区信息
$request 记录用户的http请求的首行信息
$status 记录用户的http请求状态，也就是请求发出之后，响应的状态，如200 301 404 502
$body_bytes_sent 记录服务器发给客户端的响应体数据字节大小
$http_referer 记录本次请求是从哪个连接过来的，可以根据referer信息来进行防盗链信息
$http_user_agent 记录客户端的访问信息，如浏览器信息，手机浏览器信息
$http_x_forwarded_for 捉到藏在代理服务器后面的真实客户端ip信息
```

## Nginx目录浏览功能

能够将你的机器上的目录资料，提供一个展示

