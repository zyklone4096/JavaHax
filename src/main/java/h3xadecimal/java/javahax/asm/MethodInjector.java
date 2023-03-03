package h3xadecimal.java.javahax.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class MethodInjector extends ClassVisitor {
    public final String name;
    public final String desc;

    protected MethodInjector(int api, String name, String desc) {
        super(api);

        this.name = name;
        this.desc = desc;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(this.name) && descriptor.equals(this.desc)) {
            return visit(super.visitMethod(access, name, descriptor, signature, exceptions));
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    private MethodVisitor visit(MethodVisitor mv) {
        return new Visitor(mv) {
            @Override
            protected void visitHead() {
                MethodInjector.this.visitHead(mv);
            }
        };
    }

    protected void visitHead(MethodVisitor mv) {}

    static abstract class Visitor extends MethodVisitor {
        public Visitor(MethodVisitor mv) {
            super(Opcodes.ASM9, mv);
        }

        protected abstract void visitHead();
    }
}
