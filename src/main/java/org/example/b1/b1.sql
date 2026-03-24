create database b1;
use b1;
drop table if exists hopitals;
create table hopitals(
	id int auto_increment primary key,
    name varchar(20),
    quantity int
);
insert into hopitals(id, name, quantity)
values(1, 'Paracetamol', '5');