create table products(
	product_id text PRIMARY KEY,
	product_name text,
	unit_price double,
	vendor text,
	recommendation text, 
	descriptions set<text>
);	
