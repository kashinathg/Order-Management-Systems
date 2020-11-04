use order_management;

drop table product_orders_by_user;

create table product_orders_by_user(
	user_id text,
	order_id text,
	order_date timestamp,
	product_id text,
	product_name text,
	unit_price double,
	quantity int,
	total_price double,
	vendor text,
	descriptions set<text>,
	PRIMARY KEY (user_id, order_date, order_id)
) WITH CLUSTERING ORDER BY(order_date DESC)
AND compaction={'class': 'SizeTieredCompactionStrategy'};

drop table product_orders_by_vendor;

create table product_orders_by_vendor(
	vendor text,
	order_id text,
	product_id text,
	product_name text,
	recommendation text,
	unit_price double,
	quantity int,
	total_price double,
	PRIMARY KEY (vendor, order_id, product_id)
) WITH compaction={'class': 'SizeTieredCompactionStrategy'};

drop table users_by_product;

create table users_by_product(
	product_id text,
	product_name text,
	user_id text,
	first_name text,
	last_name text,
	dob timestamp,
	street_address text,
	zip_code text,
	city_name text,
	state_name text,
	phone_number text,
	email text,
	PRIMARY KEY (product_id, user_id)
);

drop table users_by_vendor_product;

create table users_by_vendor_product(
	vendor text,
	user_id text,
	first_name text,
	last_name text,
	dob timestamp,
	street_address text,
	zip_code text,
	city_name text,
	state_name text,
	phone_number text,
	email text,
	PRIMARY KEY (vendor, user_id)
);


drop table product_orders_by_date;
 
create table product_orders_by_date(
	date text,
	order_id text,
	order_date timestamp,
	product_id text,
	product_name text,
	unit_price double,
	quantity int,
	total_price double,
	PRIMARY KEY(date, order_date, order_id)
)WITH CLUSTERING ORDER BY (order_date DESC);

drop table product_orders_by_date_hour;

create table product_orders_by_date_hour(
	date text,
	hour_start int,
	order_id text,
	order_date timestamp,
	product_id text,
	product_name text,
	unit_price double,
	quantity int,
	total_price double,
	PRIMARY KEY ((date, hour_start), order_date, order_id)
) WITH CLUSTERING ORDER BY (order_date DESC);

drop table product_orders_by_date_minute;

create table product_orders_by_date_minute(
	date text,
	minute_start int,
	order_id text,
	order_date timestamp,
	product_id text,
	product_name text,
	unit_price double,
	quantity int,
	total_price double,
	PRIMARY KEY ((date, minute_start),order_date, order_id)
) WITH CLUSTERING ORDER BY (order_date DESC);

drop table product_orders_by_date_second;

create table product_orders_by_date_second(
	date text,
	second_start int,
	order_id text,
	order_date timestamp,
	product_id text,
	product_name text,
	unit_price double,
	quantity int,
	total_price double,
	PRIMARY KEY (date, second_start, order_id)
) WITH CLUSTERING ORDER BY (second_start ASC);

drop table product_orders_user_date_range;

create table product_orders_user_date_range (
	user_id text,
	date timestamp,
	order_id text,
	product_id text,
	order_date timestamp,
	PRIMARY KEY(user_id, date, order_id)
) WITH CLUSTERING ORDER BY (date DESC);

drop table product_orders_user_hour_range;

create table product_orders_user_hour_range (
	user_id text,
	hour int,
	date timestamp,
	order_id text,
	product_id text,
	order_date timestamp,
	PRIMARY KEY((user_id, hour), date, order_id)
) WITH CLUSTERING ORDER BY (date desc);

drop table product_orders_user_minute_range;

create table product_orders_user_minute_range (
	user_id text,
	minute int,
	date timestamp,
	order_id text,
	product_id text,
	order_date timestamp,
	PRIMARY KEY((user_id, minute), date, order_id)
) WITH CLUSTERING ORDER BY (date desc);


drop table product_orders_user_second_range;

create table product_orders_user_second_range (
	user_id text,
	second int,
	date timestamp,
	order_id text,
	product_id text,
	order_date timestamp,
	PRIMARY KEY((user_id, second), date, order_id)
) WITH CLUSTERING ORDER BY (date desc);

drop table accounts;

create table accounts(	
	user_id text,
	account_id text,
	last_updated timestamp,
	balance double,
	PRIMARY KEY (user_id)
) WITH compaction={'class': 'LeveledCompactionStrategy'};

drop table order_date_counter;

create table order_date_counter (
	datename text,
	date text,
	total_for_date counter,
	PRIMARY KEY (datename, date)	
) WITH CLUSTERING ORDER BY (date desc);

drop table order_date_hour_counter;

create table order_date_hour_counter (
	date text,
	hour int,
	total_for_hour counter,	
	PRIMARY KEY  (date, hour)	
);

drop table order_date_minute_counter;

create table order_date_minute_counter (
	date text,
	minute int,
	total_for_minute counter,
	PRIMARY KEY  (date, minute)	
);

create table products(
	product_id text PRIMARY KEY,
	product_name text,
	unit_price double,
	vendor text,
	recommendation text, 
	descriptions set<text>
);	

drop table realtime_analytic;

create table realtime_analytic(
	description text,
	time_unit text,
	size int,
	value double,
	PRIMARY KEY(description, time_unit, size)
) WITH compaction={'class': 'LeveledCompactionStrategy'};


drop table users;

create table users(
	user_id text PRIMARY KEY,
	first_name text,
	middle_name text,
	last_name text,
	dob timestamp,
	street_address text,
	zip_code text,
	city_name text,
	state_name text,
	gender text,
	phone_number text,
	email text,
	country_code text
) WITH COMPACT STORAGE;

drop table products_by_product_hive;

create table products_by_product_hive(
	recommendation text,
	product_id text,
	count int,
	product_name text,	
	vendor text,
	PRIMARY KEY (recommendation, count, product_id)
) WITH CLUSTERING ORDER BY (count desc);

drop table products_by_vendor_hive;

create table products_by_vendor_hive(
	recommendation text,
	vendor text,
	product_id text,
	count int,
	product_name text,	
	PRIMARY KEY (recommendation, vendor, count, product_id)
) WITH CLUSTERING ORDER BY (vendor asc, count desc);