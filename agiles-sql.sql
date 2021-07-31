create database agiles_pruebas; 
use agiles_pruebas; 

create table usuario(
ced_usu varchar(10) primary key,
nom_usu varchar(15) not null, 
ape_usu varchar(15) not null);

create table item(
id_ite varchar(3) primary key, 
nom_ite varchar(25) not null, 
des_ite text);

create table activo(
id_act varchar(3) primary key, 
ced_usu_act varchar(10) not null, 
id_ite_act varchar(3) not null,
foreign key (ced_usu_act) references usuario(ced_usu), 
foreign key (id_ite_act) references item(id_ite));

create table proceso(
id_pro int auto_increment primary key, 
nom_pro varchar(30) not null, 
fec_cre_pro date not null,
est_pro varchar(12) not null); 

create table detalle_proceso(
id_pro_det int not null, 
id_act_det varchar(3) not null, 
rev_act_det boolean not null, 
est_act_det varchar(15) not null, 
obs_act_det text, 
foreign key (id_pro_det) references proceso(id_pro),
foreign key (id_act_det) references activo(id_act));


insert into usuario values("123", "Richard", "Carrion"); 
insert into usuario values("456", "Abraham", "Miranda"); 
insert into usuario values("789", "Alejandro", "Barrera"); 

insert into item values("001", "Silla", "Silla de MiComisariato"); 
insert into item values("002", "Computadora", "Intel i7, 16GB de RAM ..."); 
insert into item values("003", "Mesa", "Mesa de MiComisariato"); 

insert into activo values("1", "123", "001"); 
insert into activo values("2", "456", "002"); 
insert into activo values("3", "789", "003"); 


insert into activo values("4", "123", "002"); 
insert into activo values("5", "123", "003"); 
