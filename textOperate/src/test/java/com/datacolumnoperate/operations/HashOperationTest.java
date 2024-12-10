package com.datacolumnoperate.operations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class HashOperationTest {

    @Test
    public void testExecute_SHA256_Success() throws Exception {
        Operation operation = new HashOperation();
        Map<String, String> params = new HashMap<>();
        params.put("algorithm", "SHA-256");
        params.put("key", "optionalKey"); // 可选

        String input = "HelloWorld";
        String hash = operation.execute(input, params);
        assertNotNull(hash);
        assertEquals(64, hash.length()); // SHA-256哈希长度为64
    }

    @Test
    public void testExecute_MD5_Success() throws Exception {
        Operation operation = new HashOperation();
        Map<String, String> params = new HashMap<>();
        params.put("algorithm", "MD5");
        // No key

        String input = "HelloWorld";
        String hash = operation.execute(input, params);
        assertNotNull(hash);
        assertEquals(32, hash.length()); // MD5哈希长度为32
    }

    @Test
    public void testExecute_UnsupportedAlgorithm() {
        Operation operation = new HashOperation();
        Map<String, String> params = new HashMap<>();
        params.put("algorithm", "UNSUPPORTED");
        params.put("key", "key");

        String input = "HelloWorld";
        Exception exception = assertThrows(RuntimeException.class, () -> {
            operation.execute(input, params);
        });

        String expectedMessage = "HASH operation failed.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testExecute_MissingAlgorithm() {
        Operation operation = new HashOperation();
        Map<String, String> params = new HashMap<>();
        // Missing "algorithm"
        params.put("key", "key");

        String input = "HelloWorld";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            operation.execute(input, params);
        });

        String expectedMessage = "Hash algorithm not specified.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

