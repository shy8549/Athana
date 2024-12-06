package com.datacolumnoperate.utils;

import com.datacolumnoperate.common.OperationType;
import com.datacolumnoperate.utils.operations.EncryptOperation;
import com.datacolumnoperate.utils.operations.HashOperation;
import com.datacolumnoperate.utils.operations.Operation;
import com.datacolumnoperate.utils.operations.SubstringOperation;

public class OperationFactory {
    public static Operation getOperation(OperationType type) {
        switch (type) {
            case ENCRYPT:
                return new EncryptOperation();
            case SUBSTRING:
                return new SubstringOperation();
            case HASH:
                return new HashOperation();
            // 可以根据需要添加更多操作
            default:
                throw new UnsupportedOperationException("Unsupported operation type: " + type);
        }
    }
}

