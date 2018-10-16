package d.plugin.utils

import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor

public class InjectManager {


    private static HashSet<String> mInjectSet

    static byte[] injectClasses(byte[] srcByteCode, HashSet<String> set) {
        mInjectSet = set
        byte[] classBytesCode = null
        try {
            classBytesCode = inject(srcByteCode)
            return classBytesCode
        } catch (Exception e) {
            e.printStackTrace()
        }
        if (classBytesCode == null) {
            classBytesCode = srcByteCode
        }
        return classBytesCode
    }


    private static byte[] inject(byte[] srcClass) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        ClassVisitor methodFilterCV = new InjectClassVisitor(classWriter)
        ClassReader cr = new ClassReader(srcClass)
        cr.accept(methodFilterCV, ClassReader.SKIP_DEBUG)
        return classWriter.toByteArray()
    }

    static void visitMethodWithLoadedParams(MethodVisitor mv) {

        mInjectSet.each { String path ->
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitLdcInsn(path)
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "d/drouter/RouterManager", "addTargetClassPath", "(Ljava/lang/String;)V", false)
        }
    }
}
