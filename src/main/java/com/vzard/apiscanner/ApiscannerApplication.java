package com.vzard.apiscanner;

import com.bybutter.gateway.controller.SearchController;
import com.vzard.apiscanner.core.ApiMapper;
import com.vzard.apiscanner.core.Scanner;
import com.vzard.apiscanner.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

//@SpringBootApplication
public class ApiscannerApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ApiscannerApplication.class, args);

		List<String> methodNames = Scanner.scanClassMethodName("com.bybutter.gateway.controller.AccountController");

		ListIterator<String> iterator = methodNames.listIterator();
		List<String> methodDetail = new ArrayList<>();
		while (iterator.hasNext()) {
			String currentMethodName = iterator.next();
//			System.out.println(ApiMapper.getHttpMothodMap(currentMethodName));

			System.out.println(currentMethodName);


		}

		System.out.println(methodNames.size());

	}
}
