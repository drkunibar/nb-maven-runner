package io.github.netbeans.mvnrunner.node;

import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

@Messages("LB_Plugins=Plugins")
public class PluginListNode extends AbstractNode {

    private static final @StaticResource String PLUGIN_ICON = "io/github/netbeans/mvnrunner/resources/mojo.png"; // NOI18N

    public PluginListNode(NbMavenProjectWrapper project) {
        super(new PluginChildren(project), Lookup.EMPTY);
        setName(Bundle.LB_Plugins());
        setDisplayName(Bundle.LB_Plugins());
        setIconBaseWithExtension(PLUGIN_ICON);
    }

}
