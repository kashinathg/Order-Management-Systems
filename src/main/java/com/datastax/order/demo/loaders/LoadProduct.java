package com.datastax.order.demo.loaders;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.datastax.demo.utils.FileUtils;
import com.datastax.order.demo.objects.Product;

public class LoadProduct {

	// Product Example - Oil - Avocado,13.15,Reliant
	// Foodservice,P1,undecided,bland,messy
	private static final String COMMA = ",";
	private static final Logger LOG = Logger.getLogger(LoadProduct.class);

	public static Map<String, Product> processProductFile(String filename) {
		Map<String, Product> products = new HashMap<String, Product>();
		CSVReader csvReader;

		try {
			csvReader = new CSVReader(new FileReader("src/main/resources/"
					+ filename));

			Product product;
			String[] items = null;

			while ((items = csvReader.readNext()) != null) {

				product = new Product();
				product.setProductName(items[0].trim());
				product.setUnitPrice(Double.parseDouble(items[1].trim()));
				product.setVendor(items[2].trim());
				product.setProductId(items[3].trim());
				product.setRecommendation(items[4].trim());

				int attributesLenght = items.length - 5;

				for (int i = 0; i < attributesLenght; i++) {
					product.addAttribute(items[i + 5].trim());
				}

				products.put(product.getProductId(), product);
			}
			LOG.info("Loaded : " + filename + " with " + products.size()
					+ " products.");
		} catch (Exception e) {
			LOG.error("Problem processing Product File", e);
		}

		return products;
	}
}