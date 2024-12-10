package com.datacolumnoperate.operations;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegexReplaceOperationTest {

    @Test
    public void testExecute_Success() throws Exception {
        Operation operation = new RegexReplaceOperation();
        Map<String, String> params = new HashMap<>();
        params.put("regex", "\\d{3}-\\d{4}");
        params.put("replacement", "****-****");

        String input = "Contact: 123-4567";
        String expected = "Contact: ****-****";
        String result = operation.execute(input, params);
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_NoMatch() throws Exception {
        Operation operation = new RegexReplaceOperation();
        Map<String, String> params = new HashMap<>();
        params.put("regex", "\\d{3}-\\d{4}");
        params.put("replacement", "****-****");

        String input = "Contact: 1234567";
        String expected = "Contact: 1234567";
        String result = operation.execute(input, params);
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_MissingParams() {
        Operation operation = new RegexReplaceOperation();
        Map<String, String> params = new HashMap<>();
        params.put("regex", "\\d{3}-\\d{4}");
        // Missing "replacement"

        String input = "Contact: 123-4567";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            operation.execute(input, params);
        });

        String expectedMessage = "Missing 'regex' or 'replacement' parameter for REGEX_REPLACE operation.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

