package io.github.netbeans.mvnrunner.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.MethodUtils;

import lombok.SneakyThrows;

import io.github.netbeans.mvnrunner.util.ModuleUtils;

public class NetbeansActionMappingWrapper {

    private final Object netbeansActionMapping;

    @SneakyThrows
    public NetbeansActionMappingWrapper() {
        Class<?> cls = ModuleUtils.loadClassFromModule("org.netbeans.modules.maven",
                "org.netbeans.modules.maven.execute.model.NetbeansActionMapping");
        Constructor<?> constructor = cls.getConstructor();
        netbeansActionMapping = constructor.newInstance();
    }

    public NetbeansActionMappingWrapper(Object netbeansActionMapping) {
        this.netbeansActionMapping = netbeansActionMapping;
    }

    public <T> T unwrap() {
        return (T) netbeansActionMapping;
    }

    public <T> Class<T> getUnwrapClass() {
        return (Class<T>) netbeansActionMapping.getClass();
    }

    public static NetbeansActionMappingWrapper wrap(Object netbeansActionMapping) {
        return new NetbeansActionMappingWrapper(netbeansActionMapping);
    }

    @SneakyThrows
    public String getDisplayName() {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "getDisplayName");
        return (String) method.invoke(unwrap());
    }

    @SneakyThrows
    public String getActionName() {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "getActionName");
        return (String) method.invoke(unwrap());
    }

    @SneakyThrows
    public List<String> getGoals() {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "getGoals");
        return (List<String>) method.invoke(unwrap());
    }

    @SneakyThrows
    public Map<String, String> getProperties() {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "getProperties");
        return (Map<String, String>) method.invoke(unwrap());
    }

    @SneakyThrows
    public void addProperty(String key, String value) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "addProperty", String.class, String.class);
        method.invoke(unwrap(), key, value);
    }

    @SneakyThrows
    public void setGoals(List<String> goals) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "setGoals", List.class);
        method.invoke(unwrap(), goals);
    }

    @SneakyThrows
    public void setProperties(Map<String, String> properties) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "setProperties", Map.class);
        method.invoke(unwrap(), properties);
    }
}
