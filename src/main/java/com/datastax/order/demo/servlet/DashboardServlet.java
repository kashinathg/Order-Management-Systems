package com.datastax.order.demo.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DashboardServlet extends BaseServlet{

	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		String date = req.getParameter("date");
		
		Map<Integer, Integer> hourCountByDay = dao.getHourCountByDay(date);
		
		StringBuilder out = new StringBuilder();
		Map<String, Integer> data = new LinkedHashMap<String, Integer>();
	
		String headers[] = new String[2];
		headers[0] = "hour";
		headers[1] = "value";
		
		writeGoogleChartsStart(out);				
		startDrawChartFunction(out);

		String dataName = writeGoogleDrawChartDataInt(out, hourCountByDay, headers);
		String optionsName = writeGoogleDrawChartOption(out, "Orders per hour", "Hours", "green");
		
		writeGoogleDrawColumnChart(out, "mydiv1", dataName, optionsName);				
			
		endDrawChartFunction(out);
		writeGoogleChartsEnd(out);
		
		endHead(out);
		writeGoogleChartsDivs(out, "mydiv1", 500, 300);
		endHTML(out);

				
		resp.getWriter().write(out.toString());	
	}
	
}