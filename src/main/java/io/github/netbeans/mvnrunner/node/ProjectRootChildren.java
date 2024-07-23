package io.github.netbeans.mvnrunner.node;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
public class ProjectRootChildren extends Children.Keys<NbMavenProjectWrapper> implements PropertyChangeListener {

    private static final String OPEN_PROJECTS_PROP = "openProjects"; // NOI18N

    public ProjectRootChildren() {
        this(false);
    }

    public ProjectRootChildren(boolean lazy) {
        super(lazy);
        OpenProjects openProjects = OpenProjects.getDefault();
        openProjects.addPropertyChangeListener(this::propertyChange);
    }

    @Override
    protected Node[] createNodes(NbMavenProjectWrapper key) {
        return new Node[] { new ProjectNode(key) };
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        Project[] openProjects = OpenProjects.getDefault().getOpenProjects();
        Collection<NbMavenProjectWrapper> result = new ArrayList<>();
        for (Project p : openProjects) {
            if (p != null && NbMavenProjectImplWrapper.isNbMavenProjectImpl(p)) {
                NbMavenProjectImplWrapper projectImplWrapper = NbMavenProjectImplWrapper.getInstance(p);
                NbMavenProjectWrapper projectWrapper = projectImplWrapper.getNbMavenProjectWrapper();
                result.add(projectWrapper);
            }
        }
        setKeys(result.toArray(NbMavenProjectWrapper[]::new));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (Objects.equals(propertyName, OPEN_PROJECTS_PROP)) {
            addNotify();
        }
    }
}
