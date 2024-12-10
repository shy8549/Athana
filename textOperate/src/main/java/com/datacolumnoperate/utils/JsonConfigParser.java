package com.datacolumnoperate.utils;

import com.datacolumnoperate.common.Config;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * JsonConfigParser 负责解析 JSON 配置文件。
 */
public class JsonConfigParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Config parse(String configPath) throws IOException {
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            throw new IOException("Config file does not exist at path: " + configPath);
        }
        try {
            return mapper.readValue(configFile, Config.class);
        } catch (IOException e) {
            throw new IOException("Failed to parse config file: " + configPath, e);
        }
    }
}
