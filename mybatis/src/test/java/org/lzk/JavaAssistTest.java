package org.lzk;

import org.apache.ibatis.javassist.*;
import org.junit.Test;

import java.io.IOException;

/**
 * https://www.w3cschool.cn/article/35230124.html
 *
 * http://www.javassist.org/tutorial/tutorial.html
 */
public class JavaAssistTest {
    @Test
    public void testJavaAssistClass() throws CannotCompileException, IOException {

        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = cp.makeClass("org.lzk.javassist.Hello");
        ctClass.writeFile("./target/test-classes/");
    }

    @Test
    public void testJavaAssistClassWithMethod() throws CannotCompileException, IOException {

        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = cp.makeClass("org.lzk.javassist.HelloWithMethod");

        //创建一个类名为"hello"，传递参数的顺序为(int,double)，没有返回值的类
        /*
        CtMethod（...）源代码：
        public CtMethod(CtClass returnType,//这个方法的返回值类型，
                        String mname, //（method name）方法的名字是什么
                        CtClass[] parameters, //方法传入的参数类型是什么
                        CtClass declaring //添加到哪个类中
                        ) {....}
         */

        CtMethod ctMethod = new CtMethod(CtClass.voidType, "hello", new CtClass[]{CtClass.intType, CtClass.doubleType}, ctClass);
        //设置hello方法的权限为public
        ctMethod.setModifiers(Modifier.PUBLIC);

        //向ctClass中添加这个方法
        ctClass.addMethod(ctMethod);
        ctClass.writeFile("./target/test-classes/");
    }
    @Test
    public void testJavaAssistClassWithMethodVariable() throws CannotCompileException, IOException {

        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = cp.makeClass("org.lzk.javassist.HelloWithMethodVariable");

        //创建一个类名为"hello"，传递参数的顺序为(int,double)，没有返回值的类
        /*
        CtMethod（...）源代码：
        public CtMethod(CtClass returnType,//这个方法的返回值类型，
                        String mname, //（method name）方法的名字是什么
                        CtClass[] parameters, //方法传入的参数类型是什么
                        CtClass declaring //添加到哪个类中
                        ) {....}
         */

        CtMethod ctMethod = new CtMethod(CtClass.voidType, "hello", new CtClass[]{CtClass.intType, CtClass.doubleType}, ctClass);
        //设置hello方法的权限为public
        ctMethod.setModifiers(Modifier.PUBLIC);

        //向ctClass中添加这个方法
        ctClass.addMethod(ctMethod);

        //添加一个int类型的，名字为value的变量
        CtField ctField = new CtField(CtClass.intType,"value",ctClass);
        ctField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(ctField);

        ctClass.writeFile("./target/test-classes/");
    }
    @Test
    public void testJavaAssistClassWithMethodVariableGetSet() throws CannotCompileException, IOException {

        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = cp.makeClass("org.lzk.javassist.MethodVariableGetSet");

        //添加一个hello1的方法
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "hello", new CtClass[]{CtClass.intType, CtClass.doubleType}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctClass.addMethod(ctMethod);

        //添加一个int类型的，名字为value的变量
        CtField ctField = new CtField(CtClass.intType,"value",ctClass);
        ctField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(ctField);

        //为value变量添加set方法
        CtMethod setValue = new CtMethod(CtClass.voidType, "setValue", new CtClass[]{CtClass.intType}, ctClass);
        setValue.setModifiers(Modifier.PUBLIC);
        ctClass.addMethod(setValue);

        //为value变量添加get方法
        CtMethod getValue = new CtMethod(CtClass.intType, "getValue", new CtClass[]{}, ctClass);
        getValue.setModifiers(Modifier.PUBLIC);
        ctClass.addMethod(getValue);

        ctClass.writeFile("./target/test-classes/");
    }
    @Test
    public void testJavaAssistClassWithMethodVariableGetSetStatement() throws CannotCompileException, IOException {

        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = cp.makeClass("org.lzk.javassist.MethodVariableGetSetStatement");

        //添加一个hello1的方法
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "hello", new CtClass[]{CtClass.intType, CtClass.doubleType}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctClass.addMethod(ctMethod);

        //添加一个int类型的，名字为value的变量
        CtField ctField = new CtField(CtClass.intType,"value",ctClass);
        ctField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(ctField);

        //为value变量添加set方法
        CtMethod setValue = new CtMethod(CtClass.voidType, "setValue", new CtClass[]{CtClass.intType}, ctClass);
        setValue.setModifiers(Modifier.PUBLIC);
        ctClass.addMethod(setValue);
        //设置方法体
        setValue.setBody("this.value = $1;");

        //为value变量添加get方法
        CtMethod getValue = new CtMethod(CtClass.intType, "getValue", new CtClass[]{}, ctClass);
        getValue.setModifiers(Modifier.PUBLIC);
        //设置方法体
        getValue.setBody("return this.value;");
        ctClass.addMethod(getValue);

        ctClass.writeFile("./target/test-classes/");
    }

    @Test
    public void testJavaAssistClassWithMethodPlus() throws CannotCompileException, IOException {

        /**
         * $0、$1、$2、 3 、 3、 3、…	this和方法参数（1-N是方法参数的顺序）
         * $args	方法参数数组，类型为Object[]
         * $$	所有方法参数，例如：m($$)相当于m($1,$2,…)
         * $cflow(…)	control flow 变量
         * $r	返回结果的类型，在强制转换表达式中使用。
         * $w	包装器类型，在强制转换表达式中使用。
         * $_	返回的结果值
         * $sig	类型为java.lang.Class的参数类型对象数组
         * $type	类型为java.lang.Class的返回值类型
         * $class	类型为java.lang.Class的正在修改的类
         */

        ClassPool cp = ClassPool.getDefault();
        CtClass ctClass = cp.makeClass("org.lzk.javassist.WithMethodPlus");

        //添加一个hello1的方法
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "hello", new CtClass[]{CtClass.intType, CtClass.doubleType}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctClass.addMethod(ctMethod);
        //添加一个hello1的方法
        CtMethod plus = new CtMethod(CtClass.doubleType, "plus", new CtClass[]{CtClass.intType, CtClass.doubleType}, ctClass);
        plus.setModifiers(Modifier.PUBLIC);
        ctClass.addMethod(plus);
        plus.setBody("return $1+$2;");

        plus.insertBefore("System.out.println(\"我在前面插入了：\"+$1);");
        plus.insertAfter("System.out.println(\"我在最后插入了：\"+$1);");

        //添加一个int类型的，名字为value的变量
        CtField ctField = new CtField(CtClass.intType,"value",ctClass);
        ctField.setModifiers(Modifier.PRIVATE);
        ctClass.addField(ctField);

        //为value变量添加set方法
        CtMethod setValue = new CtMethod(CtClass.voidType, "setValue", new CtClass[]{CtClass.intType}, ctClass);
        setValue.setModifiers(Modifier.PUBLIC);
        ctClass.addMethod(setValue);
        //设置方法体
        setValue.setBody("this.value = $1;");

        //为value变量添加get方法
        CtMethod getValue = new CtMethod(CtClass.intType, "getValue", new CtClass[]{}, ctClass);
        getValue.setModifiers(Modifier.PUBLIC);
        //设置方法体
        getValue.setBody("return this.value;");
        ctClass.addMethod(getValue);

        ctClass.writeFile("./target/test-classes/");
    }




}
