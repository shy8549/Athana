package com.datacolumnoperate;

import com.datacolumnoperate.common.Config;
import com.datacolumnoperate.utils.FileProcessor;
import com.datacolumnoperate.utils.JsonConfigParser;
import com.datacolumnoperate.config.OperationModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * ColumnOperate 是项目的主入口类。
 *
 * 程序的主要流程如下：
 * 1. 从命令行参数获取配置文件路径。
 * 2. 使用 JsonConfigParser 解析 JSON 格式的配置文件，获取 Config 对象。
 * 3. 使用 Google Guice 创建依赖注入容器（Injector），加载 OperationModule 以绑定操作类型与实现类。
 * 4. 从 Injector 中获取 FileProcessor 实例，以完成对输入文件的列处理操作。
 * 5. 根据 Config 中的配置信息（输入文件、输出文件、分隔符、列操作列表）进行文件处理。
 *
 * 运行命令示例：
 * java -jar textOperate.jar path/to/config.json
 *
 * 请确保 config.json 存在且格式正确，否则会抛出异常并在日志中记录。
 */
public class ColumnOperate {
    // 使用 SLF4J 日志框架记录日志信息，logger 对象用于输出信息、警告和错误日志
    private static final Logger logger = LoggerFactory.getLogger(ColumnOperate.class);

    public static void main(String[] args) {
        // 检查是否传入配置文件路径参数，如果没有则输出使用示例并退出
        if (args.length < 1) {
            logger.error("Configuration file path is missing.");
            System.out.println("Usage: java -jar textOperate.jar <path_to_config.json>");
            System.exit(1);
        }

        // 从命令行参数获取配置文件路径
        String configPath = args[0];
        logger.info("Loading configuration from {}", configPath);

        try {
            // 使用 JsonConfigParser 解析 JSON 格式的配置文件，生成 Config 对象
            Config config = JsonConfigParser.parse(configPath);
            logger.info("Configuration loaded: {}", config);

            // 使用 Google Guice 创建依赖注入容器（Injector）
            // OperationModule 是我们定义的 Guice 模块，负责将 OperationType 绑定到具体的 Operation 实现类
            Injector injector = Guice.createInjector(new OperationModule());

            // 从 Injector 中获取 FileProcessor 实例
            // FileProcessor 在 OperationModule 或 Guice 配置中通过构造函数注入所需依赖（如 OperationFactory）
            FileProcessor processor = injector.getInstance(FileProcessor.class);

            // 调用 FileProcessor 的 process 方法对文件进行列操作处理
            // 参数包括输入文件、输出文件、输入/输出分隔符、以及列操作配置列表（来自 config）
            processor.process(
                    new File(config.getInputFile()),
                    new File(config.getOutputFile()),
                    config.getInputDelimiter(),
                    config.getOutputDelimiter(),
                    config.getColumns()
            );

            // 处理完成后记录信息日志
            logger.info("File processing completed successfully.");

        } catch (Exception e) {
            // 如果在处理文件过程中出现任何异常，将记录错误日志并退出程序
            logger.error("An error occurred during file processing.", e);
            System.exit(1);
        }
    }
}

