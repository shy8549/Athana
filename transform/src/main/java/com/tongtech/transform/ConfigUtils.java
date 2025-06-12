package com.tongtech.transform;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ConfigUtils {
    private static final Properties props = new Properties();
    static {
        try (InputStream in = Files.newInputStream(Paths.get("config.properties"))) {
            props.load(in);
        } catch (Exception e) {
            System.err.println("[ConfigUtils] 加载配置文件失败: " + e.getMessage());
            System.exit(1);
        }
    }
    public static String get(String key) {
        return props.getProperty(key);
    }
    public static boolean getBoolean(String key, boolean defaultValue) {
        String v = props.getProperty(key);
        if (v == null) return defaultValue;
        return v.equalsIgnoreCase("true");
    }
    public static int getInt(String key, int defaultValue) {
        String v = props.getProperty(key);
        if (v == null) return defaultValue;
        try { return Integer.parseInt(v); } catch(Exception e) { return defaultValue; }
    }
    public static long getLong(String key, long defaultValue) {
        String v = props.getProperty(key);
        if (v == null) return defaultValue;
        try { return Long.parseLong(v); } catch(Exception e) { return defaultValue; }
    }
}



