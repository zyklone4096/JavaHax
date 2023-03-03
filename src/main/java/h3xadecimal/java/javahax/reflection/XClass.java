package h3xadecimal.java.javahax.reflection;

import java.lang.reflect.Field;

public class XClass<T> {
    public final Class<T> origin;

    public XClass(Class<T> origin) {
        this.origin = origin;
    }

    public XMethod<T> accessMethod(String name, Class<?>... params) {
        try {
            return new XMethod<>(this, Reflections.accessDeclaredMethod(origin, name, params));
        } catch (Throwable t) {
            return null;
        }
    }

    public Field accessField(String name) {
        try {
            return Reflections.accessField(origin, name);
        } catch (Throwable t) {
            return null;
        }
    }
}
