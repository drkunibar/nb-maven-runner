package io.github.netbeans.mvnrunner.util;

import java.util.function.Consumer;
import lombok.experimental.UtilityClass;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

@UtilityClass
public class NodeUtils {

    public static void walkTree(Node node, Consumer<Node> consumer) {
        consumer.accept(node);
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

}
