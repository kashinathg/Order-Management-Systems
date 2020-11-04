//Allows queries to see the most ordered products by recommendation
//select * from products_by_product_hive where recommendation ='highly recommend' limit 10;

insert into table products_by_product_hive

select recommendation, COUNT(distinct order_id) AS MyCount, product_id, product_name, vendor
from product_orders_by_vendor  
group by recommendation, product_id, product_name, vendor sort by MyCount DESC;