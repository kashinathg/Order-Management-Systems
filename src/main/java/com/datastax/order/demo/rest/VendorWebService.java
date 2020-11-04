package com.datastax.order.demo.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.datastax.demo.utils.GoogleData;
import com.datastax.order.demo.dao.OrderManagementDAO;
import com.datastax.order.demo.queryobjects.ProductOrdersByVendor;


@Path("/vendor/")
public class VendorWebService {
	
	
	private OrderManagementDAO orderManagementDAO = new OrderManagementDAO(new String[]{"127.0.0.1"});

	@GET
	@Path("/get/byvendor/{vendor}")
	@Produces(MediaType.APPLICATION_JSON)
	public GoogleData getOrdersByVendor (@PathParam("vendor") String vendor){
		
		List<ProductOrdersByVendor> orders = this.orderManagementDAO.getProductOrdersByVendor(vendor);
		//Create GoogleData
		GoogleData data = new GoogleData();
		
		return data;	
	}


	@GET
	@Path("/get/byvendorarray/{vendor}")
	@Produces(MediaType.APPLICATION_JSON)
	public GoogleData getOrdersByVendorArray (@PathParam("vendor") String vendor){
		
		List<ProductOrdersByVendor> orders = this.orderManagementDAO.getProductOrdersByVendor(vendor);
		//Create GoogleData
		GoogleData data = new GoogleData();
		
		return data;
	}
}