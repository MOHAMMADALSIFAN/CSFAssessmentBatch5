-- IMPORTANT: DO NOT CHANGE THE GIVEN SCHEMA UNLESS YOU HAVE A GOOD REASON
-- IF YOU DO CHANGE IT WRITE THE JUSTIFICATION IN A COMMENT ABOVE THE CHANGE

drop database if exists restaurant;

create database restaurant;

use restaurant;

create table customers (
  username varchar(64) not null,
  password varchar(128) not null,
  -- add PRIMARY key to username in customers table 
  constraint pk_username primary key (username)
);

insert into customers(username, password) values
  ('fred', sha2('fred', 224)),
  ('barney', sha2('barney', 224)),
  ('wilma', sha2('wilma', 224)),
  ('betty', sha2('betty', 224)),
  ('pebbles', sha2('pebbles', 224));

-- TODO: Task 1.2
-- Write your task 1.2 below
CREATE TABLE place_orders (
  order_id CHAR(8),
  payment_id VARCHAR(128) NOT NULL UNIQUE,
  order_date DATE NOT NULL,
  total DECIMAL(9,2) NOT NULL,
  username VARCHAR(64) NOT NULL,
  constraint pk_order_id primary key (order_id),
  constraint fk_customers_username foreign key (username) references customers(username)
);