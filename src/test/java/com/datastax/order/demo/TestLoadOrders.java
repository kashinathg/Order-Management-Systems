package com.datastax.order.demo;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.datastax.order.demo.loaders.LoadOrder;
import com.datastax.order.demo.objects.Order;

public class TestLoadOrders {

	@Test
	public void testOrders(){
		
		Map<String, Order> orderList = LoadOrder.processOrderFile("order_list_final_tiny.csv");
		
		Assert.assertEquals(10000, orderList.size());
	}
}
