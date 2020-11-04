package com.datastax.order.demo.queryobjects;

import java.util.Date;

public class Transaction {
	private String accountId;
	private String type;
	private Date datetime;
	private double amount;
	
	public Transaction(){}
			
	public Transaction(String accountId, Transaction.Type type, Date orderDate,
			double totalPrice) {
		
		this.accountId = accountId;
		this.type = type.name();
		this.datetime = orderDate;
		this.amount = totalPrice;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Transaction [accountId=" + accountId + ", type=" + type
				+ ", datetime=" + datetime + ", amount=" + amount + "]";
	}
	
	public enum Type{
		CREDIT, DEBIT, BALANCE;
	}
}