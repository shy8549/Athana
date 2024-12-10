package com.datacolumnoperate.utils;

import com.datacolumnoperate.common.OperationType;
import com.datacolumnoperate.operations.*;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Map;

/**
 * OperationFactory 负责根据 OperationType 获取对应的 Operation 实例。
 */
public class OperationFactory {
    private final Map<OperationType, Provider<Operation>> operationProviders;

    @Inject
    public OperationFactory(Map<OperationType, Provider<Operation>> operationProviders) {
        this.operationProviders = operationProviders;
    }

    public Operation getOperation(OperationType type) {
        if (type == null) {
            throw new IllegalArgumentException("OperationType cannot be null.");
        }

        Provider<Operation> provider = operationProviders.get(type);
        if (provider != null) {
            return provider.get();
        } else {
            throw new UnsupportedOperationException("Unsupported operation type: " + type);
        }
    }
}
