### hadoop安装:
#### 1.0 基础设施
##### 1.1 设置网络IP地址
```markdown
#centos6:
vi /etc/sysconfig/network-scripts/ifcfg-eth0
	DEVICE=eth0
	TYPE=Ethernet
	ONBOOT=yes
	NM_CONTROLLED=yes
	BOOTPROTO=static
	IPADDR=192.168.205.11
	NETMASK=255.255.255.0
	GATEWAY=192.168.205.2
	DNS1=223.5.5.5
	DNS2=114.114.114.114

```

##### 1.2 设置主机名

```markdown
#centos6:
vi /etc/sysconfig/network
	NETWORKING=yes
	HOSTNAME=node01
#centos7:
    vi /etc/hostname	

```

##### 1.3 设置本机的ip到主机名的映射关系

```markdown
#centos6:
vi /etc/hosts
	192.168.205.11 node01
	192.168.205.12 node02

```

##### 1.4 关闭防火墙

```markdown
#centos6:
	service iptables stop
	chkconfig iptables off
#centos7
#查看状态：
	systemctl status firewalld.service
#临时关闭防火墙
	systemctl stop firewalld.service 
#临时打开防火墙
	systemctl start firewalld.service
#重启防火墙使配置生效	
	systemctl restart iptables.service
#禁止防火墙开机启动
	systemctl disable firewalld.service

```

##### 1.5 关闭selinux,重启网络

```markdown
vi /etc/selinux/config
	SELINUX=disabled

service network restart

```

##### 1.6 解决Centos6无法yum异常

```markdown
1、首先把fastestmirrors关了
vi /etc/yum/pluginconf.d/fastestmirror.conf
#修改
enable=0
#或者执行以下命令
sed -i "s|enabled=1|enabled=0|g" /etc/yum/pluginconf.d/fastestmirror.conf
2、先把之前的repo挪到备份，然后下面两个二选一
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.bak
替换为官方Vault源(海外服务器用)
curl -o /etc/yum.repos.d/CentOS-Base.repo https://www.xmpan.com/Centos-6-Vault-Official.repo
或者替换为阿里云的镜像(国内服务器用)
curl -o /etc/yum.repos.d/CentOS-Base.repo https://www.xmpan.com/Centos-6-Vault-Aliyun.repo
一键修复
sed -i "s|enabled=1|enabled=0|g" /etc/yum/pluginconf.d/fastestmirror.conf
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
curl -o /etc/yum.repos.d/CentOS-Base.repo https://www.xmpan.com/Centos-6-Vault-Aliyun.repo 
yum clean all
yum makecache
参考：https://www.xmpan.com/944.html

```

##### 1.8 安装ntp时间同步

```markdown
yum install ntp -y
vi /etc/ntp.conf
	server ntp1.aliyun.com
service ntpd start
chkconfig ntpd on

```

##### 1.9 安装JDK

```markdown
rpm -i jdk-8u181-linux-x64.rpm
vi /etc/profile
	*有一些软件只认：/usr/java/default
	export JAVA_HOME=/usr/java/default
	export PATH=$PATH:$JAVA_HOME/bin
source /etc/profile   | . /etc/profile

```

##### 1.10 ssh免密

```markdown
ssh localhost 1.验证自己还没免密 2.被动生成了/root/.ssh目录
ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsla
cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
如果A想免密的登录B
A:
	ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
B:
	cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
结论：B包含了A的公钥，A就可以免密的登录，你去陌生人家里得撬锁，去女朋友家里：拿钥匙开门

```
##### 1.11 伪分布式

###### 1.11.1 Hadoop的配置（应用的搭建过程）

```markdown
1、规范路径：
mkdir /opt/bigdata
tar xf hadoop-2.6.5.tar.gz
mv hadoop-2.6.5 /opt/bigdata/
2、配置环境变量
vi /etc/profile
	export JAVA_HOME=/usr/java/default
	export HADOOP_HOME=/opt/bigdata/hadoop-$2.6.5	
	export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
source /etc/profile   | . /etc/profile
cd $HADOOP_HOME #可以直接进入hadoop安装目录

3、添加JAVA_HOME
vi etc/hadoop/hadoop-env.sh
	必须给hadoop配置javahome要不ssh过去找不到
	export JAVA_HOME=/usr/java/default

4、配置NN在哪里启动
vi etc/hadoop/core-site.xml
    <configuration>
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://node1:9000</value>
        </property>
    </configuration>
5、配置hdfs 副本数为1
vi etc/hadoop/hdfs-site.xml
    <configuration>
        <property>
            <name>dfs.replication</name>
            <value>1</value>
        </property>
        <property>
            <name>dfs.namenode.name.dir</name>
            <value>/var/bigdata/hadoop/local/dfs/name</value>
        </property>
        <property>
            <name>dfs.datanode.data.dir</name>
            <value>/var/bigdata/hadoop/local/dfs/data</value>
        </property>
        <property>
            <name>dfs.namenode.secondary.http-address</name>
            <value>node01:50090</value>
        </property>
        <property>
            <name>dfs.namenode.checkpoint.dir</name>
            <value>/var/bigdata/hadoop/local/dfs/secondary</value>
        </property>        
    </configuration
    >
6、配置DN角色在哪里启动
vi etc/hadoop/slaves
	node01

```

###### 1.11.2 初始化&启动

```markdown
1、初始化
hdfs namenode -format //创建目录并初始化一个空的fsimage、VERSION CID
2、启动
cd /opt/bigdata/hadoop-2.6.5
start-dfs.sh //第一次：datanode和secondary角色初始化创建自己的数据目录

```

###### 1.11.3 简单使用

```markdown
hdfs dfs -mkdir /bigdata
hdfs dfs -mkdir -p /user/root
hdfs dfs -put hadoop-2.6.5.tar.gz /user/root
for i in `seq 100000`;do echo "hello hadoop $i" >> data.txt;done
ls -l -h
hdfs dfs -D dfs.blocksize=1048576 -put data.txt

```

