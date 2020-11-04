//All product orders for an individual within any specified day.

drop table product_orders_user_date_range;

create table product_orders_user_date_range (
	user_id text,
	date timestamp,
	order_id text,
	product_id text,
	order_date timestamp,
	PRIMARY KEY(user_id, date, order_id)
) WITH CLUSTERING ORDER BY (date DESC);




INSERT INTO product_orders_user_hour_range (user_id, date, order_id, product_id, order_date) VALUES ('U1', 0, '2020-08-09', 'O1', 'P1');