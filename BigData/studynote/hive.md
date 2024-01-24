What	Why	How

Hive简介：

解决什么问题：

​	1、存储元数据，统一管理

​	2、用sql来分析数据，学习成本低

角色：元数据	

hive的产生：非java编程者对hdfs的数据做mapreduce操作		

hvie和数据的区别	数据仓库：本质是mapreduce，写代码的方式变了，用sql方式分析数据

​		    1、数据库用来接收用户请求，并在很短的时效内要返回用户结果，而数据仓库不需要，

​			2、数据仓库可以收纳各种数据源的数据（oracle、mysql、mongodb），而数据库不行

​			3、关系型数据库可以修改，而数据仓库不能修改

Hive：解释器，编译器，优化器

Hive 运行时，元数据存储在关系数据库里面（可以采用多种，这里用mysql）

Hive架构：

架构图：

​	CLI：命令行接口

​	JDBC/ODBC：访问数据库接口

​	WEB GUI：web页面接口（淘汰），集成HUE

​	Thrift Server：rpc远程调用接口

​	Driver：核心

​	Metastore：元数据

Client	

Metastore	Driver	Hadoop

Compiler

Hive搭建模式

1、本地模式：连接到一个In-memory的数据库Derby

​				内存数据库，关机后数据丢失

2、远程服务器模式：通过网络连接到一个数据库中，是最经常使用的模式

​				学习时

3、远程服务器模式

​		用于非Java客户端访问元数据库，在服务器端启动MetaStoreServer，客户端利用Thrift协议通过		MetaStroreServer访问元数据库

​				1、元数据和数据库解耦

​				2、元数据抽象出来变成服务，可以提供其他应用使用。

安装mysql步骤：

```
yum install mysql-server -y
service mysqld start
mysql
切换数据库
	use mysql;
	查看表
	show tables;
	查看表数据
	select host,user,password from user;
	增加数据
	grant all privileges on *.* to 'root'@'%' identified by '123' with grant option;
	delet form user where host!='%';
	刷新权限或者重启mysqld的服务（service mysqld restart）
	mysql;
	flush privileges;
	mysqld的服务注册成开机启动
	chkconfig mysqld on
```

切换yum源

```
1、yum install wget
2、找到yum的安装目录
	cd /etc/yum.repos.d/
3、将所有的已经存在的文件添加备份
	1、给文件改名称添加.bak
	2、创建backup目录，将所有的文件移动进去
4、打开镜像网站
	http://mirrors.aliyun.com/
	找到对应的linux系统版本，进行文件下载
5、yum clean all
6、yum makecache
```

安装hive的步骤：

```
1、解压安装
2、修改环境变量
	export HIVE_HOME=/opt/bigdata/hive-2.3.9
	将bin目录添加到PATH路径中
3、修改配置文件，进入/opt/bigdata/hive-2.3.9/conf/
	mv hive-default.xml.template hive-site.xml
	增加配置
		进入到文件之后，将文件原有的配置删除，但保留最后一行，从<configuration></configuration>
		:.,$-1d
	增加如下配置信息：
	<property>
   		<name>hive.metastore.warehouse.dir</name>
   		<value>/user/hive/warehouse</value>
	</property>
	<property>
   		<name>javax.jdo.option.ConnectionURL</name>
   		<value>jdbc:mysql://node01:3306/hive?createDatabaseIfNotExist=true</value>
	</property>
	<property>
   		<name>javax.jdo.option.ConnectionDriverName</name>
   		<value>com.mysql.jdbc.Driver</value>
	</property>
	<property>
   		<name>javax.jdo.option.ConnectionUserName</name>
   		<value>root</value>
	</property>
	<property>
   		<name>javax.jdo.option.ConnectionPassword</name>
   		<value>123</value>
	</property>
4、添加MYSQL的驱动包拷贝到lib目录
5、执行初始化元数据数据库的步骤
	schematool -dbType mysql -initSchema
6、执行hive启动对应的服务
7、执行相应的hive SQL的基本操作

```

1、文件元数据	->	hdfs nn

2、数据	->	 hdfs

3、数据元数据	-> 	mysql

-----------------------------------------------------------------------------------------------------------------------------------------------------------

关机拍快照

9083

