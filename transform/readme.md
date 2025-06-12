# 分布式文件分发工具

## 项目简介
***背景：现网环境禁用ssh,scp等工具的时候可使用本工具。本工具基于http协议***

**本工具适用于大规模服务器集群的安全批量文件分发。** 

主要特点：

- 支持100+主机并发
- 多文件、多目录递归分发
- 分块传输，断点续传
- 块级SHA-256校验，自动重试
- 可选AES加密传输、Token权限认证
- 支持传输进度持久化、自动清理
- 控制台详细日志，易于扩展为WebUI或API

---

## 目录结构


### 文件说明

- **`com.tongtech.transform/`** - 主包目录
    - `AuthUtils.java` - 认证鉴权工具类
    - `ChunkInfo.java` - 文件分块信息类
    - `ConfigUtils.java` - 配置读取工具类
    - `CryptoUtils.java` - 加解密工具类
    - `FileDistributor.java` - 客户端文件分发主入口
    - `FileReceiverServer.java` - 服务端文件接收主入口
    - `FileUtils.java` - 文件操作工具类
    - `ProgressStore.java` - 传输进度存储类

- **根目录文件**
    - `config.properties` - 系统配置文件
    - `progress_db.json` - 记录断点续传进度的数据库文件

## 依赖环境

- **JDK 1.8+**
- **Jackson (用于JSON进度持久化)**
- **建议使用Maven构建**

## 功能特性
- **支持多台目标主机并发分发**
- **支持目录递归、批量多文件传输**
- **文件分块传输，支持断点续传与分块重试**
- **分块完整性校验（SHA-256）**
- **可选AES加密传输**
- **Token权限认证**
- **传输失败自动重试、失败统计**
- **历史分发进度自动清理**
- **支持详细日志输出（控制台）**


## 配置文件参数说明
### config.properties
```properties
auth.token=your_token
aes.key=1234567890123456
enable.encrypt=true
targets=10.10.84.251,10.10.84.252
port=18080
# 要分发的本地目录（递归所有文件）
source.dir=/data/testfile/sender
# 目标主机写入目录
target.dir=/data/testfile/receiver
# 块最大重试次数
max.retry=5
# 并发线程数
max.thread=8
# 进度持久化文件
progress.store=progress_db.json
# 历史进度保留天数
keep.days=7
# 块大小，单位字节
chunk.size=1048576
```


| 参数名             | 示例值                          | 说明                                                                 |
|--------------------|---------------------------------|----------------------------------------------------------------------|
| `auth.token`       | `your_token`                   | 认证 Token，需与服务端保持一致                                        |
| `aes.key`          | `1234567890123456`             | 16 字节 AES 密钥（当 `enable.encrypt=true` 时必填）                   |
| `enable.encrypt`   | `true`                         | 是否启用加密传输（`true`/`false`）                                    |
| `targets`          | `10.10.84.251,10.10.84.252`        | 目标主机 IP 列表，多个地址用逗号分隔                                   |
| `port`             | `18080`                        | 目标服务端监听端口号                                                  |
| `source.dir`       | `/data/testfile/sender`        | 待分发的本地源目录（支持递归分发子目录内容）                           |
| `target.dir`       | `/data/testfile/receiver`      | 目标主机上接收文件的存储目录                                           |
| `max.retry`        | `5`                            | 单个文件块传输失败时的最大重试次数                                     |
| `max.thread`       | `8`                            | 并发传输的最大线程数                                                  |
| `progress.store`   | `progress_db.json`             | 断点续传进度记录文件（默认存储在运行目录）                             |
| `keep.days`        | `7`                            | 进度历史记录的保留天数（过期自动清理）                                 |
| `chunk.size`       | `1048576`                      | 文件分块大小（单位：字节，默认 1MB = 1024×1024）                      |

### 注意事项
1. **加密相关**
    - 启用加密时需确保所有节点的 `aes.key` 完全相同
    - AES 密钥长度必须为 **16/24/32 字节**（对应 AES-128/AES-192/AES-256）
2. **网络配置**
    - `targets` 中的主机需提前开放 `port` 端口防火墙
3. **路径规范**
    - 目录路径建议使用绝对路径，Linux 系统注意权限控制
4. **文件格式**
    - 文件中不要有空格之类的不可见字符

## 使用方法

***分发端如下执行***
```bash
[root@gbase8a-253 ~]# java -cp transform.jar com.tongtech.transform.FileDistributor
[Client] 共需分发文件2个，目标主机2台
[Client] [10.10.84.251] 上传 demo1.csv (14字节, 1块)
[Client] [10.10.84.252] 上传 demo1.csv (14字节, 1块)
[Client] [10.10.84.252] demo1.csv 块1/1 成功
[Client] [10.10.84.252] demo1.csv 文件全部上传完成
[Client] [10.10.84.252] 上传 demo.csv (14字节, 1块)
[Client] [10.10.84.252] demo.csv 块1/1 成功
[Client] [10.10.84.252] demo.csv 文件全部上传完成
[Client] [10.10.84.251] demo1.csv 块1/1 成功
[Client] [10.10.84.251] demo1.csv 文件全部上传完成
[Client] [10.10.84.251] 上传 demo.csv (14字节, 1块)
[Client] [10.10.84.251] demo.csv 块1/1 成功
[Client] [10.10.84.251] demo.csv 文件全部上传完成
[Client] 所有文件分发任务已完成。
```

***接收端如下执行***

```bash
[root@gbase8a-252 ~]# java -cp transform.jar com.tongtech.transform.FileReceiverServer
[Server] FileReceiverServer started on port 18080
[Server] 10.10.84.253 文件 demo1.csv 第0块接收完成
[Server] 10.10.84.253 文件 demo1.csv 合并成功，写入 /data/testfile/receiver/demo1.csv
[Server] 10.10.84.253 文件 demo.csv 第0块接收完成
[Server] 10.10.84.253 文件 demo.csv 合并成功，写入 /data/testfile/receiver/demo.csv
```
```bash
[root@gbase8a-251 ~]# java -cp transform.jar com.tongtech.transform.FileReceiverServer
[Server] FileReceiverServer started on port 18080
[Server] 10.10.84.253 文件 demo1.csv 第0块接收完成
[Server] 10.10.84.253 文件 demo1.csv 合并成功，写入 /data/testfile/receiver/demo1.csv
[Server] 10.10.84.253 文件 demo.csv 第0块接收完成
[Server] 10.10.84.253 文件 demo.csv 合并成功，写入 /data/testfile/receiver/demo.csv
```

**⚠️ 说明**
：可使用shell脚本置于后台执行
## License
### MIT





