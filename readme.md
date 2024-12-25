# Athana 为给自己使用的工具

## Athana目前有两个模块

- dataMaker (造数工具)
- textOperate (文本处理)

---

## dataMaker

**说明**：使用IDEA工具打包为jar即可使用。
## textOperate

**说明**：textOperate使用assembly构建，mvn clean package -DskipTests 构建后为tar.gz包，解压后目录为

```bash
# 执行的时候 sh start.sh conf/config.json 即可 ;
[gpadmin@bogon tool_test]$ ll textOperate-1.0-SNAPSHOT
total 8
drwxrwxr-x. 2 gpadmin gpadmin   55 Dec 25 09:41 conf
drwxr-xr-x. 2 gpadmin gpadmin   53 Dec 24 17:49 data
drwxrwxr-x. 2 gpadmin gpadmin 4096 Dec 24 17:46 libs
drwxrwxr-x. 2 gpadmin gpadmin   56 Dec 25 09:42 logs
-rw-r--r--. 1 gpadmin gpadmin  216 Dec 20 14:24 start.sh
```

可对文本的列进行函数操作，目前包括时间转换，加密，截取，正则，哈希
