package com.datacolumnoperate.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class ConfigTest {

    @Test
    public void testConfig_GettersAndSetters() {
        Config config = new Config();
        config.setInputFile("input.csv");
        config.setOutputFile("output.csv");
        config.setInputDelimiter(",");
        config.setOutputDelimiter("|");

        List<ColumnOperationConfig> columns = new ArrayList<>();
        ColumnOperationConfig column = new ColumnOperationConfig();
        column.setColumnIndex(1);
        columns.add(column);
        config.setColumns(columns);

        assertEquals("input.csv", config.getInputFile());
        assertEquals("output.csv", config.getOutputFile());
        assertEquals(",", config.getInputDelimiter());
        assertEquals("|", config.getOutputDelimiter());
        assertNotNull(config.getColumns());
        assertEquals(1, config.getColumns().size());
        assertEquals(1, config.getColumns().get(0).getColumnIndex());
    }

    @Test
    public void testConfig_ToString() {
        Config config = new Config();
        config.setInputFile("input.csv");
        config.setOutputFile("output.csv");
        config.setInputDelimiter(",");
        config.setOutputDelimiter("|");

        List<ColumnOperationConfig> columns = new ArrayList<>();
        ColumnOperationConfig column = new ColumnOperationConfig();
        column.setColumnIndex(1);
        columns.add(column);
        config.setColumns(columns);

        String configString = config.toString();
        assertTrue(configString.contains("inputFile='input.csv'"));
        assertTrue(configString.contains("outputFile='output.csv'"));
        assertTrue(configString.contains("inputDelimiter=','"));
        assertTrue(configString.contains("outputDelimiter='|'"));
        assertTrue(configString.contains("columnIndex=1"));
    }
}

