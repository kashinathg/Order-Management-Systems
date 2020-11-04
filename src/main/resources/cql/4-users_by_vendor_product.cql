//All users that have purchased any product from a specific vendor.

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

INSERT INTO users_by_vendor_product (vendor, user_id, first_name, last_name, dob, street_address, zip_code, city_name, state_name, phone_number, email)