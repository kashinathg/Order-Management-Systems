package com.datastax.order.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.datastax.order.demo.loaders.LoadOrder;
import com.datastax.order.demo.objects.Order;

public class StreamOrderProcessorImpl implements StreamProcessor {

	private static final String COMMA = ",";
	private static final Logger LOG = Logger.getLogger(LoadOrder.class);

	private static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	private int batchSize = 0;
	private String filename;
	private String[] contactPoints;

	public StreamOrderProcessorImpl(String filename, String[] contactPoints,
			int batchSize) {
		this.filename = filename;
		this.contactPoints = contactPoints;
		this.batchSize = batchSize;

		LOG.info("Processing Order file : " + this.filename
				+ " with batch size : " + batchSize);
	}

	public void startStreaming() {
		int totalSize = 0;

		Map<String, Order> orders = new LinkedHashMap<String, Order>();
		ProcessAllQueryTables processAllQueryTables = new ProcessAllQueryTables(
				contactPoints);
		processAllQueryTables.init();

		BufferedReader br = null;
		File file = new File("src/main/resources", filename);

		try {
			String currentLine;
			Order order;
			br = new BufferedReader(new FileReader(file));

			while ((currentLine = br.readLine()) != null) {

				String[] items = currentLine.split(COMMA);

				order = new Order();
				order.setOrderId(items[0].trim());
				order.setUserId(items[1].trim());
				order.setProductId(items[2].trim());
				order.setQuantity(Integer.parseInt(items[3].trim()));

				try {
					order.setOrderDate(dateFormat.parse(items[4].trim()));
				} catch (ParseException e) {
					e.printStackTrace();
					continue;
				}

				orders.put(order.getOrderId(), order);

				if (orders.size() == this.batchSize) {
					processAllQueryTables.processOrders(orders);

					orders.clear();
					totalSize += this.batchSize;
					LOG.info("Processed " + this.batchSize
							+ " orders (Total size : " + totalSize + ") Last Order date : " + new DateTime(order.getOrderDate()).toString());
				}
			}
			// Finish any remaining.
			totalSize += orders.size();
			processAllQueryTables.processOrders(orders);
			LOG.info("Processed a total of " + totalSize + " orders");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			processAllQueryTables.shutdown();
		}
	}

	public static void main(String[] args) {
		new StreamOrderProcessorImpl("order_list_final_tiny.csv",
				new String[] { "127.0.0.1" }, 500).startStreaming();
	}
}