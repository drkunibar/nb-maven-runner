package io.github.netbeans.mvnrunner.model;

import java.io.Reader;
import java.lang.reflect.Method;

import lombok.SneakyThrows;

import io.github.netbeans.mvnrunner.util.ModuleUtils;

public class PluginDescriptorBuilderWrapper extends Wrapper {

    private static final Class<?> builderClass = ModuleUtils.loadClassFromModule("org.netbeans.modules.maven.embedder",
            "org.apache.maven.plugin.descriptor.PluginDescriptorBuilder");

    public PluginDescriptorBuilderWrapper() {
        super(createInstance());
    }

    public PluginDescriptorBuilderWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    @SneakyThrows
    private static Object createInstance() {
        return builderClass.getConstructor().newInstance();
    }

    @SneakyThrows
    public PluginDescriptorWrapper build(Reader reader) {
        Method method = builderClass.getDeclaredMethod("build", Reader.class);
        return new PluginDescriptorWrapper(method.invoke(unwrap(), reader));
    }
}
