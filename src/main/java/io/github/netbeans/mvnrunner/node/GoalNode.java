package io.github.netbeans.mvnrunner.node;

import static io.github.netbeans.mvnrunner.node.Bundle.ACT_Execute;
import static io.github.netbeans.mvnrunner.node.Bundle.ACT_Execute_help;
import static io.github.netbeans.mvnrunner.node.Bundle.ACT_Execute_mod;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.swing.AbstractAction;
import javax.swing.Action;

import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectConfigurationProvider;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

import lombok.SneakyThrows;

import io.github.netbeans.mvnrunner.action.AddFavoriteAction;
import io.github.netbeans.mvnrunner.favorite.FavoriteDescriptor;
import io.github.netbeans.mvnrunner.model.ArtifactWrapper;
import io.github.netbeans.mvnrunner.model.MavenConfigurationWrapper;
import io.github.netbeans.mvnrunner.model.Mojo;
import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;
import io.github.netbeans.mvnrunner.model.NetbeansActionMappingWrapper;
import io.github.netbeans.mvnrunner.model.RunConfigWrapper;
import io.github.netbeans.mvnrunner.model.RunUtilsWrapper;
import io.github.netbeans.mvnrunner.util.MavenProjectUtils;

// @formatter:off
@Messages({
    "ACT_Execute_mod=Execute Goal With Modifiers...",
    "ACT_Execute_help=Show Documentation...",
    "# {0} - plugin",
    "# {1} - goal prefix",
    "# {2} - goal",
    "# {3} - is lifecycle",
    "TT_goal=<html><b>Plugin:</b> {0}<br/>\n<b>Goal:</b> {1}:{2}<br/>\n<b>Lifecycle:</b> {3}</htlm>"
})
// @formatter:on
public class GoalNode extends AbstractNode implements FavoriteableNode {

    private static final @StaticResource String PLUGIN_ICON = "io/github/netbeans/mvnrunner/resources/mojo.png"; // NOI18N
    private static final @StaticResource String LIFECYCLE_ICON
            = "io/github/netbeans/mvnrunner/resources/thread_running_16.png"; // NOI18N

    private final NbMavenProjectWrapper project;
    private final Mojo mojo;

    public GoalNode(NbMavenProjectWrapper project, Mojo goal) {
        super(Children.LEAF, Lookup.EMPTY);
        this.project = project;
        this.mojo = goal;
        setName(goal.getPrefix() + ":" + goal.getGoal());
        setDisplayName(goal.getPrefix() + ":" + goal.getGoal());
        setIconBaseWithExtension(goal.isLifecycleBound() ? LIFECYCLE_ICON : PLUGIN_ICON);
        setShortDescription(String.format(
                Bundle.TT_goal(goal.getArtifact().getId(), goal.getPrefix(), goal.getGoal(), goal.isLifecycleBound())));
    }

    @Override
    public Action[] getActions(boolean context) {
        NetbeansActionMappingWrapper mapp = new NetbeansActionMappingWrapper();
        for (Mojo.Param p : mojo.getNotSetParams()) {
            if (p.getProperty() != null) {
                mapp.addProperty(p.getProperty(), "");
            }
        }
        // Execute with modifiers
        mapp.setGoals(Collections.singletonList(getCompleteGoal(mojo)));
        Action runGoalWithModsAction = MavenProjectUtils.createCustomMavenAction(getSimpleGoal(mojo), mapp, true,
                Lookup.EMPTY, getProject());
        runGoalWithModsAction.putValue(Action.NAME, ACT_Execute_mod());

        // Show Documentation
        NetbeansActionMappingWrapper mappForHelpDesc = new NetbeansActionMappingWrapper();
        mappForHelpDesc.setGoals(Collections.singletonList("help:describe")); // NOI18N
        HashMap<String, String> m = new HashMap<>();
        m.put("cmd", getCompleteGoal(mojo)); // NOI18N
        m.put("detail", "true"); // NOI18N
        mappForHelpDesc.setProperties(m);
        Action runHelpDescAction = MavenProjectUtils.createCustomMavenAction(
                String.format("help:describe for %s:%s", mojo.getPrefix(), mojo.getGoal()), mappForHelpDesc, false,
                Lookup.EMPTY, getProject());
        runHelpDescAction.putValue(Action.NAME, ACT_Execute_help());

        Action addFavoriteAction = new AddFavoriteAction(this);
        return new Action[] { new RunGoalAction(mojo, getProject()), runGoalWithModsAction, null, runHelpDescAction,
                null, addFavoriteAction };
    }

    @Override
    public Action getPreferredAction() {
        return new RunGoalAction(mojo, getProject());
    }

    @Messages("ACT_Execute=Execute Goal")
    private static class RunGoalAction extends AbstractAction {

        private final Mojo mojo;
        private final Project project;

        public RunGoalAction(Mojo mojo, Project prj) {
            this.mojo = mojo;
            this.project = prj;
            putValue(Action.NAME, ACT_Execute());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            RunConfigWrapper config = RunUtilsWrapper.createRunConfig(FileUtil.toFile(project.getProjectDirectory()),
                    project, getSimpleGoal(mojo), Collections.singletonList(getCompleteGoal(mojo)));
            ProjectConfigurationProvider prof = project.getLookup().lookup(ProjectConfigurationProvider.class);
            MavenConfigurationWrapper m2c = MavenConfigurationWrapper.getInstance(prof.getActiveConfiguration());

            if (m2c != null) {
                config.addProperties(m2c.getProperties());
                config.setActivatedProfiles(m2c.getActivatedProfiles());
            }
            RunUtilsWrapper.run(config);
        }

    }

    private static String getSimpleGoal(@Nonnull Mojo mojo) {
        return String.format("%s:%s", mojo.getPrefix(), mojo.getGoal());
    }

    private static String getCompleteGoal(@Nonnull Mojo mojo) {
        ArtifactWrapper artifact = mojo.getArtifact();
        return String.format("%s:%s:%s:%s", artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                mojo.getGoal());
    }

    @SneakyThrows
    private Project getProject() {
        return project.getNbMavenProjectImpl();
    }

    @Override
    public String getNodeIdentifier() {
        return new StringBuilder().append("GoalNode_")
                .append(project.getNbMavenProjectImpl().getProjectDirectory().getPath())
                .append("_")
                .append(getName())
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
