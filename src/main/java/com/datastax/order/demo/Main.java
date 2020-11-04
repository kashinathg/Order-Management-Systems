package com.datastax.order.demo;

import com.datastax.demo.utils.PropertyHelper;


public class Main{

	public static void main(String[] args){
		
		String orderFile = PropertyHelper.getProperty("orderFile", "order_list_final.csv");
		String batchSizeStr = PropertyHelper.getProperty("batchSize", "3000");
		String contactPointsStr = PropertyHelper.getProperty("contactPoints", "127.0.0.1");

		StreamProcessor processor = new StreamOrderProcessorImpl(orderFile, contactPointsStr.split(","), Integer.parseInt(batchSizeStr));
		processor.startStreaming();
	}
}