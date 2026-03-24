create database b5;
use b5;

create table beds(
	bed_id varchar(10) primary key,
    status varchar(20)
);
create table patients(
	patient_id int auto_increment primary key,
    name varchar(100),
    age int,
    bed_id varchar(10),
    foreign key (bed_id) references beds(bed_id)
);
create table payments(
	id int auto_increment primary key,
    patient_id int,
    amount double,
    foreign key (patient_id) references patients(patient_id)
);