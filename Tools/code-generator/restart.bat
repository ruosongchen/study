@echo off
::延迟5秒启动程序
@ping 127.0.0.1 -n 5 & java -jar code-generator-0.0.1-SNAPSHOT.jar
pause;