package io.github.netbeans.mvnrunner.model;

import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.MethodUtils;

import lombok.SneakyThrows;

import io.github.netbeans.mvnrunner.util.ModuleUtils;

public class EmbedderFactoryWrapper {

    private static final Class<?> embedderFactoryClass = ModuleUtils.loadClassFromModule(
            "org.netbeans.modules.maven.embedder", "org.netbeans.modules.maven.embedder.EmbedderFactory");

    @SneakyThrows
    public static MavenEmbedderWrapper getOnlineEmbedder() {
        Method method = MethodUtils.getAccessibleMethod(embedderFactoryClass, "getOnlineEmbedder");
        Object mavenEmbedder = method.invoke(null);
        return new MavenEmbedderWrapper(mavenEmbedder);
    }
}
