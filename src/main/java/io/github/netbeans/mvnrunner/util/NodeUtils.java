package io.github.netbeans.mvnrunner.util;

import java.util.function.Consumer;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

import lombok.experimental.UtilityClass;

import io.github.netbeans.mvnrunner.node.FavoriteListNode;

@UtilityClass
public class NodeUtils {

    public static void walkTree(Node node, Consumer<Node> consumer) {
        consumer.accept(node);
        if (node instanceof FavoriteListNode) {
            return;
        }
        Children children = node.getChildren();
        for (Node child : children.getNodes()) {
            walkTree(child, consumer);
        }
    }

    public static void walkTree(Children nodes, Consumer<Node> consumer) {
        for (Node node : nodes.getNodes()) {
            walkTree(node, consumer);
        }
    }

    public String getTreePath(Node node) {
        StringBuilder result = new StringBuilder(8192);
        do {
            result.append(node.getName());
            result.append(" / ");
        } while ((node = node.getParentNode()) != null);
        return result.toString();
    }
}
