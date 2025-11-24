package com.jintakkim.postevaluator.test;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class TestProperties {
    private final Map<String, Object> properties;
    private static final TestProperties INSTANCE = readFromResources();

    private TestProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public static TestProperties getInstance() {
        return INSTANCE;
    }

    private static TestProperties readFromResources() {
        Map<String, Object> properties;
        Yaml yaml = new Yaml();
        try (InputStream is = TestProperties.class.getClassLoader().getResourceAsStream("test-properties.yml")) {
            if (is == null) {
                throw new IllegalStateException("Cannot find 'test-properties.yml' in classpath");
            }
            properties = yaml.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test-properties.yml", e);
        }
        return new TestProperties(properties);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String path, Class<T> type) {
        String[] keys = path.split("\\.");
        Object current = properties;

        for (String key : keys) {
            if (!(current instanceof Map)) {
                return null;
            }
            current = ((Map<String, Object>) current).get(key);
            if (current == null) {
                return null;
            }
        }
        if (type.isInstance(current)) {
            return type.cast(current);
        }
        return null;
    }

    public String getProperty(String path) {
        return getProperty(path, String.class);
    }
}
