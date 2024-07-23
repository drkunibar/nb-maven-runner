package io.github.netbeans.mvnrunner.model;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.swing.Action;
import javax.swing.Icon;

import org.apache.commons.lang3.ClassUtils;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

import lombok.Delegate;
import lombok.SneakyThrows;

public class ProjectActionWrapper implements Action {

    @Delegate
    private final Action projectAction;

    private ProjectActionWrapper(@Nonnull Action projectAction) {
        this.projectAction = projectAction;
    }

    public <T> T unwrap() {
        return (T) projectAction;
    }

    public Action getAction() {
        return projectAction;
    }

    @SneakyThrows
    public static ProjectActionWrapper getInstance(String command, String namePattern, String popupPattern, Icon icon,
            Lookup lookup) {
        Collection<? extends Action> allActions = Lookups.forPath("Actions").lookupAll(Action.class);
        Optional<? extends Action> anyProjectAction = allActions.stream()
                .filter(a -> Objects.equals(ClassUtils.getName(a),
                        "org.netbeans.modules.project.ui.actions.ProjectAction"))
                .findAny();
        if (anyProjectAction.isEmpty()) {
            throw new IllegalStateException("No ProjectAction found");
        }
        Action projectAction = anyProjectAction.get();
        Class<?> cls = projectAction.getClass();
        ClassLoader classLoader = cls.getClassLoader();
        Class<?> actionClass = classLoader.loadClass("org.netbeans.modules.project.ui.actions.ProjectAction");
        Constructor<?> constructor
                = actionClass.getConstructor(String.class, String.class, String.class, Icon.class, Lookup.class);
        Action result = (Action) constructor.newInstance(command, namePattern, popupPattern, icon, lookup);
        return new ProjectActionWrapper(result);
    }
}
