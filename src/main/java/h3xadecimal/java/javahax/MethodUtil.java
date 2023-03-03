package h3xadecimal.java.javahax;

import h3xadecimal.java.javahax.reflection.XMethod;

public class MethodUtil {
    public static String generateDescriptor(XMethod<?> m) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (m.origin.getParameterCount() != 0) {
            for (Class<?> c: m.origin.getParameterTypes()) {
                sb.append(getASMName(c)).append(";");
            }
        }
        sb.append(")");

        if (m.origin.getReturnType() == void.class) {
            sb.append("V;");
        } else {
            sb.append(getASMName(m.origin.getReturnType())).append(";");
        }
        return sb.toString();
    }

    public static String getASMName(Class<?> c) {
        if (c == void.class) {
            return "V";
        } else if (c == int.class) {
            return "I";
        } else if (c == double.class) {
            return "D";
        } else if (c == float.class) {
            return "F";
        } else if (c == boolean.class) {
            return "Z";
        }
        return "L" + c.getName().replace(".", "/");
    }
}
