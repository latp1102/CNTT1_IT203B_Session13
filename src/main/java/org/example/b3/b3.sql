 create database b3;
 use b3;
 
create table patient_wallet(
	id int primary key,
    balance double
);

create table patient(
	id int primary key,
    status varchar(20)
);
create table bed(
    id int primary key,
    status varchar(20)
);
insert into patient_wallet values(1, 1000);
insert into patient values(1, 'DISCHARGED');
insert into bed values(1, 'AVAILABLE');