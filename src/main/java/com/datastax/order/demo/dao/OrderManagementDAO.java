package com.datastax.order.demo.dao;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.datastax.demo.utils.QueryObjectMapper;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ExecutionInfo;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Query;
import com.datastax.driver.core.QueryTrace;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.order.demo.MovingAverageObject;
import com.datastax.order.demo.objects.Account;
import com.datastax.order.demo.objects.Product;
import com.datastax.order.demo.objects.User;
import com.datastax.order.demo.policy.TimeOfDayRetryPolicy;
import com.datastax.order.demo.queryobjects.ProductOrderUsersByDateRange;
import com.datastax.order.demo.queryobjects.ProductOrderUsersByDateTimeUnitRange;
import com.datastax.order.demo.queryobjects.ProductOrdersByDate;
import com.datastax.order.demo.queryobjects.ProductOrdersByDateTimeUnit;
import com.datastax.order.demo.queryobjects.ProductOrdersByUser;
import com.datastax.order.demo.queryobjects.ProductOrdersByVendor;
import com.datastax.order.demo.queryobjects.UsersByProduct;
import com.datastax.order.demo.queryobjects.UsersByVendorProduct;

public class OrderManagementDAO {

	private static final Logger LOG = Logger.getLogger(OrderManagementDAO.class);

