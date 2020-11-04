package com.datastax.order.demo.queryobjects;

import java.util.Date;
import java.util.Set;

/**
 * Mirrors the table - product_orders_by_user (shopping card)
 * 	user_id text,
	order_id text,
	order_date timeuuid,
	product_id text,
	product_name text,
	unit_price double,
	quantity int,
	total_price double,
	vendor text,
	descriptions set<text>,
	PRIMARY KEY (user_id)
	
 * @author patcho
 *
 */
public class ProductOrdersByUser {
	
	private String userId;
	private String orderId;
	private Date orderDate;
	private String productId;
	private String productName;
	private double unitPrice;
	private int quantity;
	private double totalPrice;
	private String vendor;
	private Set<String> descriptions;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public Set<String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(Set<String> descriptions) {
		this.descriptions = descriptions;
	}
	@Override
	public String toString() {
		return "ProductOrdersByUser [userId=" + userId + ", orderId=" + orderId
				+ ", orderDate=" + orderDate + ", productName=" + productName + ", productId=" + productId
				+ ", unitPrice=" + unitPrice + ", quantity=" + quantity
				+ ", totalPrice=" + totalPrice + ", vendor=" + vendor
				+ ", descriptions=" + descriptions + "]";
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
}