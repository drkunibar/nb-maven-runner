package io.github.netbeans.mvnrunner.node;

import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.Lookups;

import io.github.netbeans.mvnrunner.model.MavenProjectWrapper;
import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

public class ProjectNode extends AbstractNode {

    private static final @StaticResource String MAVEN_PROJECT_ICON
            = "io/github/netbeans/mvnrunner/resources/Maven2Icon.gif"; // NOI18N

    private final NbMavenProjectWrapper project;

    public ProjectNode(NbMavenProjectWrapper project) {
        super(new ProjectChildren(project), Lookups.singleton(project));
        this.project = project;
        MavenProjectWrapper mavenProject = project.getMavenProject();
        setName(mavenProject.getArtifactId());
        setDisplayName(mavenProject.getArtifactId());
        setIconBaseWithExtension(MAVEN_PROJECT_ICON);
        setShortDescription(mavenProject.getId() + " - " + mavenProject.getBasedir());
        project.addPropertyChangeListener((evt) -> {
            setDisplayName(mavenProject.getArtifactId());
            setShortDescription(mavenProject.getId() + " - " + mavenProject.getBasedir());
        });
    }

    public Project getProject() {
        return project.getNbMavenProjectImpl();
    }
}
