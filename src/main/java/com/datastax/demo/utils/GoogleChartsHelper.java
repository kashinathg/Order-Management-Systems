package com.datastax.demo.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class GoogleChartsHelper {
	
	public static void main(String args[]){
		
		GoogleData d = new GoogleData();
		d.cols.add(new Col("id", "heading1", "string"));
		d.cols.add(new Col("id", "heading2", "number"));
		
		Row r = new Row();
		r.add(new RowCol("Mursa", null));
		r.add(new RowCol("2", null));
		d.rows.add(r);
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			System.out.println(mapper.defaultPrettyPrintingWriter().writeValueAsString(d));
			
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}