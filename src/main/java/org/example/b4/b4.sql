create database b4;
use b4;

create table patient(
	maBN varchar(10) primary key,
    tenBN varchar(100)
);
create table DichVuSuDung(
	id int auto_increment primary key,
    maBN varchar(10),
    tenDichVu varchar(100),
    foreign key (maBN)references patient(maBN)
);
insert into patient(maBN, tenBN) values
('BN01', 'Nguyen van A');