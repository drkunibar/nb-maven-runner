package io.github.netbeans.mvnrunner.node;

import javax.annotation.Nonnull;

import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle.Messages;

import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

// @formatter:off
@Messages({
    "LB_Actions=Actions",
    "LB_Actions_Description=Global and custom actions"
})
// @formatter:on
public class ActionListNode extends AbstractNode {

    private static final String ACTION_ICON = "io/github/netbeans/mvnrunner/resources/Maven2IconRun1.png"; // NOI18N

    public ActionListNode(@Nonnull NbMavenProjectWrapper project) {
        super(new ActionChildren(project.getNbMavenProjectImpl()));
        setDisplayName(Bundle.LB_Actions());
        setName(Bundle.LB_Actions());
        setIconBaseWithExtension(ACTION_ICON);
        setShortDescription(Bundle.LB_Actions_Description());
    }

}
