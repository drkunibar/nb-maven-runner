package io.github.netbeans.mvnrunner.node;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

import lombok.SneakyThrows;

import io.github.netbeans.mvnrunner.favorite.FavoriteChangeListener;
import io.github.netbeans.mvnrunner.favorite.FavoriteDescriptor;
import io.github.netbeans.mvnrunner.favorite.FavoriteService;
import io.github.netbeans.mvnrunner.util.NodeUtils;

public class FavoriteChildren extends Children.Keys<FavoriteDescriptor> implements FavoriteChangeListener {

    // private final java.util.Map<String, FavoriteableNode> favorites;
    private final Node rootNode;

    public FavoriteChildren(Node rootNode) {
        super(false);
        this.rootNode = rootNode;
        // this.favorites = new LinkedHashMap<>();
        FavoriteService service = Lookup.getDefault().lookup(FavoriteService.class);
        if (service != null) {
            service.addFavoritableListener(this::favoriteChanged);
        }
    }

    // public void addIdentifier(FavoriteableNode node) {
    // favorites.put(node.getNodeIdentifier(), node);
    // }

    @Override
    protected Node[] createNodes(FavoriteDescriptor key) {
        AtomicReference<Node> resultRef = new AtomicReference<>();
        NodeUtils.walkTree(rootNode, n -> {
            if (n instanceof FavoriteableNode fnode) {
                if (Objects.equals(fnode.getNodeIdentifier(), key.getIdentifier())) {
                    resultRef.set(new FavoriteNode((AbstractNode) n, key));
                }
            }
        });
        return resultRef.get() != null ? new Node[] { resultRef.get() } : new Node[0];
        // FavoriteableNode node = favorites.get(key);
        // if (node != null) {
        // return new Node[] { node.getFavoriteNode("foo", null) };
        // } else {
        // return new Node[0];
        // }
    }

    @SneakyThrows
    @Override
    public void addNotify() {
        FavoriteService service = Lookup.getDefault().lookup(FavoriteService.class);
        if (service != null) {
            Collection<FavoriteDescriptor> favorites = service.getFavorites();
            Set<String> nodeIdentities = new HashSet<>();
            NodeUtils.walkTree(rootNode, n -> {
                if (n instanceof FavoriteableNode fnode) {
                    nodeIdentities.add(fnode.getNodeIdentifier());
                }
            });
            List<FavoriteDescriptor> foundFavorites = favorites.stream()
                    .filter(f -> nodeIdentities.contains(f.getIdentifier()))
                    .collect(Collectors.toList());
            setKeys(foundFavorites);
            //
            // setKeys(service.getFavorites());
            // super.addNotify();
        }
    }

    @Override
    public void favoriteChanged() {
        addNotify();
    }

}
