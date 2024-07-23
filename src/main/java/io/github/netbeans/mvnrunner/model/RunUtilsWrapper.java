package io.github.netbeans.mvnrunner.model;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.api.project.Project;
import org.openide.execution.ExecutorTask;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import io.github.netbeans.mvnrunner.util.ModuleUtils;

@UtilityClass
public class RunUtilsWrapper {

    private static final Class<?> runUtilClass = ModuleUtils.loadClassFromModule("org.netbeans.modules.maven",
            "org.netbeans.modules.maven.api.execute.RunUtils");

    @SneakyThrows
    public static RunConfigWrapper createRunConfig(File execDir, Project prj, String displayName, List<String> goals) {
        Method method = MethodUtils.getAccessibleMethod(runUtilClass, "createRunConfig", File.class, Project.class,
                String.class, List.class);
        return new RunConfigWrapper(method.invoke(null, execDir, prj, displayName, goals));
    }

    @SneakyThrows
    public static @CheckForNull ExecutorTask run(RunConfigWrapper config) {
        ClassLoader classLoader = config.getUnwrapClass().getClassLoader();
        Class<?> runClass = Class.forName("org.netbeans.modules.maven.api.execute.RunUtils", true, classLoader);
        Class<?> runConfigClass = classLoader.loadClass("org.netbeans.modules.maven.api.execute.RunConfig");
        Method method = MethodUtils.getAccessibleMethod(runClass, "run", runConfigClass);
        return (ExecutorTask) method.invoke(null, method.getParameterTypes()[0].cast(config.unwrap()));
    }
}
