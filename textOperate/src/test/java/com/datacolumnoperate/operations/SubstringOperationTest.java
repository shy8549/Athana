package com.datacolumnoperate.operations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class SubstringOperationTest {

    @Test
    public void testExecute_ValidIndices() throws Exception {
        Operation operation = new SubstringOperation();
        Map<String, String> params = new HashMap<>();
        params.put("start", "0");
        params.put("end", "5");

        String input = "HelloWorld";
        String expected = "Hello";
        String result = operation.execute(input, params);
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_StartGreaterThanEnd() throws Exception {
        Operation operation = new SubstringOperation();
        Map<String, String> params = new HashMap<>();
        params.put("start", "6");
        params.put("end", "5");

        String input = "HelloWorld";
        String expected = "HelloWorld"; // 根据实现，返回原始字符串
        String result = operation.execute(input, params);
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_InvalidIndices() throws Exception {
        Operation operation = new SubstringOperation();
        Map<String, String> params = new HashMap<>();
        params.put("start", "-1");
        params.put("end", "5");

        String input = "HelloWorld";
        String expected = "HelloWorld"; // 根据实现，返回原始字符串
        String result = operation.execute(input, params);
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_OutOfBoundsIndices() throws Exception {
        Operation operation = new SubstringOperation();
        Map<String, String> params = new HashMap<>();
        params.put("start", "0");
        params.put("end", "20");

        String input = "HelloWorld";
        String expected = "HelloWorld"; // 根据实现，返回原始字符串
        String result = operation.execute(input, params);
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_MissingParams() {
        Operation operation = new SubstringOperation();
        Map<String, String> params = new HashMap<>();
        params.put("start", "0");
        // Missing "end"

        String input = "HelloWorld";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            operation.execute(input, params);
        });

        String expectedMessage = "Missing 'start' or 'end' parameter for SUBSTRING operation.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testExecute_InvalidNumberFormat() {
        Operation operation = new SubstringOperation();
        Map<String, String> params = new HashMap<>();
        params.put("start", "a");
        params.put("end", "5");

        String input = "HelloWorld";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            operation.execute(input, params);
        });

        String expectedMessage = "Invalid 'start' or 'end' parameter format for SUBSTRING operation.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
