package com.datacolumnoperate.utils;

import com.datacolumnoperate.common.ColumnOperationConfig;
import com.datacolumnoperate.common.OperationConfig;
import com.datacolumnoperate.operations.Operation;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * FileProcessor 负责读取输入文件，执行列级操作，并写入输出文件。
 */
public class FileProcessor {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);
    private static final int BUFFER_SIZE = 8192;
    private final OperationFactory operationFactory;

    /**
     * 构造函数，通过依赖注入获取 OperationFactory。
     *
     * @param operationFactory 操作工厂实例
     */
    @Inject
    public FileProcessor(OperationFactory operationFactory) {
        this.operationFactory = operationFactory;
    }

    /**
     * 处理文件。
     *
     * @param inputFile      输入文件
     * @param outputFile     输出文件
     * @param inputDelimiter 输入分隔符
     * @param outputDelimiter 输出分隔符
     * @param columnConfigs  列操作配置列表
     */
    public void process(File inputFile, File outputFile, String inputDelimiter, String outputDelimiter, List<ColumnOperationConfig> columnConfigs) {
        logger.info("Starting file processing: {} -> {}", inputFile.getAbsolutePath(), outputFile.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8), BUFFER_SIZE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8), BUFFER_SIZE)) {

            String line;
            long lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] columns = line.split(inputDelimiter, -1); // -1 保留空字符串

                for (ColumnOperationConfig colConfig : columnConfigs) {
                    // 调整列索引，从1开始
                    int index = colConfig.getColumnIndex() - 1;
                    if (index < 0 || index >= columns.length) {
                        logger.warn("Line {}: Column index {} (real file index {}) is out of bounds for line: {}", lineNumber, index, colConfig.getColumnIndex(), line);
                        continue;
                    }

                    String originalValue = columns[index];
                    String processedValue = originalValue;

                    List<OperationConfig> operations = colConfig.getOperations();
                    if (operations == null || operations.isEmpty()) {
                        logger.warn("Line {}: No operations defined for column index {}", lineNumber, index);
                        continue;
                    }

                    for (OperationConfig opConfig : operations) {
                        Operation operation;
                        try {
                            operation = operationFactory.getOperation(opConfig.getType());
                            processedValue = operation.execute(processedValue, opConfig.getParams());
                        } catch (Exception e) {
                            logger.error("Line {}: Error processing column index {} with value '{}'. Operation: {}", lineNumber, index, originalValue, opConfig.getType(), e);
                            // 根据需求决定是否继续处理其他操作或跳过
                            // 这里选择继续处理下一个操作
                        }
                    }

                    columns[index] = processedValue;
                }

                String processedLine = String.join(outputDelimiter, columns);
                writer.write(processedLine);
                writer.newLine();
            }

            logger.info("File processing completed. Total lines processed: {}", lineNumber);

        } catch (IOException e) {
            logger.error("I/O error during file processing.", e);
            throw new RuntimeException("File processing failed.", e);
        }
    }
}
