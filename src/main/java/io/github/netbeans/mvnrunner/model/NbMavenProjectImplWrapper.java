package io.github.netbeans.mvnrunner.model;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.netbeans.api.project.Project;
import org.openide.util.NbBundle;

import lombok.SneakyThrows;

/**
 * Wraps a {@code NbMavenProjectImpl}, as it is not visible for us.
 */
@NbBundle.Messages({ "# {0} - project", "EX_noNbProjectImpl=Project is not a NbMavenProjectImpl: {0}" })
public class NbMavenProjectImplWrapper {

    private static final String NBMAVENPROJECTIMPL_CLASSNAME = "org.netbeans.modules.maven.NbMavenProjectImpl";
    private static final String ADDPROPERTYCHANGELISTENER = "addPropertyChangeListener";
    private final Project nbMavenProjectImpl;

    private NbMavenProjectImplWrapper(@Nonnull Project nbMavenProjectImpl) {
        this.nbMavenProjectImpl = nbMavenProjectImpl;
    }

    /**
     * Returns the {@code NbMavenProjectImpl} from the given project.
     * 
     * @return The {@code NbMavenProjectImp} from the given project.
     * @see #getInstance(org.netbeans.api.project.Project)
     */
    public <T extends Project> T unwrap() {
        return (T) nbMavenProjectImpl;
    }

    public <T extends Project> Class<T> getUnwrapClass() {
        return (Class<T>) unwrap().getClass();
    }

    /**
     * Create a {@link NbMavenProjectImplWrapper}.
     * 
     * @param project The {@code NbMavenProjectImpl} or an {@link Project} wich
     *     {@link Project#getLookup() Lookup} returns a {@code NbMavenProjectImpl}.
     * @return A new createt {@code NbMavenProjectImpl}.
     * @throws IllegalArgumentException Will be thrown, if the given {@code project}
     *     is not a {@code NbMavenProjectImpl}
     */
    public static NbMavenProjectImplWrapper getInstance(@Nonnull Project project) throws IllegalArgumentException {
        return Optional.ofNullable(project)
                .map(p -> new NbMavenProjectImplWrapper(project))
                .orElseThrow(() -> new IllegalArgumentException(Bundle.EX_noNbProjectImpl(project)));
    }

    public static boolean isNbMavenProjectImpl(@Nonnull Project project) {
        return getNbMavenProjectImpl(project) != null;
    }

    @Nullable
    private static Project getNbMavenProjectImpl(@Nonnull Project project) {
        String clsName = ClassUtils.getName(project.getClass());
        if (Objects.equals(clsName, NBMAVENPROJECTIMPL_CLASSNAME)) {
            return project;
        }
        project = project.getLookup().lookup(Project.class);
        if (Objects.equals(clsName, NBMAVENPROJECTIMPL_CLASSNAME)) {
            return project;
        }
        return null;
    }

    /**
     * Returns the {@code NbMavenProject} from the given {@code NbMavenProjectImpl}.
     * 
     * @return The {@code NbMavenProject} from the given project.
     * @see #getInstance(org.netbeans.api.project.Project)
     */
    @SneakyThrows
    public <T> T getNbMavenProject() {
        return getNbMavenProjectWrapper().unwrap();
    }

    public NbMavenProjectWrapper getNbMavenProjectWrapper() {
        return new NbMavenProjectWrapper(this);
    }

    @SneakyThrows
    public void addPropertyChangeListener(@Nonnull PropertyChangeListener listener) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), ADDPROPERTYCHANGELISTENER, Project.class,
                PropertyChangeListener.class);
        method.invoke(null, listener);
    }

}
