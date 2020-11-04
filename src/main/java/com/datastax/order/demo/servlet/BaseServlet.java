package com.datastax.order.demo.servlet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import com.datastax.order.demo.dao.OrderManagementDAO;
import com.datastax.order.demo.objects.Account;
import com.datastax.order.demo.queryobjects.ProductOrdersByUser;
import com.datastax.order.demo.queryobjects.ProductOrdersByVendor;

public class BaseServlet extends HttpServlet{
	
	OrderManagementDAO dao = new OrderManagementDAO(new String[]{"127.0.0.1"});
	
	public void writeGoogleChartsStart(StringBuilder builder){
				
		builder.append("<html>\n");
		builder.append("<head>\n");
		builder.append("<script type='text/javascript' src='https://www.google.com/jsapi'></script>\n");
		builder.append("<script type='text/javascript'>\n");
		builder.append("google.load('visualization', '1', {packages:['corechart', 'table']})\n");
		builder.append("google.setOnLoadCallback(drawChart)\n");		
	}

	public void writeGoogleChartsEnd(StringBuilder builder){
		builder.append("</script>\n");
	}
	
	public void writeAccount(StringBuilder builder, Account account, String div, int width, int height) {
		builder.append("<div id='" + div + "' style='width: "+ width +"px; height: " + height + "px;'>\n");
		builder.append("<p>Account balance : " + account.getBalance() + "</p>\n");
		builder.append("</div>\n");
	}

	protected String writeGoogleHighlyRecommendProductData(StringBuilder builder, Map<String, Integer> data) {
		String dataName = "data" + System.currentTimeMillis();
		
		builder.append("var " + dataName + " = google.visualization.arrayToDataTable([\n");
		builder.append("['Count', 'Product Name']");
		if (data.size() > 0){
			builder.append(",\n");
		}
		
		int count=0;
		for (String key : data.keySet()){
			
			builder.append("[" + data.get(key) + ",'" + key + "']");			
			if (count++ < data.size()-1){
				builder.append(",\n");
			}
		}
		
		builder.append("]);\n");
	        
	    return dataName;

	}

	public String writeGoogleDrawChartData(StringBuilder builder, Map<String, Integer> data, String[] headers){
		String dataName = "data" + System.currentTimeMillis();
		
		builder.append("var " + dataName + " = google.visualization.arrayToDataTable([\n");
		builder.append("['"+ headers[0] + "', '" + headers[1] + "']");
		if (data.size() > 0){
			builder.append(",\n");
		}
		
		int count=0;
		for (String key : data.keySet()){
			
			builder.append("['" + key + "', " + data.get(key) + "]");			
			if (count++ < data.size()-1){
				builder.append(",\n");
			}
		}
		
		builder.append("]);\n");
	        
	    return dataName;
	}
	public void startDrawChartFunction(StringBuilder builder){
		builder.append("\n");
		builder.append("function drawChart() {\n");
	}
	
	public void endDrawChartFunction(StringBuilder builder){
		builder.append("};\n");
	}
	
	public String writeGoogleProductOrderByUserData(StringBuilder builder, List<ProductOrdersByUser> data){
		String dataName = "data" + System.currentTimeMillis();
		
		builder.append("var " + dataName + " = google.visualization.arrayToDataTable([\n");
		builder.append("['User Id', 'OrderId', 'Order date', 'Product Id', 'Product Name', 'Vendor', 'Total_Price']");
		
		if (data.size() > 0){
			builder.append(",\n");
		}		
		int count=0;
		for (ProductOrdersByUser productOrdersByUser : data){
			
			builder.append("['" + productOrdersByUser.getUserId() + "', '" + productOrdersByUser.getOrderId() + "','"
					 + productOrdersByUser.getOrderDate() + "','"					
					+ productOrdersByUser.getProductId() + "','" + productOrdersByUser.getProductName() + "','"
					+ productOrdersByUser.getVendor().replaceAll("'", "%27") + "'," + productOrdersByUser.getTotalPrice() + "]");
			
			if (count++ < data.size()-1){
				builder.append(",\n");
			}
		}			
		builder.append("]);\n");
	        
	    return dataName;
	}
	
	public String writeGoogleProductOrdersVendorData(StringBuilder builder,
		List<ProductOrdersByVendor> data) {
		String dataName = "data" + System.currentTimeMillis();
		
		builder.append("var " + dataName + " = google.visualization.arrayToDataTable([\n");
		builder.append("['Vendor', 'OrderId', 'Product Id', 'Product Name','Total_Price']");
		
		if (data.size() > 0){
			builder.append(",\n");
		}		
		int count=0;
		for (ProductOrdersByVendor order : data){
			
			builder.append("['" + order.getVendor() + "', '" + order.getOrderId() + "','"					
					+ order.getProductId() + "','" + order.getProductName() + "',"
					+ order.getTotalPrice() + "]");
			
			if (count++ < data.size()-1){
				builder.append(",\n");
			}
		}		
		builder.append("]);\n");
	        
	    return dataName;
	}


