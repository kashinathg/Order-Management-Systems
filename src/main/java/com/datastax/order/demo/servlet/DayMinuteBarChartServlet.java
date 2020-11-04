package com.datastax.order.demo.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DayMinuteBarChartServlet extends BaseServlet{

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String date = req.getParameter("date");
		String min = req.getParameter("min");
		String max = req.getParameter("max");
		
		Map<Integer, Integer> hourCountByDay = dao.getMinuteCountByDay(date, Integer.parseInt(min), Integer.parseInt(max));
		
		StringBuilder out = new StringBuilder();
		Map<String, Integer> data = new LinkedHashMap<String, Integer>();
	
		String headers[] = new String[2];
		headers[0] = "min";
		headers[1] = "value";
		
		writeGoogleChartsStart(out);				
		startDrawChartFunction(out);

		String dataName = writeGoogleDrawChartDataInt(out, hourCountByDay, headers);
		String optionsName = writeGoogleDrawChartOption(out, "Orders per Min", "Mins", "green");
		
		writeGoogleDrawColumnChart(out, "mydiv1", dataName, optionsName);				
			
		endDrawChartFunction(out);
		writeGoogleChartsEnd(out);
		
		endHead(out);
		writeGoogleChartsDivs(out, "mydiv1", 1000, 600);
		endHTML(out);
		
		resp.getWriter().write(out.toString());
	}
}