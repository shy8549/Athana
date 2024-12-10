package com.datacolumnoperate;

import com.datacolumnoperate.common.Config;
import com.datacolumnoperate.common.ColumnOperationConfig;
import com.datacolumnoperate.utils.FileProcessor;
import com.datacolumnoperate.utils.JsonConfigParser;
import com.datacolumnoperate.config.OperationModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * 主类，程序入口点。
 */
public class ColumnOperate {
    private static final Logger logger = LoggerFactory.getLogger(ColumnOperate.class);

    public static void main(String[] args) {
        if (args.length < 1) {
            logger.error("Configuration file path is missing.");
            System.out.println("Usage: java -jar textOperate.jar <path_to_config.json>");
            System.exit(1);
        }

        String configPath = args[0];
        logger.info("Loading configuration from {}", configPath);

        try {
            // 解析配置文件
            Config config = JsonConfigParser.parse(configPath);
            logger.info("Configuration loaded: {}", config);

            // 创建 Guice Injector 并加载 OperationModule
            Injector injector = Guice.createInjector(new OperationModule());

            // 获取 FileProcessor 实例（通过依赖注入）
            FileProcessor processor = injector.getInstance(FileProcessor.class);

            // 执行文件处理
            processor.process(
                    new File(config.getInputFile()),
                    new File(config.getOutputFile()),
                    config.getInputDelimiter(),
                    config.getOutputDelimiter(),
                    config.getColumns()
            );

            logger.info("File processing completed successfully.");

        } catch (Exception e) {
            logger.error("An error occurred during file processing.", e);
            System.exit(1);
        }
    }
}
