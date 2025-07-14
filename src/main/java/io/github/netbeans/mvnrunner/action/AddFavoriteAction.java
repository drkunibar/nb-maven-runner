package io.github.netbeans.mvnrunner.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import org.openide.util.Lookup;

import io.github.netbeans.mvnrunner.favorite.FavoriteDescriptor;
import io.github.netbeans.mvnrunner.favorite.FavoriteService;
import io.github.netbeans.mvnrunner.node.FavoriteableNode;

public class AddFavoriteAction extends AbstractAction {

    private final FavoriteableNode favoriteableNode;

    public AddFavoriteAction(FavoriteableNode favoriteableNode) {
        super("Add to favorites");
        this.favoriteableNode = favoriteableNode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FavoriteService service = Lookup.getDefault().lookup(FavoriteService.class);
        FavoriteDescriptor descriptor = new FavoriteDescriptor(favoriteableNode.getNodeIdentifier(),
                favoriteableNode.getDisplayName(), null, "");
        if (service != null) {
            FavoriteDlg dlg = new FavoriteDlg(null, descriptor);
            dlg.setSize(480, 350);
            dlg.setLocationRelativeTo(null);
            dlg.setModal(true);
            dlg.setVisible(true);
            if (dlg.isOkClicked()) {
                descriptor = new FavoriteDescriptor(favoriteableNode.getNodeIdentifier(), dlg.getName(),
                        dlg.getImageUri(), dlg.getDescription());
                service.addFavorite(descriptor);
            }
        }
    }
}
