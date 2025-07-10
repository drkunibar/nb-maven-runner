package io.github.netbeans.mvnrunner.node;

import org.openide.nodes.Node;

/**
 * A {@link Node} that can be a favorite.
 */
public interface FavoriteableNode {

    /**
     * The displayed Name.
     *
     * @return The displayed name.
     */
    String getDisplayName();

    /**
     * A {@link String} that identifies the {@link Node}.
     * <p>
     * This will be needed to identify the 'real' Node. It must be unique and it
     * should be immutable.
     * 
     * @return The identifier of the {@link Node}.
     */
    String getNodeIdentifier();

    // /**
    // * Execute the main action of the {@link Node}.
    // */
    // void execute();
    //
    // /**
    // * The icon of the Node.
    // *
    // * @return The icon of the node.
    // */
    // Image getIcon();

    /**
     * Returns
     * 
     * @return
     */
    FavoriteNode getFavoriteNode(String name, String imageUri, String description);
}
