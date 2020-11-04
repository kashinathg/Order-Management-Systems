package com.datastax.order.demo.queryobjects;

import java.util.Date;

public class ProductOrderUsersByDateTimeUnitRange {

	private String userId;
	private Date date;
	private String orderId;
	private String productId;
	private Date orderDate;
	private int timeUnit;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public int getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(int timeUnit) {
		this.timeUnit = timeUnit;
	}
	@Override
	public String toString() {
		return "ProductOrderUsersByDateTimeUnitRange [userId=" + userId
				+ ", date=" + date + ", orderId=" + orderId + ", productId="
				+ productId + ", orderDate=" + orderDate + ", timeUnit="
				+ timeUnit + "]";
	}	
}