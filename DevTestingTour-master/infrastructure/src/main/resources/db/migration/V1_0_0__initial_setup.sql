create sequence hibernate_sequence start with 1 increment by 1;
create table train (row_id integer not null, booking varchar(255), coach varchar(255), seat integer, train varchar(255), primary key (row_id));
alter table train add constraint train_business_key unique (train, coach, seat);
