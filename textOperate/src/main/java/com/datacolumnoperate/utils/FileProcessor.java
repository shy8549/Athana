package com.datacolumnoperate.utils;

import com.datacolumnoperate.common.ColumnOperationConfig;
import com.datacolumnoperate.common.Config;
import com.datacolumnoperate.common.OperationType;
import com.datacolumnoperate.utils.operations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileProcessor {
    private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);

    private Config config;
    private Map<Integer, List<OperationWithParams>> operationsMap = new HashMap<>();

    public FileProcessor(Config config) {
        this.config = config;
        initializeOperations();
    }

    private void initializeOperations() {
        for (ColumnOperationConfig colOpConfig : config.getColumns()) {
            int columnIndex = colOpConfig.getColumnIndex();
            List<OperationWithParams> opList = new ArrayList<>();
            for (ColumnOperationConfig.OperationConfig opConfig : colOpConfig.getOperations()) {
                Operation operation = OperationFactory.getOperation(opConfig.getType());
                opList.add(new OperationWithParams(operation, opConfig.getParams()));
            }
            operationsMap.put(columnIndex, opList);
            logger.info("Initialized operations for column index {}: {}", columnIndex, colOpConfig.getOperations());
        }
    }

    public void process() throws Exception {
        Path inputPath = Paths.get(config.getInputFile());
        Path outputPath = Paths.get(config.getOutputFile());

        logger.info("Starting processing. Input: {}, Output: {}", inputPath, outputPath);

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
             Stream<String> lines = Files.lines(inputPath, StandardCharsets.UTF_8)) {

            String inputDelimiter = config.getInputDelimiter();
            String outputDelimiter = config.getOutputDelimiter();

            lines.forEach(line -> {
                try {
                    String[] tokens = line.split(Pattern.quote(inputDelimiter), -1);
                    for (Map.Entry<Integer, List<OperationWithParams>> entry : operationsMap.entrySet()) {
                        int colIndex = entry.getKey();
                        if (colIndex < 0 || colIndex >= tokens.length) {
                            logger.warn("Column index {} out of bounds for line: {}", colIndex, line);
                            continue; // 跳过该列的操作
                        }
                        String original = tokens[colIndex];
                        String transformed = original;
                        for (OperationWithParams opWithParams : entry.getValue()) {
                            transformed = opWithParams.operation.execute(transformed, opWithParams.params);
                        }
                        tokens[colIndex] = transformed;
                    }
                    String outputLine = String.join(outputDelimiter, tokens);
                    writer.write(outputLine);
                    writer.newLine();
                } catch (Exception e) {
                    logger.error("Error processing line: {}. Error: {}", line, e.getMessage(), e);
                    // 根据需求决定是否继续处理其他行
                }
            });
        }

        logger.info("Processing completed successfully.");
    }

    // 内部类用于存储操作及其参数
    private static class OperationWithParams {
        Operation operation;
        Map<String, String> params;

        OperationWithParams(Operation operation, Map<String, String> params) {
            this.operation = operation;
            this.params = params;
        }
    }
}
