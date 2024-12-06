package com.datacolumnoperate.utils;

import com.datacolumnoperate.common.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonConfigParser {
    public static Config parseConfig(String jsonFilePath) throws IOException {
        byte[] jsonData = Files.readAllBytes(Paths.get(jsonFilePath));
        ObjectMapper objectMapper = new ObjectMapper();
        // 允许忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(jsonData, Config.class);
    }
}

