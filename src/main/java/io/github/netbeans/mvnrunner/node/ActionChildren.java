package io.github.netbeans.mvnrunner.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.swing.AbstractAction;
import javax.swing.Action;

import org.netbeans.api.project.Project;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import io.github.netbeans.mvnrunner.action.AddFavoriteAction;
import io.github.netbeans.mvnrunner.favorite.FavoriteDescriptor;
import io.github.netbeans.mvnrunner.model.NetbeansActionMappingWrapper;
import io.github.netbeans.mvnrunner.model.ProjectActionWrapper;
import io.github.netbeans.mvnrunner.node.ActionChildren.MavenAction;
import io.github.netbeans.mvnrunner.util.ActionUtils;
import io.github.netbeans.mvnrunner.util.MavenProjectUtils;

@Slf4j
public class ActionChildren extends Children.Keys<Object> {

    private final Project project;

    public ActionChildren(@Nonnull Project project) {
        super(false);
        this.project = project;
    }

    @Override
    protected Node[] createNodes(Object key) {
        if (key instanceof MavenAction action) {
            NetbeansActionMappingWrapper actionMapping = action.getActionMapping();
            return new Node[] { new ActionNode(project, actionMapping, action.isGlobal()) };
        } else if (key instanceof ProjectActionWrapper projectAction) { // Default Action like 'Run', 'Debug' etc.
            return new Node[] { new SimpleActionNode(projectAction.unwrap()) };
        } else {
            throw new IllegalArgumentException("Unknow key type: " + key);
        }
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        Collection<Object> result = new ArrayList<>();
        // Default project actions
        Optional<ProjectNode> projectNodeRef = getProjectNode();
        if (projectNodeRef.isPresent()) {
            ProjectNode projectNode = projectNodeRef.get();
            result.add(ActionUtils.createProjectRunAction(projectNode));
            result.add(ActionUtils.createProjectDebugAction(projectNode));
            result.add(ActionUtils.createProjectBuildAction(projectNode));
            result.add(ActionUtils.createProjectRebuildAction(projectNode));
        }
        // Custom project actions
        Project nbproject1 = project;
        NetbeansActionMappingWrapper[] activeCustomMappings = MavenProjectUtils.getActiveCustomMappings(nbproject1);
        Arrays.stream(activeCustomMappings).forEach(nam -> result.add(new MavenAction(nam, false)));
        setKeys(result);
    }

    private Optional<ProjectNode> getProjectNode() {
        Node node = getNode();
        while (node != null) {
            if ((node instanceof ProjectNode)) {
                return Optional.of((ProjectNode) node);
            }
            node = node.getParentNode();
        }
        return Optional.empty();
    }

    @Value
    public static class ActionContainer<T> {

        private final T action;
    }

    @Value
    public static class MavenAction {

        NetbeansActionMappingWrapper actionMapping;
        boolean global;
    }

    public class SimpleActionNode extends AbstractNode implements FavoriteableNode {

        private final AbstractAction action;

        public SimpleActionNode(@Nonnull AbstractAction action) {
            super(Children.LEAF, Lookups.fixed(project));
            this.action = action;
            this.action.setEnabled(true);
            this.action.putValue(Action.NAME, (String) action.getValue(ActionUtils.TITLE_PROP));
            this.setDisplayName((String) action.getValue(ActionUtils.TITLE_PROP));
            this.setIconBaseWithExtension((String) action.getValue(ActionUtils.ICONBASE_PROP));
            this.setShortDescription((String) action.getValue(Action.SHORT_DESCRIPTION));
        }

        @Override
        public Action[] getActions(boolean context) {
            Action addFavoriteAction = new AddFavoriteAction(this);
            return new Action[] { addFavoriteAction };

        }

        @Override
        public Action getPreferredAction() {
            return action;
        }

        @Override
        public String getNodeIdentifier() {
            return new StringBuilder().append("SimpleActionNode_")
                    .append(project.getProjectDirectory().getPath())
                    .append("_")
                    .append(action.getValue(Action.NAME))
                    .toString();
        }

        @Override
        public FavoriteNode getFavoriteNode(String name, String imageUri, String description) {
            FavoriteDescriptor favoriteDescriptor
                    = new FavoriteDescriptor(getNodeIdentifier(), name, imageUri, description);
            FavoriteNode result = new FavoriteNode(this, favoriteDescriptor);
            return result;
        }

    }
}
