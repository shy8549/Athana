package com.datacolumnoperate.common;

import java.util.List;

public class Config {
    private String inputFile;
    private String outputFile;
    private String inputDelimiter;
    private String outputDelimiter;
    private List<ColumnOperationConfig> columns;

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
}
