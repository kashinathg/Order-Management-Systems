package com.datastax.order.demo.queryobjects;


/**
 * Query object for table product_orders_by_vendor(
	vendor text,
	order_id text,
	order_date int,
	product_id text,
	product_name text,
	unit_price double,
	quantity int,
	total_price double,
	attributes set<text>,
	PRIMARY KEY (vendor, order_id, product_id)
 * @author patcho
 *
 */
public class ProductOrdersByVendor {

	private String vendor;
	private String orderId;
	private int orderDate;
	private String productId;
	private String productName;
	private String recommendation;
	private double unitPrice;
	private int quantity;
	private double totalPrice;
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
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
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	@Override
	public String toString() {
		return "ProductOrdersByVendor [vendor=" + vendor + ", orderId="
				+ orderId + ", productId=" + productId + ", productName="
				+ productName + ", unitPrice=" + unitPrice + ", quantity="
				+ quantity + ", totalPrice=" + totalPrice + "]";
	}
	public int getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(int orderDate) {
		this.orderDate = orderDate;
	}
}