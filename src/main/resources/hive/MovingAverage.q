--Access the data in cassandra
create table moving_average_minute (
        order_day text,
        minute int,
        total_price double,
        moving_average double,
        PRIMARY KEY (order_day, minute)
);
        
--first calculate returns
DROP TABLE IF EXISTS second_totals;
CREATE TABLE second_totals(date_day string, second int, sum_per_second double)
STORED AS SEQUENCEFILE;

INSERT OVERWRITE TABLE second_totals
select to_date(order_date) orderdate, second_start, sum(total_price)
from product_orders_by_date_second
group by order_date, second_start
order by orderdate ASC, second_start ASC;

INSERT OVERWRITE TABLE moving_average_second
select a.date_day, a.second, a.sum_per_second, avg(b.sum_per_second) as moving_average
from second_totals a JOIN second_totals b ON (true)
where   a.date_day = b.date_day AND
        b.second >
        a.second-60 AND
        b.second <= a.second
group by a.date_day, a.second, a.sum_per_second
order by date_day ASC, second ASC;