###### 1.11.3 验证知识点

```markdown
1、观察editlog的id是不是在fsimage的后面
    cd	/var/bigdata/hadoop/local/dfs/name/current
    SNN 只需要从NN拷贝最后时点的FSimage和增量的Editlog
    cd	/var/bigdata/hadoop/local/dfs/secondary/current
2、观察数据
    /var/bigdata/hadoop/local/dfs/data/current/BP-896149735-192.168.17.61-1640656360547/current/finalized/subdir0/subdir0
    检查data.txt被切割的块，他们的数据是什么样子

```

###### 1.11.4 总结

```markdown
伪分布式：在一个节点启动所有的角色：NN,DN,SNN

基础环境

部署配置

角色在哪里启动

​	NN : core-site.xml：fs.defaultFS	hdfs://node01:9000

​	DN：slaves：node01

​	SNN：hdfs-site.xml：dfs.namenode.secondary.http.address node01:50090

角色启动时的细节配置：

​	dfs.namenode.dir

​	dfs.datanode.data.dir

初始化&启动

格式化

​		Fsimage、VERSION

start-dfs.sh

​	加载我们的配置文件

​	通过ssh免密的方式去启动相应角色

ps -fe  显示操作系统进程，jps 只显示Java进程

Ifconfig  查看IP

scp  ./jdk-8u181-linux-x64.rpm  node02:/root/    分发jdk

node01：stop-dfs.sh

ssh 免密是为了什么：启动start-dfs.sh  在哪里启动，那台就要对别人公开自己的公钥

这一台有什么特殊要求吗：没有

```
-----------------------------------------------------------------------------------------------------------------------------------------------------------

##### 1.12 完全分布式

###### 1.12.1 从伪分布式到完成分布式，角色重新规划

| HOST   | NN | JNN | DN |
|--------|----|-----|----|
| node01 |    |     |    |
| node02 |    |     |    |
| node03 |    |     |    |
| node04 |    |     |    |


###### 1.12.2克隆虚拟机

```markdown
1、修改主机名
vi /etc/sysconfig/network

2、修改IP
vi /etc/sysconfig/network-scripts/ifcfg-eth0

3、删除文件
rm -rf /etc/udev/rules.d/70-persistent-net.rules
reboot

```

###### 1.12.3 修改Windows系统hosts

```markdown
修改Windows系统hosts：C:\Windows\System32\drivers\etc\hosts
192.168.205.11 node01
192.168.205.12 node02
192.168.205.13 node03
192.168.205.14 node04

```
###### 1.12.4 做免密，使node01可以免密登陆node02、node03、node04
```markdown
node01：
	scp ./id_dsa.pub   node02:/root/.ssh/node01.pub
	scp ./id_dsa.pub   node03:/root/.ssh/node01.pub
	scp ./id_dsa.pub   node04:/root/.ssh/node01.pub
node02：
	cd ~/.ssh
	cat node01.pub  >> authorized_keys
node03：
	cd ~/.ssh
	cat node01.pub  >> authorized_keys
node04：
	cd ~/.ssh
	cat node01.pub  >> authorized_keys	

```

###### 1.12.5 配置部署

```markdown
node01
cd $HADOOP/etc/hadoop
vi core.site.xml //不需要改
vi hdfs.site.xml
    <configuration>
            <property>
                <name>dfs.replication</name>
                <value>2</value>
            </property>
            <property>
                <name>dfs.namenode.name.dir</name>
                <value>/var/bigdata/hadoop/full/dfs/name</value>
            </property>
            <property>
                <name>dfs.datanode.data.dir</name>
                <value>/var/bigdata/hadoop/full/dfs/data</value>
            </property>
            <property>
                <name>dfs.namenode.secondary.http-address</name>
                <value>node02:50090</value>
            </property>
            <property>
                <name>dfs.namenode.checkpoint.dir</name>
                <value>/var/bigdata/hadoop/full/dfs/secondary</value>
            </property>
    </configuration>
vi slaves
	node02
	node03
	node04
分发：
	cd /opt
	scp -r ./bigdata/ node02:`pwd`
	scp -r ./bigdata/ node03:`pwd`
	scp -r ./bigdata/ node04:`pwd`    
格式化：
hdfs namenode -format
启动：
start-dfs.sh

```

###### 1.12.6 思路

```markdown
主从集群：结果相对简单，主与从协作
主：单点，数据一致好掌握
问题：
1、单点故障，集群整体不可用
2、压力过大，内存受限

解决方案：
1、单点故障
高可用方案：HA（High Available）
多个NM，主备切换，
2、压力过大，内存受限
联邦机制：Federation（元数据分片）
多个NM，管理不同的元数据

HA 方案
多台NN主备模式，Active和Standby状态
HADOOP 2.x 只支持HA的一主一备
Active对外提供服务
增加journalnode角色（>3台），负责同步NN的editlog
最终一致性
增加zkfc角色（与NN同台），通过zookeeper集群协调NN的主从选举和切换
事件回调机制
DN同时向NN汇报block清单

HDFS-Federation解决方案
NN的压力过大，内存受限问题：
元数据分治，复用DN存储
元数据访问隔离性
DN目录隔离block

```

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

##### 1.13 HDFS解决方案

###### 1.13.1 角色规划

| HOST   | NN  | JNN | DN  | ZKFC | ZK |
|--------|-----|-----|-----|------|----|
| node01 | *   |     |     |      |    |
| node02 | *   |     |     |      |    |
| node03 |     |     |     |      |    |
| node04 |     |     |     |      |    |

###### 1.13.2 配置部署
1、基础环境

​	1、增加NNs的ssh免密

2、应用搭建

​	1、zookeeper

