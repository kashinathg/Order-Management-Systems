//All product orders associated with a given day.

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
