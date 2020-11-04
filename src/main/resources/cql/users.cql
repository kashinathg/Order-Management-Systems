drop table users;

create table users(
	user_id text PRIMARY KEY,
	first_name text,
	middle_name text,
	last_name text,
	dob timestamp,
	street_address text,
	zip_code text,
	city_name text,
	state_name text,
	gender text,
	phone_number text,
	email text,
	country_code text
) WITH COMPACT STORAGE;