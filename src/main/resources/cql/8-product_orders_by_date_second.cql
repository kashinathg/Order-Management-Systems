//All product orders within a given second for a specified day.

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
