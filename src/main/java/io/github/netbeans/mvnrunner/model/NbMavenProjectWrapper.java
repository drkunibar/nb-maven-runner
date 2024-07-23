package io.github.netbeans.mvnrunner.model;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.netbeans.api.project.Project;

import lombok.SneakyThrows;

/**
 * Wraps a {@code NbMavenProject}, as it is not visible for us.
 */
public class NbMavenProjectWrapper extends Wrapper {

    public static final String PROP_PROJECT = "MavenProject"; // NOI18N
    public static final String PROP_RESOURCE = "RESOURCES"; // NOI18N
    private static final String PROJECT_FIELD = "project";
    private static final String WATCHER_FIELD = "watcher"; // NOI18N
    private static final String ADDPROPERTYCHANGELISTENER = "addPropertyChangeListener"; // NOI18N

    public NbMavenProjectWrapper(NbMavenProjectImplWrapper nbMavenProjectImpl) {
        super(getNbMavenProject(nbMavenProjectImpl));
    }

    @SneakyThrows
    private static <T> T getNbMavenProject(NbMavenProjectImplWrapper nbMavenProjectImpl) {
        Field field = getField(nbMavenProjectImpl.getUnwrapClass(), WATCHER_FIELD);
        return (T) field.get(nbMavenProjectImpl.unwrap());
    }

    @SneakyThrows
    public <T extends Project> T getNbMavenProjectImpl() {
        Field field = getField(getUnwrapClass(), PROJECT_FIELD);
        return (T) field.get(unwrap());
    }

    @Nonnull
    public MavenProjectWrapper getMavenProject() {
        return new MavenProjectWrapper(this);
    }

    @SneakyThrows
    public void addPropertyChangeListener(@Nonnull PropertyChangeListener listener) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), ADDPROPERTYCHANGELISTENER,
                PropertyChangeListener.class);
        method.invoke(unwrap(), listener);
    }

    @SneakyThrows
    public boolean isUnloadable() {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "isUnloadable");
        return (boolean) method.invoke(unwrap());
    }

    @SneakyThrows
    public boolean isMavenProjectLoaded() {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "isMavenProjectLoaded");
        return (boolean) method.invoke(unwrap());
    }

}
