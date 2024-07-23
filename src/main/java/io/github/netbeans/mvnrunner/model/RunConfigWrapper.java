package io.github.netbeans.mvnrunner.model;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.netbeans.api.annotations.common.NonNull;

import lombok.SneakyThrows;

public class RunConfigWrapper {

    private final Object runConfig;

    RunConfigWrapper(Object runConfig) {
        this.runConfig = runConfig;
    }

    public <T> T unwrap() {
        return (T) runConfig;
    }

    public <T> Class<T> getUnwrapClass() {
        return (Class<T>) unwrap().getClass();
    }

    @SneakyThrows
    public void addProperties(@NonNull Map<String, String> properties) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "addProperties", Map.class);
        method.invoke(unwrap(), properties);
    }

    @SneakyThrows
    public void setActivatedProfiles(List<String> profiles) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "setActivatedProfiles", List.class);
        method.invoke(unwrap(), profiles);
    }
}
