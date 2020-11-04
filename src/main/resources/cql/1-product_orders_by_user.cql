//All product orders associated with each individual user (the Shopping Cart). Include all descriptive //attributes for each product.

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


INSERT INTO product_orders_by_user(user_id, order_id, order_date, product_id) VALUES ('U1', 'O1', '2013-08-09', 'P1');
INSERT INTO product_orders_by_user(user_id, order_id, order_date, product_id) VALUES ('U1', 'O2', '2013-08-09', 'P2');
INSERT INTO product_orders_by_user(user_id, order_id, order_date, product_id) VALUES ('U1', 'O3', '2013-08-09', 'P3');
INSERT INTO product_orders_by_user(user_id, order_id, order_date, product_id) VALUES ('U1', 'O4', '2013-08-09', 'P4');
INSERT INTO product_orders_by_user(user_id, order_id, order_date, product_id) VALUES ('U1', 'O5', '2013-08-10', 'P5');
INSERT INTO product_orders_by_user(user_id, order_id, order_date, product_id) VALUES ('U1', 'O6', '2013-08-10', 'P3');

INSERT INTO product_orders_by_user(user_id, order_id, order_date, product_id, product_name, unit_price, quantity, total_price, vendor, descriptions set<text>)

