package com.datacolumnoperate.utils;

import com.datacolumnoperate.common.OperationType;
import com.datacolumnoperate.operations.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.datacolumnoperate.config.OperationModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OperationFactoryTest {

    private static OperationFactory operationFactory;

    @BeforeAll
    public static void setup() {
        Injector injector = Guice.createInjector(new OperationModule());
        operationFactory = injector.getInstance(OperationFactory.class);
    }

    @Test
    public void testGetOperation_ENCRYPT() {
        Operation operation = operationFactory.getOperation(OperationType.ENCRYPT);
        assertNotNull(operation);
        assertTrue(operation instanceof EncryptOperation);
    }

    @Test
    public void testGetOperation_HASH() {
        Operation operation = operationFactory.getOperation(OperationType.HASH);
        assertNotNull(operation);
        assertTrue(operation instanceof HashOperation);
    }

    @Test
    public void testGetOperation_REGEX_REPLACE() {
        Operation operation = operationFactory.getOperation(OperationType.REGEX_REPLACE);
        assertNotNull(operation);
        assertTrue(operation instanceof RegexReplaceOperation);
    }

    @Test
    public void testGetOperation_SUBSTRING() {
        Operation operation = operationFactory.getOperation(OperationType.SUBSTRING);
        assertNotNull(operation);
        assertTrue(operation instanceof SubstringOperation);
    }

    @Test
    public void testGetOperation_TIME_CONVERT() {
        Operation operation = operationFactory.getOperation(OperationType.TIME_CONVERT);
        assertNotNull(operation);
        assertTrue(operation instanceof TimeConvertOperation);
    }

    @Test
    public void testGetOperation_NullType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            operationFactory.getOperation(null);
        });

        String expectedMessage = "OperationType cannot be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetOperation_UnsupportedType() {
        // 使用新增的 UNSUPPORTED 枚举值
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            operationFactory.getOperation(OperationType.UNSUPPORTED);
        });

        String expectedMessage = "Unsupported operation type: UNSUPPORTED";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
