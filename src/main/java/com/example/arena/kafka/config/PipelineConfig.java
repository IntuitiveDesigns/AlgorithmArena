package com.example.arena.kafka.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Enterprise Config Loader.
 * Reads from 'pipeline.properties' on the classpath.
 * Fails fast if required properties are missing.
 */
public class PipelineConfig {

    private static final PipelineConfig INSTANCE = new PipelineConfig();
    private final Properties props = new Properties();

    private PipelineConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("pipeline.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find pipeline.properties");
                return;
            }
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static PipelineConfig get() {
        return INSTANCE;
    }

    // --- GENERIC GETTERS ---

    public String getProperty(String key) {
        String val = props.getProperty(key);
        if (val == null) throw new RuntimeException("Missing Configuration: " + key);
        return val;
    }

    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public int getInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}