drop table realtime_analytic;

create table realtime_analytic(
	description text,
	time_unit text,
	size int,
	value double,
	PRIMARY KEY(description, time_unit, size)
) WITH compaction={'class': 'LeveledCompactionStrategy'};