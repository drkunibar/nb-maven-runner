package io.github.netbeans.mvnrunner.model;

public class ProfileWrapper extends Wrapper {

    public ProfileWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    public String getId() {
        return invokeSimple("getId");
    }

}
