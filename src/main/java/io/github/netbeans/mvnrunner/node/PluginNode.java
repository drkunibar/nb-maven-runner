package io.github.netbeans.mvnrunner.node;

import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;

import io.github.netbeans.mvnrunner.model.ArtifactWrapper;
import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

public class PluginNode extends AbstractNode {

    private static final @StaticResource String PLUGIN_ICON = "io/github/netbeans/mvnrunner/resources/mojo.png"; // NOI18N

    public PluginNode(NbMavenProjectWrapper project, ArtifactWrapper artifact) {
        super(new GoalChildren(project, artifact));
        setName(artifact.getArtifactId());
        setDisplayName(artifact.getArtifactId());
        setIconBaseWithExtension(PLUGIN_ICON);
        setShortDescription(artifact.getId());
    }

}
