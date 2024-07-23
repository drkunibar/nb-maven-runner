package io.github.netbeans.mvnrunner.model;

import java.io.File;

public class ArtifactWrapper extends Wrapper {

    public ArtifactWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    public String getArtifactId() {
        return invokeSimple("getArtifactId");
    }

    public String getId() {
        return invokeSimple("getId");
    }

    public File getFile() {
        return invokeSimple("getFile");
    }

    public String getGroupId() {
        return invokeSimple("getGroupId");
    }

    public String getVersion() {
        return invokeSimple("getVersion");
    }
}
