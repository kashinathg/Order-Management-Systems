package com.datastax.order.demo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.datastax.order.demo.analytics.MovingAverageTotalPrice;
import com.datastax.order.demo.dao.OrderManagementDAO;
import com.datastax.order.demo.loaders.LoadAccount;
import com.datastax.order.demo.loaders.LoadProduct;
import com.datastax.order.demo.loaders.LoadUser;
import com.datastax.order.demo.objects.Account;
import com.datastax.order.demo.objects.Order;
import com.datastax.order.demo.objects.Product;
import com.datastax.order.demo.objects.User;
import com.datastax.order.demo.queryobjects.ProductOrderUsersByDateRange;
import com.datastax.order.demo.queryobjects.ProductOrderUsersByDateTimeUnitRange;
import com.datastax.order.demo.queryobjects.ProductOrdersByDate;
import com.datastax.order.demo.queryobjects.ProductOrdersByDateTimeUnit;
import com.datastax.order.demo.queryobjects.ProductOrdersByUser;
import com.datastax.order.demo.queryobjects.ProductOrdersByVendor;
import com.datastax.order.demo.queryobjects.UsersByProduct;
import com.datastax.order.demo.queryobjects.UsersByVendorProduct;

/**
 * This class is not thread safe.
 * 
 * @author patcho
 */
public class ProcessAllQueryTables {
	private static final String MA = "MA";

	private static final Logger LOG = Logger.getLogger(ProcessAllQueryTables.class);

	private DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
	private OrderManagementDAO orderManagementDAO;
	
	private Map<String, User> users;
	private Map<String, Product> products;
	private Map<String, Account> accounts;

	private MovingAverageTotalPrice hour5MovingAverage;
	private MovingAverageTotalPrice min15MovingAverage;
	private MovingAverageTotalPrice second30MovingAverage;

	public ProcessAllQueryTables(String[] contactPoints) {
		this.orderManagementDAO = new OrderManagementDAO(contactPoints);
		this.orderManagementDAO.startScheduler();
	}

	public void shutdown() {
		this.orderManagementDAO.shutdown();
	}

	public void init() {

		this.accounts = LoadAccount.processAccountFile("account_list_final.csv");
		this.users = LoadUser.processUserFile("user_list_final.csv");
		this.products = LoadProduct.processProductFile("product_list_final.csv");

		this.orderManagementDAO.insertProducts(products.values());
		this.orderManagementDAO.insertUsers(users.values());
		this.orderManagementDAO.insertAccounts(accounts.values());

		this.hour5MovingAverage = new MovingAverageTotalPrice(5);
		this.min15MovingAverage = new MovingAverageTotalPrice(15);
		this.second30MovingAverage = new MovingAverageTotalPrice(30);
	}

	public void processOrders(Map<String, Order> batchOrders) {

		Order lastOrder = null;

		for (String orderId : batchOrders.keySet()) {

			Order order = batchOrders.get(orderId);
			if (lastOrder == null) {
				lastOrder = order;
			} else {
				sleep(order.getOrderDate().getTime() - lastOrder.getOrderDate().getTime());
			}

			Map<String, Order> orders = new HashMap<String, Order>();
			order.setOrderDate(new Date());
			orders.put(orderId, order);

			this.orderManagementDAO.insertMovingAverages(calculateMovingAverage(order,
					products.get(order.getProductId())));

			this.orderManagementDAO.insertProductOrdersByUser(createProductOrdersByUser(products, orders, users),
					accounts);
			this.orderManagementDAO.insertProductOrdersByVendor(createProductOrdersByVendor(products, orders, users));
			this.orderManagementDAO.insertUsersByProduct(createUsersByProduct(products, orders, users));
			this.orderManagementDAO.insertUsersByVendor(createUsersByVendor(products, orders, users));

			this.orderManagementDAO.insertProductOrdersByDate(createProductOrdersByDate(products, orders));
			this.orderManagementDAO.insertProductOrdersByDateSecond(createProductOrdersByDateSecond(products, orders));
			this.orderManagementDAO.insertProductOrderUsersByDateRange(createProductOrdersUserDateRange(products,
					orders, users));
			this.orderManagementDAO.insertProductOrderUsersByDateHourRange(createProductOrdersUserDateHourRange(
					products, orders, users));
			this.orderManagementDAO.insertProductOrderUsersByDateMinuteRange(createProductOrdersUserDateMinuteRange(
					products, orders, users));
			this.orderManagementDAO.insertProductOrderUsersByDateSecondRange(createProductOrdersUserDateSecondRange(
					products, orders, users));

		}
	}

