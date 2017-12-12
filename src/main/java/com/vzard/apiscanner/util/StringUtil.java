package com.vzard.apiscanner.util;


import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 格式化方法名称为 getXxxYyyy(type,type)
     * @param method
     * @return
     */
    public static String ReformatMethodName(Method method){

        StringBuilder stringBuilder =  new StringBuilder();
        stringBuilder.append(method.getName()+"(");
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length ; i++) {
            stringBuilder.append(paramTypes[i].getCanonicalName()+",");
        }
        //去掉参数列表最后一个“,”
        if (paramTypes.length >= 1) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    /**
     * 从Url中解析出参数列表
     *
     * @param url
     * @return
     */
    public static List<String> getUrlParamList(String url){
        List<String> params = new ArrayList<>();
        //匹配“{xxx}”的正则
        Pattern brace = Pattern.compile("\\{[^}]*\\}");
        Matcher matcher = brace.matcher(url);
        while (matcher.find()){
            String param = matcher.group();
            params.add(param);
        }
        return params;
    }

    /**
     * 转换字符数组到一个字符串
     *
     * @param strings
     * @return
     */
    public static String castStringArrayToString(String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            stringBuilder.append(strings[i]);
        }
        return stringBuilder.toString();
    }

    /**
     * 转换RequestMrthod数组到一个字符串
     *
     * @param requestMethods
     * @return
     */
    public static String castRequestMethodToString(RequestMethod[] requestMethods) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < requestMethods.length; i++) {
            stringBuilder.append(requestMethods[i].name());
        }

        return stringBuilder.toString();
    }



}
