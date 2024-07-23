package io.github.netbeans.mvnrunner.model;

public class ParameterWrapper extends Wrapper {

    public ParameterWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    public String getName() {
        return invokeSimple("getName");
    }

    public boolean isEditable() {
        return invokeSimple("isEditable");
    }

    public boolean isRequired() {
        return invokeSimple("isRequired");
    }

}
