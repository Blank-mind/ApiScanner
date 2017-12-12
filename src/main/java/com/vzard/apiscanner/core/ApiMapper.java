package com.vzard.apiscanner.core;


import com.vzard.apiscanner.util.StringUtil;

import java.lang.reflect.Method;
import java.util.*;

public class ApiMapper {


    /**
     * 获取method-url Map
     *
     * @param rootPackageName
     * @return
     */
    public static HashMap<String, String> getUrlMap(String rootPackageName) {

        HashMap<String, String> urlMap = new HashMap<>();

        // List<String> classNameList = Scanner.scanPackageClass(rootPackageName);
        HashMap<String, String> classUrl = Scanner.scanPackageAnnotationOnClass(rootPackageName);
        //Map<String,String> methodUrl = ReflectUtil.scanAnnotationOnMethod(rootPackageName);
        //Map<String,String> httpMethod = ReflectUtil.scanPackageAnnotationAndGetHttpMethod(rootPackageName);
        Iterator iterator = classUrl.entrySet().iterator();
        while (iterator.hasNext()) {
            String urlOnClass = "";
            HashMap.Entry currentClassNameEntry = (HashMap.Entry) iterator.next();
            String currentClassName = currentClassNameEntry.getKey().toString();
            urlOnClass = classUrl.get(currentClassName);//添加当前类的注解到url
            if (urlOnClass != null && urlOnClass.endsWith("/")) {
                urlOnClass = new StringBuilder(urlOnClass).deleteCharAt(urlOnClass.length() - 1).toString();
            }
            // List<String> methodNameList = Scanner.scanClassMethodName(currentClassName);
            HashMap<String, String> methodUrl = Scanner.scanAnnotationOnMethod(currentClassName);

            Iterator methodNameIterator = methodUrl.entrySet().iterator();
            while (methodNameIterator.hasNext()) {
                String urlOnMrthod = "";
                HashMap.Entry currentMethodEntry = (HashMap.Entry) methodNameIterator.next();
                String currentMethod = currentMethodEntry.getKey().toString();
                urlOnMrthod = methodUrl.get(currentMethod);
                if (urlOnMrthod != null && urlOnMrthod.startsWith("/")) {
                    urlOnMrthod = new StringBuilder(urlOnMrthod).deleteCharAt(0).toString();
                }
                urlMap.put(currentMethod, new StringBuilder(urlOnClass + "/" + urlOnMrthod).toString());
            }

        }

        return urlMap;

    }


    /**
     * 获取url-httpMethod Map
     *
     * @param rootPackageName
     * @return
     */
    public static HashMap<String, String> getHttpMothodMap(String rootPackageName) {
        return Scanner.scanAnnotationAndGetHttpMethod(rootPackageName);
    }

    /**
     * 根据方法名获取参数类型数组
     * @param rootPackage
     * @return
     */
    public static Map<String,Class<?>[]> getMethodParamMap(String rootPackage){
        Map<String,Class<?>[]> methodParamsMap = new HashMap<>();
        List<Method> methodNames = Scanner.scanPackageMethod(rootPackage);
        ListIterator<Method> iterator = methodNames.listIterator();
        while (iterator.hasNext()){
            Method currentMethod = iterator.next();
            methodParamsMap.put(StringUtil.ReformatMethodName(currentMethod),currentMethod.getParameterTypes());
        }
        return methodParamsMap;
    }






}
