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
