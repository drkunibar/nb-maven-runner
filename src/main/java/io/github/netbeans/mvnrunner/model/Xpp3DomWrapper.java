package io.github.netbeans.mvnrunner.model;

import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.MethodUtils;

import lombok.SneakyThrows;

public class Xpp3DomWrapper extends Wrapper {

    public Xpp3DomWrapper(Object wrappedObject) {
        super(wrappedObject);
    }

    @SneakyThrows
    public Object getChild(String childName) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), "getChild", String.class);
        return (String) method.invoke(unwrap(), childName);
    }

}
