package io.github.netbeans.mvnrunner.node;

import java.util.Collection;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import io.github.netbeans.mvnrunner.model.MavenProjectWrapper;
import io.github.netbeans.mvnrunner.model.Mojo.Phase;
import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;
import io.github.netbeans.mvnrunner.util.MavenProjectUtils;

@Slf4j
public class LifecycleChildren extends Children.Keys<Phase> {

    private final NbMavenProjectWrapper project;

    public LifecycleChildren(NbMavenProjectWrapper project) {
        this.project = project;
        project.addPropertyChangeListener((evt) -> {
            addNotify();
        });
    }

    @Override
    protected Node[] createNodes(Phase key) {
        return new Node[] { new LifecycleNode(project.getNbMavenProjectImpl(), key) };
    }

    @SneakyThrows
    @Override
    protected void addNotify() {
        super.addNotify();
        MavenProjectWrapper mavenProject = project.getMavenProject();
        Collection<Phase> phases = MavenProjectUtils.getLifecycle(mavenProject);
        setKeys(phases);
    }

}
