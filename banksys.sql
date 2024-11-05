create database bankms;
use bankms;
create table signup(form_no varchar(30), name varchar(30), father_name varchar(30), DOB varchar(30), gender varchar(30), email varchar(60), marital_status varchar(30), address varchar(60), city varchar(30), pincode varchar(30), state varchar(50));
select * from signup;

create table signuptwo(form_no varchar(30), religion varchar(30), category varchar(30), income varchar(30), education varchar(30), occupation varchar(60), Pan varchar(30), aadhar varchar(60), seniorcitizen varchar(30), existing_account varchar(30));
select * from signuptwo;

create table signupthree(form_no varchar(30), account_type varchar(40), card_number varchar(30), pin varchar(30), facility varchar(200));
select * from signupthree;

create table login(form_no varchar(30), card_number varchar(50), pin varchar(30));
select * from login;

create table bank(pin varchar(10), date varchar(50), type varchar(20), amount varchar(20));
select * from bank;

CREATE TABLE admin_users (
    username VARCHAR(30),
    password VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO admin_users (username, password) 
VALUES ('1234', '1234');

DROP TABLE IF EXISTS admin_users;
CREATE TABLE admin_users (
    admin_id varchar(30) PRIMARY KEY,
    username VARCHAR(30) UNIQUE,
    password VARCHAR(100),
    employee_name VARCHAR(50),
    email VARCHAR(50),
    phone VARCHAR(15),
    designation VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
select * from admin_users;
