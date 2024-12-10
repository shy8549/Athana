package com.datacolumnoperate.common;

import java.util.List;

/**
 * 项目配置类，映射 config.json 中的配置。
 */
public class Config {
    private String inputFile;
    private String outputFile;
    private String inputDelimiter;
    private String outputDelimiter;
    private List<ColumnOperationConfig> columns;

    // 默认构造函数
    public Config() {
    }

    // 带参构造函数
    public Config(String inputFile, String outputFile, String inputDelimiter, String outputDelimiter, List<ColumnOperationConfig> columns) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.inputDelimiter = inputDelimiter;
        this.outputDelimiter = outputDelimiter;
        this.columns = columns;
    }

    // Getters and Setters

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getInputDelimiter() {
        return inputDelimiter;
    }

    public void setInputDelimiter(String inputDelimiter) {
        this.inputDelimiter = inputDelimiter;
    }

    public String getOutputDelimiter() {
        return outputDelimiter;
    }

    public void setOutputDelimiter(String outputDelimiter) {
        this.outputDelimiter = outputDelimiter;
    }

    public List<ColumnOperationConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnOperationConfig> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "Config{" +
                "inputFile='" + inputFile + '\'' +
                ", outputFile='" + outputFile + '\'' +
                ", inputDelimiter='" + inputDelimiter + '\'' +
                ", outputDelimiter='" + outputDelimiter + '\'' +
                ", columns=" + columns +
                '}';
    }
}
