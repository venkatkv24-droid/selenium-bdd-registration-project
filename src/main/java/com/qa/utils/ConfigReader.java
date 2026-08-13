package com.qa.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads key/value pairs from src/test/resources/config.properties.
 * Kept as a simple singleton so every step definition / page reads the same values.
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
                properties.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load config.properties from " + CONFIG_PATH, e);
            }
        }
    }

    public static String get(String key) {
        loadProperties();
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value;
    }

    public static String getBaseUrl() {
        return get("baseUrl");
    }

    public static String getExcelPath() {
        return get("excelPath");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(get("implicitWait"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(get("explicitWait"));
    }
}
