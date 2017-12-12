package com.vzard.apiscanner.core;


import com.vzard.apiscanner.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class Scanner {


    static Logger logger = LoggerFactory.getLogger(Scanner.class);


    /**
     * 获得包内所有有注解的方法的方法名
     *
     * @param rootPackageName
     * @return
     */
    public static List<String> scanPackageMethodWithAnnotation(String rootPackageName) {
        List<String> classNames = new ArrayList<>();
        List<List<Method>> methodes = new ArrayList<>();
        try {
            classNames = scanPackageClass(rootPackageName);
            ListIterator<String> iterator = classNames.listIterator();
            while (iterator.hasNext()) {
                List<Method> currentClassMethods = scanClassMethod(iterator.next())
                        .stream()
                        .filter(t -> (t.getAnnotation(RequestMapping.class) != null))
                        .collect(Collectors.toList());


                methodes.add(currentClassMethods);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodes.stream().flatMap(t -> t.stream()).map(t -> StringUtil.ReformatMethodName(t)).collect(Collectors.toList());

    }


    /**
     * 扫描获取方法上的http方法
     * @param rootPackageName
     * @return
     */
    public static HashMap<String, String> scanAnnotationAndGetHttpMethod(String rootPackageName){

        List<String> classNameList = scanPackageClass(rootPackageName);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Iterator iterator = classNameList.listIterator();
        HashMap<String,String> methodAnnotationHashMap = new HashMap<>();
        try {
            while (iterator.hasNext()){
                String className = iterator.next().toString();
                Class<?> clazz = loader.loadClass(className);
                Method[] methods = clazz.getDeclaredMethods();
                for (int i = 0; i < methods.length ; i++) {
                    RequestMapping methodAnnotation = methods[i].getAnnotation(RequestMapping.class);
                    String value = "";
                    if (methodAnnotation != null) {
                        if (methodAnnotation.method().length != 0) {
                            value = castRequestMethodToString(methodAnnotation.method());
                        }
                        if (value != "") {
                            methodAnnotationHashMap.put(StringUtil.ReformatMethodName(methods[i]), value);
                        }
                    }
                }
            }


        }catch (ClassNotFoundException e){

        }

        return  methodAnnotationHashMap;


    }


    /**
     * 扫描指定类，获取方法上的所有RequestMapping注解的value
     * @param className
     * @return
     */
    public static HashMap<String, String> scanAnnotationOnMethod(String className){


        //List<String> classNameList = scanPackageClass(rootPackageName);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // Iterator iterator = classNameList.listIterator();
        HashMap<String,String> methodAnnotationHashMap = new HashMap<>();
        try {
            //while (iterator.hasNext()){
            //    String className = iterator.next().toString();
            Class<?> clazz = loader.loadClass(className);
            Method[] methods = clazz.getDeclaredMethods();
            for (int i = 0; i < methods.length ; i++) {
                RequestMapping methodAnnotation = methods[i].getAnnotation(RequestMapping.class);

                String value = "";
                if (null != methodAnnotation) {
                    //logger.info(methodAnnotation.toString());
                    if (methodAnnotation.value().length != 0) {
                        value = castStringArrayToString(methodAnnotation.value());
                    } else if (methodAnnotation.path().length != 0) {
                        value = castStringArrayToString(methodAnnotation.path());
                    }
                    if (value != "" && null != value) {
                        methodAnnotationHashMap.put(StringUtil.ReformatMethodName(methods[i]), value);
                    }
                }
            }

        }catch (ClassNotFoundException e){

        }

        return  methodAnnotationHashMap;
    }


    /**
     * 扫描获取类上的所有RequestMapping注解的value
     * @param rootPackageName
     * @return
     */
    public static HashMap<String, String> scanPackageAnnotationOnClass(String rootPackageName){

        List<String> classNameList = scanPackageClass(rootPackageName);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Iterator iterator = classNameList.listIterator();
        HashMap<String,String> classAnnotationHashMap = new HashMap<>();
        try {

            while (iterator.hasNext()) {
                String className = iterator.next().toString();
                Class<?> clazz = loader.loadClass(className);
                RequestMapping clazzAnnotation = clazz.getAnnotation(RequestMapping.class);
                String annotationValue = "";
                if (null != clazzAnnotation) {
                    if (clazzAnnotation.value().length != 0) {
                        annotationValue = castStringArrayToString(clazzAnnotation.value());
                    } else if (clazzAnnotation.path().length != 0) {
                        annotationValue = castStringArrayToString(clazzAnnotation.path());
                    }
                    if (annotationValue != "" && null != annotationValue) {
                        classAnnotationHashMap.put(className, annotationValue);
                    }
                }

            }
        }catch (ClassNotFoundException e){

        }


        return classAnnotationHashMap;
    }


    /**
     * 扫描类里面所有的方法
     * @param className
     * @return
     */
    public static List<String> scanClassMethodName(String className){
        List<String> methodNames = new ArrayList<String>();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = loader.loadClass(className);
            Method[] methods = clazz.getDeclaredMethods();

            for (int i = 0; i < methods.length ; i++) {
                methodNames.add(StringUtil.ReformatMethodName(methods[i]));
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return methodNames;
    }

    /**
     * 扫描获取类中的方法
     * @param className
     * @return
     */
    public static List<Method> scanClassMethod(String className){

        List<Method> methodsList = new ArrayList<>();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> clazz = loader.loadClass(className);
            Method[] methods = clazz.getDeclaredMethods();

            for (int i = 0; i < methods.length ; i++) {
                methodsList.add(methods[i]);
            }

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return methodsList;
    }

    /**
     * 扫描获取指定包里的方法
     * @param rootPackageName
     * @return
     */
    public static List<Method> scanPackageMethod(String rootPackageName){
        List<String> classNames = new ArrayList<>();
        List<List<Method>> methodes = new ArrayList<>();
        try {
            classNames = scanPackageClass(rootPackageName);
            ListIterator<String> iterator = classNames.listIterator();
            while (iterator.hasNext()){
                List<Method> currentClassMethods = scanClassMethod(iterator.next());
                methodes.add(currentClassMethods);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return methodes.stream().flatMap(t->t.stream()).collect(Collectors.toList());
    }


    /**
     * 扫描包里面的所有方法
     * @param rootPackageName
     * @return
     */
    public static List<String> scanPackageMethodName(String rootPackageName){
        List<String> classNames = new ArrayList<>();
        List<List<String>> methodNames = new ArrayList<>();

        try {
            classNames = scanPackageClass(rootPackageName);
            ListIterator<String> iterator = classNames.listIterator();
            while (iterator.hasNext()){
                List<String> currentClassMethodsNames = scanClassMethodName(iterator.next());
                methodNames.add(currentClassMethodsNames);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return methodNames.stream().flatMap(t->t.stream()).collect(Collectors.toList());

    }

    /**
     * 扫描包下面所有类
     * @param rootPackageName
     * @return
     */
    public static List<String> scanPackageClass(String rootPackageName){

        List<String> classNames = new ArrayList<String>();
        try{
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL url = loader.getResource(rootPackageName.replace(".","/"));

            String protocol = url.getProtocol();
            if ("file".equals(protocol)){
                File[] files = new File(url.toURI()).listFiles();
                for (File f:files){
                    scanPackageClassNameInFile(rootPackageName,f,classNames);
                }
            }else if ("jar".equals(protocol)){
                JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                Scanner.scanPackageClassNameInJar(jarFile,rootPackageName,classNames);
            }

        }catch (URISyntaxException e){

        }catch (IOException e){

        }

        return classNames;

    }



    /**
     * 扫描文件夹下的所有class文件
     * @param rootPackageName
     * @param rootFile
     * @param classNames
     */
    private static void scanPackageClassNameInFile(String rootPackageName, File rootFile, List<String> classNames) {

        String absFileName = rootPackageName + "." + rootFile.getName();
        if (rootFile.isFile() && absFileName.endsWith(".class")) {
            classNames.add(absFileName.substring(0, absFileName.length() - 6));

        } else if (rootFile.isDirectory()) {
            String tmPackageName = rootPackageName + "." + rootFile.getName();
            for (File f : rootFile.listFiles()) {
                scanPackageClassNameInFile(tmPackageName, f, classNames);
            }
        }
    }

    /**
     * 扫描Jar里面的类
     * @param jar
     * @param packageDirName
     * @param classNames
     */
    private static void scanPackageClassNameInJar(JarFile jar, String packageDirName, List<String> classNames){
        Enumeration<JarEntry> entries = jar.entries();
        while(entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
            String name = entry.getName().replace("/",".");
            if (name.startsWith(packageDirName)&&name.endsWith(".class")){
                classNames.add(name.substring(0,name.length()-6));
            }
        }

    }


    private static void scanPackageClassInJar(JarFile jar, String packageDirName, List<Class<?>> classes){
        Enumeration<JarEntry> entries = jar.entries();
        while(entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
            String name = entry.getName().replace("/",".");
            if (name.startsWith(packageDirName)&&name.endsWith(".class")){
                classes.add(entry.getClass());
            }
        }

    }

    private static String castStringArrayToString(String[] strings){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <strings.length ; i++) {
            stringBuilder.append(strings[i]);
        }
        return stringBuilder.toString();
    }

    private static String castRequestMethodToString(RequestMethod[] requestMethods){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < requestMethods.length  ; i++) {
            stringBuilder.append(requestMethods[i].name());
        }

        return stringBuilder.toString();
    }





}