```
CREATE [TEMPORARY] [EXTERNAL] TABLE [IF NOT EXISTS] [db_name.]table_name  -- (Note: TEMPORARY available in Hive ``0.14``.``0` `and later)
 ``[(col_name data_type [column_constraint_specification] [COMMENT col_comment], ... [constraint_specification])]
 ``[COMMENT table_comment]
 ``[PARTITIONED BY (col_name data_type [COMMENT col_comment], ...)]
 ``[CLUSTERED BY (col_name, col_name, ...) [SORTED BY (col_name [ASC|DESC], ...)] INTO num_buckets BUCKETS]
 ``[SKEWED BY (col_name, col_name, ...)         -- (Note: Available in Hive ``0.10``.``0` `and later)]
   ``ON ((col_value, col_value, ...), (col_value, col_value, ...), ...)
   ``[STORED AS DIRECTORIES]
 ``[
  ``[ROW FORMAT row_format] 
  ``[STORED AS file_format]
   ``| STORED BY ``'storage.handler.class.name'` `[WITH SERDEPROPERTIES (...)] -- (Note: Available in Hive ``0.6``.``0` `and later)
 ``]
 ``[LOCATION hdfs_path]
 ``[TBLPROPERTIES (property_name=property_value, ...)]  -- (Note: Available in Hive ``0.6``.``0` `and later)
 ``[AS select_statement];  -- (Note: Available in Hive ``0.5``.``0` `and later; not supported ``for` `external tables)
```

 

```
CREATE [TEMPORARY] [EXTERNAL] TABLE [IF NOT EXISTS] [db_name.]table_name
 ``LIKE existing_table_or_view_name
 ``[LOCATION hdfs_path];
```

 

```
data_type
 ``: primitive_type
 ``| array_type
 ``| map_type
 ``| struct_type
 ``| union_type -- (Note: Available in Hive ``0.7``.``0` `and later)
```

 

```
primitive_type
 ``: TINYINT
 ``| SMALLINT
 ``| INT
 ``| BIGINT
 ``| BOOLEAN
 ``| FLOAT
 ``| DOUBLE
 ``| DOUBLE PRECISION -- (Note: Available in Hive ``2.2``.``0` `and later)
 ``| STRING
 ``| BINARY   -- (Note: Available in Hive ``0.8``.``0` `and later)
 ``| TIMESTAMP  -- (Note: Available in Hive ``0.8``.``0` `and later)
 ``| DECIMAL   -- (Note: Available in Hive ``0.11``.``0` `and later)
 ``| DECIMAL(precision, scale) -- (Note: Available in Hive ``0.13``.``0` `and later)
 ``| DATE    -- (Note: Available in Hive ``0.12``.``0` `and later)
 ``| VARCHAR   -- (Note: Available in Hive ``0.12``.``0` `and later)
 ``| CHAR    -- (Note: Available in Hive ``0.13``.``0` `and later)
```

 

```
array_type
 ``: ARRAY < data_type >
```

 

```
map_type
 ``: MAP < primitive_type, data_type >
```

 

```
struct_type
 ``: STRUCT < col_name : data_type [COMMENT col_comment], ...>
```

 

```
union_type
  ``: UNIONTYPE < data_type, data_type, ... > -- (Note: Available in Hive ``0.7``.``0` `and later)
```

 

```
row_format
 ``: DELIMITED [FIELDS TERMINATED BY ``char` `[ESCAPED BY ``char``]] [COLLECTION ITEMS TERMINATED BY ``char``]
    ``[MAP KEYS TERMINATED BY ``char``] [LINES TERMINATED BY ``char``]
    ``[NULL DEFINED AS ``char``]  -- (Note: Available in Hive ``0.13` `and later)
 ``| SERDE serde_name [WITH SERDEPROPERTIES (property_name=property_value, property_name=property_value, ...)]
```

 

```
file_format:
 ``: SEQUENCEFILE
 ``| TEXTFILE  -- (Default, depending on hive.``default``.fileformat configuration)
 ``| RCFILE   -- (Note: Available in Hive ``0.6``.``0` `and later)
 ``| ORC     -- (Note: Available in Hive ``0.11``.``0` `and later)
 ``| PARQUET   -- (Note: Available in Hive ``0.13``.``0` `and later)
 ``| AVRO    -- (Note: Available in Hive ``0.14``.``0` `and later)
 ``| JSONFILE  -- (Note: Available in Hive ``4.0``.``0` `and later)
 ``| INPUTFORMAT input_format_classname OUTPUTFORMAT output_format_classname
```

 

