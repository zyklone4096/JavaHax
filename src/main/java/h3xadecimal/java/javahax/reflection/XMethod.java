package h3xadecimal.java.javahax.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XMethod<O> {
    public final XClass<O> owner;
    public final Method origin;
    public final Class<?>[] params;

    public XMethod(Class<O> owner, Method origin) {
        this(new XClass<O>(owner), origin);
    }

    public XMethod(XClass<O> owner, Method origin) {
        this.owner = owner;
        this.origin = origin;
        params = origin.getParameterTypes();
    }

    public Object invoke(Object... params) throws InvocationTargetException {
        for (int i = 0; i < params.length; i++) {
            if (params[i].getClass() != this.params[i]) throw new IllegalArgumentException("Illegal parameter type");
        }
        if (!origin.isAccessible()) origin.setAccessible(true);
        try {
            return origin.invoke(params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Impossible IllegalAccessException", e);
        }
    }
}