	private List<MovingAverageObject> calculateMovingAverage(Order order, Product product) {

		if (product == null) {
			return null;
		}
		DateTime orderDate = new DateTime(order.getOrderDate());

		this.hour5MovingAverage.processOrder(orderDate.getHourOfDay(), order.getQuantity() * product.getUnitPrice());
		this.min15MovingAverage.processOrder(orderDate.getMinuteOfDay(), order.getQuantity() * product.getUnitPrice());
		this.second30MovingAverage.processOrder(orderDate.getSecondOfDay(),
				order.getQuantity() * product.getUnitPrice());

		List<MovingAverageObject> maObjects = new ArrayList<MovingAverageObject>();

		double lastMovingAverage = this.hour5MovingAverage.getLastMovingAverage();
		if (lastMovingAverage != -1) {
			maObjects.add(new MovingAverageObject(MA, "hour", 5, lastMovingAverage));
		}

		lastMovingAverage = this.min15MovingAverage.getLastMovingAverage();
		if (lastMovingAverage != -1) {
			maObjects.add(new MovingAverageObject(MA, "min", 15, lastMovingAverage));
		}

		lastMovingAverage = this.second30MovingAverage.getLastMovingAverage();
		if (lastMovingAverage != -1) {
			maObjects.add(new MovingAverageObject(MA, "second", 30, lastMovingAverage));
		}

		return maObjects;
	}

