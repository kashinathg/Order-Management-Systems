package com.datastax.order.demo.loaders;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.datastax.demo.utils.FileUtils;
import com.datastax.order.demo.objects.Order;

public class LoadOrder {
	//Order example - O1, U4860, P335, 1, 2020-08-09 00:01:01
	private static final String COMMA = ",";
	private static final Logger LOG = Logger.getLogger(LoadOrder.class);
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public static Map<String, Order> processOrderFile(String filename){
		Map<String, Order> orders = new LinkedHashMap<String, Order>();
	
		List<String> readFileIntoList = FileUtils.readFileIntoList(filename);
		
		Order order;
		
		for (String row : readFileIntoList){
			
			String[] items = row.split(COMMA);
			
			order = new Order();
			order.setOrderId(items[0].trim());
			order.setUserId(items[1].trim());
			order.setProductId(items[2].trim());
			order.setQuantity(Integer.parseInt(items[3].trim()));
			
			try {
				order.setOrderDate(dateFormat.parse(items[4].trim()));
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}
	
			orders.put(order.getOrderId(), order);
		}
		LOG.info("Loaded : " + filename); 
		
		return orders;
	}
	
	
	public static Map<String, Order> processOrderFileStreaming(String filename){
		Map<String, Order> orders = new HashMap<String, Order>();
	
		List<String> readFileIntoList = FileUtils.readFileIntoList(filename);	
		Order order;
		
		for (String row : readFileIntoList){
			
			String[] items = row.split(COMMA);
			
			order = new Order();
			order.setOrderId(items[0].trim());
			order.setUserId(items[1].trim());
			order.setProductId(items[2].trim());
			order.setQuantity(Integer.parseInt(items[3].trim()));
			
			try {
				order.setOrderDate(dateFormat.parse(items[4].trim()));
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}
	
			orders.put(order.getOrderId(), order);
		}
		LOG.info("Loaded : " + filename); 
		
		return orders;
	}
	
 
}