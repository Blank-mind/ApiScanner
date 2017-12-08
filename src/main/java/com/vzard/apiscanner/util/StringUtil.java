package com.vzard.apiscanner.util;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 格式化方法名称为 xxxx(type,type)
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



}
