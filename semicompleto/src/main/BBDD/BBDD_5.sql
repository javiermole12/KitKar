SET SQL_SAFE_UPDATES = 0;
drop database if exists PiezasCoche;
create database PiezasCoche;
use PiezasCoche;

-- Creacion tabla usuarios
create table Usuarios (
id int primary key auto_increment,
userName varchar (50) not null,
contrasena varchar (50) not null,
rol varchar (50) not null
);


-- Creacion tabla stock
create table Stock (
                       id int primary key auto_increment,
                       precio double not null,
                       nombre varchar (50) not null,
                       cantidad int not null
);

-- Creación tabla pedidos
create table Pedidos (
                         id int primary key auto_increment,
                         nom_prov varchar (50) not null,
                         ape_prov varchar (50) not null,
                         piezas varchar(50) not null,
                         total double not null
);

-- Insercion datos tabla Stock
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (1, 50.99, 'Filtro de aire', 100);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (2, 25.75, 'Bujía', 150);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (3, 120.50, 'Freno de disco', 80);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (4, 35.25, 'Pastillas de freno', 120);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (5, 80.00, 'Amortiguador', 90);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (6, 55.99, 'Batería', 110);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (7, 15.50, 'Filtro de aceite', 200);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (8, 70.25, 'Radiador', 70);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (9, 40.00, 'Correa de distribución', 100);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (10, 65.75, 'Lámpara delantera', 150);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (11, 30.00, 'Escobilla limpiaparabrisas', 120);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (12, 90.99, 'Alternador', 90);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (13, 20.50, 'Filtro de combustible', 200);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (14, 45.25, 'Embrague', 70);
INSERT INTO Stock (id, precio, nombre, cantidad) VALUES (15, 55.00, 'Termostato', 100);

-- Inserción datos tabla Pedidos
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (1, "Juan", "Alonso", 'Filtro de aire', 509.90);
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (2, 'Alberto', "Fernandez", 'Bujía', 257.50);
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (3, 'Fernando', "Martinez", 'Freno de disco', 1205.00);
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (4, 'Juan', "Alonso", 'Pastillas de freno', 352.50);
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (5, 'Ivan', "Perez", 'Amortiguador', 800.00);
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (6, 'Pablo', "Marquez", 'Batería', 559.90);
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (7, 'Marcos', "Alonso", 'Filtro de aceite', 310.00);
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (8, 'Javier', "Sanchez", 'Radiador', 702.50);
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (9, 'Pedro', "Alonso", 'Correa de distribución', 400.00);
INSERT INTO Pedidos (id, nom_prov, ape_prov, piezas, total) VALUES (10, 'Aimar', "Castillejo", 'Lámpara delantera', 657.50);


-- Insercion datos tabla Usuarios
INSERT INTO Usuarios (id, username, contrasena, rol) VALUES (1, "admin","0000","admin");
INSERT INTO Usuarios (id, username, contrasena, rol) VALUES (2, "lector","0000","lector");

UPDATE Usuarios SET rol = "Mozo de Almacen" WHERE rol = "admin";
UPDATE Usuarios SET rol = "Proveedor" WHERE rol = "lector";

UPDATE Usuarios SET username = "Mozo" WHERE username = "admin";
UPDATE Usuarios SET username = "Proveedor" WHERE username = "lector";

select * from Pedidos;