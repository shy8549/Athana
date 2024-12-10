package com.datacolumnoperate.config;

import com.datacolumnoperate.common.OperationType;
import com.datacolumnoperate.operations.*;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * OperationModule 是一个 Guice Module，用于将 OperationType 枚举值绑定到对应的 Operation 实现类。
 *
 * 在项目中，需要根据配置中指定的操作类型（OperationType）获取相应的 Operation 实例。
 * 使用 Guice 的 MapBinder，可以将枚举值（OperationType）与相应的操作类（Operation 的实现类）建立映射关系。
 *
 * 当程序运行时，OperationFactory 会从 Guice 的 Injector 中通过依赖注入获取映射关系，
 * 从而在不修改核心逻辑的情况下，轻松添加、删除或更改操作实现。
 *
 * 例如：
 * - OperationType.ENCRYPT 绑定到 EncryptOperation.class
 * - OperationType.HASH 绑定到 HashOperation.class
 * - ... 以此类推
 *
 * 若将来需要新增一种操作类型（例如 COMPRESS），只需在此模块中添加相应的 binding，而无需修改 OperationFactory 的代码。
 */
public class OperationModule extends AbstractModule {
    @Override
    protected void configure() {
        // 使用 MapBinder 将 OperationType 枚举值与 Operation 实现类进行映射绑定。
        // MapBinder 的键为 OperationType，值为 Operation（接口类型）。
        MapBinder<OperationType, Operation> mapBinder = MapBinder.newMapBinder(binder(), OperationType.class, Operation.class);

        // 将 ENCRYPT 操作类型绑定到 EncryptOperation 实现类。
        // 当请求 ENCRYPT 类型的 Operation 时，会返回 EncryptOperation 实例。
        mapBinder.addBinding(OperationType.ENCRYPT).to(EncryptOperation.class);

        // 将 HASH 操作类型绑定到 HashOperation 实现类。
        // 当请求 HASH 类型的 Operation 时，会返回 HashOperation 实例。
        mapBinder.addBinding(OperationType.HASH).to(HashOperation.class);

        // 将 REGEX_REPLACE 操作类型绑定到 RegexReplaceOperation 实现类。
        // 当请求 REGEX_REPLACE 类型的 Operation 时，会返回 RegexReplaceOperation 实例。
        mapBinder.addBinding(OperationType.REGEX_REPLACE).to(RegexReplaceOperation.class);

        // 将 SUBSTRING 操作类型绑定到 SubstringOperation 实现类。
        // 当请求 SUBSTRING 类型的 Operation 时，会返回 SubstringOperation 实例。
        mapBinder.addBinding(OperationType.SUBSTRING).to(SubstringOperation.class);

        // 将 TIME_CONVERT 操作类型绑定到 TimeConvertOperation 实现类。
        // 当请求 TIME_CONVERT 类型的 Operation 时，会返回 TimeConvertOperation 实例。
        mapBinder.addBinding(OperationType.TIME_CONVERT).to(TimeConvertOperation.class);

        // 如有需要，可以在此处继续添加新的操作类型绑定。
        // 例如：mapBinder.addBinding(OperationType.NEW_TYPE).to(NewOperation.class);
    }
}