​	2、格式化NN

​	3、格式化ZK

3、启动集群

FULL -> HA：

HA模式下：有一个问题，你的NN是2台？在某一时刻，谁是Active呢？

core.site.xml

fs.defaultFs -> hdfs://node01:9000

core.site.xml配置逻辑名称

```markdown
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://mycluster</value>
    </property>
    <property>
        <name>ha.zookeeper.quorum</name>
        <value>node02:2181,node03:2181,node04:2181</value>
    </property>    
</configuration>
```

hdfs-site.xml，定义逻辑名称，一对多映射，逻辑物理映射

```markdown
<configuration>
		#以下是 一对多,逻辑到物理节点的映射
        <property>
            <name>dfs.replication</name>
            <value>2</value>
        </property>
        <property>
            <name>dfs.namenode.name.dir</name>
            <value>/var/bigdata/hadoop/ha/dfs/name</value>
        </property>
        <property>
            <name>dfs.datanode.data.dir</name>
            <value>/var/bigdata/hadoop/ha/dfs/data</value>
        </property>
        <property>
            <name>dfs.nameservices</name>
            <value>mycluster</value>
        </property>
        <property>
            <name>dfs.ha.namenodes.mycluster</name>
            <value>nn1,nn2</value>
        </property>
        <property>
            <name>dfs.namenode.rpc-address.mycluster.nn1</name>
            <value>node01:8020</value>
        </property>
        <property>
            <name>dfs.namenode.rpc-address.mycluster.nn2</name>
            <value>node02:8020</value>
        </property>
        <property>
            <name>dfs.namenode.http-address.mycluster.nn1</name>
            <value>node01:50070</value>
        </property>
        <property>
            <name>dfs.namenode.http-address.mycluster.nn2</name>
            <value>node02:50070</value>
        </property>
        #以下是JN在哪里启动，数据存哪里
        <property>
            <name>dfs.namenode.shared.edits.dir</name>
            <value>qjournal://node01:8485;node02:8485;node03:8485/mycluster</value>
        </property>
        <property>
        	<name>dfs.journalnode.edits.dir</name>
        	<value>/var/bigdata/hadoop/ha/dfs/jn</value>
        </property>
        #HA角色切换的代理类和实现方法，我们用的是ssh免密
        <property>
        	<name>dfs.client.failover.proxy.provider.mycluster</name>
      		<value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
        </property>
        <property>
        	<name>dfs.ha.fencing.methods</name>
        	<value>sshfence</value>
        </property>
        <property>
        	<name>dfs.ha.fencing.ssh.private-key-files</name>
        	<value>/root/.ssh/id_dsa</value>
        </property>
        #开启自动化，zkfc进程
        <property>
        	<name>dfs.ha.automatic-failover.enabled</name>
        	<value>true</value>
        </property>
</configuration>        
```

流程：

基础设施

​	ssh免密：1）启动start-dfs.sh脚本的机器需要将公钥分发给别的节点

​			 2）在HA模式下，每一个NN身边会启动ZKFC，ZKFC会用免密的方式控制自己和其他NN节点状态

应用搭建

​				HA依赖ZK集群

​				修改hadoop的配置文件，并集群同步

初始化启动

​				1）先启动JN  hadoop-daemon.sh start journalnode

​				2）选择一个NN做格式化：hdfs namenode -format	<只有第一次搭建做，以后不用做>

​				3） 启动这个格式化的NN，以被另外一台同步 hadoop-daemon.sh start namenode 

​				4）在另外一台机器中，hdfs namenode -bootstrapStandby，同步元数据

​				5）格式化zookeeper：hdfs zkfc -formatZK	<只有第一次搭建做，以后不用做>

​				6）start-dfs.sh

使用

实操

1）停止之前的集群

2）免密：node01，node02

```
node02：
	cd ~/.ssh
	ssh-keygen -t dsa -P '' -f ./id_dsa
	cat id_dsa.pub  >> authorized_keys
	scp ./id_dsa.pub  node01:`pwd`/node02.pub
node01:
	cd ~/.ssh
	cat node02.pub  >> authorized_keys
```

3）zookeeper集群搭建	java语言开发	需要jdk	部署在2，3，4

```markdown
node02:
    tar xf zookeeper-3.4.6.tar.gz
    mv zookeeper-3.4.6  /opt/bigdata/
    cd /opt/bigdata/zookeeper-3.4.6/conf
    cp zoo_sample.cfg  zoo.cfg
    vi zoo.cfg
        dataDir=/var/bigdata/hadoop/zk

        server.1=node02:2888:3888
        server.2=node03:2888:3888
        server.3=node04:2888:3888
    mkdir /var/bigdata/hadoop/zk
    echo 1 > /var/bigdata/hadoop/zk/myid
    vi /etc/profile
        export ZOOKEEPER_HOME=/opt/bigdata/zookeeper-3.4.6
        export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$ZOOKEEPER_HOME/bin
    cd /opt/bigdata
    scp -r ./zookeeper-3.4.6/ node03:`pwd`
    scp -r ./zookeeper-3.4.6/ node04:`pwd`
    cd /etc
    scp ./profile node03:`pwd`
    scp ./profile node03:`pwd`    
node03:
    mkdir /var/bigdata/hadoop/zk
    echo 2 > /var/bigdata/hadoop/zk/myid
    source /etc/profile
node04:
    mkdir /var/bigdata/hadoop/zk
    echo 3 > /var/bigdata/hadoop/zk/myid
    source /etc/profile  
node02~node04
zkServer.sh start
zkServer.sh status

```

4）配置hadoop的core和hdfs

5）分发配置，给每一台分发

```
scp core-site.xml  hdfs-site.xml  node02:`pwd`
scp core-site.xml  hdfs-site.xml  node03:`pwd`
scp core-site.xml  hdfs-site.xml  node04:`pwd`
```

