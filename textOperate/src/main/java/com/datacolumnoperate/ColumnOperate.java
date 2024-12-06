package com.datacolumnoperate;

import com.datacolumnoperate.common.Config;
import com.datacolumnoperate.utils.FileProcessor;
import com.datacolumnoperate.utils.JsonConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColumnOperate {
    private static final Logger logger = LoggerFactory.getLogger(ColumnOperate.class);

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar ColumnOperate.jar <config.json>");
            System.exit(1);
        }

        String configPath = args[0];
        logger.info("Starting ColumnOperate with config file: {}", configPath);

        try {
            // 解析配置
            Config config = JsonConfigParser.parseConfig(configPath);
            logger.info("Configuration parsed successfully.");

            // 处理文件
            FileProcessor processor = new FileProcessor(config);
            processor.process();

            logger.info("文件处理完成。");
        } catch (Exception e) {
            logger.error("文件处理失败: {}", e.getMessage(), e);
            System.err.println("文件处理失败: " + e.getMessage());
            System.exit(1);
        }
    }
}
