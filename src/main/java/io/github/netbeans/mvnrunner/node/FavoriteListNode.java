package io.github.netbeans.mvnrunner.node;

import org.netbeans.api.annotations.common.StaticResource;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;

/**
 * The {@link Node} that contains the {@link FavoriteNode}.
 */
@NbBundle.Messages({ "LB_Favorites=Favorites", "HINT_Favorits=Actions that marked as favorite" })
public class FavoriteListNode extends AbstractNode {

    public static final @StaticResource String FAVORITES_ICON = "io/github/netbeans/mvnrunner/resources/favorites.png"; // NOI18N

    private final FavoriteChildren children;

    public FavoriteListNode(Node rootNode) {
        super(new FavoriteChildren(rootNode));
        children = (FavoriteChildren) this.getChildren();
        setName(Bundle.LB_Favorites());
        setDisplayName(Bundle.LB_Favorites());
        setIconBaseWithExtension(FAVORITES_ICON);
        setShortDescription(Bundle.HINT_Favorits());
        // FavoriteService service = Lookup.getDefault().lookup(FavoriteService.class);
        // if (service != null) {
        // service.addFavoritableListener(() -> {
        // service.getFavorites().forEach(FavoriteDescriptor::getIdentifier);
        // });
        // }
    }

    public void addFavoriteNode(FavoriteableNode node) {
        // children.addIdentifier(node);
        children.addNotify();
    }

    public void addNotify() {
        children.addNotify();
    }

}