6）初始化

​				1）先启动JN  hadoop-daemon.sh start journalnode

​				2）选择一个NN做格式化：hdfs namenode -format	<只有第一次搭建做，以后不用做>

​				3） 启动这个格式化的NN，以被另外一台同步 hadoop-daemon.sh start namenode 

​				4）在另外一台机器中，hdfs namenode -bootstrapStandby，同步元数据

​				5）格式化zookeeper：hdfs zkfc -formatZK	<只有第一次搭建做，以后不用做>

​				6）start-dfs.sh

使用验证

​				1）去看jn的日志和目录变化

​				2）node04	zkCli.sh	

​										ls /

​										启动之后可以看到锁

​										get /hadoop-ha/mycluster/ActiveStandbyElectorLock

​				3）杀死namenode 杀死zkfc

​						kill -9  xxx

​						 a）杀死active NN

​						 b）杀死active NN 身边的zkfc

​								启动zkfc：hadoop-daemon.sh start zkfc

​						 c）shutdown activeNN 主机的网卡：ifconfig eth0 down

​								2节点一直阻塞降级

​								如果恢复1上的网卡 ifconfig eth0 up

​								最终2变成active

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

1、hdfs的权限

2、hdfs java api idea

3、mapreduce 启蒙

hdfs dfs -mkdir -p /user/root

| Permission | Owner | Group      | Size | Replication | Block Size | Name |
| :--------- | :---- | :--------- | :--- | :---------- | :--------- | :--- |
| drwxr-xr-x | root  | supergroup | 0 B  | 0           | 0 B        | user |

hdfs dfs -put install.log /

| Permission | Owner | Group      | Size    | Replication | Block Size | Name        |
| :--------- | :---- | :--------- | :------ | :---------- | :--------- | :---------- |
| -rw-r--r-- | root  | supergroup | 8.61 KB | 2           | 128 MB     | install.log |
| drwxr-xr-x | root  | supergroup | 0 B     | 0           | 0 B        | user        |

hdfs是一个文件系统

类unix、linux

有用户概念

​	hdfs没有相关命令和接口去创建用户，信任客户端 <-  默认情况使用的 操作系统提供的用户，扩展kerberos LDAP 继承第三方用户认证系统

有超级用户的概念

​	linux系统中超级用户：root

​	hdfs系统中超级用户：是namenode进程的启动的用户

有权限概念

​	hdfs的权限是自己控制的，来自于hdfs的超级用户

实操：（一般企业中不会用root做什么事情）

面向操作系统	root是管理员 其实用户都叫【普通用户】

面向操作系统的软件	谁启动，管理这个进程，那么这个用户叫做这个软件的管理员

切换我们用root搭建的HDFS，用god这个用户来启动

node01~node04

​	*）stop-dfs.sh

​	1）添加god用户

```ssh
	useradd god
	passwd god
```

​	2）将资源与用户绑定（a.安装部署程序；b.数据存放目录）

```ssh
	chown -R god src
	chown -R god /opt/bigdata/hadoop-2.6.5
	chown -R god /var/bigdata/hadoop
```

​	3）切换到god去启动 start-dfs..sh < 需要免密

​			给god做免密

​			*HA模式，免密2个场景都要做

ssh localhost >> 为了拿到.ssh

```
node01~node02:
	cd .ssh
node01
	ssh-keygen -t dsa -P '' -f ./id_dsa
	node01
	ssh-copy-id -i id_dsa  node01
	ssh-copy-id -i id_dsa  node02
	ssh-copy-id -i id_dsa  node03
	ssh-copy-id -i id_dsa  node04
node02 
	cd /home/god/.ssh
	ssh-keygen -t dsa -P '' -f ./id_dsa	
	ssh-copy-id -i id_dsa node01
	ssh-copy-id -i id_dsa node02
```

4）hdfs-site.xml

        <property>
          <name>dfs.ha.fencing.ssh.private-key-files</name>
          <value>/home/god/.ssh/id_dsa</value>
        </property>

分发给node02~04

5）god	： start-dfs.sh

----------------------用户权限验证实操

```ssh
node01：
	su god
	hdfs dfs -mkdir /temp
	hdfs dfs -chown god:ooxx /temp
	hdfs dfs -chmod 770 /temp
```

```ssh
node04:
	root:
		useradd good
		groupadd ooxx
		usermod -a -G ooxx good
		id good
	su good
		hdfs dfs -mkdir /temp/abc   <失败>
		hdfs groups
				good:	<因为hdfs已经启动了，不知道你操作系统又偷偷摸摸创建了用户和组>
*node01:
	root:		
			useradd good
			groupadd ooxx
			usermod -a -G ooxx good
	su god
			hdfs dfs -refreshUserToGroupsMappings
	node04:
		god:
			hdfs groups
				good:good ooxx
结论：默认hdfs依赖操作系统上的用户和组
```

----------------------------------------------hdfs  api 实操：

windows  idea  eclipse 叫什么：集成开发环境	ide  你不需要做太多，用它~！

语义：开发hdfs的client

权限：1）参考系统登陆用户名；2）参考环境变量；3）代码中给出；

HADOOP_USER_NAME	god	这一步操作优先启动idea

jdk版本：集群和开发环境jdk版本一致~！！

maven：构建工具	包含了

​	依赖管理（pom），

​	jar包有仓库的概念，互联网参考，全，大

​	本地仓库，用过的会缓存

​	打包、测试、清除，构建项目目录。。。

​	GAV定位。。。

```
https://mvnrepository.com/
```

hdfs的pom：

​	hadoop:	（common，hdfs，yarn，mapreduce）

com.msb.hadoop

hadoop-hdfs

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

计算  compute

为什么叫MapReduce？

