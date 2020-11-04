package com.datastax.order.demo.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.datastax.demo.utils.GoogleData;
import com.datastax.order.demo.dao.OrderManagementDAO;
import com.datastax.order.demo.queryobjects.ProductOrdersByDateTimeUnit;


@Path("/dashboard/")
public class DashBoardWebService {
		
	private OrderManagementDAO orderManagementDAO = new OrderManagementDAO(new String[]{"127.0.0.1"});

	@GET
	@Path("/get/bydatetime/{date}/{fromseconds}/{toseconds}")
	@Produces(MediaType.APPLICATION_JSON)
	public GoogleData getOrdersByDate (@PathParam("date") String date,
			@PathParam("fromseconds") int fromSeconds, @PathParam("toseconds") int toSeconds){
		
		List<ProductOrdersByDateTimeUnit> orders = orderManagementDAO.getProductOrdersBySecond(date, fromSeconds, toSeconds);
		
		//Create GoogleData
		GoogleData data = new GoogleData();
		
		return data;		
	}

	
}