	public String writeGoogleDrawChartDataInt(StringBuilder builder,
			Map<Integer, Integer> data, String[] headers) {
		String dataName = "data" + System.currentTimeMillis();
		
		builder.append("var " + dataName + " = google.visualization.arrayToDataTable([\n");
		builder.append("['"+ headers[0] + "', '" + headers[1] + "']");
		if (data.size() > 0){
			builder.append(",\n");
		}
		
		int count=0;
		for (Integer key : data.keySet()){
			
			builder.append("[" + key + ", " + data.get(key) + "]");			
			if (count++ < data.size()-1){
				builder.append(",\n");
			}
		}
		
		builder.append("]);\n");
	        
	    return dataName;
	}
	
	
	public String writeGoogleDrawChartOption(StringBuilder builder, String title, String vAxisTitle, String color){
		String optionsName = "options" + System.currentTimeMillis();
		
		builder.append("var " + optionsName + "= {\n");
		builder.append("title: '" + title + "',\n");
		builder.append("is3D: true,\n");
		builder.append("vAxis: {title: '" + vAxisTitle + "',  titleTextStyle: {color: '" + color + "'}}\n");
		builder.append("};\n");
		
		return optionsName;
	}

	public String writeGoogleDrawBarChart(StringBuilder builder, String div, String data, String options){
		
		String chartName = "chart" + System.currentTimeMillis();
	
		builder.append("var " + chartName + "= new google.visualization.BarChart(document.getElementById('" + div + "'));\n");
		builder.append(chartName + ".draw(" + data + "," + options + ");\n");
		
		return chartName;
	}
	

	public String writeGoogleDrawPieChart(StringBuilder builder, String div, String data, String options){
		
		String chartName = "chart" + System.currentTimeMillis();
	
		builder.append("var " + chartName + "= new google.visualization.PieChart(document.getElementById('" + div + "'));\n");
		builder.append(chartName + ".draw(" + data + "," + options + ");\n");
		
		return chartName;
	}

	public String writeGoogleDrawColumnChart(StringBuilder builder, String div, String data, String options){
		
		String chartName = "chart" + System.currentTimeMillis();
	
		builder.append("var " + chartName + "= new google.visualization.ColumnChart(document.getElementById('" + div + "'));\n");
		builder.append(chartName + ".draw(" + data + "," + options + ");\n");
		
		return chartName;
	}

	public String writeGoogleDrawTable(StringBuilder builder, String div, String data, String options){
		
		String chartName = "chart" + System.currentTimeMillis();
	
		builder.append("var " + chartName + "= new google.visualization.Table(document.getElementById('" + div + "'));\n");
		builder.append(chartName + ".draw(" + data + ",{showRowNumber: true});\n");
		return chartName;
	}
	
	public void writeGoogleChartsDivs(StringBuilder builder, List<String> divsList) {
		this.endHead(builder);
		for (String div : divsList){
			this.writeGoogleChartsDivs(builder, div, 900, 500);
		}
		this.endHTML(builder);
	}

	public void endHead(StringBuilder builder){
		builder.append("</head>\n");
		builder.append("<body>\n");
	}

	public void endHTML(StringBuilder builder){
		builder.append("</body>\n");
		builder.append("</html>\n");
	}
	
	public void writeGoogleChartsDivs(StringBuilder builder, String div, int width, int height) {
		builder.append("<div id='" + div + "' style='width: "+ width +"px; height: " + height + "px;'></div>\n");
	}
	
	public void writeGoogleChartsHandler(StringBuilder builder, String chart, String event, String dataName) {
	
		builder.append("google.visualization.events.addListener(" + chart + ", 'select', selectHandler);");

		builder.append("function selectHandler(e) {");
	
		builder.append("var item = chart.getSelection()[0];");
		builder.append("var selected = " + dataName + ".getFormattedValue(item.row, 0)");			
		builder.append("alert('A table row was selected ' + selected);");
		builder.append("}");
	}
		
	public void writeGoogleChartsHandlerCustom(StringBuilder builder, String chart, String event, String dataName, String code) {
		
		builder.append("google.visualization.events.addListener(" + chart + ", '" + event + "', selectHandler);");

		builder.append("function selectHandler(e) {");	
		builder.append(code);		
		builder.append("}");
	}
	
	public static void main(String args[]){
		
		//BaseServlet test = new BaseServlet();
		
		StringBuilder out = new StringBuilder();
				
//		Map<String, Integer> data = new LinkedHashMap<String, Integer>();
//		data.put("1", 32);
//		data.put("2", 37);
//		data.put("3", 35);
//		data.put("4", 36);
//		data.put("5", 38);
//		String headers[] = new String[2];
//		headers[0] = "hour";
//		headers[1] = "value";
//		
//		test.writeGoogleChartsStart(out);
//		String dataName = test.writeGoogleDrawChartData(out, data, headers);
//		String optionsName = test.writeGoogleDrawChartOption(out, "My Lovely Chart", "Hours", "green");
//		
//		test.writeGoogleDrawBarChart(out, "mydiv1", dataName, optionsName);		
//		test.writeGoogleDrawPieChart(out, "mydiv2", dataName, optionsName);
//		test.writeGoogleDrawColumnChart(out, "mydiv3", dataName, optionsName);	
//		
//		List<String> divsList = Arrays.asList(new String[]{"mydiv1", "mydiv2", "mydiv3"});
//		
//		test.writeGoogleChartsEnd(out);
//		test.writeGoogleChartsDivs(out, divsList);
		
//		System.out.println(out.toString());
		
		System.out.println(("Hello's there").replaceAll("'", "%27"));
	}

}