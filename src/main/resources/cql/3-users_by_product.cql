//All users that have purchased a specific product

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

INSERT INTO users_by_product (product_id, product_name, user_id,first_name, last_name, dob, street_address, zip_code, city_name, state_name, phone_number, email)