| NAME     | MAJOR  | BENDER | ADDRESS |
|:---------|:-------|:-------|:--------|
| ZHANSAN  | JAVA   | 1      | BJ,SH   |
| LISI     | JAVA   | 0      | BJ      |
| ZHANGSAN | SCALA  | 1      | BJ,SH   |
| WANGWU   | JAVA   | 1      | SH      |
| LISI     | PYTHON | 0      | BJ      |

过滤性别为0的

转换码值为字典值

展开字段复合值

Map： 以一条记录为单位做映射~！

一个专业有几个人学？

Reduce：以一组为单位做计算~！

什么叫做一组？分组~！

依赖一种数据格式：key：val

K、V的实现：由map映射实现~！

MAP：（1）映射、变换、过滤	（2）1进N出

Reduce：（1）分解、缩小、归纳	（2）一组进N出

（KEY,VAL）：键值对的键划分数据分组

输入数据集 -> map() ->中间数据集 ->reduce() ->最终结果集

input HDFS

1个split  对应	1个map计算

split 默认约等于block块，加一层解耦

块是物理切割，split是逻辑

1行cpu忙1个小时，cup密集型计算

IO上读取，IO密集型计算

HDFS -> B?

split >block	split =block	split <block  

控制并行度

split 格式化一条一条记录

*组是最小粒度，不可被分开

reduce并行度由什么决定？由开发人员决定，默认1个

MT/RT

MT -- 分组

RT -- 分区

block > split

1:1

N:1

1:N

split > map

1:1

map>reduce

N:1

N:N

1:1

1:N

group(key) > partition

1:1

N:1

N:N

1:N

MR：数据以一条记录为单位经过map方法映射成KV，相同的key为一组，这一组数据调用一次reduce方法，在方法内迭代计算这一组数据。

作业：迭代器模式

经验：数据集一般是用迭代计算的方式

细粒度图

map task

input split

1、切片回格式化出记录，以记录为单位调用map方法

2、map的输出映射成KV，kv会参与分区计算，拿着key算出P，分区号，KVP

map task的输出是一个文件，存在本地的文件系统中

buffer in memery（100M）

内存，分区排序

内部有序，外部无序

3、内存缓冲区溢写磁盘时：做一个二次排序：分区有序，且分区内key有序，未来相同的一组key会相邻的在一起

merge 归并 IO复杂度：o1

4、reduce的归并排序其实可以和reduce方法的计算同时发生，尽量减少IO,因为有迭代器模式的支持！！！

迭代器模式是批量计算中非常优美的实现形式~！

找出相同的行

```
abc							abc,null,1		reduce	def,null,0
def		map		def,null,0
ghi 						ghi,null,3		reduce	abc,null,1		abc
													abc,null,1							
													mno,null,1                                       
jkl							jkl,null,2		reduce	jkl,null,2						
mno		map		mno,null,1		
abc						  	abc,null,1		reduce	ghi,null,3
```


相同的KEY，哈希值一定相同，模数一定相同

分组统计概念

word count（Hello、World、Hadoop出现几次） 

```
Hello World				Hello,1,0	reduce	Hello,1,0
Hello Hadoop	map		Hello,1,0			Hello,1,0		Hello,4
						World,1,1			Hello,1,0			
						Hadoop,1,2			Hello,1,0
									reduce	World,1,1		World,2
Hello World				Hello,1,0			World,1,1
Hello Hadoop	map		Hello,1,0				
						World,1,1	reduce	Hadoop,1,2						
						Hadoop,1,2			Hadoop,1,2		Hadoop,2
-> 统计相同词频的个数
Hello，4
World，2
Hadoop，2
再走一次MapReduce
--------------------------------------------------------------------------------------
MR计算框架：计算向数据移动如何实现？

```

-------------------------------------------------------------------------------------------------------------------------------------------

学习方法。。。。。。。

hdfs：存储模型 -> 切块，散列 ->分治	目的：分布式计算

​			实现：-> 框架	角色：NN,DN

​			特长/特点	->	架构师：【技术选型】

​					读写流程就很重要

mapreduce，分布式计算框架，批量计算，对应流式计算

​	计算模型

​		2个阶段：map和reduce是线性关系，也是一种阻塞关系

​			map阶段：

​				单条记录加工和处理

​			reduce阶段：

​				按组，多条记录加工处理

实现：->	框架

计算向数据移动

​	hdfs暴露数据的位置

​	1）资源管理

​	2）任务调度

​	角色：

​	JobTracker（主）

​			1、资源管理

​			2、任务调度

​	TaskTracker（从）

​		任务管理

​		资源汇报

```
Cli													Cli
JobTrackr											NN
TaskTracker、TaskTracker、TaskTracker
DN			  DN		  DN
```

【Cli	?】

1、会根据每次的计算数据，咨询NN元数据（block）=》算：split	得到一个切片【清单】 map的数量就有了

split是逻辑的，block是物理的，block身上有（offset，locations），split和block是有映射关系

结果：split包含偏移量，以及split对应的map任务应该移动到哪些节点（locations）

split0	A	0	500	n1	n3	n5

可以支持计算向数据移动了~！！！

2、生成计算程序未来运行时的相关配置的文件：。。。xml

3、未来的移动应该相对可靠	cli会将jar，split清单，配置xml	上传到hdfs的目录中（上传的数据，副本数10）

4、cli会调用JobTracker，通知要启动一个计算程序了，并且告知文件都放在了hdfs的哪些地方

JobTracker收到启动程序之后

​	1、从hdfs中取回【split清单】

​	2、根据自己收到的TT汇报的资源，最终确定每一个split对应的map应该去到哪一个节点【确定清单】

​	3、未来，TT在心跳的时候会取回分配给自己的任务信息~！

TaskTracker

​		1、在心跳取回任务后

​		2、从hdfs中下载jar、xml。。到本机

​		3、最终启动任务描述中的MapTask/ReduceTask	（最终，代码在某一个节点被启动，是通过，cli上传，TT下载：计算向数据移动实现）

