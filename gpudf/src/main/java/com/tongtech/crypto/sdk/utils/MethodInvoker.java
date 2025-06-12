package com.tongtech.crypto.sdk.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodInvoker {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java -cp /path/to/java/gpudf.jar  com.tongtech.crypto.sdk.utils.MethodInvoker  <class> <method> <param1> [param2] ...");
            return;
        }

        String className = args[0];
        String methodName = args[1];

        try {
            Class<?> clazz = Class.forName(className);
            Object instance = null;

            // 所有参数原始值都是 String
            String[] rawArgs = new String[args.length - 2];
            System.arraycopy(args, 2, rawArgs, 0, args.length - 2);

            Method matchedMethod = null;
            Object[] convertedArgs = null;

            // 尝试找到匹配方法并转换参数
            for (Method method : clazz.getMethods()) {
                if (!method.getName().equals(methodName)) continue;
                if (method.getParameterCount() != rawArgs.length) continue;

                Class<?>[] paramTypes = method.getParameterTypes();
                Object[] tempArgs = new Object[rawArgs.length];
                boolean match = true;

                for (int i = 0; i < rawArgs.length; i++) {
                    Object converted = convertType(rawArgs[i], paramTypes[i]);
                    if (converted == null && paramTypes[i].isPrimitive()) {
                        match = false;
                        break;
                    }
                    tempArgs[i] = converted;
                }

                if (match) {
                    matchedMethod = method;
                    convertedArgs = tempArgs;
                    break;
                }
            }

            if (matchedMethod == null) {
                throw new NoSuchMethodException("No suitable method found with name '" + methodName + "' and convertible parameters.");
            }

            if (!Modifier.isStatic(matchedMethod.getModifiers())) {
                instance = clazz.getDeclaredConstructor().newInstance();
            }

            Object result = matchedMethod.invoke(instance, convertedArgs);
            System.out.println(result != null ? result.toString() : "null");

        } catch (Exception e) {
            System.err.println("Error invoking method: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }
    }

    private static Object convertType(String value, Class<?> targetType) {
        try {
            if (targetType == String.class) return value;
            if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(value);
            if (targetType == long.class || targetType == Long.class) return Long.parseLong(value);
            if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(value);
            if (targetType == double.class || targetType == Double.class) return Double.parseDouble(value);
            if (targetType == float.class || targetType == Float.class) return Float.parseFloat(value);
            if (targetType == Object.class) return value;  // 默认转成字符串
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
