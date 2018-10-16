package d.plugin.utils

import jdk.internal.org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class InjectClassVisitor extends ClassVisitor {
    private String superName
    private String className
    private String[] interfaces

    public InjectClassVisitor(final ClassVisitor cv) {
        super(Opcodes.ASM5, cv)
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.superName = superName
        this.interfaces = interfaces
        this.className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        MethodVisitor myMv = null
        //基本所有点击事件都基于接口来实现

        println( className + "   我进来了  "  + name)

            if ("init".equals(name)) {


            try {
                MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
                myMv = new MethodVisitor(Opcodes.ASM5, methodVisitor) {
                    @Override
                    void visitCode() {
                        super.visitCode()
                        InjectManager.visitMethodWithLoadedParams(methodVisitor)
                    }
                }
            } catch (Exception e) {
                e.printStackTrace()
                myMv = null
            }

            }





//        if (interfaces != null && interfaces.length > 0) {
//            MethodCell methodCell = InjectConfig.InterfaceMethods.get(name + desc)
//            if (methodCell != null && interfaces.contains(methodCell.parent)) {
//                try {
//                    MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
//                    myMv = new MethodVisitor(Opcodes.ASM5, methodVisitor) {
//                        @Override
//                        void visitCode() {
//                            InjectManager.visitMethodWithLoadedParams(className, methodVisitor, Opcodes.INVOKESTATIC, InjectConfig.AgentClassName, methodCell.agentName, methodCell.agentDesc, methodCell.paramsStart, methodCell.paramsCount, methodCell.opcodes)
//                            super.visitCode()
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace()
//                    myMv = null
//                }
//            }
//        }

//        MethodCell fMethodCell = InjectConfig.FragmentActivityMethods.get(name + desc)
//        if (fMethodCell != null) {
//            try {
//                MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
//                myMv = new MethodVisitor(Opcodes.ASM5, methodVisitor) {
//
//                    @Override
//                    void visitInsn(int opcode) {
//                        if (opcode == Opcodes.RETURN) { //在返回之前注入代码
//                            InjectManager.visitMethodWithLoadedParams(className, methodVisitor, Opcodes.INVOKESTATIC, InjectConfig.AgentClassName, fMethodCell.agentName, fMethodCell.agentDesc, fMethodCell.paramsStart, fMethodCell.paramsCount, fMethodCell.opcodes)
//                        }
//                        super.visitInsn(opcode)
//                    }
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace()
//                myMv = null
//            }
//        }

        if (myMv != null) {
            return myMv
        } else {
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}