问题：

​	JobTracker	3个问题

1、单点故障

2、压力过大

3、集成了【资源管理和任务调度】，两者耦合

​	弊端：未来新的计算框架不能复用资源管理

​				1、重复造轮子

​				2、因为各自实现资源管理，但是他们部署在同一个硬件上，因为隔离，所以不能感知对方的				使用	so：资源争抢~！！！

思路：（因果关系导向学习）

​				计算要向数据移动->哪些节点可以去呢（需要有整体资源的把控）

​				->确定了节点后对方怎么知道呢（任务调度），还有比如有一个失败了，应该重新在哪个节				点重试

​				 ->来个JobTracker搞定了这2件事情。。。但是，后来，问题也暴露了。。。

1.x 淘汰，

yarn架构	主从架构	从MapReduce的资源管理，独立出来，和MapReduce无关

Client

Resource Manager

NodeManager

App Mstr

Container	资源描述

反向注册App Mstr

反射成对象，执行方法

hadoop2.x	yarn

​	模型：

​			Container容器	不是docker

​				虚的

​					对象：属性：NM，cpu，mem，io量

​				物理的

​					JVM -> 操作系统进程	

​					1，NM会有线程监控container资源情况，N,M直接kill掉

​					2、cgroup内核级技术：在启动jvm进程，由kernel约束死

​					*整合docker

​	实现：架构/框架

​		角色：

​			Resource Manager	主

​				负责整体资源的管理

​			NodeManager	从

​				向RS汇报心跳，提交自己的资源情况

​		MR运行	MapReduce	on	yarn

​			1、MR-cli（切片清单/配置/jar/上传到HDFS）

​				   访问RM申请AppMaster

​			2、RM选择一台不忙的节点通知NM启动一个Container，在里面反射一个MRAppMaster

​			3、启动MRAppMaster，从hdfs下载切片清单，向RM申请资源

​			4、用RM根据自己掌握的资源情况得到一个确定清单，通知NM来启动container

​			5、container启动后会反向注册到已经启动的MRAppMaster进程

​			6、MRAppMaster（曾经的JobTracker阉割版不带资源管理）最终将任务Task发送

​				  给container（消息）

​			7、container会放射相应的Task类为对象，调用方法执行，其结果就是我们的业务逻辑代码的					执行

​			8、计算框架都有Task失败重试的机制

​			结论：

​					问题：

​							1、单点故障（曾经是全局的，JT挂了，整个计算层没有了调度）

​									yarn：每个APP有一个自己的AppMaster调度！（计算程序级别）

​									yarn支持AppMaster失败重试~！

​							2、压力过大

​									yarn中每个计算程序自有AppMaster，每个AppMaster只负责自己计算程序的任									务调度，轻量了

​									AppMasters是在不同的节点中启动的，默认有了负载的光环

​							3、集成了【资源管理和任务调度】，两者耦合

​									因为Yarn只是资源管理，不负责具体的任务调度

​									是公立的，只要计算框架继承yarn的AppMaster,大家都可以使用一个统一视图									的资源层

​				总结感悟：

​								从1.x到2.x

​								JT，TT是MR的常服务。。。

​								2.x之后没有了这些服务

​									相对的：MR的cli，【调度】，任务，这些都是临时服务了。。。

1、最终去开发MR计算程序

​				HDFS 和YARN是俩概念

2、hadoop2.x	出现了一个yarn：资源管理 》MR 没有后台服务

​				yarn模型：container	容器，里面会运行我们AppMaster，map/reduce	Task

​						解耦

​				mapreduce on yarn

​				架构：

​							RM

​							NM

​				搭建：				    NN 	    JN	    ZKFC	ZK	    DN	            RM	      NM

​							node01		*		*		*					

​							node02		*		*		*		*		*					*

​							node03				   *				  *		*		*		 *

​							node04									   *		*		*		  *

hadoop 1.x 		2.x		3.x

hdfs		no ha	ha（向前兼容，没有过多的改NN,,而是通过新增了角色，zkfc）

yarn		no yarn	yarn（不是新增角色，而是直接在RM进程中增加了HA的模块）



-----通过官网

mapred-site.xml	>	mapreduce on yarn

```
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
```

yarn-site.xml

//shuffle	洗牌	M	-shuffle	> R

```
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
</configuration>
```

```
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
 	<property>
   		<name>yarn.resourcemanager.ha.enabled</name>
   		<value>true</value>
 	</property>
  	<property>
   		<name>yarn.resourcemanager.zk-address</name>
   		<value>node02:2181,node03:2181,node04:2181</value>
 	</property>
 
 	<property>
   		<name>yarn.resourcemanager.cluster-id</name>
   		<value>mashibing</value>
 	</property>
 	<property>
   		<name>yarn.resourcemanager.ha.rm-ids</name>
   		<value>rm1,rm2</value>
 	</property>
 	<property>
   		<name>yarn.resourcemanager.hostname.rm1</name>
   		<value>node03</value>
 	</property>
 	<property>
   		<name>yarn.resourcemanager.hostname.rm2</name>
   		<value>node04</value>
 	</property>
```

流程：

```
node01：		
    cd $HADOOP_HOME/etc/hadoop
    cp mapred-site.xml.template mapred-site.xml
    vi mapred-site.xml
    vi yarn-site.xml
    scp mapred-site.xml yarn-site.xml	node02:`pwd`
    scp mapred-site.xml yarn-site.xml	node03:`pwd`
    scp mapred-site.xml yarn-site.xml	node04:`pwd`
    vi slaves	//可以不用管，搭建hdfs时候已经改过了
    start-yarn.sh
node03~04
	yarn-daemon.sh start resourcemanager
http://node03:8088/cluster/cluster
http://node04:8088/cluster/cluster	
```

