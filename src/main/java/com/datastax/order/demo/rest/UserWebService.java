package com.datastax.order.demo.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.datastax.demo.utils.Col;
import com.datastax.demo.utils.GoogleData;
import com.datastax.order.demo.dao.OrderManagementDAO;
import com.datastax.order.demo.queryobjects.ProductOrdersByDate;
import com.datastax.order.demo.queryobjects.ProductOrdersByUser;


@Path("/users/")
public class UserWebService {
	
	
	private OrderManagementDAO orderManagementDAO = new OrderManagementDAO(new String[]{"127.0.0.1"});

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public GoogleData getUsersOrdersInJSON(@PathParam("userId") String userId) {
  
		List<ProductOrdersByUser> orders = orderManagementDAO.getProductOrdersByUser(userId, 20);		
		
		//Create GoogleData
		GoogleData data = new GoogleData();
		
		data.cols.add(new Col("poductName", "Product Name", "string"));
		data.cols.add(new Col("quantity", "Quantity", "number"));
		data.cols.add(new Col("number", "Total Price", "number"));
		data.cols.add(new Col("", "", ""));
		
		
		return data;		
	}
	
	@GET
	@Path("/bydate/{date}")
	@Produces(MediaType.APPLICATION_JSON)
	public GoogleData getOrdersByDate (@PathParam("date") String date){
		
		List<ProductOrdersByDate> orders = orderManagementDAO.getProductOrdersByDate(date);
		
		//Create GoogleData
		GoogleData data = new GoogleData();
		
		return data;			
	}
}