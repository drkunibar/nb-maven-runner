package io.github.netbeans.mvnrunner.model;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import lombok.SneakyThrows;

/**
 * Wraps {@code MavenProject}.
 */
public class MavenProjectWrapper extends Wrapper {

    private static final String MAVENPROJECT_PROP = "getMavenProject";

    public MavenProjectWrapper(NbMavenProjectWrapper nbMavenProjectWrapper) {
        super(getMavenProject(nbMavenProjectWrapper));
    }

    @SneakyThrows
    private static <T> T getMavenProject(NbMavenProjectWrapper nbMavenProjectWrapper) {
        Method method = MethodUtils.getAccessibleMethod(nbMavenProjectWrapper.getUnwrapClass(), MAVENPROJECT_PROP);
        return (T) method.invoke(nbMavenProjectWrapper.unwrap());
    }

    @SneakyThrows
    public Collection<ArtifactWrapper> getPluginArtifacts() {
        Collection<?> artifacts = invokeSimple("getPluginArtifacts");
        return artifacts.stream().map(ArtifactWrapper::new).collect(Collectors.toList());
    }

    @SneakyThrows
    public PluginWrapper getPlugin(String constructKey) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "getPlugin", String.class);
        return new PluginWrapper(method.invoke(unwrap(), constructKey));
    }

    @SneakyThrows
    public Properties getProperties() {
        return invokeSimple("getProperties");
    }

    public List<ArtifactRepositoryWrapper> getPluginArtifactRepositories() {
        List<?> repositories = invokeSimple("getPluginArtifactRepositories");
        return repositories.stream().map(ArtifactRepositoryWrapper::new).collect(Collectors.toList());
    }

    public String getArtifactId() {
        return invokeSimple("getArtifactId");
    }

    public String getId() {
        return invokeSimple("getId");
    }

    public File getBasedir() {
        return invokeSimple("getBasedir");
    }

    public List<ProfileWrapper> getActiveProfiles() {
        List<?> profileList = invokeSimple("getActiveProfiles");
        return profileList.stream().map(ProfileWrapper::new).collect(Collectors.toList());
    }

    public Map<String, List<String>> getInjectedProfileIds() {
        return invokeSimple("getInjectedProfileIds");
    }

    @SneakyThrows
    public Set<String> getLifecyclePhases() {
        Field field = FieldUtils.getField(getUnwrapClass(), "lifecyclePhases", true);
        return (Set<String>) field.get(unwrap());
    }
}
