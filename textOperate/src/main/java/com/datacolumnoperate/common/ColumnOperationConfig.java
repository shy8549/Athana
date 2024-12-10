package com.datacolumnoperate.common;

import java.util.List;

/**
 * 列操作配置类，定义每一列需要执行的操作及其参数。
 */
public class ColumnOperationConfig {
    private int columnIndex;
    private List<OperationConfig> operations;

    // Getters and Setters

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public List<OperationConfig> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationConfig> operations) {
        this.operations = operations;
    }

    @Override
    public String toString() {
        return "ColumnOperationConfig{" +
                "columnIndex=" + columnIndex +
                ", operations=" + operations +
                '}';
    }
}
