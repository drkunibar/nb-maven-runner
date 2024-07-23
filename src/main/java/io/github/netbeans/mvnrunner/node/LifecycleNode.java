package io.github.netbeans.mvnrunner.node;

import static io.github.netbeans.mvnrunner.node.Bundle.ACT_Execute;
import static io.github.netbeans.mvnrunner.node.Bundle.ACT_Execute_mod;

import java.util.stream.Collectors;
import javax.swing.Action;

import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

import io.github.netbeans.mvnrunner.model.Mojo.Phase;
import io.github.netbeans.mvnrunner.util.MavenProjectUtils;

// TODO: Is mostly identical with GoalNode
// @formatter:off
@Messages({
    "# {0} - phase",
    "# {1} - goals",
    "TT_lifecycle=<html><b>Phase:</b> {0}<br/>\n<b>Goals:</b> {1}</htlm>"
})
// @formatter:on
public class LifecycleNode extends AbstractNode {

    private static final @StaticResource String LIFECYCLE_ICON
            = "io/github/netbeans/mvnrunner/resources/thread_running_16.png"; // NOI18N
    private final Project project;
    private final Phase phase;

    public LifecycleNode(Project project, Phase phase) {
        super(Children.LEAF);
        this.project = project;
        this.phase = phase;
        setDisplayName(phase.getName());
        setName(phase.getName());
        setIconBaseWithExtension(LIFECYCLE_ICON);
        String goals = phase.getMojos()
                .stream()
                .map(m -> m.getPrefix() + ':' + m.getGoal())
                .collect(Collectors.joining(", "));
        setShortDescription(String.format(Bundle.TT_lifecycle(phase.getName(), goals)));
    }

    @Override
    public Action[] getActions(boolean context) {
        Action runGoalAction = MavenProjectUtils.createCustomMavenAction(phase.getName(),
                MavenProjectUtils.createNetbeansActionMapping(project, phase), false, Lookup.EMPTY, project);
        runGoalAction.putValue(Action.NAME, ACT_Execute());
        Action runGoalWithModsAction = MavenProjectUtils.createCustomMavenAction(phase.getName(),
                MavenProjectUtils.createNetbeansActionMapping(project, phase), true, Lookup.EMPTY, project);
        runGoalWithModsAction.putValue(Action.NAME, ACT_Execute_mod());
        return new Action[] { runGoalAction, runGoalWithModsAction };
    }

    @Override
    public Action getPreferredAction() {
        return MavenProjectUtils.createCustomMavenAction(phase.getName(),
                MavenProjectUtils.createNetbeansActionMapping(project, phase), false, Lookup.EMPTY, project);
    }

}
