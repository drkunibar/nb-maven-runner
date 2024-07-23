package io.github.netbeans.mvnrunner.node;

import java.util.Set;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

import lombok.SneakyThrows;

import io.github.netbeans.mvnrunner.model.ArtifactWrapper;
import io.github.netbeans.mvnrunner.model.Mojo;
import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;
import io.github.netbeans.mvnrunner.util.MavenProjectUtils;

public class GoalChildren extends Children.Keys<Mojo> {

    private final NbMavenProjectWrapper project;
    private final ArtifactWrapper artifact;

    public GoalChildren(NbMavenProjectWrapper project, ArtifactWrapper artifact) {
        this.project = project;
        this.artifact = artifact;
        project.addPropertyChangeListener((evt) -> {
            addNotify();
        });
    }

    @Override
    protected Node[] createNodes(Mojo key) {
        return new Node[] { new GoalNode(project, key) };
    }

    @SneakyThrows
    @Override
    protected void addNotify() {
        super.addNotify();
        Set<Mojo> goals = MavenProjectUtils.getGoals(artifact, project.getMavenProject());
        setKeys(goals);
    }

}