```
column_constraint_specification:
 ``: [ PRIMARY KEY|UNIQUE|NOT NULL|DEFAULT [default_value]|CHECK [check_expression] ENABLE|DISABLE NOVALIDATE RELY/NORELY ]
```

 

```
default_value:
 ``: [ LITERAL|CURRENT_USER()|CURRENT_DATE()|CURRENT_TIMESTAMP()|NULL ] 
```

 

```
constraint_specification:
 ``: [, PRIMARY KEY (col_name, ...) DISABLE NOVALIDATE RELY/NORELY ]
  ``[, PRIMARY KEY (col_name, ...) DISABLE NOVALIDATE RELY/NORELY ]
  ``[, CONSTRAINT constraint_name FOREIGN KEY (col_name, ...) REFERENCES table_name(col_name, ...) DISABLE NOVALIDATE 
  ``[, CONSTRAINT constraint_name UNIQUE (col_name, ...) DISABLE NOVALIDATE RELY/NORELY ]
  ``[, CONSTRAINT constraint_name CHECK [check_expression] ENABLE|DISABLE NOVALIDATE RELY/NORELY ]
```

```
create table psn
(
id int,
name string,
likes array<string>,
address map<string,string>
)
row format delimited
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':';

load data local inpath '/root/data/data' into table psn;

create table psn2
(
id int,
name string,
likes array<string>,
address map<string,string>
);

create table psn3
(
id int,
name string,
likes array<string>,
address map<string,string>
)

row format delimited
fields terminated by '\001'
collection items terminated by '\002'
map keys terminated by '\003';
```

内外部表的区别

1、内部表创建的时候，数据存储在hive的默认目录中，外部表创建的时候需要指定external的关键字同时需要指定存储目录

2、内部表在进行删除的时候，将数据和元数据全部删除，外部表在删除的时候只会删除元数据，不会删除数据

应用场景：

​			内部表，先创建表，在添加数据

​			外部表，可以先创建表，添加数据，也可以先添加数据再创建表

根据公司的业务来决定，但是外部表确实用的比较多

```
create external table psn4
(
id int,
name string,
likes array<string>,
address map<string,string>
)
row format delimited
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':'
location '/hive_data';
load data local inpath '/root/data/data' into table psn4;
```

Hive  分区partition

提高检索数据的效率

分区表

当创建的分区表包含多个分区列的时候，插入数据的时候必须要将所有的分区列都添加值，不可以只写其中一个，分区列的顺序无所谓

当需要单独 添加分区列的时候，必须要添加所有的分区列

当删除表的分区列的值的时候，可以只指定一个分区列，会把当前表的所有满足条件的分区列都删除

当前处理方式存在问题？

​		当数据进入hive的时候，需要根据数据的某一个字段向hive表插入数据，此时无法满足需求，因此需要使用动态分区

当hdfs目录中存在数据，并且符合分区的格式，此时创建外部表的时候，一定要修复分区才可以查询到对应的结构，否则是没有结果的

```
create table psn5
(
id int,
name string,
likes array<string>,
address map<string,string>
)
partitioned by (gender string)
row format delimited
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':';
```



```
create table psn6
(
id int,
name string,
likes array<string>,
address map<string,string>
)
partitioned by (gender string,age int)
row format delimited
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':';
```



```
create external table psn7
(
id int,
name string,
likes array<string>,
address map<string,string>
)
partitioned by (age int)
row format delimited
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':'
location '/msb02';
```



-----------------------------------------------------------------------------------------------------------------------------------------------------------



Hive DML

```
create table psn9
(
id int,
name string
)
row format delimited
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':';

INSERT OVERWRITE TABLE psn9
SELECT id,name FROM psn;
-- 效率非常慢，不推荐使用，工作中用的非常多
from psn
insert into table psn9
select id,name
insert into table psn10
select id;


create table psn10
(
id int
)
row format delimited
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':';

-- 一般不用
insert overwrite local directory  '/root/result' select * from psn;
```

