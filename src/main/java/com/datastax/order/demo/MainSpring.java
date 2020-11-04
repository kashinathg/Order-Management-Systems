package com.datastax.order.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainSpring {

	public static void main(String[] args){
		
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
		
		StreamProcessor processor = (StreamProcessor) context.getBean("streamProcessor");
		processor.startStreaming();
	}
}