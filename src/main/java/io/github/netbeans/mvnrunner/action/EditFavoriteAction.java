package io.github.netbeans.mvnrunner.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import org.openide.util.Lookup;

import io.github.netbeans.mvnrunner.favorite.FavoriteDescriptor;
import io.github.netbeans.mvnrunner.favorite.FavoriteService;
import io.github.netbeans.mvnrunner.node.FavoriteNode;

public class EditFavoriteAction extends AbstractAction {

    private final FavoriteNode favoriteNode;

    public EditFavoriteAction(FavoriteNode favoriteNode) {
        super("Edit");
        this.favoriteNode = favoriteNode;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FavoriteService service = Lookup.getDefault().lookup(FavoriteService.class);
        FavoriteDescriptor descriptor = new FavoriteDescriptor(favoriteNode.getNodeIdentifier(),
                favoriteNode.getDisplayName(), favoriteNode.getIconUrl(), "");
        if (service != null) {
            FavoriteDlg dlg = new FavoriteDlg(null, descriptor);
            dlg.setSize(480, 350);
            dlg.setLocationRelativeTo(null);
            dlg.setModal(true);
            dlg.setVisible(true);
            if (dlg.isOkClicked()) {
                descriptor = new FavoriteDescriptor(favoriteNode.getNodeIdentifier(), dlg.getName(), dlg.getImageUri(),
                        dlg.getDescription());
                service.addFavorite(descriptor);
                descriptor = new FavoriteDescriptor(favoriteNode.getNodeIdentifier(), dlg.getName(), dlg.getImageUri(),
                        dlg.getDescription());
                service.addFavorite(descriptor);
            }
        }
    }

}
