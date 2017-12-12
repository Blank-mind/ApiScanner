package com.vzard.apiscanner.http;


import com.vzard.apiscanner.core.ApiMapper;
import com.vzard.apiscanner.core.Scanner;
import com.vzard.apiscanner.util.StringUtil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.List;
import java.util.ListIterator;

public class HttpLauncher {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void sendToPackage(String rootPackageName){
        StringBuilder URL = new StringBuilder("http://localhost:8088");
        List<String> methodNames = Scanner.scanPackageMethodWithAnnotation(rootPackageName);
        ListIterator<String> iterator = methodNames.listIterator();
        while (iterator.hasNext()){
            String currentMethodName = "";
            URL = URL.append(ApiMapper.getUrlMap(rootPackageName).get(currentMethodName));
            String httpMethod = ApiMapper.getHttpMothodMap(rootPackageName).get(currentMethodName);
            if (httpMethod.equals("GET")){
                String url = URL.toString();
                if (StringUtil.getUrlParamList(url) != null){

                    System.out.println("todo...");
                }

                HttpGet httpGet = new HttpGet(url);


            }
        }











    }









}
