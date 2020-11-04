package com.datastax.order.demo.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datastax.order.demo.objects.Account;
import com.datastax.order.demo.queryobjects.ProductOrdersByUser;

public class ProductOrderUserTableServlet extends BaseServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String userId = req.getParameter("userid");
		List<ProductOrdersByUser> data = dao.getProductOrdersByUser(userId, 20);
		Account account = dao.getUserAccountBalance(userId);
		
		StringBuilder out = new StringBuilder();		
		writeGoogleChartsStart(out);				
		startDrawChartFunction(out);
		
		String dataName = writeGoogleProductOrderByUserData(out, data);				
		writeGoogleDrawTable(out, "mydiv2", dataName, null);
		
		endDrawChartFunction(out);
		writeGoogleChartsEnd(out);
		
		endHead(out);
		writeAccount(out, account, "mydiv1", 400, 20);
		
		writeGoogleChartsDivs(out, "mydiv2", 1100, 600);
		endHTML(out);
		
		resp.getWriter().write(out.toString());
	}
}