package com.datastax.order.demo.queryobjects;

import java.util.Date;

/** 
 * Query Object for <pre>
 * create table products_orders_by_date_hour(
	date text,
	hour_start int,
	order_id text,
	order_date timeuuid,
	product_id text,
	product_name text,
	unit_price double,
	quantity int,
	total_price double,
	PRIMARY KEY (date, hour_start, order_id)
) WITH CLUSTERING ORDER BY (hour_start DESC);
</pre>
 *
 * @author patcho
 *
 */
public class ProductOrdersByDateHour {

	private String date; //Format yyyyMMdd
	private int hourStart;
	private String orderId;
	private Date orderDate;
	private String productId;
	private String productName;
	private double unitPrice;
	private int quantity;
	private double totalPrice;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getHourStart() {
		return hourStart;
	}
	public void setHourStart(int hourStart) {
		this.hourStart = hourStart;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	@Override
	public String toString() {
		return "ProductOrdersByDateHour [date=" + date + ", hourStart="
				+ hourStart + ", orderId=" + orderId + ", orderDate="
				+ orderDate + ", productId=" + productId + ", productName="
				+ productName + ", unitPrice=" + unitPrice + ", quantity="
				+ quantity + ", totalPrice=" + totalPrice + "]";
	}
	
	
}