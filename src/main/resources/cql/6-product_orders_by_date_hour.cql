//All product orders within a given hourly range for a specified day.

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
