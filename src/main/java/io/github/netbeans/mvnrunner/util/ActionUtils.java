package io.github.netbeans.mvnrunner.util;

import java.text.MessageFormat;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.swing.Action;
import javax.swing.ImageIcon;

import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ActionProvider;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import com.google.common.collect.ImmutableMap;

import lombok.experimental.UtilityClass;

import io.github.netbeans.mvnrunner.model.ProjectActionWrapper;
import io.github.netbeans.mvnrunner.node.ProjectNode;

@UtilityClass
// @formatter:off
@Messages({
    "ACT_"+ActionProvider.COMMAND_RUN+"=Run Project ({0})",
    "ACT_"+ActionProvider.COMMAND_DEBUG+"=Debug Project ({0})",
    "ACT_"+ActionProvider.COMMAND_BUILD+"=Build Project ({0})",
    "ACT_"+ActionProvider.COMMAND_REBUILD+"=Rebuild Project ({0})",
    "ACT_"+ActionProvider.COMMAND_RUN+"Simple=Run Project",
    "ACT_"+ActionProvider.COMMAND_DEBUG+"Simple=Debug Project",
    "ACT_"+ActionProvider.COMMAND_BUILD+"Simple=Build Project",
    "ACT_"+ActionProvider.COMMAND_REBUILD+"Simple=Rebuild Project"
})
// @formatter:on
public class ActionUtils {

    public static final String ICONBASE_PROP = "iconBase"; // NOI18N
    public static final String TITLE_PROP = "title"; // NOI18N

    private static final @StaticResource String RUN_ICON = "io/github/netbeans/mvnrunner/resources/runProject.png"; // NOI18N
    private static final @StaticResource String DEBUG_ICON = "io/github/netbeans/mvnrunner/resources/debugProject.png"; // NOI18N
    private static final @StaticResource String BUILD_ICON = "io/github/netbeans/mvnrunner/resources/buildProject.png"; // NOI18N
    private static final @StaticResource String REBUILD_ICON
            = "io/github/netbeans/mvnrunner/resources/rebuildProject.png"; // NOI18N
    private static final Map<String, String> ICON_MAP = ImmutableMap.<String, String>builder()
            .put(ActionProvider.COMMAND_RUN, RUN_ICON)
            .put(ActionProvider.COMMAND_DEBUG, DEBUG_ICON)
            .put(ActionProvider.COMMAND_BUILD, BUILD_ICON)
            .put(ActionProvider.COMMAND_REBUILD, REBUILD_ICON)
            .build();

    @Nonnull
    public static Action createProjectRunAction(@Nonnull ProjectNode projectNode) {
        return createProjectAction(projectNode, ActionProvider.COMMAND_RUN);
    }

    @Nonnull
    public static Action createProjectDebugAction(@Nonnull ProjectNode projectNode) {
        return createProjectAction(projectNode, ActionProvider.COMMAND_DEBUG);
    }

    @Nonnull
    public static Action createProjectBuildAction(@Nonnull ProjectNode projectNode) {
        return createProjectAction(projectNode, ActionProvider.COMMAND_BUILD);
    }

    @Nonnull
    public static Action createProjectRebuildAction(@Nonnull ProjectNode projectNode) {
        return createProjectAction(projectNode, ActionProvider.COMMAND_REBUILD);
    }

    private static Action createProjectAction(ProjectNode projectNode, String command) {
        Project project = projectNode.getProject();
        String shortDescriptionMessage = NbBundle.getMessage(ActionUtils.class, "ACT_" + command);
        String shortDescription = MessageFormat.format(shortDescriptionMessage, projectNode.getDisplayName());
        String title = NbBundle.getMessage(ActionUtils.class, "ACT_" + command + "Simple");
        ImageIcon icon = ImageUtilities.loadImageIcon(ICON_MAP.get(command), true);
        Lookup lookup = Lookups.fixed(project);
        ProjectActionWrapper action = ProjectActionWrapper.getInstance(command, "", shortDescription, icon, lookup);
        action.putValue(ICONBASE_PROP, ICON_MAP.get(command));
        action.putValue(TITLE_PROP, title);
        action.putValue(Action.SHORT_DESCRIPTION, shortDescription);
        action.setEnabled(action.accept(project));
        return action;
    }
}
