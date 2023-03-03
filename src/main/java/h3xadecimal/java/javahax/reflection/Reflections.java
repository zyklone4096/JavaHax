package h3xadecimal.java.javahax.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflections {
    public static Method accessDeclaredMethod(Class<?> c, String name, Class<?>... params) throws NoSuchMethodException {
        Method m = c.getDeclaredMethod(name, params);
        if (!m.isAccessible()) m.setAccessible(true);
        return m;
    }

    public static Field accessField(Class<?> c, String name) throws NoSuchFieldException {
        Field f = c.getDeclaredField(name);
        if (!f.isAccessible()) f.setAccessible(true);
        return f;
    }

    public static Class<?> accessClass(String name) {
        try {
            return Class.forName(name);
        } catch (Throwable t) {
            return null;
        }
    }
}
