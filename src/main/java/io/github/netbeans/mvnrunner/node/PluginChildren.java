package io.github.netbeans.mvnrunner.node;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

import io.github.netbeans.mvnrunner.model.ArtifactWrapper;
import io.github.netbeans.mvnrunner.model.MavenProjectWrapper;
import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

public class PluginChildren extends Children.Keys<String> {

    private final NbMavenProjectWrapper project;

    public PluginChildren(NbMavenProjectWrapper project) {
        this.project = project;
        project.addPropertyChangeListener((evt) -> {
            addNotify();
        });
    }

    @Override
    protected Node[] createNodes(String key) {
        MavenProjectWrapper mavenProject = project.getMavenProject();
        Optional<ArtifactWrapper> pluginRef = mavenProject.getPluginArtifacts()
                .stream()
                .filter(a -> Objects.equals(a.getGroupId() + ":" + a.getArtifactId(), key))
                .findAny();
        if (pluginRef.isPresent()) {
            ArtifactWrapper plugin = pluginRef.get();
            return new Node[] { new PluginNode(project, plugin) };
        } else {
            return new Node[0];
        }
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        MavenProjectWrapper mavenProject = project.getMavenProject();
        List<String> keys = mavenProject.getPluginArtifacts()
                .stream()
                .map(a -> a.getGroupId() + ":" + a.getArtifactId())
                .collect(Collectors.toList());
        setKeys(keys);
    }

}
