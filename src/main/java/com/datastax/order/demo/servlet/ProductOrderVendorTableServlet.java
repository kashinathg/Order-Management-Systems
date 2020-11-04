package com.datastax.order.demo.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.order.demo.queryobjects.ProductOrdersByVendor;

public class ProductOrderVendorTableServlet extends BaseServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String vendor = req.getParameter("vendor"); 
		List<ProductOrdersByVendor> data = dao.getProductOrdersByVendor(vendor);
		Map<String, Integer> vendorData = dao.getHighRecommendByVendor(vendor);
		
		StringBuilder out = new StringBuilder();
		writeGoogleChartsStart(out);
		startDrawChartFunction(out);
		
		
		String mostHighName = writeGoogleHighlyRecommendProductData(out, vendorData);				
		writeGoogleDrawTable(out, "mydiv1", mostHighName, null);			

		
		String dataName = writeGoogleProductOrdersVendorData(out, data);				
		writeGoogleDrawTable(out, "mydiv2", dataName, null);						
				
		endDrawChartFunction(out);
		writeGoogleChartsEnd(out);
		
		endHead(out);
		writeGoogleChartsDivs(out, "mydiv1", 500, 300);
		writeGoogleChartsDivs(out, "mydiv2", 1100, 600);
		endHTML(out);

				
		resp.getWriter().write(out.toString());

	}
}