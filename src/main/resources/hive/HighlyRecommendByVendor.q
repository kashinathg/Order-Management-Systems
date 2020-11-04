//Allows queries to see the most ordered products by recommendation
//select * from products_by_vendor_hive where recommendation ='highly recommend' limit 10;

insert into table products_by_vendor_hive
select recommendation, vendor, COUNT(distinct order_id) AS MyCount, product_id, product_name 
from product_orders_by_vendor 
group by recommendation, vendor, product_id, product_name sort by vendor, MyCount DESC;

