package io.github.netbeans.mvnrunner.action;

import java.awt.event.ActionEvent;
import javax.annotation.Nonnull;
import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.openide.util.Lookup;

import io.github.netbeans.mvnrunner.favorite.FavoriteService;
import io.github.netbeans.mvnrunner.node.FavoriteNode;

public class RemoveFavoriteAction extends AbstractAction {

    private final FavoriteNode node;

    public RemoveFavoriteAction(@Nonnull FavoriteNode node) {
        this.node = node;
    }

    public RemoveFavoriteAction(@Nonnull FavoriteNode node, String name) {
        super(name);
        this.node = node;
    }

    public RemoveFavoriteAction(@Nonnull FavoriteNode node, String name, Icon icon) {
        super(name, icon);
        this.node = node;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FavoriteService service = Lookup.getDefault().lookup(FavoriteService.class);
        if (service != null) {
            service.removeFavorite(node.getName());
        }
    }

}
