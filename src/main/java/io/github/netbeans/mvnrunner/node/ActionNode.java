package io.github.netbeans.mvnrunner.node;

import static io.github.netbeans.mvnrunner.node.Bundle.ACT_Execute;
import static io.github.netbeans.mvnrunner.node.Bundle.ACT_Execute_mod;

import javax.annotation.Nonnull;
import javax.swing.Action;

import org.netbeans.api.project.Project;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

import lombok.extern.slf4j.Slf4j;

import io.github.netbeans.mvnrunner.model.NetbeansActionMappingWrapper;
import io.github.netbeans.mvnrunner.util.MavenProjectUtils;

// @formatter:off
@Messages({
    "Actionode.ACT_Execute=Execute Goal",
    "ActioNode.ACT_Execute_mod=Execute Goal With Modifiers..."
})
// @formatter:on
@Slf4j
public class ActionNode extends AbstractNode {

    private static final String ACTION_ICON = "io/github/netbeans/mvnrunner/resources/Maven2IconRun1.png"; // NOI18N
    private static final String GLOBAL_ACTION_ICON = "io/github/netbeans/mvnrunner/resources/Maven2IconRun3.png"; // NOI18N
    private final NetbeansActionMappingWrapper actionMapping;
    private final Project project;

    public ActionNode(@Nonnull Project project, @Nonnull NetbeansActionMappingWrapper actionMapping, boolean global) {
        super(Children.LEAF);
        this.project = project;
        this.actionMapping = actionMapping;
        setName(actionMapping.getDisplayName());
        setDisplayName(actionMapping.getDisplayName());
        setIconBaseWithExtension(global ? GLOBAL_ACTION_ICON : ACTION_ICON);
        this.setShortDescription(actionMapping.getDisplayName() + (global ? " (global)" : ""));
    }

    @Override
    public Action[] getActions(boolean context) {
        Action runGoalAction
                = MavenProjectUtils.createCustomMavenAction(ACT_Execute(), actionMapping, false, Lookup.EMPTY, project);
        Action runGoalWithModsAction = MavenProjectUtils.createCustomMavenAction(ACT_Execute_mod(), actionMapping, true,
                Lookup.EMPTY, project);
        return new Action[] { runGoalAction, runGoalWithModsAction };
    }

    @Override
    public Action getPreferredAction() {
        return MavenProjectUtils.createCustomMavenAction(ACT_Execute(), actionMapping, false, Lookup.EMPTY, project);
    }
}
