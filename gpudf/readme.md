# greenplum java udf

## 前置条件
- 需要安装greenplum的PL/java
- 每个segment节点都需要放置jar包到同一目录

## 使用方法

**创建函数**

```sql
CREATE FUNCTION javatest."ENCRYPT"(hex_cipher text)
RETURNS text
AS 'com.tongtech.crypto.sdk.utils.StringOperation.AesEncoder'
LANGUAGE java;


CREATE FUNCTION javatest."DECRYPT"(hex_cipher text)
RETURNS text
AS 'com.tongtech.crypto.sdk.utils.StringOperation.AesDecoder'
LANGUAGE java;

CREATE or replace FUNCTION javatest."maskMiddle"(cipher_text text, startIndex int, endIndex int)
RETURNS text
AS 'com.tongtech.crypto.sdk.utils.StringOperation.maskMiddle'
LANGUAGE java;

CREATE or replace FUNCTION javatest."maskFront"(cipher_text text, startIndex int)
RETURNS text
AS 'com.tongtech.crypto.sdk.utils.StringOperation.maskFront'
LANGUAGE java;

CREATE or replace FUNCTION javatest."maskRear"(cipher_text text, endIndex int)
RETURNS text
AS 'com.tongtech.crypto.sdk.utils.StringOperation.maskRear'
LANGUAGE java;

CREATE or replace FUNCTION javatest."maskRear"(cipher_text text, endIndex int)
RETURNS text
AS 'com.tongtech.crypto.sdk.utils.StringOperation.maskRear'
LANGUAGE java;
```

**调用函数**
```sql
SJGXPT=# select javatest."maskRear"('abcdefghijklmnopqrstuvwxyz',16);
          maskRear          
----------------------------
 abcdefghij****************
(1 row)

SJGXPT=# select javatest."maskFront"('abcdefghijklmnopqrstuvwxyz',9);
         maskFront          
----------------------------
 *********jklmnopqrstuvwxyz
(1 row)

SJGXPT=# select javatest."maskMiddle"('abcdefghijklmnopqrstuvwxyz',9,16);
         maskMiddle         
----------------------------
 abcdefghi********rstuvwxyz
(1 row)

SJGXPT=# select javatest."ENCRYPT"('河南省，信阳市，淮滨县，方集镇');
                                             ENCRYPT                                              
--------------------------------------------------------------------------------------------------
 BCE8ED23E84D49F56261278D6B46CD82BD94900954CDF7223308037E728318F7DD7199C733D6BD8667138A6FD888A02D
(1 row)

SJGXPT=# select javatest."DECRYPT"('BCE8ED23E84D49F56261278D6B46CD82BD94900954CDF7223308037E728318F7DD7199C733D6BD8667138A6FD888A02D');
       
```

**单独调用jar包**

```bash
[gpadmin@tongzt-71 java]$ java -cp /home/gpadmin/java/gpudf.jar  com.tongtech.crypto.sdk.utils.MethodInvoker com.tongtech.crypto.sdk.utils.AesEncoder eval "河南省，信阳市，淮滨县，方集镇" "112323123|||||||"
BCE8ED23E84D49F56261278D6B46CD82BD94900954CDF7223308037E728318F7DD7199C733D6BD8667138A6FD888A02D
[gpadmin@tongzt-71 java]$ java -cp /home/gpadmin/java/gpudf.jar  com.tongtech.crypto.sdk.utils.MethodInvoker com.tongtech.crypto.sdk.utils.AesDecoder eval "BCE8ED23E84D49F56261278D6B46CD82BD94900954CDF7223308037E728318F7DD7199C733D6BD8667138A6FD888A02D" "112323123|||||||"
河南省，信阳市，淮滨县，方集镇
[gpadmin@tongzt-71 java]$ java -cp /home/gpadmin/java/gpudf.jar  com.tongtech.crypto.sdk.utils.MethodInvoker com.tongtech.crypto.sdk.utils.MaskUtil maskAll "abcdefghijklmnopqrstuvwxyz"
**************************
[gpadmin@tongzt-71 java]$ java -cp /home/gpadmin/java/gpudf.jar  com.tongtech.crypto.sdk.utils.MethodInvoker com.tongtech.crypto.sdk.utils.MaskUtil maskFront "abcdefghijklmnopqrstuvwxyz" 9
*********jklmnopqrstuvwxyz
[gpadmin@tongzt-71 java]$ java -cp /home/gpadmin/java/gpudf.jar  com.tongtech.crypto.sdk.utils.MethodInvoker com.tongtech.crypto.sdk.utils.MaskUtil maskRear "abcdefghijklmnopqrstuvwxyz" 16
abcdefghij****************
[gpadmin@tongzt-71 java]$ java -cp /home/gpadmin/java/gpudf.jar  com.tongtech.crypto.sdk.utils.MethodInvoker com.tongtech.crypto.sdk.utils.MaskUtil maskMiddle "abcdefghijklmnopqrstuvwxyz" 9 16
abcdefghi********rstuvwxyz
```
## License
### MIT