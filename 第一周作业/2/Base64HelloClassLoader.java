package jvm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Base64;


public class Base64HelloClassLoader extends ClassLoader {

    private static String inFile = "E:\\computer study soft\\eclipse-java-2019-06-R-win32-x86_64\\eclipse\\eclipse-workplace\\Classloader\\src\\jvm\\Hello.xlass";
    private static String outFile = "E:\\computer study soft\\eclipse-java-2019-06-R-win32-x86_64\\eclipse\\eclipse-workplace\\Classloader\\src\\jvm\\Hello.class";

    public static void main(String[] args) {
        readFileByBytes();
        try {
//            new Base64HelloClassLoader().findClass("com.demo.week1.work.Hello").newInstance();
//            new Base64HelloClassLoader().findClass("Hello").newInstance();

            Class<?> hello = new Base64HelloClassLoader().loadClass("Hello");
            for (Method m : hello.getDeclaredMethods()) {
                System.out.println(hello.getSimpleName() + "." + m.getName());
            }
            Object instance = hello.newInstance();
            Method method = hello.getMethod("hello");
            method.invoke(instance);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void readFileByBytes(){
        File file = new File(inFile);
        String fileName = file.getName();
        System.out.println(fileName);
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            for(int i=0;i<bytes.length;i++){
                bytes[i] = (byte)(255-bytes[i]);
            }
            OutputStream out = new FileOutputStream(outFile);
            out.write(bytes);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String helloBase64 = "NQFFQf///8v/4/X/+f/x9v/w/+/3/+71/+3/7Pj/6/j/6v7/+cOWkZaLwf7//NfWqf7/+7yQm5r+//CzlpGasYqSnZqNq56dk5r+//qXmpOTkP7/9ayQio2cmrmWk5r+//W3mpOTkNGVnome8//4//f4/+nz/+j/5/7/7Leak5OQ09+ck56MjLOQnpuajd74/+bz/+X/5P7/+reak5OQ/v/vlZ6JntCTnpGY0LCdlZqci/7/75WeiZ7Qk56RmNCshoyLmpL+//yQiov+/+qzlZ6JntCWkNCvjZaRi6yLjZqeksT+/+yVnome0JaQ0K+NlpGLrIuNmp6S/v/4j42WkYuTkf7/6tezlZ6JntCTnpGY0KyLjZaRmMTWqf/e//r/+f///////f/+//j/9//+//b////i//7//v////rVSP/+Tv////7/9f////n//v////7//v/0//f//v/2////2v/9//7////2Tf/97fxJ//tO/////v/1////9f/9////+//3//r//v/z/////f/y";
        byte[] bytes = decode(helloBase64);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
    /**
     *
     * @param base64
     * @return
     */
    private byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
