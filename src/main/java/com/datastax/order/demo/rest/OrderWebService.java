package com.datastax.order.demo.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.mortbay.log.Log;

import com.datastax.order.demo.dao.OrderManagementDAO;
import com.datastax.order.demo.queryobjects.ProductOrdersByDate;
import com.datastax.order.demo.queryobjects.ProductOrdersByDateTimeUnit;
import com.datastax.order.demo.queryobjects.ProductOrdersByUser;
import com.datastax.order.demo.queryobjects.ProductOrdersByVendor;


@Path("/orders/")
public class OrderWebService {
	
	
	private OrderManagementDAO orderManagementDAO = new OrderManagementDAO(new String[]{"127.0.0.1"});

	@GET
	@Path("/get/users/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductOrdersByUser> getUsersOrdersInJSON(@PathParam("userId") String userId) {
  
		List<ProductOrdersByUser> orders = orderManagementDAO.getProductOrdersByUser(userId, 20);		
		return orders;
	}
	
	@GET
	@Path("/get/bydate/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductOrdersByDate> getOrdersByDate (@PathParam("date") String date){
		
		List<ProductOrdersByDate> orders = orderManagementDAO.getProductOrdersByDate(date);		
		return orders;		
	}
	
	@GET
	@Path("/get/bydatetime/{date}/{fromseconds}/{toseconds}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductOrdersByDateTimeUnit> getOrdersByDate (@PathParam("date") String date,
			@PathParam("fromseconds") int fromSeconds, @PathParam("toseconds") int toSeconds){
		
		List<ProductOrdersByDateTimeUnit> orders = orderManagementDAO.getProductOrdersBySecond(date, fromSeconds, toSeconds);		
		return orders;		
	}

	@GET
	@Path("/get/byvendor/{vendor}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductOrdersByVendor> getOrdersByVendor (@PathParam("vendor") String vendor){
		
		List<ProductOrdersByVendor> orders = this.orderManagementDAO.getProductOrdersByVendor(vendor);
		return orders;
	}


	@GET
	@Path("/get/byvendorarray/{vendor}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductOrdersByVendor[] getOrdersByVendorArray (@PathParam("vendor") String vendor){
		
		List<ProductOrdersByVendor> orders = this.orderManagementDAO.getProductOrdersByVendor(vendor);
		return orders.toArray(new ProductOrdersByVendor[0]);
	}
}