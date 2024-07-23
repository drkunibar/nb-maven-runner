package io.github.netbeans.mvnrunner.node;

import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

@Messages("LB_Lifecycle=Lifecycle")
public class LifecycleListNode extends AbstractNode {

    private static final @StaticResource String LIFECYCLE_ICON
            = "io/github/netbeans/mvnrunner/resources/thread_running_16.png"; // NOI18N

    public LifecycleListNode(NbMavenProjectWrapper project) {
        super(new LifecycleChildren(project), Lookup.EMPTY);
        setName(Bundle.LB_Lifecycle());
        setDisplayName(Bundle.LB_Lifecycle());
        setIconBaseWithExtension(LIFECYCLE_ICON);
    }

}
