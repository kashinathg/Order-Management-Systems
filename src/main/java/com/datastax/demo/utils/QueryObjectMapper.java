package com.datastax.demo.utils;

import com.datastax.driver.core.Row;
import com.datastax.order.demo.MovingAverageObject;
import com.datastax.order.demo.objects.Account;
import com.datastax.order.demo.queryobjects.ProductOrdersByDate;
import com.datastax.order.demo.queryobjects.ProductOrdersByDateTimeUnit;
import com.datastax.order.demo.queryobjects.ProductOrdersByUser;
import com.datastax.order.demo.queryobjects.ProductOrdersByVendor;
import com.datastax.order.demo.queryobjects.Transaction;

public class QueryObjectMapper {

	public static ProductOrdersByUser mapProductOrderByUser(Row row) {
		
		ProductOrdersByUser productOrdersByUser = new ProductOrdersByUser();
		
		productOrdersByUser.setUserId(row.getString("user_id"));
		productOrdersByUser.setDescriptions(row.getSet("descriptions", String.class));
		productOrdersByUser.setOrderDate(row.getDate("order_date"));
		productOrdersByUser.setOrderId(row.getString("order_id"));
		productOrdersByUser.setProductId(row.getString("product_id"));
		productOrdersByUser.setProductName(row.getString("product_name"));
		productOrdersByUser.setQuantity(row.getInt("quantity"));
		productOrdersByUser.setTotalPrice(row.getDouble("total_price"));
		productOrdersByUser.setUnitPrice(row.getDouble("unit_price"));
		productOrdersByUser.setVendor(row.getString("vendor"));
		
		return productOrdersByUser;
	}

	public static ProductOrdersByDate mapProductOrderByDate(Row row) {
		
		ProductOrdersByDate productOrdersByDate = new ProductOrdersByDate();

		productOrdersByDate.setDate(row.getString("date"));
		productOrdersByDate.setOrderDate(row.getDate("order_date"));
		productOrdersByDate.setOrderId(row.getString("order_id"));
		productOrdersByDate.setProductId(row.getString("product_id"));
		productOrdersByDate.setProductName(row.getString("product_name"));
		productOrdersByDate.setQuantity(row.getInt("quantity"));
		productOrdersByDate.setTotalPrice(row.getDouble("total_price"));
		productOrdersByDate.setUnitPrice(row.getDouble("unit_price"));
		
		return productOrdersByDate;
	}	
	
	public static ProductOrdersByDateTimeUnit mapProductOrderByDateHour(Row row) {
		
		ProductOrdersByDateTimeUnit productOrdersByDateHour = new ProductOrdersByDateTimeUnit();

		productOrdersByDateHour.setDate(row.getString("date"));
		productOrdersByDateHour.setTimeUnit(row.getInt("hour_start"));
		productOrdersByDateHour.setOrderDate(row.getDate("order_date"));
		productOrdersByDateHour.setOrderId(row.getString("order_id"));
		productOrdersByDateHour.setProductId(row.getString("product_id"));
		productOrdersByDateHour.setProductName(row.getString("product_name"));
		productOrdersByDateHour.setQuantity(row.getInt("quantity"));
		productOrdersByDateHour.setTotalPrice(row.getDouble("total_price"));
		productOrdersByDateHour.setUnitPrice(row.getDouble("unit_price"));
		
		return productOrdersByDateHour;
	}	
	
	public static ProductOrdersByDateTimeUnit mapProductOrderByDateMinute(Row row) {
		
		ProductOrdersByDateTimeUnit productOrdersByDateHour = new ProductOrdersByDateTimeUnit();

		productOrdersByDateHour.setDate(row.getString("date"));
		productOrdersByDateHour.setTimeUnit(row.getInt("minute_start"));
		productOrdersByDateHour.setOrderDate(row.getDate("order_date"));
		productOrdersByDateHour.setOrderId(row.getString("order_id"));
		productOrdersByDateHour.setProductId(row.getString("product_id"));
		productOrdersByDateHour.setProductName(row.getString("product_name"));
		productOrdersByDateHour.setQuantity(row.getInt("quantity"));
		productOrdersByDateHour.setTotalPrice(row.getDouble("total_price"));
		productOrdersByDateHour.setUnitPrice(row.getDouble("unit_price"));
		
		return productOrdersByDateHour;
	}
	
	public static ProductOrdersByDateTimeUnit mapProductOrderByDateSecond(Row row) {
		
		ProductOrdersByDateTimeUnit productOrdersByDateHour = new ProductOrdersByDateTimeUnit();

		productOrdersByDateHour.setDate(row.getString("date"));
		productOrdersByDateHour.setTimeUnit(row.getInt("second_start"));
		productOrdersByDateHour.setOrderDate(row.getDate("order_date"));
		productOrdersByDateHour.setOrderId(row.getString("order_id"));
		productOrdersByDateHour.setProductId(row.getString("product_id"));
		productOrdersByDateHour.setProductName(row.getString("product_name"));
		productOrdersByDateHour.setQuantity(row.getInt("quantity"));
		productOrdersByDateHour.setTotalPrice(row.getDouble("total_price"));
		productOrdersByDateHour.setUnitPrice(row.getDouble("unit_price"));
		
		return productOrdersByDateHour;
	}

	public static ProductOrdersByVendor mapProductOrderByVendor(Row row) {
		ProductOrdersByVendor productOrdersByVendor = new ProductOrdersByVendor();
		
		productOrdersByVendor.setOrderId(row.getString("order_id"));
		productOrdersByVendor.setProductId(row.getString("product_id"));
		productOrdersByVendor.setProductName(row.getString("product_name"));
		productOrdersByVendor.setQuantity(row.getInt("quantity"));
		productOrdersByVendor.setRecommendation(row.getString("recommendation"));
		productOrdersByVendor.setTotalPrice(row.getDouble("total_price"));
		productOrdersByVendor.setUnitPrice(row.getDouble("unit_price"));
		productOrdersByVendor.setVendor(row.getString("vendor"));
		
		return productOrdersByVendor;
	}

	public static Transaction mapTransaction(Row row) {
		
		Transaction transaction = new Transaction();
		transaction.setAccountId(row.getString("account_id"));
		transaction.setAmount(row.getDouble("acount"));
		transaction.setDatetime(row.getDate("date_time"));
		transaction.setType(row.getString("type"));
		
		return transaction;
	}

	public static Account mapAccount(Row row) {
		Account account = new Account();
		
		account.setAccountId(row.getString("account_id"));
		account.setBalance(row.getDouble("balance"));
		account.setUserId(row.getString("user_id"));
		account.setLastUpdated(row.getDate("last_updated"));
		
		return account;
	}

	public static MovingAverageObject mapMovingAverage(Row row) {
		
		MovingAverageObject object = new MovingAverageObject(row.getString("description"),
				row.getString("time_unit"), row.getInt("size"),
				row.getDouble("value")
				);
	
		return object;
	}	
	
}