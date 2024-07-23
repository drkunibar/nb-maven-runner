package io.github.netbeans.mvnrunner.model;

import java.lang.reflect.Method;

import lombok.SneakyThrows;

public class PlexusConfigurationWrapper extends Wrapper {

    public PlexusConfigurationWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    @SneakyThrows
    public PlexusConfigurationWrapper getChild(String childName) {
        Method metod = getUnwrapClass().getDeclaredMethod("getChild", String.class);
        Object o = metod.invoke(unwrap(), childName);
        return o == null ? null : new PlexusConfigurationWrapper(o);
    }

    @SneakyThrows
    public String getAttribute(String attrName) {
        Method metod = getUnwrapClass().getDeclaredMethod("getAttribute", String.class);
        return (String) metod.invoke(unwrap(), attrName);
    }

    public String getValue() {
        return invokeSimple("getValue");
    }

}
