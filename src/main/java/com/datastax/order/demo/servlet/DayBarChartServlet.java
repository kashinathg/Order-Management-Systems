package com.datastax.order.demo.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DayBarChartServlet extends BaseServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		Map<Integer, Integer> countByDay = dao.getCountByDay();
		
		StringBuilder out = new StringBuilder();
		Map<String, Integer> data = new LinkedHashMap<String, Integer>();
	
		String headers[] = new String[2];
		headers[0] = "Date";
		headers[1] = "value";
		
		writeGoogleChartsStart(out);
		String dataName = writeGoogleDrawChartDataInt(out, countByDay, headers);
		String optionsName = writeGoogleDrawChartOption(out, "Orders per date", "Date", "green");
		
		writeGoogleDrawColumnChart(out, "mydiv1", dataName, optionsName);				
		writeGoogleChartsEnd(out);
		
		endHead(out);
		writeGoogleChartsDivs(out, "mydiv1", 500, 300);
		endHTML(out);
				
		resp.getWriter().write(out.toString());
	}
}