-----------------------------MR 官方案例使用：wc

​									实战：MR ON YARN 的运行方式：

```
hdfs dfs -mkdir -p /data/wc/input
hdfs dfs -D dfs -blocksize=1048576 -put data.txt /data/wc/input
cd	$HADOOP_HOME
cd 	share/hadoop/mapreduce
hadoop jar hadoop-mapreduce-examples-2.6.5.jar wordcount /data/wc/input /data/wc/output
```

1 ) webui:

2 ) cli:

```
hdfs dfs -ls /data/wc/output
Found 2 items
-rw-r--r--   2 root supergroup          0 2022-01-26 17:46 /data/wc/output/_SUCCESS	//标志成功的文件
-rw-r--r--   2 root supergroup     788922 2022-01-26 17:46 /data/wc/output/part-r-00000	//数据文件
part-r-0000
part-m-0000
r/m：	map-reduce	r	/map	m
hdfs dfs -cat /data/wc/output/part-r-00000
hdfs dfs -get /data/wc/output/part-r-00000	./
```

抛出一个问题：	

​		data.txt上传会切割成2个block，计算完，发现数据是对的~！~？后面主要听源码~！~~					

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

MR	提交方式

源码

提交方式：

​					1、 	开发	->	jar->	上传到集群中的某一个节点	->	hadoop jar	ooxx.jar	ooxx	in	out

​					2、	嵌入【linux、windows】（非hadoop jar）的集群方式	on yarn  

​							  	集群：M、R

​								  client -> RM -> AppMaster

​								 mapreduce.framework.name -> yarn	//决定了集群运行

​								conf.set("mapreduce.app-submission.cross-platform","true");

​								 job.setJar("F:\\Dev\\msbhadoop\\target\\hadoop-hdfs-1.0-0.1.jar");	//推送jar包到hdfs

​					3、	local，单机、自测

​								mapreduce.framework.name	-> local

hadoop No FileSystem for scheme: hdfs 异常

```class
hadoopConfig.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
hadoopConfig.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName() );
```

IDE	->	集成开发：

​					hadoop最好的平台是linux

​					部署hadoop，bin

​					1、win系统部署Hadoop

​					2、在我给你的资料中\hadoop-install\soft\bin 文件覆盖到你部署的bin目录下，还要将hadoop.dll复制到c:\windows\system32\

​					3、设置环境变量：HADOOP_HOME

**ExitCodeException exitCode=-1073741515** 异常

操作系统缺少 *msvcr120.dll*文件

解决办法：安装常用C++库合集（x86 + 64位）

参数个性化；

```
GenericOptionsParser parser = new GenericOptionsParser(conf,args);//工具类帮我们把-D等等的属性直接set到conf，会留下-commandOptions
String[] othargs = parser.getRemainingArgs();
```

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

源码分析：（目的）更好的理解你学的技术的细节	以及原理

​				what？why？how？

​				3个环节	<-	 分布式计算	<-	追求：计算向数据移动，并行度、分治，数据本地化读取

Client	

​							没有计算发生

​							很重要，支撑了计算向数据移动和计算的并行度

```wiki
1.Checking the input and output specifications of the job.
2.Computing the InputSplits for the job.	//split	-> 并行度和计算向数据移动就可以实现
3.Setup the requisite accounting information for the DistributedCache of the job, if necessary.
4.Copying the job's jar and configuration to the map-reduce system directory on the distributed file-system.
5.Submitting the job to the JobTracker and optionally monitoring it's status.
```
MR 框架 默认的输入格式化类：TextInputFormat	<	FileInputFormat	<	InputFormat

​																								getSplits()

​			minSize=1

​			maxSize = Long.Max

​			blockSize = file

​			splitSize = Math.max(minSize, Math.min(maxSize, blockSize));	//默认split大小等于block的大小

​			切片split是一个窗口机制（调大split改小，调小split改大）

​			如果我想要得到一个比block大的split：改minSize			

```
//判断，我们的offset（偏移量）要大于起始，小于结束
if ((blkLocations[i].getOffset() <= offset) &&
    (offset < blkLocations[i].getOffset() + blkLocations[i].getLength())){
  return i;
}
```

split：解耦	存储层和计算层

​		1、file（归属于哪个文件）

​		2、offset（）

​		3、length	//切片大小

​		4、hosts	//支撑了计算向数据移动（最重要的属性）

MapTask

input	->	map	->	output

input：（split+format）通用的知识，未来的spark底层也是

​			来自于我们的输入格式化类给我们实际返回的记录读取器对象

​			TextInputFormat	->	LineRecordReader

​															split：file,	offset,	length,

​															init：in = fs.open(file).seek(offset)

​															除了第一个切片对应的map，之后的map都在init环节，从切片包含的数据中，让出第一行，并把切片的起始更新为切片的第															二行，换而言之，前一个map会多读取一行，来弥补hdfs把数据切割的问题~！

​															nextKeyValue()：

​																	1、读取数据中的一条记录对key、value赋值

​																	2、返回布尔值

​															getCurrentKey()	getCurrentValue()

output

​		NewOutputCollector

​				partitioner

​				collector

​				MapOutputBuffer

​					init()：

​						spillper =0.8M

​						sortmb = 100

​						sorter = QuickSort（排序器）

​						comparator = job.getOutputKeyComparator();（比较器）

​											1、优先取用户覆盖的自定义排序比较器

​											2、保底	->	取key这个类型自身的比较器

​						combiner	=	reduce？框架里面默认没有，要设置

​						minSpillsForCombine = 3

​						spillThread  //溢写线程

​							sortAndSpill

​								if (combinerRunner == null) 

​				map输出的KV会序列化成字节数组，算出P，最中是3元组：K、V、P

​				buffer是使用的环形缓冲区

​					1、本质还是线性字节数组

​					2、赤道，两端方向放KV，索引

