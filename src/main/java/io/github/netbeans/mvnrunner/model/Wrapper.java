package io.github.netbeans.mvnrunner.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.annotation.Nonnull;

import org.apache.commons.lang3.reflect.MethodUtils;

import lombok.SneakyThrows;

/**
 * Wrapper for <i>unreachable</i> classes.
 * <p>
 * While the Maven modules haven't got any public API, we need a hack.
 */
class Wrapper {

    private final Object wrappedObject;

    protected Wrapper(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    @SneakyThrows
    public final <T> T unwrap() {
        return (T) wrappedObject;
    }

    public final <T> Class<T> getUnwrapClass() {
        return (Class<T>) unwrap().getClass();
    }

    @SneakyThrows
    protected final <T> T invokeSimple(@Nonnull String methodName) {
        Method method = MethodUtils.getAccessibleMethod(getUnwrapClass(), methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find any Method for " + getUnwrapClass() + "." + methodName);
        }
        return (T) method.invoke(unwrap());
    }

    @SneakyThrows
    protected static Field getField(Class<?> cls, String fieldName) {
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
}
