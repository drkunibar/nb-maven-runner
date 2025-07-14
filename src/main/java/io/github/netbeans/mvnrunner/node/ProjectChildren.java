package io.github.netbeans.mvnrunner.node;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.annotation.Nonnull;

import org.netbeans.api.project.Project;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

import lombok.extern.slf4j.Slf4j;

import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

@Slf4j
// @formatter:off
@Messages({
    "MSG_missingProject=Project update event without project"
})
// @formatter:on
public class ProjectChildren extends Children.Keys<ProjectChildren.NodeTypes> implements PropertyChangeListener {

    public enum NodeTypes {
        PROFILES, ACTION, LIFECYCLE, PLUGINS
    }

    private final NbMavenProjectWrapper project;

    public ProjectChildren(@Nonnull NbMavenProjectWrapper project) {
        super(false);
        this.project = project;
        project.addPropertyChangeListener(this::propertyChange);
    }

    @Override
    protected Node[] createNodes(NodeTypes key) {
        switch (key) {
            case PROFILES -> {
                return new Node[] { new ProfileListNode(project) };
            }
            case ACTION -> {
                return new Node[] { new ActionListNode(project) };
            }
            case LIFECYCLE -> {
                return new Node[] { new LifecycleListNode(project) };
            }
            case PLUGINS -> {
                return new Node[] { new PluginListNode(project) };
            }
            default -> throw new AssertionError();
        }
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys(NodeTypes.values());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch (propertyName) {
            case NbMavenProjectWrapper.PROP_PROJECT -> {
                if (evt.getSource() instanceof Project changedProject) {
                    updateProject(changedProject);
                } else {
                    log.warn(Bundle.MSG_missingProject());
                }
            }
            case NbMavenProjectWrapper.PROP_RESOURCE -> {
                // Don't know when this happens
            }
            default -> {
                // No default
            }
        }
    }

    private void updateProject(Project changedProject) {
        Project nbProjectImpl = project.getNbMavenProjectImpl();
        if (nbProjectImpl == changedProject) {
            addNotify();
        }
    }

}
