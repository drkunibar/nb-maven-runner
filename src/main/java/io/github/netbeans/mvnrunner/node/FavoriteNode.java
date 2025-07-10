package io.github.netbeans.mvnrunner.node;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import javax.annotation.Nonnull;
import javax.swing.Action;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;

import io.github.netbeans.mvnrunner.action.AddFavoriteAction;
import io.github.netbeans.mvnrunner.action.EditFavoriteAction;
import io.github.netbeans.mvnrunner.action.RemoveFavoriteAction;
import io.github.netbeans.mvnrunner.favorite.FavoriteDescriptor;
import io.github.netbeans.mvnrunner.util.ImageUtils;

public class FavoriteNode<T extends Node & FavoriteableNode> extends AbstractNode {

    private final T linkedNode;
    private final String iconUri;

    public FavoriteNode(@Nonnull T linkedNode, @Nonnull FavoriteDescriptor favoriteDescriptor) {
        this(linkedNode, favoriteDescriptor, new Properties());
    }

    public FavoriteNode(@Nonnull T linkedNode, @Nonnull FavoriteDescriptor favoriteDescriptor,
            @Nonnull Properties nodeProperties) {
        super(Children.LEAF, linkedNode.getLookup());
        setName(favoriteDescriptor.getIdentifier());
        setDisplayName(favoriteDescriptor.getName());
        setShortDescription(favoriteDescriptor.getDescription());
        this.iconUri = favoriteDescriptor.getImageUri();
        this.linkedNode = linkedNode;
    }

    public T unwrap() {
        return linkedNode;
    }

    public String getNodeIdentifier() {
        return linkedNode.getNodeIdentifier();
    }

    public String getIconUrl() {
        return iconUri;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtils.getNodeScaledImage(type, iconUri, linkedNode.getIcon(type));
    }

    @Override
    public Action getPreferredAction() {
        return linkedNode.getPreferredAction();
    }

    @Override
    public Action[] getActions(boolean context) {
        Collection<Action> actions = new ArrayList<>(Arrays.asList(linkedNode.getActions(context)));
        actions.add(new EditFavoriteAction(this));
        actions.add(new RemoveFavoriteAction(this, "Remove from Favorites"));
        return actions.stream().filter(a -> !(a instanceof AddFavoriteAction)).toList().toArray(Action[]::new);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return linkedNode.getHelpCtx();
    }

    // public Properties getAsProperties() {
    // Properties result = new Properties();
    // result.put("name", getName());
    // result.put("shortDescription", getShortDescription());
    // result.put("iconPath", iconUri);
    // result.put("path", getTreePathHash(linkedNode));
    // return result;
    // // FavoriteNodeProperties properties = new FavoriteNodeProperties(this);
    //
    // }
    //
    // private String getTreePathHash(Node node) {
    // StringBuilder result = new StringBuilder(8192);
    // do {
    // result.append(node.getDisplayName());
    // result.append(" / ");
    // } while ((node = node.getParentNode()) != null);
    // return result.toString();
    // }
    //
    // public static class FavoriteNodeProperties {
    //
    // private String patth;
    // private String name;
    // private String iconPath;
    // private String shortDescription;
    //
    // protected FavoriteNodeProperties() {
    // }
    //
    // protected FavoriteNodeProperties(FavoriteNode node) {
    // this.patth = getTreePathHash(node.linkedNode);
    // this.name = node.getName();
    // this.shortDescription = node.getShortDescription();
    // }
    //
    // public String serialize() {
    // return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    // }
    //
    // private String getTreePathHash(Node node) {
    // StringBuilder result = new StringBuilder(8192);
    // do {
    // result.append(node.getName());
    // result.append(" / ");
    // } while ((node = node.getParentNode()) != null);
    // return result.toString();
    // }
    //
    // }

}
