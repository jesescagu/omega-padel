INSERT INTO Usuario(usuario,contrasenya,activo) VALUES ('admin1','admin1',TRUE);
INSERT INTO Usuario(usuario,contrasenya,activo) VALUES ('cliente1','cliente1',TRUE);
INSERT INTO Usuario(usuario,contrasenya,activo) VALUES ('cliente2','cliente2',TRUE);
INSERT INTO Usuario(usuario,contrasenya,activo) VALUES ('empleado1','empleado1',TRUE);
INSERT INTO Usuario(usuario,contrasenya,activo) VALUES ('empleado2','empleado2',TRUE);

INSERT INTO roles(nombre_usuario,rol) VALUES ('admin1','admin');
INSERT INTO roles(nombre_usuario,rol) VALUES ('cliente1','cliente');
INSERT INTO roles(nombre_usuario,rol) VALUES ('cliente2','cliente');
INSERT INTO roles(nombre_usuario,rol) VALUES ('empleado1','empleado');
INSERT INTO roles(nombre_usuario,rol) VALUES ('empleado2','empleado');

INSERT INTO cliente(dni,nombre,apellido1,apellido2,email,usuario) VALUES ('11111111A','Pepe','Amador','Jerez','paj@google.es','cliente1');
INSERT INTO cliente(dni,nombre,apellido1,apellido2,email,usuario) VALUES ('11111111B','Juan','Perez','Lopez','jpl@google.es','cliente2');
INSERT INTO empleado(dni,nombre,apellido1,apellido2,email,usuario) VALUES ('11111111C','Paco','Soria','Aguilar','psa@google.es','empleado1');
INSERT INTO empleado(dni,nombre,apellido1,apellido2,email,usuario) VALUES ('11111111D','Luis','Lopez','Escobar','lle@google.es','empleado2');

INSERT INTO pala(stock,marca,modelo,temporada) VALUES (1000,'head','alpha',2020);
INSERT INTO pala(stock,marca,modelo,temporada) VALUES (99,'adidas','k90',2021);
INSERT INTO pala(stock,marca,modelo,temporada) VALUES (3,'bullpadel','shot',2019);
INSERT INTO pala(stock,marca,modelo,temporada) VALUES (500,'asics','run',2021);
INSERT INTO pala(stock,marca,modelo,temporada) VALUES (50,'nox','silver',2020);

INSERT INTO pelota(stock,marca,modelo,numero) VALUES (2000,'head','normal',3);
INSERT INTO pelota(stock,marca,modelo,numero) VALUES (53,'siux','power',3);
INSERT INTO pelota(stock,marca,modelo,numero) VALUES (100,'nox','control',9);


INSERT INTO tipo_accesorio(nombre) VALUES ('camiseta');
INSERT INTO tipo_accesorio(nombre) VALUES ('muñequera');
INSERT INTO tipo_accesorio(nombre) VALUES ('calzonas');


INSERT INTO accesorio(stock,marca,modelo,tipo) VALUES (150,'head','sanyo','camiseta');
INSERT INTO accesorio(stock,marca,modelo,tipo) VALUES (30,'nike','cortez','calzonas');
INSERT INTO accesorio(stock,marca,modelo,tipo) VALUES (69,'adidas','leo','camiseta');
INSERT INTO accesorio(stock,marca,modelo,tipo) VALUES (73,'asics','adipower',muñequera);