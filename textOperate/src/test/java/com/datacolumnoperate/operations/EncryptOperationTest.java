package com.datacolumnoperate.operations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class EncryptOperationTest {

    @Test
    public void testExecute_AES_Success() throws Exception {
        Operation operation = new EncryptOperation();
        Map<String, String> params = new HashMap<>();
        params.put("method", "AES");
        params.put("key", "mySecretKey12345"); // 16字节密钥

        String input = "HelloWorld";
        String encrypted = operation.execute(input, params);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());
    }

    @Test
    public void testExecute_DES_Success() throws Exception {
        Operation operation = new EncryptOperation();
        Map<String, String> params = new HashMap<>();
        params.put("method", "DES");
        params.put("key", "myDESKey"); // 8字节密钥

        String input = "HelloWorld";
        String encrypted = operation.execute(input, params);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());
    }

    @Test
    public void testExecute_MD5_Success() throws Exception {
        Operation operation = new EncryptOperation();
        Map<String, String> params = new HashMap<>();
        params.put("method", "MD5");
        // MD5 不需要 key

        String input = "HelloWorld";
        String encrypted = operation.execute(input, params);
        assertNotNull(encrypted);
        assertEquals(32, encrypted.length()); // MD5哈希长度为32
    }

    @Test
    public void testExecute_UnsupportedMethod() {
        Operation operation = new EncryptOperation();
        Map<String, String> params = new HashMap<>();
        params.put("method", "UNSUPPORTED");
        params.put("key", "key");

        String input = "HelloWorld";
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            operation.execute(input, params);
        });

        String expectedMessage = "Unsupported encryption method: UNSUPPORTED";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testExecute_MissingMethod() {
        Operation operation = new EncryptOperation();
        Map<String, String> params = new HashMap<>();
        // Missing "method"
        params.put("key", "mySecretKey12345");

        String input = "HelloWorld";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            operation.execute(input, params);
        });

        String expectedMessage = "Encryption method not specified.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testExecute_MissingKeyForAES() {
        Operation operation = new EncryptOperation();
        Map<String, String> params = new HashMap<>();
        params.put("method", "AES");
        // Missing "key"

        String input = "HelloWorld";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            operation.execute(input, params);
        });

        String expectedMessage = "AES encryption requires a 16-byte key.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