	private void sleep(long millis) {
		if (millis <= 0)
			return;
		if (millis >= 1000)
			millis = 10;

		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private List<ProductOrderUsersByDateTimeUnitRange> createProductOrdersUserDateSecondRange(
			Map<String, Product> products, Map<String, Order> orders, Map<String, User> users) {

		List<ProductOrderUsersByDateTimeUnitRange> productOrders = new ArrayList<ProductOrderUsersByDateTimeUnitRange>();

		for (Order order : orders.values()) {

			ProductOrderUsersByDateTimeUnitRange productOrderUsersByDateTimeUnitRange = new ProductOrderUsersByDateTimeUnitRange();

			DateTime dateTime = new DateTime(order.getOrderDate());

			productOrderUsersByDateTimeUnitRange.setTimeUnit(dateTime.getSecondOfDay());
			productOrderUsersByDateTimeUnitRange.setDate(order.getOrderDate());
			productOrderUsersByDateTimeUnitRange.setOrderDate(order.getOrderDate());
			productOrderUsersByDateTimeUnitRange.setOrderId(order.getOrderId());
			productOrderUsersByDateTimeUnitRange.setProductId(order.getProductId());
			productOrderUsersByDateTimeUnitRange.setUserId(order.getUserId());

			productOrders.add(productOrderUsersByDateTimeUnitRange);
		}

		return productOrders;
	}

	private List<ProductOrderUsersByDateTimeUnitRange> createProductOrdersUserDateMinuteRange(
			Map<String, Product> products, Map<String, Order> orders, Map<String, User> users) {

		List<ProductOrderUsersByDateTimeUnitRange> productOrders = new ArrayList<ProductOrderUsersByDateTimeUnitRange>();

		for (Order order : orders.values()) {

			ProductOrderUsersByDateTimeUnitRange productOrderUsersByDateTimeUnitRange = new ProductOrderUsersByDateTimeUnitRange();

			DateTime dateTime = new DateTime(order.getOrderDate());

			productOrderUsersByDateTimeUnitRange.setTimeUnit(dateTime.getMinuteOfDay());
			productOrderUsersByDateTimeUnitRange.setDate(order.getOrderDate());
			productOrderUsersByDateTimeUnitRange.setOrderDate(order.getOrderDate());
			productOrderUsersByDateTimeUnitRange.setOrderId(order.getOrderId());
			productOrderUsersByDateTimeUnitRange.setProductId(order.getProductId());
			productOrderUsersByDateTimeUnitRange.setUserId(order.getUserId());

			productOrders.add(productOrderUsersByDateTimeUnitRange);
		}
		return productOrders;
	}

	private List<ProductOrderUsersByDateTimeUnitRange> createProductOrdersUserDateHourRange(
			Map<String, Product> products, Map<String, Order> orders, Map<String, User> users) {
		List<ProductOrderUsersByDateTimeUnitRange> productOrders = new ArrayList<ProductOrderUsersByDateTimeUnitRange>();

		for (Order order : orders.values()) {

			ProductOrderUsersByDateTimeUnitRange productOrderUsersByDateTimeUnitRange = new ProductOrderUsersByDateTimeUnitRange();

			DateTime dateTime = new DateTime(order.getOrderDate());

			productOrderUsersByDateTimeUnitRange.setTimeUnit(dateTime.getHourOfDay());
			productOrderUsersByDateTimeUnitRange.setDate(order.getOrderDate());
			productOrderUsersByDateTimeUnitRange.setOrderDate(order.getOrderDate());
			productOrderUsersByDateTimeUnitRange.setOrderId(order.getOrderId());
			productOrderUsersByDateTimeUnitRange.setProductId(order.getProductId());
			productOrderUsersByDateTimeUnitRange.setUserId(order.getUserId());

			productOrders.add(productOrderUsersByDateTimeUnitRange);
		}
		return productOrders;
	}

	private List<ProductOrderUsersByDateRange> createProductOrdersUserDateRange(Map<String, Product> products,
			Map<String, Order> orders, Map<String, User> users) {
		List<ProductOrderUsersByDateRange> productOrders = new ArrayList<ProductOrderUsersByDateRange>();

		for (Order order : orders.values()) {

			ProductOrderUsersByDateRange productOrderUsersByDateRange = new ProductOrderUsersByDateRange();
			productOrderUsersByDateRange.setDate(order.getOrderDate());
			productOrderUsersByDateRange.setOrderDate(order.getOrderDate());
			productOrderUsersByDateRange.setOrderId(order.getOrderId());
			productOrderUsersByDateRange.setProductId(order.getProductId());
			productOrderUsersByDateRange.setUserId(order.getUserId());

			productOrders.add(productOrderUsersByDateRange);
		}

		return productOrders;
	}

	private List<UsersByVendorProduct> createUsersByVendor(Map<String, Product> products, Map<String, Order> orders,
			Map<String, User> users) {

		List<UsersByVendorProduct> usersByVendorProducts = new ArrayList<UsersByVendorProduct>();

		for (Order order : orders.values()) {
			// Get the product
			Product product = products.get(order.getProductId());
			User user = users.get(order.getUserId());

			if (!checkProduct(product, order))
				continue;
			if (!checkUser(user, order))
				continue;

			UsersByVendorProduct usersByVendorProduct = new UsersByVendorProduct();

			usersByVendorProduct.setCityName(user.getCityName());
			usersByVendorProduct.setCountryCode(user.getCountryCode());
			usersByVendorProduct.setDob(user.getDob());
			usersByVendorProduct.setEmail(user.getEmail());
			usersByVendorProduct.setFirstname(user.getFirstname());
			usersByVendorProduct.setGender(user.getGender());
			usersByVendorProduct.setLastname(user.getLastname());
			usersByVendorProduct.setMiddlename(user.getMiddlename());
			usersByVendorProduct.setLastname(user.getLastname());
			usersByVendorProduct.setPhoneNumber(user.getPhoneNumber());
			usersByVendorProduct.setStateName(user.getStateName());
			usersByVendorProduct.setZipCode(user.getZipCode());
			usersByVendorProduct.setVendor(product.getVendor());
			usersByVendorProduct.setUserId(user.getUserId());
			usersByVendorProduct.setStreetAddress(user.getStreetAddress());

			usersByVendorProducts.add(usersByVendorProduct);
		}

		return usersByVendorProducts;
	}

	private List<UsersByProduct> createUsersByProduct(Map<String, Product> products, Map<String, Order> orders,
			Map<String, User> users) {

		List<UsersByProduct> usersByProducts = new ArrayList<UsersByProduct>();

		for (Order order : orders.values()) {
			// Get the product
			Product product = products.get(order.getProductId());
			User user = users.get(order.getUserId());

			if (!checkProduct(product, order))
				continue;
			if (!checkUser(user, order))
				continue;

			UsersByProduct usersByProduct = new UsersByProduct();

			usersByProduct.setCityName(user.getCityName());
			usersByProduct.setCountryCode(user.getCountryCode());
			usersByProduct.setDob(user.getDob());
			usersByProduct.setEmail(user.getEmail());
			usersByProduct.setFirstname(user.getFirstname());
			usersByProduct.setGender(user.getGender());
			usersByProduct.setLastname(user.getLastname());
			usersByProduct.setMiddlename(user.getMiddlename());
			usersByProduct.setLastname(user.getLastname());
			usersByProduct.setPhoneNumber(user.getPhoneNumber());
			usersByProduct.setProductId(product.getProductId());
			usersByProduct.setProductName(product.getProductName());
			usersByProduct.setStateName(user.getStateName());
			usersByProduct.setZipCode(user.getZipCode());
			usersByProduct.setUserId(user.getUserId());
			usersByProduct.setStreetAddress(user.getStreetAddress());

			usersByProducts.add(usersByProduct);
		}

		return usersByProducts;
	}

	private List<ProductOrdersByVendor> createProductOrdersByVendor(Map<String, Product> products,
			Map<String, Order> orders, Map<String, User> users) {

		List<ProductOrdersByVendor> productOrdersByVendors = new ArrayList<ProductOrdersByVendor>();

		for (Order order : orders.values()) {
			// Get the product
			Product product = products.get(order.getProductId());
			User user = users.get(order.getUserId());

			if (!checkProduct(product, order))
				continue;
			if (!checkUser(user, order))
				continue;

			ProductOrdersByVendor productOrdersByVendor = new ProductOrdersByVendor();

			productOrdersByVendor.setOrderId(order.getOrderId());
			productOrdersByVendor.setOrderDate(getOrderDateAsNumber(order.getOrderDate()));
			productOrdersByVendor.setProductId(product.getProductId());
			productOrdersByVendor.setProductName(product.getProductName());
			productOrdersByVendor.setRecommendation(product.getRecommendation());
			productOrdersByVendor.setQuantity(order.getQuantity());
			productOrdersByVendor.setUnitPrice(product.getUnitPrice());
			productOrdersByVendor.setTotalPrice(product.getUnitPrice() * order.getQuantity());
			productOrdersByVendor.setVendor(product.getVendor());

			productOrdersByVendors.add(productOrdersByVendor);
		}
		return productOrdersByVendors;
	}


	private List<ProductOrdersByUser> createProductOrdersByUser(Map<String, Product> products,
			Map<String, Order> orders, Map<String, User> users) {

		List<ProductOrdersByUser> productOrdersByUsers = new ArrayList<ProductOrdersByUser>();

		for (Order order : orders.values()) {
			// Get the product
			Product product = products.get(order.getProductId());
			User user = users.get(order.getUserId());

			if (!checkProduct(product, order))
				continue;
			if (!checkUser(user, order))
				continue;

			ProductOrdersByUser productOrdersByUser = new ProductOrdersByUser();

			productOrdersByUser.setDescriptions(product.getAttributes());
			productOrdersByUser.setOrderDate(order.getOrderDate());
			productOrdersByUser.setOrderId(order.getOrderId());
			productOrdersByUser.setProductId(order.getProductId());
			productOrdersByUser.setProductName(product.getProductName());
			productOrdersByUser.setQuantity(order.getQuantity());
			productOrdersByUser.setUnitPrice(product.getUnitPrice());
			productOrdersByUser.setTotalPrice(order.getQuantity() * product.getUnitPrice());
			productOrdersByUser.setUserId(user.getUserId());
			productOrdersByUser.setVendor(product.getVendor());

			productOrdersByUsers.add(productOrdersByUser);
		}

		return productOrdersByUsers;
	}


	private List<ProductOrdersByDateTimeUnit> createProductOrdersByDateSecond(Map<String, Product> products,
			Map<String, Order> orders) {

		List<ProductOrdersByDateTimeUnit> productOrdersByDateSecondList = new ArrayList<ProductOrdersByDateTimeUnit>();

		for (Order order : orders.values()) {
			// Get the product
			Product product = products.get(order.getProductId());

			if (!checkProduct(product, order))
				continue;

			ProductOrdersByDateTimeUnit productOrdersByDateSecond = new ProductOrdersByDateTimeUnit();

			DateTime dateTime = new DateTime(order.getOrderDate());

			productOrdersByDateSecond.setTimeUnit(dateTime.getSecondOfDay());
			productOrdersByDateSecond.setDate(dateFormatter.format(order.getOrderDate()));
			productOrdersByDateSecond.setOrderId(order.getOrderId());
			productOrdersByDateSecond.setOrderDate(order.getOrderDate());
			productOrdersByDateSecond.setProductId(order.getProductId());
			productOrdersByDateSecond.setProductName(product.getProductName());
			productOrdersByDateSecond.setQuantity(order.getQuantity());
			productOrdersByDateSecond.setUnitPrice(product.getUnitPrice());
			productOrdersByDateSecond.setTotalPrice(order.getQuantity() * product.getUnitPrice());

			productOrdersByDateSecondList.add(productOrdersByDateSecond);
		}

		return productOrdersByDateSecondList;
	}

	private List<ProductOrdersByDate> createProductOrdersByDate(Map<String, Product> products, Map<String, Order> orders) {

		List<ProductOrdersByDate> productOrdersByDateList = new ArrayList<ProductOrdersByDate>();

		for (Order order : orders.values()) {
			// Get the product
			Product product = products.get(order.getProductId());

			if (!checkProduct(product, order))
				continue;

			ProductOrdersByDate productOrdersByDate = new ProductOrdersByDate();

			productOrdersByDate.setDate(dateFormatter.format(order.getOrderDate()));
			productOrdersByDate.setOrderId(order.getOrderId());
			productOrdersByDate.setOrderDate(order.getOrderDate());
			productOrdersByDate.setProductId(order.getProductId());
			productOrdersByDate.setProductName(product.getProductName());
			productOrdersByDate.setQuantity(order.getQuantity());
			productOrdersByDate.setUnitPrice(product.getUnitPrice());
			productOrdersByDate.setTotalPrice(order.getQuantity() * product.getUnitPrice());

			productOrdersByDateList.add(productOrdersByDate);
		}

		return productOrdersByDateList;
	}

	private boolean checkProduct(Product product, Order order) {
		if (product == null) {
			LOG.debug("No Product found for product id :" + order.getProductId() + " and order :" + order.getOrderId());
			return false;
		}
		return true;
	}

	private boolean checkUser(User user, Order order) {
		if (user == null) {
			LOG.debug("No User found for user id :" + order.getUserId() + " and order :" + order.getOrderId());
			return false;
		}
		return true;
	}

	private Date parseDate(String userId, String dob) {
		try {
			return this.dateFormatter.parse(dob);
		} catch (Exception e) {
			LOG.warn("Dob :" + dob + " for user :" + userId + " is not a valid date");
			return new Date();
		}
	}

	private int getOrderDateAsNumber(Date orderDate) {
		
		try{			
			return Integer.parseInt(this.dateFormatter.format(orderDate));
		}catch (Exception e) {
			LOG.warn("OrderDate :" + orderDate + " is not a valid date");
			return -1;
		}		
	}
}