package h3xadecimal.java.javahax;

import h3xadecimal.java.javahax.asm.MethodInjector;
import h3xadecimal.java.javahax.reflection.Annotations;
import h3xadecimal.java.javahax.reflection.Reflections;
import h3xadecimal.java.javahax.reflection.XMethod;
import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@NoHax
public class ClassUtil {
    private static final HashMap<String, Runnable> hooks = new HashMap<>();

    public static boolean validateProtection(Class<?> c) {
        return Annotations.hasAnnotation(c, NoHax.class);
    }

    public static String getClassByteName(byte[] clazz) {
        ClassReader cr = new ClassReader(clazz);
        return cr.getClassName().replace("/", ".");
    }

    /**
     * Load a class from bytes  /   从字节数组加载类
     * @throws NoSuchMethodException Unable to access defineClass method    /   无法访问defineClass方法
     * @throws IllegalAccessException Unable to invoke defineClass method   /  无法调用defineClass方法
     * @throws InvocationTargetException Exception in invocation target /   调用defineClass时出错
     * @throws InvalidClassException Exception in validation by ClassReader /   使用ClassReader验证类格式时出错
     */
    public static Class<?> loadClass(ClassLoader cl, byte[] c) throws InvalidClassException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            ClassReader validation = new ClassReader(c);
            if (Reflections.accessClass(getClassByteName(c)) != null) throw new IllegalArgumentException("Class already loaded");
        } catch (IllegalArgumentException e) {
            throw new InvalidClassException(e.getMessage());
        }

        return (Class<?>) Reflections.accessDeclaredMethod(cl.getClass(), "defineClass", String.class, byte[].class, int.class, int.class)
                .invoke(cl, getClassByteName(c), 0, c.length);
    }

    /**
     * Replace a loaded class   /   替换一个已加载的类
     * May cause problems   /   可能导致问题
     * @return Is replaced successfully /   是否替换成功
     * @throws NoSuchMethodException Unable to access defineClass method    /   无法访问defineClass方法
     * @throws IllegalAccessException Unable to invoke defineClass method   /  无法调用defineClass方法
     * @throws InvocationTargetException Exception in invocation target /   调用defineClass时出错
     * @throws IllegalArgumentException Target class name not equals new class name /   替换目标类名不等于
     */
    public static boolean replaceClass(Class<?> target, byte[] newClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IllegalArgumentException, InvalidClassException {
        if (validateProtection(target)) return false;
        if (!target.getName().equals(getClassByteName(newClass))) throw new IllegalArgumentException("");

        ClassLoader cl = target.getClassLoader();
        Method defineClass = Reflections.accessDeclaredMethod(target, "defineClass", String.class, byte[].class, int.class, int.class);
        defineClass.invoke(cl, getClassByteName(newClass), newClass, 0, newClass.length);
        return true;
    }

    /**
     *
     * @param method Target method
     * @param hook Hook method
     * @return Is hooked successfully
     * @throws IOException Unable to find target class
     */
    public static boolean hookMethod(XMethod<?> method, Runnable hook) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (validateProtection(method.owner.origin)) return false;

        ClassReader cr = new ClassReader(method.owner.origin.getName());
        UUID u = UUID.randomUUID();
        while (hooks.containsKey(u.toString())) {
            u = UUID.randomUUID();
        }
        AtomicReference<UUID> uar = new AtomicReference<>(u);
        cr.accept(new MethodInjector(Opcodes.ASM9, method.origin.getName(), MethodUtil.generateDescriptor(method)) {
            @Override
            protected void visitHead(MethodVisitor mv) {
                mv.visitLdcInsn(uar.get().toString());
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "h3xadecimal/java/javahax/ClassUtil", "invokeHook", "(Ljava/lang/String)V;", false);
            }
        }, 0);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cr.accept(cw, 0);

        hooks.put(u.toString(), hook);
        return replaceClass(method.owner.origin, cw.toByteArray());
    }

    /**
     * DO NOT INVOKE THIS   /   调用后出错后果自负
     */
    public static void invokeHook(String uuid) {
        if (hooks.containsKey(uuid)) {
            try {
                hooks.get(uuid).run();
            } catch (Throwable ignored) {}
        }
    }
}
