//One for date, hours and minutes;
drop table order_date_counter;

	create table order_date_counter (
		datename text,
		date text,
		total_for_date counter,
		PRIMARY KEY (datename, date)	
	) WITH CLUSTERING ORDER BY (date desc);

drop table order_date_hour_counter;

create table order_date_hour_counter (
	date text,
	hour int,
	total_for_hour counter,	
	PRIMARY KEY  (date, hour)	
); 

drop table order_date_minute_counter;

create table order_date_minute_counter (
	date text,
	minute int,
	total_for_minute counter,
	PRIMARY KEY  (date, minute)	
);
