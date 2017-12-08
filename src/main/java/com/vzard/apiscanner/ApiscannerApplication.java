package com.vzard.apiscanner;

import com.vzard.apiscanner.core.ApiMapper;
import com.vzard.apiscanner.core.Scanner;

import java.util.List;
import java.util.ListIterator;

//@SpringBootApplication
public class ApiscannerApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ApiscannerApplication.class, args);

		List<String> methodNames = Scanner.scanPackageMethodName("com.vzard.apiscanner.controller");

		ListIterator<String> iterator = methodNames.listIterator();
		while (iterator.hasNext()){
			String currentMethodName = iterator.next();
//			System.out.println(ApiMapper.getHttpMothodMap(currentMethodName));

			System.out.println(currentMethodName+" > "
			+ ApiMapper.getHttpMothodMap("com.vzard.apiscanner.controller").get(currentMethodName)+"::"
			+ ApiMapper.getUrlMap("com.vzard.apiscanner.controller").get(currentMethodName));


		}




	}
}
