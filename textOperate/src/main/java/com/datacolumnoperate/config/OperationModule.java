package com.datacolumnoperate.config;

import com.datacolumnoperate.common.OperationType;
import com.datacolumnoperate.operations.*;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Guice Module，用于绑定操作类型和操作实现类。
 */
public class OperationModule extends AbstractModule {
    @Override
    protected void configure() {
        MapBinder<OperationType, Operation> mapBinder = MapBinder.newMapBinder(binder(), OperationType.class, Operation.class);
        mapBinder.addBinding(OperationType.ENCRYPT).to(EncryptOperation.class);
        mapBinder.addBinding(OperationType.HASH).to(HashOperation.class);
        mapBinder.addBinding(OperationType.REGEX_REPLACE).to(RegexReplaceOperation.class);
        mapBinder.addBinding(OperationType.SUBSTRING).to(SubstringOperation.class);
        mapBinder.addBinding(OperationType.TIME_CONVERT).to(TimeConvertOperation.class);
    }
}


