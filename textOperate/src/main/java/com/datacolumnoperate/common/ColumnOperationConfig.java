package com.datacolumnoperate.common;

import java.util.List;
import java.util.Map;

public class ColumnOperationConfig {
    private int columnIndex;
    private List<OperationConfig> operations;

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

    public static class OperationConfig {
        private OperationType type;
        private Map<String, String> params;

        public OperationType getType() {
            return type;
        }

        public void setType(OperationType type) {
            this.type = type;
        }

        public Map<String, String> getParams() {
            return params;
        }

        public void setParams(Map<String, String> params) {
            this.params = params;
        }
    }
}
