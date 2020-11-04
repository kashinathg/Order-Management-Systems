drop table accounts;

create table accounts(	
	user_id text,
	account_id text,
	last_updated timestamp,
	balance double,
	PRIMARY KEY (user_id)
) WITH compaction={'class': 'LeveledCompactionStrategy'};
