package com.datastax.order.demo;

public class MovingAverageObject {

	private String desc;
	private String timeUnit;
	private int size;
	private double value;
	
	public MovingAverageObject(String desc, String timeUnit, int size,
			double value) {
		super();
		this.desc = desc;
		this.timeUnit = timeUnit;
		this.size = size;
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public int getSize() {
		return size;
	}
	public double getValue() {
		return value;
	}
}