	private static final String INSERT_INTO_PRODUCT_ORDERS_BY_DATE = "INSERT INTO product_orders_by_date (date, order_id, order_date, product_id, product_name,"
			+ "unit_price, quantity, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCT_ORDERS_BY_DATE_HOUR = "INSERT INTO product_orders_by_date_hour (date, hour_start, order_id, order_date, product_id, product_name,"
			+ "unit_price, quantity, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCT_ORDERS_BY_DATE_MINUTE = "INSERT INTO product_orders_by_date_minute (date, minute_start, order_id, order_date, product_id, product_name,"
			+ "unit_price, quantity, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCT_ORDERS_BY_DATE_SECOND = "INSERT INTO product_orders_by_date_second (date, second_start, order_id, order_date, product_id, product_name,"
			+ "unit_price, quantity, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCT_ORDERS_BY_VENDOR = "INSERT INTO product_orders_by_vendor (vendor, order_id, product_id, product_name, recommendation, unit_price, quantity, "
			+ "total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCT_ORDERS_BY_USER = "INSERT INTO product_orders_by_user(user_id, order_id, order_date, product_id, product_name, unit_price, quantity, "
			+ "total_price, vendor, descriptions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_USER_BY_PRODUCT = "INSERT INTO users_by_product (product_id, product_name, user_id,first_name, last_name, dob, street_address, zip_code, "
			+ "city_name, state_name, phone_number, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_USER_BY_VENDOR_PRODUCT = "INSERT INTO users_by_vendor_product (vendor, user_id, first_name, last_name, dob, street_address, zip_code, "
			+ "city_name, state_name, phone_number, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCT_ORDERS_USER_RANGE = "INSERT INTO product_orders_user_date_range (user_id, date, order_id, product_id, order_date) VALUES (?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCT_ORDERS_USER_HOUR_RANGE = "INSERT INTO product_orders_user_hour_range (user_id, hour, date, order_id, product_id, order_date) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCT_ORDERS_USER_MINUTE_RANGE = "INSERT INTO product_orders_user_minute_range (user_id, minute, date, order_id, product_id, order_date) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCT_ORDERS_USER_SECOND_RANGE = "INSERT INTO product_orders_user_second_range (user_id, second, date, order_id, product_id, order_date) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_PRODUCTS_TABLE = "INSERT INTO products (product_id, product_name,vendor,unit_price,recommendation, descriptions) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_USER_TABLE = "INSERT into users (user_id,first_name,middle_name,last_name,dob,street_address,zip_code,city_name,state_name,gender,phone_number,email,country_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_INTO_ACCOUNTS_TABLE = "INSERT into accounts (account_id, user_id, last_updated, balance) VALUES (?, ?, ?, ?)";
	private static final String INSERT_INTO_MOVING_AVERAGE = "INSERT into realtime_analytic (description, time_unit, size, value) values (?, ?, ?, ?)";

	private static final String GET_PRODUCT_ORDERS_BY_VENDOR = "SELECT * FROM product_orders_by_vendor where vendor = ? LIMIT 10";
	private static final String GET_PRODUCT_ORDERS_BY_DATE_SECOND = "SELECT * from product_orders_by_date_second where date = ? and second_start >= ? and second_start < ?";
	
	private static final String GET_LAST_ORDERS_FOR_USER = "select user_id, order_date, total_price from product_orders_by_user where user_id = ? and order_date > ? order by order_date asc";
	private static final String GET_PRODUCT_ORDERS_BY_USER = "select * from product_orders_by_user where user_id = ? LIMIT 10";
	private static final String GET_ACCOUNTS = "select account_id, user_id, last_updated, balance from accounts";
	private static final String GET_ACCOUNTS_BY_USER = "select account_id, user_id, last_updated, balance from accounts where user_id = ?";
	private static final String GET_HIGHEST_BY_VENDOR = "select product_name, count from products_by_vendor_hive where recommendation = 'highly recommend' and vendor = ? limit 10";
	private static final String GET_HIGHEST_BY_PRODUCT = "select product_name, count from products_by_product_hive where recommendation = 'highly recommend'  limit 10";
	private static final String GET_REALTIME_ANALYTIC = "select * from realtime_analytic";

	private Session session;
	private Cluster cluster;

	private static int BALANCE_UPDATE_IN_MINS = 2;
	private static DecimalFormat df = new DecimalFormat("#.##");

	private PreparedStatement insertUsersStatement;
	private PreparedStatement insertProductsStatement;
	private PreparedStatement insertAccountsStatement;
	private PreparedStatement insertProductOrdersByUserStatement;
	private PreparedStatement insertProductOrdersByDateStatement;
	private PreparedStatement insertProductOrdersByDateHourStatement;
	private PreparedStatement insertProductOrdersByDateMinuteStatement;
	private PreparedStatement insertProductOrdersByDateSecondStatement;
	private PreparedStatement insertProductOrdersByVendorStatement;
	private PreparedStatement insertUserByProductStatement;
	private PreparedStatement insertUserByVendorProductStatement;
	private PreparedStatement insertProductOrdersUserRangeStatement;
	private PreparedStatement insertProductOrdersUserHourRangeStatement;
	private PreparedStatement insertProductOrdersUserMinuteRangeStatement;
	private PreparedStatement insertProductOrdersUserSecondRangeStatement;
	private PreparedStatement insertMovingAverageStatement;

	private PreparedStatement getProductOrdersByVendorStatement;
	private PreparedStatement getProductOrdersByDateSecondStatement;
	private PreparedStatement getLastOrdersForUserStatement;
	private PreparedStatement getAccountsStatement;
	private PreparedStatement getAccountsStatementByUser;
	private PreparedStatement getProductOrdersByUserLimit;
	private PreparedStatement getHighRecommendByVendor;
	private PreparedStatement getHighRecommendByProduct;
	private PreparedStatement getRealTimeAnalytic;

	public OrderManagementDAO(String[] contactPoints) {

		cluster = Cluster.builder().addContactPoints(contactPoints)				
				.withRetryPolicy(new TimeOfDayRetryPolicy(9, 17))
				.build();
		
		session = cluster.connect("order_management");
		
		this.insertUsersStatement = this.session.prepare(INSERT_INTO_USER_TABLE);
		this.insertProductsStatement = this.session.prepare(INSERT_INTO_PRODUCTS_TABLE);
		this.insertAccountsStatement = this.session.prepare(INSERT_INTO_ACCOUNTS_TABLE);
		this.insertProductOrdersByUserStatement = this.session.prepare(INSERT_INTO_PRODUCT_ORDERS_BY_USER);
		this.insertProductOrdersByDateStatement = this.session.prepare(INSERT_INTO_PRODUCT_ORDERS_BY_DATE);
		this.insertProductOrdersByDateHourStatement = this.session.prepare(INSERT_INTO_PRODUCT_ORDERS_BY_DATE_HOUR);
		this.insertProductOrdersByDateMinuteStatement = this.session.prepare(INSERT_INTO_PRODUCT_ORDERS_BY_DATE_MINUTE);
		this.insertProductOrdersByDateSecondStatement = this.session.prepare(INSERT_INTO_PRODUCT_ORDERS_BY_DATE_SECOND);
		this.insertProductOrdersByVendorStatement = this.session.prepare(INSERT_INTO_PRODUCT_ORDERS_BY_VENDOR);
		this.insertProductOrdersByDateStatement = this.session.prepare(INSERT_INTO_PRODUCT_ORDERS_BY_USER);
		this.insertUserByProductStatement = this.session.prepare(INSERT_INTO_USER_BY_PRODUCT);
		this.insertUserByVendorProductStatement = this.session.prepare(INSERT_INTO_USER_BY_VENDOR_PRODUCT);
		this.insertProductOrdersUserRangeStatement = this.session.prepare(INSERT_INTO_PRODUCT_ORDERS_USER_RANGE);
		this.insertProductOrdersUserHourRangeStatement = this.session
				.prepare(INSERT_INTO_PRODUCT_ORDERS_USER_HOUR_RANGE);
		this.insertProductOrdersUserMinuteRangeStatement = this.session
				.prepare(INSERT_INTO_PRODUCT_ORDERS_USER_MINUTE_RANGE);
		this.insertProductOrdersUserSecondRangeStatement = this.session
				.prepare(INSERT_INTO_PRODUCT_ORDERS_USER_SECOND_RANGE);
		this.insertMovingAverageStatement = this.session.prepare(INSERT_INTO_MOVING_AVERAGE);		
		this.getAccountsStatementByUser = this.session.prepare(GET_ACCOUNTS_BY_USER);
		this.getRealTimeAnalytic = this.session.prepare(GET_REALTIME_ANALYTIC);

		this.getProductOrdersByVendorStatement = this.session.prepare(GET_PRODUCT_ORDERS_BY_VENDOR);
		this.getProductOrdersByDateSecondStatement = this.session.prepare(GET_PRODUCT_ORDERS_BY_DATE_SECOND);
		this.getLastOrdersForUserStatement = this.session.prepare(GET_LAST_ORDERS_FOR_USER);
		this.getAccountsStatement = this.session.prepare(GET_ACCOUNTS);
		this.getProductOrdersByUserLimit = this.session.prepare(GET_PRODUCT_ORDERS_BY_USER);
		this.getHighRecommendByVendor = this.session.prepare(GET_HIGHEST_BY_VENDOR);
		this.getHighRecommendByProduct = this.session.prepare(GET_HIGHEST_BY_PRODUCT);
		
		//Consistency Level
		this.insertUsersStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductsStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertAccountsStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersByUserStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersByDateStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersByDateHourStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersByDateMinuteStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersByDateSecondStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersByVendorStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersByDateStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertUserByProductStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertUserByVendorProductStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersUserRangeStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersUserHourRangeStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersUserMinuteRangeStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertProductOrdersUserSecondRangeStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.insertMovingAverageStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		
		this.getProductOrdersByVendorStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.getProductOrdersByDateSecondStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.getLastOrdersForUserStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.getAccountsStatement.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.getProductOrdersByUserLimit.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.getHighRecommendByVendor.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.getHighRecommendByProduct.setConsistencyLevel(ConsistencyLevel.QUORUM);
		this.getRealTimeAnalytic.setConsistencyLevel(ConsistencyLevel.QUORUM);
	}

	public void startScheduler() {
		// Schedule the update balance
		ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
		exec.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				LOG.info("Starting balance update.....");
				updateAccountWithBalance();
				LOG.info("Finished balance update.....");
			}

		}, BALANCE_UPDATE_IN_MINS, BALANCE_UPDATE_IN_MINS, TimeUnit.MINUTES);
	}

	public void insertAccounts(Collection<Account> collection) {
		LOG.info("Inserting to accounts");

		BoundStatement boundStatement = insertAccountsStatement.bind();
		int count = 1;

		for (Account account : collection) {

			this.session.execute(boundStatement.bind(account.getAccountId(), account.getUserId(),
					account.getLastUpdated(), account.getBalance()));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " account records");
			}
			count++;
		}
	}

	public void insertProducts(Collection<Product> collection) {
		LOG.info("Inserting to products");

		BoundStatement boundStatement = insertProductsStatement.bind();
		int count = 1;

		for (Product product : collection) {

			this.session.execute(boundStatement.bind(product.getProductId(), product.getProductName(),
					product.getVendor(), product.getUnitPrice(), product.getRecommendation(), product.getAttributes()));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " product records");
			}
			count++;
		}
	}

	public void insertUsers(Collection<User> collection) {

		LOG.info("Inserting to users");

		BoundStatement boundStatement = insertUsersStatement.bind();

		int count = 1;

		// (user_id,first_name,middle_name,last_name,dob,street_address,zip_code,city_name,state_name,gender,phone_number,email,country_code
		for (User user : collection) {

			this.session.execute(boundStatement.bind(user.getUserId(), user.getFirstname(), user.getMiddlename(),
					user.getLastname(), user.getDob(), user.getStreetAddress(), user.getZipCode(), user.getCityName(),
					user.getStateName(), user.getGender(), user.getPhoneNumber(), user.getEmail(),
					user.getCountryCode()));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " user records");
			}
			count++;
		}
	}

	public void insertProductOrdersByDate(List<ProductOrdersByDate> productOrdersByDateList) {

		LOG.debug("Inserting to product_orders_by_date");

		BoundStatement boundStatement = insertProductOrdersByDateStatement.bind();

		int count = 1;
		for (ProductOrdersByDate productOrdersByDate : productOrdersByDateList) {

			this.session.execute(boundStatement.bind(productOrdersByDate.getDate(), productOrdersByDate.getOrderId(),
					new java.sql.Timestamp(productOrdersByDate.getOrderDate().getTime()),
					productOrdersByDate.getProductId(), productOrdersByDate.getProductName(),
					productOrdersByDate.getUnitPrice(), productOrdersByDate.getQuantity(),
					Double.parseDouble(df.format(productOrdersByDate.getTotalPrice()))));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}
			count++;

			session.execute("update order_date_counter set total_for_date = total_for_date + 1 where datename = 'datecounter' and date = '"
					+ productOrdersByDate.getDate() + "'");
		}
	}

	public void insertProductOrdersByDateHour(List<ProductOrdersByDateTimeUnit> productOrdersByDateHourList) {
		LOG.debug("Inserting to product_orders_by_date_hour");

		BoundStatement boundStatement = insertProductOrdersByDateHourStatement.bind();

		int count = 1;
		for (ProductOrdersByDateTimeUnit productOrdersByDateHour : productOrdersByDateHourList) {

			this.session.execute(boundStatement.bind(productOrdersByDateHour.getDate(),
					productOrdersByDateHour.getTimeUnit(), productOrdersByDateHour.getOrderId(),
					new java.sql.Timestamp(productOrdersByDateHour.getOrderDate().getTime()),
					productOrdersByDateHour.getProductId(), productOrdersByDateHour.getProductName(),
					productOrdersByDateHour.getUnitPrice(), productOrdersByDateHour.getQuantity(),
					Double.parseDouble(df.format(productOrdersByDateHour.getTotalPrice()))));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}

			this.incrementCounter("order_date_hour_counter", "total_for_hour", productOrdersByDateHour.getDate(),
					"hour", productOrdersByDateHour.getTimeUnit());
			count++;
		}
	}

	public void insertProductOrdersByDateMinute(List<ProductOrdersByDateTimeUnit> productOrdersByDateMinuteList) {
		LOG.debug("Inserting to product_orders_by_date_hour_minute");

		BoundStatement boundStatement = insertProductOrdersByDateMinuteStatement.bind();

		int count = 1;
		for (ProductOrdersByDateTimeUnit productOrdersByDateMinute : productOrdersByDateMinuteList) {

			this.session.execute(boundStatement.bind(productOrdersByDateMinute.getDate(),
					productOrdersByDateMinute.getTimeUnit(), productOrdersByDateMinute.getOrderId(),
					new java.sql.Timestamp(productOrdersByDateMinute.getOrderDate().getTime()),
					productOrdersByDateMinute.getProductId(), productOrdersByDateMinute.getProductName(),
					productOrdersByDateMinute.getUnitPrice(), productOrdersByDateMinute.getQuantity(),
					Double.parseDouble(df.format(productOrdersByDateMinute.getTotalPrice()))));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}

			count++;
		}
	}

	public void insertProductOrdersByDateSecond(List<ProductOrdersByDateTimeUnit> productOrdersByDateSecondList) {

		LOG.debug("Inserting to product_orders_by_date_hour_second");

		BoundStatement boundStatement = insertProductOrdersByDateSecondStatement.bind();

		int count = 1;
		for (ProductOrdersByDateTimeUnit productOrdersByDateSecond : productOrdersByDateSecondList) {

			this.session.execute(boundStatement.bind(productOrdersByDateSecond.getDate(),
					productOrdersByDateSecond.getTimeUnit(), productOrdersByDateSecond.getOrderId(),
					new java.sql.Timestamp(productOrdersByDateSecond.getOrderDate().getTime()),
					productOrdersByDateSecond.getProductId(), productOrdersByDateSecond.getProductName(),
					productOrdersByDateSecond.getUnitPrice(), productOrdersByDateSecond.getQuantity(),
					Double.parseDouble(df.format(productOrdersByDateSecond.getTotalPrice()))));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}

			DateTime dateTime = new DateTime(productOrdersByDateSecond.getOrderDate());

			this.incrementCounter("order_date_hour_counter", "total_for_hour", productOrdersByDateSecond.getDate(),
					"hour", dateTime.getHourOfDay());

			this.incrementCounter("order_date_minute_counter", "total_for_minute", productOrdersByDateSecond.getDate(),
					"minute", dateTime.getMinuteOfDay());

			count++;
		}
	}

	public void insertProductOrderUsersByDateRange(List<ProductOrderUsersByDateRange> productOrders) {

		LOG.debug("Inserting to product_orders_user_date_range");

		BoundStatement boundStatement = insertProductOrdersUserRangeStatement.bind();

		int count = 1;
		for (ProductOrderUsersByDateRange productOrder : productOrders) {

			this.session.execute(boundStatement.bind(productOrder.getUserId(), productOrder.getDate(), productOrder
					.getOrderId(), productOrder.getProductId(), new java.sql.Timestamp(productOrder.getOrderDate()
					.getTime())));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}
			count++;
		}
	}

	public void insertProductOrderUsersByDateHourRange(List<ProductOrderUsersByDateTimeUnitRange> productOrders) {

		LOG.debug("Inserting to product_orders_user_hour_range");

		BoundStatement boundStatement = insertProductOrdersUserHourRangeStatement.bind();

		int count = 1;
		for (ProductOrderUsersByDateTimeUnitRange productOrder : productOrders) {

			this.session.execute(boundStatement.bind(productOrder.getUserId(), productOrder.getTimeUnit(),
					productOrder.getDate(), productOrder.getOrderId(), productOrder.getProductId(),
					new java.sql.Timestamp(productOrder.getOrderDate().getTime())));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}
			count++;
		}
	}

	public void insertProductOrderUsersByDateMinuteRange(List<ProductOrderUsersByDateTimeUnitRange> productOrders) {

		LOG.debug("Inserting to product_orders_user_minute_range");

		BoundStatement boundStatement = insertProductOrdersUserMinuteRangeStatement.bind();

		int count = 1;
		for (ProductOrderUsersByDateTimeUnitRange productOrder : productOrders) {

			this.session.execute(boundStatement.bind(productOrder.getUserId(), productOrder.getTimeUnit(),
					productOrder.getDate(), productOrder.getOrderId(), productOrder.getProductId(),
					new java.sql.Timestamp(productOrder.getOrderDate().getTime())));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}
			count++;
		}
	}

	public void insertProductOrderUsersByDateSecondRange(List<ProductOrderUsersByDateTimeUnitRange> productOrders) {

		LOG.debug("Inserting to product_orders_user_second_range");

		BoundStatement boundStatement = insertProductOrdersUserSecondRangeStatement.bind();

		int count = 1;
		for (ProductOrderUsersByDateTimeUnitRange productOrder : productOrders) {

			this.session.execute(boundStatement.bind(productOrder.getUserId(), productOrder.getTimeUnit(),
					productOrder.getDate(), productOrder.getOrderId(), productOrder.getProductId(),
					new java.sql.Timestamp(productOrder.getOrderDate().getTime())));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}
			count++;
		}
	}
	
	public void insertProductOrdersByUser(List<ProductOrdersByUser> productOrdersByUserList,
			Map<String, Account> accounts) {

		LOG.debug("Inserting to product_orders_by_user");

		BoundStatement boundStatement = insertProductOrdersByUserStatement.bind();

		int count = 1;

		for (ProductOrdersByUser productOrdersByUser : productOrdersByUserList) {

			this.session.execute(boundStatement.bind(productOrdersByUser.getUserId(), productOrdersByUser.getOrderId(),
					productOrdersByUser.getOrderDate(), productOrdersByUser.getProductId(),
					productOrdersByUser.getProductName(), productOrdersByUser.getUnitPrice(),
					productOrdersByUser.getQuantity(),
					Double.parseDouble(df.format(productOrdersByUser.getTotalPrice())),
					productOrdersByUser.getVendor(), productOrdersByUser.getDescriptions()));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}

			count++;
		}
	}

	public Account getUserAccountBalance(String userId){
		BoundStatement bound = new BoundStatement(this.getAccountsStatementByUser);
		BoundStatement boundGetLastOrders = new BoundStatement(this.getLastOrdersForUserStatement);
			
		ResultSet accountResultSet = session.execute(bound.bind(userId));		
		Account account = QueryObjectMapper.mapAccount(accountResultSet.one());
		
		double balance = account.getBalance();
		
		
		ResultSet resultSet = this.session.execute(boundGetLastOrders.bind(userId, account.getLastUpdated()));

		for (Row orderRow : resultSet) {
			balance = balance + orderRow.getDouble("total_price");
		}

		// Insert new balance at the time of the last order processed.
		account.setBalance(Double.parseDouble(df.format(balance)));

		return account;
	}
	
	private void updateAccountWithBalance() {
		BoundStatement bound = new BoundStatement(this.getAccountsStatement);
		BoundStatement boundGetLastOrders = new BoundStatement(this.getLastOrdersForUserStatement);
		List<Account> accounts = new ArrayList<Account>();

		ResultSet results = this.session.execute(bound.bind());

		for (Row row : results) {
			Account account = QueryObjectMapper.mapAccount(row);
			accounts.add(account);
		}
		results = null;

		for (Account account : accounts) {
			double balance = account.getBalance();
			Date lastUpdated = account.getLastUpdated();

			// Get all order rows for this user since it was last updated
			ResultSet resultSet = this.session.execute(boundGetLastOrders.bind(account.getUserId(),
					account.getLastUpdated()));

			for (Row orderRow : resultSet) {
				lastUpdated = orderRow.getDate("order_date");
				balance = balance + orderRow.getDouble("total_price");
			}

			// Insert new balance at the time of the last order processed.
			account.setBalance(Double.parseDouble(df.format(balance)));
			account.setLastUpdated(lastUpdated);
		}
		this.insertAccounts(accounts);
	}
		
	public void insertProductOrdersByVendor(List<ProductOrdersByVendor> productOrdersByVendor) {

		LOG.debug("Inserting to product_orders_by_vendor");

		BoundStatement boundStatement = insertProductOrdersByVendorStatement.bind();

		int count = 1;

		for (ProductOrdersByVendor productOrder : productOrdersByVendor) {

			this.session.execute(boundStatement.bind(productOrder.getVendor(), productOrder.getOrderId(),
					productOrder.getProductId(), productOrder.getProductName(), productOrder.getRecommendation(),
					productOrder.getUnitPrice(), productOrder.getQuantity(),
					Double.parseDouble(df.format(productOrder.getTotalPrice()))));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}
			count++;
		}
	}

	public void insertUsersByProduct(List<UsersByProduct> usersByProduct) {

		LOG.debug("Inserting to users_by_product");

		BoundStatement boundStatement = insertUserByProductStatement.bind();

		int count = 1;

		for (UsersByProduct user : usersByProduct) {

			this.session.execute(boundStatement.bind(user.getProductId(), user.getProductName(), user.getUserId(),
					user.getFirstname(), user.getLastname(), user.getDob(), user.getStreetAddress(), user.getZipCode(),
					user.getCityName(), user.getStateName(), user.getPhoneNumber(), user.getEmail()));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}
			count++;
		}

	}

	public void insertUsersByVendor(List<UsersByVendorProduct> usersByVendor) {

		LOG.debug("Inserting to users_by_vendor");

		BoundStatement boundStatement = insertUserByVendorProductStatement.bind();

		int count = 1;

		for (UsersByVendorProduct user : usersByVendor) {

			this.session.execute(boundStatement.bind(user.getVendor(), user.getUserId(), user.getFirstname(),
					user.getLastname(), user.getDob(), user.getStreetAddress(), user.getZipCode(), user.getCityName(),
					user.getStateName(), user.getPhoneNumber(), user.getEmail()));

			if (count % 1000 == 0) {
				LOG.debug("Inserted " + count + " records");
			}
			count++;
		}
	}

	private void incrementCounter(String table, String col, String date, String counterType, int counterValue) {

		String cql = "update " + table + " set " + col + "=" + col + " + 1 where date='" + date + "' and "
				+ counterType + " = " + counterValue;
		this.session.execute(cql);
	}

	public void insertMovingAverages(List<MovingAverageObject> movingAverages) {

		if (movingAverages == null || movingAverages.size() < 0) {
			return;
		}

		BoundStatement statement = insertMovingAverageStatement.bind();

		for (MovingAverageObject object : movingAverages) {
			this.session.execute(statement.bind(object.getDesc(), object.getTimeUnit(), object.getSize(),
					object.getValue()));
		}
	}
	
	// Date Retrieval methods	
	public List<MovingAverageObject> getMovingAverages() {

		List<MovingAverageObject> orders = new ArrayList<MovingAverageObject>();

		BoundStatement bound = new BoundStatement(this.getRealTimeAnalytic);

		ResultSet results = session.execute(bound.bind());

		for (Row row : results) {

			orders.add(QueryObjectMapper.mapMovingAverage(row));
		}

		return orders;
	}

	public List<ProductOrdersByUser> getProductOrdersByUser(String userId, int size) {

		List<ProductOrdersByUser> orders = new ArrayList<ProductOrdersByUser>();

		BoundStatement bound = new BoundStatement(this.getProductOrdersByUserLimit);

		ResultSet results = session.execute(bound.bind(userId));

		for (Row row : results) {

			orders.add(QueryObjectMapper.mapProductOrderByUser(row));
		}

		return orders;
	}

	public List<ProductOrdersByDate> getProductOrdersByDate(String date) {
		List<ProductOrdersByDate> orders = new ArrayList<ProductOrdersByDate>();

		Query query = QueryBuilder.select().all().from("product_orders_by_date");
		ResultSet results = session.execute(query);

		for (Row row : results) {
			orders.add(QueryObjectMapper.mapProductOrderByDate(row));
		}

		return orders;
	}

	public List<ProductOrdersByDateTimeUnit> getProductOrdersByHour(String date, int hourStart, int hourEnd) {
		List<ProductOrdersByDateTimeUnit> orders = new ArrayList<ProductOrdersByDateTimeUnit>();

		Query query = QueryBuilder.select().all().from("product_orders_by_hour");
		ResultSet results = session.execute(query);

		for (Row row : results) {
			orders.add(QueryObjectMapper.mapProductOrderByDateHour(row));
		}

		return orders;
	}

	public List<ProductOrdersByDateTimeUnit> getProductOrdersByMinute(String date, int minuteStart, int minuteEnd) {
		List<ProductOrdersByDateTimeUnit> orders = new ArrayList<ProductOrdersByDateTimeUnit>();

		Query query = QueryBuilder.select().all().from("product_orders_by_date");
		ResultSet results = session.execute(query);

		for (Row row : results) {
			orders.add(QueryObjectMapper.mapProductOrderByDateMinute(row));
		}

		return orders;
	}

	public List<ProductOrdersByDateTimeUnit> getProductOrdersBySecond(String date, int secondStart, int secondEnd) {
		List<ProductOrdersByDateTimeUnit> orders = new ArrayList<ProductOrdersByDateTimeUnit>();

		BoundStatement bound = new BoundStatement(getProductOrdersByDateSecondStatement);
		// bound.enableTracing();
		ResultSet results = session.execute(bound.bind(date, secondStart, secondEnd));

		// ExecutionInfo executionInfo = results.getExecutionInfo();
		// printQueryTrace(executionInfo);

		for (Row row : results) {
			orders.add(QueryObjectMapper.mapProductOrderByDateSecond(row));
		}

		return orders;
	}

	public Map<Integer, Integer> getCountByDay() {

		ResultSet resultSet = this.session.execute("select  * from order_date_hour_counter LIMIT 10 ");

		Map<Integer, Integer> hourCount = new LinkedHashMap<Integer, Integer>();

		for (Row row : resultSet) {
			hourCount.put(row.getInt("hour"), new Long(row.getLong("total_for_hour")).intValue());
		}

		return hourCount;
	}

	public Map<Integer, Integer> getHourCountByDay(String date) {

		ResultSet resultSet = this.session
				.execute("select * from order_date_hour_counter where date = '" + date + "'");

		Map<Integer, Integer> hourCount = new LinkedHashMap<Integer, Integer>();

		for (Row row : resultSet) {
			hourCount.put(row.getInt("hour"), new Long(row.getLong("total_for_hour")).intValue());
		}

		return hourCount;
	}

	public Map<Integer, Integer> getMinuteCountByDay(String date, int startMin, int endMin) {

		ResultSet resultSet = this.session.execute("select * from order_date_minute_counter " + "where date = '" + date
				+ "' and minute >= " + startMin + " and minute < " + endMin);

		Map<Integer, Integer> hourCount = new LinkedHashMap<Integer, Integer>();

		for (Row row : resultSet) {
			hourCount.put(row.getInt("minute"), new Long(row.getLong("total_for_minute")).intValue());
		}

		return hourCount;
	}

	public List<ProductOrdersByVendor> getProductOrdersByVendor(String vendor) {
		List<ProductOrdersByVendor> orders = new ArrayList<ProductOrdersByVendor>();

		BoundStatement bound = new BoundStatement(getProductOrdersByVendorStatement);
		ResultSet results = session.execute(bound.bind(vendor));

		for (Row row : results) {
			orders.add(QueryObjectMapper.mapProductOrderByVendor(row));
		}

		return orders;
	}
	
	public Map<String, Integer> getHighRecommendByVendor(String vendor){
		
		Map<String, Integer> productCount = new LinkedHashMap<String, Integer>();

		BoundStatement bound = new BoundStatement(this.getHighRecommendByVendor);
		ResultSet results = session.execute(bound.bind(vendor));

		for (Row row : results) {
			productCount.put(row.getString("product_name"), row.getInt("count"));
		}

		return productCount;
	}

	public Map<String, Integer> getHighRecommendByProduct(String product){
		
		Map<String, Integer> productCount = new LinkedHashMap<String, Integer>();

		BoundStatement bound = new BoundStatement(getProductOrdersByVendorStatement);
		ResultSet results = session.execute(bound.bind(product));

		for (Row row : results) {
			productCount.put(row.getString("product_name"), row.getInt("count"));
		}

		return productCount;
	}
	
	
	private void printQueryTrace(ExecutionInfo executionInfo) {
		System.out.printf("Host (queried): %s\n", executionInfo.getQueriedHost().toString());
		for (Host host : executionInfo.getTriedHosts()) {
			System.out.printf("Host (tried): %s\n", host.toString());
		}
		QueryTrace queryTrace = executionInfo.getQueryTrace();
		System.out.printf("Trace id: %s\n\n", queryTrace.getTraceId());
		System.out.println("---------------------------------------+--------------+------------+--------------");
		for (QueryTrace.Event event : queryTrace.getEvents()) {
			System.out.printf("%38s | %12s | %10s | %12s\n", event.getDescription(), millis2Date(event.getTimestamp()),
					event.getSource(), event.getSourceElapsedMicros());
		}
	}

	// Utility methods.
	private static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");

	private String millis2Date(long timestamp) {
		return format.format(timestamp);
	}

	public void shutdown() {
		this.session.shutdown();
		this.cluster.shutdown();
	}
}