package com.datastax.order.demo;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.datastax.order.demo.loaders.LoadProduct;
import com.datastax.order.demo.objects.Product;

public class TestLoadProducts {

	@Test
	public void testProduct(){
		
		Map<String, Product> productList;
		try {
			productList = LoadProduct.processProductFile("product_list_final.csv");
			Assert.assertEquals(4954, productList.size());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}

