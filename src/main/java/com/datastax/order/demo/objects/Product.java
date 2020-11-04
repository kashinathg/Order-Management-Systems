package com.datastax.order.demo.objects;

import java.util.HashSet;
import java.util.Set;

public class Product {
	private String productName;
	private double unitPrice;
	private String vendor;
	private String productId;
	private String recommendation;
	private Set<String> attributes = new HashSet<String>();
	
	public Product(){}
	
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
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	public Set<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Set<String> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute(String value){
		this.attributes.add(value);
	}

	@Override
	public String toString() {
		return "Product [productName=" + productName + ", unitPrice="
				+ unitPrice + ", vendor=" + vendor + ", productId=" + productId
				+ ", recommendation=" + recommendation + ", attributes="
				+ attributes + "]";
	}
	
}