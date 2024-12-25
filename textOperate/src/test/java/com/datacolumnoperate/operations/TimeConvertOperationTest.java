package com.datacolumnoperate.operations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.datacolumnoperate.exceptions.*;
import java.util.HashMap;
import java.util.Map;

public class TimeConvertOperationTest {

    @Test
    public void testExecute_FormatToMS() throws Exception {
        Operation operation = new TimeConvertOperation();
        Map<String, String> params = new HashMap<>();
        params.put("inputFormat", "yyyyMMddHHmmss");
        params.put("outputFormat", "MS");

        String input = "20231210120000"; // 2023-12-10 12:00:00
        String result = operation.execute(input, params);
        assertNotNull(result);
        assertTrue(result.matches("\\d+")); // 确保输出为数字
    }

    @Test
    public void testExecute_MSToFormat() throws Exception {
        Operation operation = new TimeConvertOperation();
        Map<String, String> params = new HashMap<>();
        params.put("inputFormat", "MS");
        params.put("outputFormat", "yyyy-MM-dd HH:mm:ss");

        long currentMs = System.currentTimeMillis();
        String input = String.valueOf(currentMs);
        String result = operation.execute(input, params);
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")); // 例如 "2023-12-10 12:00:00"
    }

    @Test
    public void testExecute_InvalidInputFormat() {
        Operation operation = new TimeConvertOperation();
        Map<String, String> params = new HashMap<>();
        params.put("inputFormat", "INVALID_FORMAT");
        params.put("outputFormat", "MS");

        String input = "2023-12-10 12:00:00";
        Exception exception = assertThrows(RuntimeException.class, () -> operation.execute(input, params));

        String expectedMessage = "TIME_CONVERT operation failed.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testExecute_MissingParams() {
        Operation operation = new TimeConvertOperation();
        Map<String, String> params = new HashMap<>();
        params.put("inputFormat", "yyyyMMddHHmmss");
        // Missing "outputFormat"

        String input = "20231210120000";
        Exception exception = assertThrows(TimeConvertException.class, () -> operation.execute(input, params));

        String expectedMessage = "Missing 'inputFormat' or 'outputFormat' parameter for TIME_CONVERT operation.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testExecute_InvalidMSInput() {
        Operation operation = new TimeConvertOperation();
        Map<String, String> params = new HashMap<>();
        params.put("inputFormat", "MS");
        params.put("outputFormat", "yyyy-MM-dd HH:mm:ss");

        String input = "invalid_ms";
        Exception exception = assertThrows(RuntimeException.class, () -> operation.execute(input, params));

        String expectedMessage = "TIME_CONVERT operation failed.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

