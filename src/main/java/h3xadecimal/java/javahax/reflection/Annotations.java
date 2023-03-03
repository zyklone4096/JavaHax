package h3xadecimal.java.javahax.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Annotations {
    public static boolean hasAnnotation(Class<?> c, Class<?> annotation) {
        if (!annotation.isAnnotation()) throw new IllegalArgumentException();

        for (Annotation a: c.getDeclaredAnnotations()) {
            if (a.annotationType() == annotation) return true;
        }
        return false;
    }

    public static boolean hasAnnotation(Method m, Class<?> annotation) {
        if (!annotation.isAnnotation()) throw new IllegalArgumentException();
        for (Annotation a: m.getDeclaredAnnotations()) {
            if (a.annotationType() == annotation) return true;
        }
        return false;
    }

    public static boolean hasAnnotation(Field f, Class<?> annotation) {
        if (!annotation.isAnnotation()) throw new IllegalArgumentException();
        for (Annotation a: f.getDeclaredAnnotations()) {
            if (a.annotationType() == annotation) return true;
        }
        return false;
    }
}
