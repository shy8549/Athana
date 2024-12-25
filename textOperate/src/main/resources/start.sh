#!/bin/bash

# 设置日志目录
LOG_DIR="logs"
mkdir -p "$LOG_DIR"

# 运行 JAR 文件
java -cp "libs/*:textOperate-1.0-SNAPSHOT.jar" com.datacolumnoperate.ColumnOperate "$@" 2>&1 | tee "$LOG_DIR/application.log"
