package io.github.netbeans.mvnrunner.node;

import javax.annotation.Nonnull;

import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

public class ProfileListNode extends AbstractNode {

    private static final @StaticResource String PROFILE_ICON = "io/github/netbeans/mvnrunner/resources/config.png"; // NOI18N

    @NbBundle.Messages("LB_Profiles=Profiles")
    public ProfileListNode(@Nonnull NbMavenProjectWrapper project) {
        super(new ProfileChildren(project), Lookup.EMPTY);
        setName(Bundle.LB_Profiles());
        setDisplayName(Bundle.LB_Profiles());
        setIconBaseWithExtension(PROFILE_ICON);
    }
}
