package io.github.netbeans.mvnrunner.model;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.MethodUtils;

import lombok.SneakyThrows;

import io.github.netbeans.mvnrunner.util.ModuleUtils;

public class PluginWrapper extends Wrapper {

    private static final Class<?> pluginClass
            = ModuleUtils.loadClassFromModule("org.netbeans.modules.maven", "org.apache.maven.model.Plugin");

    PluginWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    @SneakyThrows
    public static String constructKey(String groupId, String artifactId) {
        Method method = MethodUtils.getAccessibleMethod(pluginClass, "constructKey", String.class, String.class);
        return (String) method.invoke(null, groupId, artifactId);
    }

    public List<PluginExecutionWrapper> getExecutions() {
        List executions = invokeSimple("getExecutions");
        return (List<PluginExecutionWrapper>) executions.stream()
                .map(PluginExecutionWrapper::new)
                .collect(Collectors.toList());
    }

    public Xpp3DomWrapper getConfiguration() {
        return new Xpp3DomWrapper(invokeSimple("getConfiguration"));
    }
}
