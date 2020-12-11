package com.dz.fulldo.classLoad;

/**
 * 《Java虚拟机规范》则是严格规定了有且只有六种情况必须立即对类进行“初始化”
 * 1）遇到new、getstatic、putstatic或invokestatic这四条字节码指令时，如果类型没有进行过初始化，则需要先触发其初始化阶段。能够生成这四条指令的典型Java代码场景有：·使用new关键字实例化对象的时候。·读取或设置一个类型的静态字段（被final修饰、已在编译期把结果放入常量池的静态字段除外）的时候。·调用一个类型的静态方法的时候；
 * 2）使用java.lang.reflect包的方法对类型进行反射调用的时候，如果类型没有进行过初始化，则需要先触发其初始化；
 * 3）当初始化类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化；
 * 4）当虚拟机启动时，用户需要指定一个要执行的主类（包含main()方法的那个类），虚拟机会先初始化这个主类；
 * 5）当使用JDK 7新加入的动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果为REF_getStatic、REF_putStatic、REF_invokeStatic、REF_newInvokeSpecial四种类型的方法句柄，并且这个方法句柄对应的类没有进行过初始化，则需要先触发其初始化；
 * 6）当一个接口中定义了JDK 8新加入的默认方法（被default关键字修饰的接口方法）时，如果有这个接口的实现类发生了初始化，那该接口要在其之前被初始化。
 */
public class Caller {

    static {
        System.out.println("Caller init!");
    }
    public static void main(String[] args) {
        initialzation();
    }

    /**
     * 只有父类被初始化
     */
    public static void notInitialzation() {
        System.out.println(Callee.value);
    }

    /**
     * final修饰字段、已在编译期把结果放入常量池
     */
    public static void notInitialzation2() {
        System.out.println(Callee.staticFieldFinal);
    }

    /**
     * 被初始化
     */
    public static void initialzation() {
        System.out.println(Callee.staticField);
    }
}
