package org.example;

import org.objectweb.asm.*;

import java.io.FileOutputStream;

import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;

import org.example.expr.*;
import org.example.stmt.*;
import org.example.func.*;

public class Main {
    public static void main(String[] args) {

        Stmt[] stmt = new Stmt[]{
            //  if (n == 0) {
            new IfStmt(
                new IntCmp(new Var("a"), new Int(0), "=="),
                // return 1;
                new Stmt[]{
                    new ReturnStmt(new Int(1))
                },
                // } else {
                new Stmt[]{
                    // return n * factorial(n - 1);
                    new ReturnStmt(
                        new IntOp(
                            new Var("a"),
                            new FnCall("factorial",
                                new Expr[]{
                                    new IntOp(new Var("a"), new Int(1), "-")
                                }),
                            "*"
                        )
                    )
                }
                // }
            ),
        };
        Func func = new Func("factorial", new Var[]{new Var("a")}, stmt);

        byte[] bytes = codeGen(func);

        try {
            FileOutputStream fos = new FileOutputStream(func.getName() + ".class");
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    /**
     * Generates bytecode for the given function.
     * @param func the function to generate bytecode for
     * @return the bytecode for the given function
     */
    private static byte[] codeGen(Func func) {
        ClassWriter classWriter = new ClassWriter(COMPUTE_MAXS);
        classWriter.visit(V17, ACC_PUBLIC | ACC_SUPER, func.getName(), null, "java/lang/Object", null);
        genInit(classWriter);
        genFunc(func, classWriter);
        genFixedMain(func, classWriter);
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    /**
     * Generates the constructor for the given class.
     * @param classWriter the class to generate the constructor for
     */
    private static void genInit(ClassWriter classWriter) {
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
    }

    /**
     * Generates the main method for the given class.
     * @param func the function to generate the main method for
     * @param classWriter the class to generate the main method for
     */
    private static void genFixedMain(Func func, ClassWriter classWriter) {
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        methodVisitor.visitCode();
        for (int i = 0; i < func.argLength() ; i++) {
            parseArg(methodVisitor, i, i + 1);
        }
        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        for (int i = 0; i < func.argLength(); i++) {
            methodVisitor.visitVarInsn(ILOAD, i + 1);
        }
        methodVisitor.visitMethodInsn(INVOKESTATIC, func.getName(), func.getName(), genSig(func.argLength()), false);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

    /**
     * Parses the given argument and stores it in the given variable index.
     * @param methodVisitor the method visitor to use
     * @param argIndex the index of the argument to parse
     * @param varIndex the index of the variable to store the parsed argument in
     */
    private static void parseArg(MethodVisitor methodVisitor, int argIndex, int varIndex) {
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitIntInsn(BIPUSH, argIndex);
        methodVisitor.visitInsn(AALOAD);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
        methodVisitor.visitVarInsn(ISTORE, varIndex);
    }

    /**
     * Generates the given function.
     * @param func the function to generate
     * @param classWriter the class to generate the function for
     */
    private static void genFunc(Func func, ClassWriter classWriter) {
        MethodVisitor methodVisitor;
        methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, func.getName(), genSig(func.argLength()), null, null);
        methodVisitor.visitCode();
        for (Stmt s : func.getStmts()) {
            if (s instanceof ReturnStmt) {
                genReturnStmt((ReturnStmt) s, methodVisitor, func.getArgs());
            } else if (s instanceof IfStmt) {
                genIf((IfStmt) s, methodVisitor, func.getArgs());
            }
        }
        methodVisitor.visitMaxs(0,0);
        methodVisitor.visitEnd();
    }

    /**
     * Generates the given return statement.
     * @param s the return statement to generate
     * @param methodVisitor the method visitor to use
     * @param args the arguments of the function
     */
    private static void genReturnStmt(ReturnStmt s, MethodVisitor methodVisitor, Var[] args) {
        genExpr(s.getExpr(), methodVisitor, args);
        methodVisitor.visitInsn(IRETURN);
    }

    /**
     * Generates the given if statement.
     * @param s the if statement to generate
     * @param methodVisitor the method visitor to use
     * @param args the arguments of the function
     */
    private static void genIf(IfStmt s, MethodVisitor methodVisitor, Var[] args) {
        genExpr(s.getExpr(), methodVisitor, args);
        Label label1 = new Label();
        methodVisitor.visitJumpInsn(IFNE, label1);
        Label label2 = new Label();
        methodVisitor.visitLabel(label2);
        for (Stmt stmt : s.getElseStmts()) {
            if (stmt instanceof ReturnStmt) {
                genReturnStmt((ReturnStmt) stmt, methodVisitor, args);
            }
        }
        methodVisitor.visitLabel(label1);
        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        for (Stmt stmt : s.getStmts()) {
            if (stmt instanceof ReturnStmt) {
                genReturnStmt((ReturnStmt) stmt, methodVisitor, args);
            }
        }
    }

    /**
     * Generates the given function call.
     * @param fnCall the function call to generate
     * @param methodVisitor the method visitor to use
     * @param args the arguments of the function
     */
    private static void genFnCall(FnCall fnCall, MethodVisitor methodVisitor, Var[] args) {
        for (Expr arg : fnCall.getArgs()) {
            genExpr(arg, methodVisitor, args);
        }
        methodVisitor.visitMethodInsn(INVOKESTATIC, fnCall.getName(), fnCall.getName(), genSig(fnCall.argLength()), false);
    }

    /**
     * Generates the given expression.
     * @param expr the expression to generate
     * @param methodVisitor the method visitor to use
     * @param args the arguments of the function
     */
    private static void genExpr(Expr expr, MethodVisitor methodVisitor, Var[] args) {
        if (expr instanceof IntOp) {
            genExpr(((IntOp) expr).getLeft(), methodVisitor, args);
            genExpr(((IntOp) expr).getRight(), methodVisitor, args);
            switch (((IntOp) expr).getOp()) {
                case "+" -> methodVisitor.visitInsn(IADD);
                case "-" -> methodVisitor.visitInsn(ISUB);
                case "*" -> methodVisitor.visitInsn(IMUL);
                case "/" -> methodVisitor.visitInsn(IDIV);
            }
        } else if (expr instanceof Int) {
            methodVisitor.visitIntInsn(BIPUSH, ((Int) expr).getValue());
        } else if (expr instanceof Var) {
            methodVisitor.visitVarInsn(ILOAD, getIndex((Var) expr, args));
        } else if (expr instanceof IntCmp) {
            genExpr(((IntCmp) expr).getLeft(), methodVisitor, args);
            genExpr(((IntCmp) expr).getRight(), methodVisitor, args);
            Label label1 = new Label();
            Label label2 = new Label();
            switch (((IntCmp) expr).getOp()) {
                case "==" -> methodVisitor.visitJumpInsn(IF_ICMPEQ, label1);
                case "!=" -> methodVisitor.visitJumpInsn(IF_ICMPNE, label1);
            }
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitJumpInsn(GOTO, label2);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
        } else if (expr instanceof FnCall) {
            genFnCall((FnCall) expr, methodVisitor, args);
        }
    }

    /**
     * Gets the index of the given variable in the given array of variables.
     * @param v the variable to get the index of
     * @param args the array of variables to search
     * @return the index of the given variable in the given array of variables
     */
    private static int getIndex(Var v, Var[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].getName().equals((v).getName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Generates a signature for a function with the given number of arguments.
     * @param length the number of arguments of the function
     * @return a signature for a function with the given number of arguments
     */
    private static String genSig(int length) {
        return "(" +
                "I".repeat(Math.max(0, length)) +
                ")I";
    }

}