​					3、索引：是固定宽度：16字节

​							a）Partition

​							b）KS

​							c）VS

​							d）VL

​						4、如果数据填充到阈值：80%，启动线程：

​										快速排序80%数据，同时map输出的线程向剩余的空间写

​										快速排序的过程：是比较key排序，但是移动的是索引

​						5、最终，溢写时只要按照排序的索引，卸下的文件中的数据就是有序的

​								注意：排序是二次排序

​										分区有序：最后reduce拉取是按照分区的

​										分区内key有序，因为reduce计算是按分组计算，分组的语义（相同的key排在了一起）

​						7、调优：combiner

​								1、其实就是一个map里的reduce

​											按组统计

​								2、发生在哪个时间点：

​											a）内存溢写数据之前排序之后

​											溢写的io变少~！

​											b）最终map输出结束，过程中，buffer溢写出多个小文件（内部有序）

​											minSpillsForCombine = 3

​											 map最终会把溢写出来的小文件合并成一个大文件：

​											 避免小文件的碎片化对未来reduce拉取数据造成的随机读写，线性读写快

​															也会触发combiner

​								3、combiner注意

​												必须幂等

​												例子：

​														1、求和计算

​														2、平均数计算

​																80：数值和，个数和

ReduceTask

​		input	->	reduce	->	output

费曼学习法

```class
map	->	while (context.nextKeyValue()) {//一条记录调一次map
reduce	->	while (context.nextKey()) {//一组数据调一次reduce
1、shuffle：洗牌（相同的key被拉取到一个分区），拉取数据
2、sort	整个MR框架中只有map端是无序到有序的过程，用的是快速排序
			reduce这里的所谓的sort其实你可以想成一个对着map排好序的一堆小文件归并排序
		grouping comparator
		1970-1-22	33	bj
		1970-1-8	23	sh
			排序比较器：年、月、温度，，且温度倒序
			分组比较器：年、月
3 reduce
	run：rIter = shuffle..	//reduce拉取回自己的数据，并包装成迭代器~！真@迭代器
				file(磁盘上) -> open ->	readline -> hasNext() next()
				时时刻刻想：我们做的是大数据计算，数据可能撑爆内存
	RawComparator comparator = job.getOutputValueGroupingComparator();
				1、取用户设置的分组比较器
				2、取getOutputKeyComparator();
					1、优先取用户覆盖的自定义排序比较器
					2、保底，取key这个类型自身的比较器
				a、分组比较器可不可以复用排序比较器
					什么叫做排序比较器：	返回值：-1，0，1
					什么叫做分组比较器：	返回值：布尔值：false/true
					排序比较器可不可以做分组比较器：可以的
					
				mapTask						reduceTask
				1、取用户定义的排序比较器		 1、取用户自定义的分组比较器
                2、取key自身的排序比较器		  2、取用户定义的排序比较器
                							3、取key自身的排序比较器
                组合方式：
                		1）不设置排序和分组比较器；
                				map：取key自身的排序比较器
                				reduce：取key自身的排序比较器
                		2）设置了排序
                				map：取用户定义的排序比较器
                				reduce：用户定义的排序比较器
                		3）设置了分组：
                				map:取key自身的排序比较器
                				reduce:取用户自定义的分组比较器
						4）设置了排序分组
								map：取用户定义的排序比较器
								reduce：取用户自定义的分组比较器
				做减法：结论，框架很灵活，给了我们各种加工数据排序和分组的方式
				ReduceContextImpl
					input = rIter 真@迭代器
					hasMore	= true
					nextKeyIsSame = false
					iterable = ValueIterable //内部类
					iterator = ValueIterator
					ValueIterable
						iterator()
							return iterator;
					ValueIterator	假@迭代器	嵌套迭代器
                    	hasNext
                    		 return firstValue || nextKeyIsSame;
                    	next
                    		nextKeyValue()
					nextKey()
						nextKeyValue()
					nextKeyValue()
						1、通过input取数据，取key和value赋值
						2、返回布尔值
						3、多取一条记录判断更新nextKeyIsSame 窥探下一条记录是不是还是一组的
					getCurrentKey()
						return key
					getValues()
						return iterable
				为什么有迭代器模式：迭代器 是对象，没有数据 有游标，有指向
                举一个栗子：如果有一个集合，集合中有很多元素，只有一个方法，只有一个线程，集合传进去是没				有问题的，维护一个游标就可以了，如果有2个方法，2个线程，集合各给一份传给2个线程，线程拥				 有对象的引用地址，操控对象迭代的时候，用的是同一个游标
                 
                            
 				reduceTask拉取回的数据被包装成一个迭代器
 				reduce方法被调用的时候，并没有把一组数据真的加载到内存
 					而是传递一个迭代器-values
 					在reduce方法中使用这个迭代器的时候
 					hasNext方法判断nextKeyIsSame：下一条是不是还是一组
 					next方法：负责调取nextKeyValue（）方法，从reduceTask级别的迭代器中取记录，并同时更新nextKeyIsSame
 				以上设计的艺术
 					充分利用了迭代器模式：规避了内存数据OOM的问题
 										且：之前不是说了框架是排序的
 											所以真假迭代器一次I/O就可以线性处理每一组数据。
```

分组取topN 

	2019-6-1 22:22:22		1	31
	2019-5-21 22:22:22		3	33
	2019-6-1 22:22:22		1	33
	2019-6-2 22:22:22		2	38
	2018-3-11 22:22:22		3	18
	2018-4-23 22:22:22		1	22
	1970-8-23 22:22:22		2	23
	1970-8-8 22:22:22		1	32
需求：每个月气温最高的2天

把某一年某一月的数据摘出来，判断出那2天气温最高

map最重要的环节，k，v正确的设计

hadoop put 强制覆盖文件
hdfs dfs -put -f  dataTopN.txt /data/topn/input

