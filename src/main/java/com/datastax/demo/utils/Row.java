package com.datastax.demo.utils;

import java.util.ArrayList;
import java.util.List;

public class Row{
	public List<RowCol> c;			
	
	public Row(){
		c = new ArrayList<RowCol>();
	}
	
	public void add(RowCol rowCol){
		c.add(rowCol);
	}
}
