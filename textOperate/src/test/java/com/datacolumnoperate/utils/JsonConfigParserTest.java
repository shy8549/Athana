package com.datacolumnoperate.utils;

import com.datacolumnoperate.common.Config;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonConfigParserTest {

    @Test
    public void testParse_ValidConfig() throws Exception {
        // 创建临时配置文件
        File tempConfig = File.createTempFile("config", ".json");
        tempConfig.deleteOnExit();

        String jsonContent = "{\n" +
                "    \"inputFile\": \"data/input.csv\",\n" +
                "    \"outputFile\": \"data/output.csv\",\n" +
                "    \"inputDelimiter\": \",\",\n" +
                "    \"outputDelimiter\": \",\",\n" +
                "    \"columns\": [\n" +
                "        {\n" +
                "            \"columnIndex\": 0,\n" +
                "            \"operations\": [\n" +
                "                {\n" +
                "                    \"type\": \"TIME_CONVERT\",\n" +
                "                    \"params\": {\n" +
                "                        \"inputFormat\": \"yyyyMMddHHmmss\",\n" +
                "                        \"outputFormat\": \"MS\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        try (FileWriter writer = new FileWriter(tempConfig)) {
            writer.write(jsonContent);
        }

        Config config = JsonConfigParser.parse(tempConfig.getAbsolutePath());
        assertNotNull(config);
        assertEquals("data/input.csv", config.getInputFile());
        assertEquals("data/output.csv", config.getOutputFile());
        assertEquals(",", config.getInputDelimiter());
        assertEquals(",", config.getOutputDelimiter());
        assertNotNull(config.getColumns());
        assertEquals(1, config.getColumns().size());
        assertEquals(0, config.getColumns().get(0).getColumnIndex());
    }

    @Test
    public void testParse_InvalidJson() {
        // 创建临时配置文件
        File tempConfig;
        try {
            tempConfig = File.createTempFile("invalid_config", ".json");
            tempConfig.deleteOnExit();
        } catch (IOException e) {
            fail("Failed to create temporary file for testing.");
            return;
        }

        String invalidJson = "{ invalid json }";

        Exception exception = assertThrows(IOException.class, () -> {
            try (FileWriter writer = new FileWriter(tempConfig)) {
                writer.write(invalidJson);
            }
            JsonConfigParser.parse(tempConfig.getAbsolutePath());
        });

        String expectedMessage = "Failed to parse config file";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testParse_NonExistentFile() {
        String nonExistentPath = "non_existent_config.json";

        Exception exception = assertThrows(IOException.class, () -> {
            JsonConfigParser.parse(nonExistentPath);
        });

        String expectedMessage = "Config file does not exist at path: " + nonExistentPath;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
