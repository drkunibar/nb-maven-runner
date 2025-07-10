package io.github.netbeans.mvnrunner.node;

import java.awt.Image;
import javax.annotation.Nonnull;

import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.openide.nodes.AbstractNode;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

import io.github.netbeans.mvnrunner.model.MavenProjectWrapper;
import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

public class ProjectNode extends AbstractNode {

    private static final @StaticResource String MAVEN_PROJECT_ICON
            = "io/github/netbeans/mvnrunner/resources/Maven2Icon.gif"; // NOI18N

    private final NbMavenProjectWrapper project;
    private final ProjectInformation projectInformation;

    public ProjectNode(@Nonnull NbMavenProjectWrapper project) {
        super(new ProjectChildren(project), Lookups.singleton(project));
        this.project = project;
        MavenProjectWrapper mavenProject = project.getMavenProject();
        setName(mavenProject.getArtifactId());
        setDisplayName(mavenProject.getArtifactId());
        setIconBaseWithExtension(MAVEN_PROJECT_ICON);
        setShortDescription(mavenProject.getId() + " - " + mavenProject.getBasedir());
        projectInformation = ProjectUtils.getInformation(project.getNbMavenProjectImpl());
        project.addPropertyChangeListener((evt) -> {
            setDisplayName(mavenProject.getArtifactId());
            setShortDescription(mavenProject.getId() + " - " + mavenProject.getBasedir());
        });
    }

    public Project getProject() {
        return project.getNbMavenProjectImpl();
    }

    @Override
    public Image getIcon(int type) {
        if (projectInformation == null) {
            return super.getIcon(type);
        } else {
            return ImageUtilities.icon2Image(projectInformation.getIcon());
        }
    }
}
