package com.datacolumnoperate.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class ColumnOperationConfigTest {

    @Test
    public void testColumnOperationConfig_GettersAndSetters() {
        ColumnOperationConfig colConfig = new ColumnOperationConfig();
        colConfig.setColumnIndex(2);

        List<OperationConfig> operations = new ArrayList<>();
        OperationConfig opConfig = new OperationConfig();
        opConfig.setType(OperationType.ENCRYPT);
        operations.add(opConfig);
        colConfig.setOperations(operations);

        assertEquals(2, colConfig.getColumnIndex());
        assertNotNull(colConfig.getOperations());
        assertEquals(1, colConfig.getOperations().size());
        assertEquals(OperationType.ENCRYPT, colConfig.getOperations().get(0).getType());
    }

    @Test
    public void testColumnOperationConfig_ToString() {
        ColumnOperationConfig colConfig = new ColumnOperationConfig();
        colConfig.setColumnIndex(2);

        List<OperationConfig> operations = new ArrayList<>();
        OperationConfig opConfig = new OperationConfig();
        opConfig.setType(OperationType.ENCRYPT);
        opConfig.setParams(null);
        operations.add(opConfig);
        colConfig.setOperations(operations);

        String colConfigString = colConfig.toString();
        assertTrue(colConfigString.contains("columnIndex=2"));
        assertTrue(colConfigString.contains("type=ENCRYPT"));
    }
}

