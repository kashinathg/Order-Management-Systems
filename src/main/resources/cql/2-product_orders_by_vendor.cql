//All product orders associated with each individual Vendor.

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
