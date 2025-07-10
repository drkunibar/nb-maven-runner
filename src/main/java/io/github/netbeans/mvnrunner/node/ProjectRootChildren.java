package io.github.netbeans.mvnrunner.node;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

import lombok.extern.java.Log;

import io.github.netbeans.mvnrunner.model.NbMavenProjectImplWrapper;
import io.github.netbeans.mvnrunner.model.NbMavenProjectWrapper;

@Log
public class ProjectRootChildren extends Children.Keys<Object> {

    private static final String OPEN_PROJECTS_PROP = "openProjects"; // NOI18N

    private FavoriteListNode favoriteListNode;

    public ProjectRootChildren(boolean lazy) {
        super(lazy);
        OpenProjects openProjects = OpenProjects.getDefault();
        openProjects.addPropertyChangeListener(this::onOpenProjectsPropertyChange);
    }

    @Override
    protected Node[] createNodes(Object key) {
        if (key instanceof NbMavenProjectWrapper projectWrapper) {
            return new Node[] { new ProjectNode(projectWrapper) };
        } else if (key instanceof FavoriteListNode favListNode) {
            return new Node[] { favListNode };
        } else {
            return new Node[0];
        }
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        Collection keys = new ArrayList<>();
        if (favoriteListNode == null) {
            this.favoriteListNode = new FavoriteListNode(this.getNode());
        } else {
            favoriteListNode.addNotify();
        }
        keys.add(favoriteListNode);
        Project[] openProjects = OpenProjects.getDefault().getOpenProjects();
        for (Project p : openProjects) {
            if (p != null && NbMavenProjectImplWrapper.isNbMavenProjectImpl(p)) {
                NbMavenProjectImplWrapper projectImplWrapper = NbMavenProjectImplWrapper.getInstance(p);
                NbMavenProjectWrapper projectWrapper = projectImplWrapper.getNbMavenProjectWrapper();
                keys.add(projectWrapper);
            }
        }
        setKeys(keys.toArray(Object[]::new));
    }

    public void onOpenProjectsPropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (Objects.equals(propertyName, OPEN_PROJECTS_PROP)) {
            addNotify();
        }
    }
}
