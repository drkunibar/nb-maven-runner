package io.github.netbeans.mvnrunner.util;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import lombok.Delegate;

public class PropertiesWrapper {

    @Delegate
    private final Properties properties;

    public PropertiesWrapper(@Nonnull Properties properties) {
        this.properties = properties;
    }

    @Nonnull
    public Properties unwrap() {
        return properties;
    }

    public void putAll(@Nonnull Properties props) {
        properties.putAll(props);
    }

    @Nonnull
    public Set<String> getKeys() {
        return properties.keySet().stream().map(k -> k.toString()).collect(Collectors.toSet());
    }

    @Nonnull
    public Optional<String> getString(@Nonnull String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

    @Nonnull
    public String getString(@Nonnull String key, String defaultValue) {
        return getString(key).orElse(defaultValue);
    }

    @Nonnull
    public Optional<Boolean> getBoolean(@Nonnull String key) {
        Object value = properties.get(key);
        if (value instanceof String valueStr) {
            return Optional.of(Boolean.valueOf(valueStr));
        } else {
            return Optional.ofNullable((Boolean) value);
        }
    }

    public boolean getBoolean(@Nonnull String key, boolean defaultValue) {
        return getBoolean(key).orElse(defaultValue);
    }

    public void put(String key, String value) {
        properties.put(key, value);
    }

    public void put(String key, boolean value) {
        properties.put(key, value);
    }

    public void remove(String key) {
        properties.remove(key);
    }
}
