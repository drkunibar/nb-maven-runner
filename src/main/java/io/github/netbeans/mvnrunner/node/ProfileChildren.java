package io.github.netbeans.mvnrunner.node;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectConfiguration;
import org.netbeans.spi.project.ProjectConfigurationProvider;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import io.github.netbeans.mvnrunner.model.MavenConfigurationWrapper;
import io.github.netbeans.mvnrunner.model.MavenProjectWrapper;
import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;
import io.github.netbeans.mvnrunner.node.ProfileChildren.ProfileDescription;

@Slf4j
public class ProfileChildren extends Children.Keys<ProfileDescription> {

    private final NbMavenProjectWrapper project;

    public ProfileChildren(NbMavenProjectWrapper project) {
        this.project = project;
        project.addPropertyChangeListener((evt) -> {
            addNotify();
        });
    }

    @Override
    protected Node[] createNodes(ProfileDescription key) {
        return new Node[] { new ProfileNode(key) };
    }

    @SneakyThrows
    @Override
    protected void addNotify() {
        super.addNotify();
        if (project.isUnloadable()) {
            return;
        }
        if (!project.isMavenProjectLoaded()) {
            return;
        }
        Project nbproject = project.getNbMavenProjectImpl();
        // Collect active profiles
        MavenProjectWrapper mavenProject = project.getMavenProject();
        Set<String> activeProfiles
                = mavenProject.getActiveProfiles().stream().map(p -> p.getId()).collect(Collectors.toSet());

        Set<ProfileDescription> result = new TreeSet<>();

        Collection<? extends ProjectConfigurationProvider> confProviders
                = nbproject.getLookup().lookupAll(ProjectConfigurationProvider.class);
        // Add Profiles from selected configuration
        confProviders.stream()
                .map(ProjectConfigurationProvider::getActiveConfiguration)
                .map(MavenConfigurationWrapper::getInstance)
                .map(MavenConfigurationWrapper::getActivatedProfiles)
                .flatMap(Collection<String>::stream)
                .forEach(activeProfiles::add);
        // Collect profiles from configurations
        Collection<String> profiles = confProviders.stream()
                .map(ProjectConfigurationProvider::getConfigurations)
                .flatMap(Collection<ProjectConfiguration>::stream)
                .map(MavenConfigurationWrapper::getInstance)
                .map(MavenConfigurationWrapper::getActivatedProfiles)
                .flatMap(List<String>::stream)
                .collect(Collectors.toCollection(TreeSet::new));
        // Collect external profiles - It seems to me that this does not work very well
        // We have to check why this does'nt return alle profiles
        java.util.Map<String, List<String>> injectedProfileIds = mavenProject.getInjectedProfileIds();
        for (Entry<String, List<String>> entry : injectedProfileIds.entrySet()) {
            log.debug("SOURCE: ", entry);
            for (String s : entry.getValue()) {
                log.debug("  VALUE: {}", s);
            }
        }
        // Collect internal profiles
        profiles.stream().forEach(p -> {
            ProfileDescription profileDescription = new ProfileDescription(p, "N/A", activeProfiles.contains(p));
            result.add(profileDescription);
        });
        setKeys(result);
    }

    @Value
    public static class ProfileDescription implements Comparable<ProfileDescription> {

        private String id;
        private String source;
        private boolean active;

        @Override
        public int compareTo(ProfileDescription o) {
            return Comparator.comparing((ProfileDescription p) -> p.getId()).compare(this, o);
        }
    }

}
