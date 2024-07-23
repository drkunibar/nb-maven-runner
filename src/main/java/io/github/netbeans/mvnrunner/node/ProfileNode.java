package io.github.netbeans.mvnrunner.node;

import org.netbeans.api.annotations.common.StaticResource;
import org.openide.explorer.view.CheckableNode;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.NbBundle.Messages;

import io.github.netbeans.mvnrunner.node.ProfileChildren.ProfileDescription;

// @formatter:off
@Messages({
    "# {0} - profile name",
    "# {1} - source",
    "# {2} - active",
    "TT_profile=<html><b>Id:</b> {0}<br/><b>Source:</b> {1}<br/><b>Active:</b> {2}</htlm>"
})
// formatter:on
public class ProfileNode extends AbstractNode implements CheckableNode {

    private static final @StaticResource String PROFILE_ICON = "io/github/netbeans/mvnrunner/resources/config.png"; // NOI18N
    private final ProfileDescription profileDescription;

    public ProfileNode(ProfileDescription profileDescription) {
        super(Children.LEAF);
        this.profileDescription = profileDescription;
        setName(profileDescription.getId());
        setDisplayName(profileDescription.getId());
        setIconBaseWithExtension(PROFILE_ICON);
        setShortDescription(String.format(Bundle.TT_profile(profileDescription.getId(), profileDescription.getSource(),
                profileDescription.isActive())));
    }

    @Override
    public boolean isCheckable() {
        return true;
    }

    @Override
    public boolean isCheckEnabled() {
        return false;
    }

    @Override
    public Boolean isSelected() {
        return profileDescription.isActive();
    }

    @Override
    public void setSelected(Boolean selected) {
        // NOP
    }
}
