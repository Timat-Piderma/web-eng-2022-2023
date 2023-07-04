/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+02:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

drop database if exists progettoDB;
create database progettoDB;
use progettoDB;

DROP USER IF EXISTS 'website'@'localhost';
CREATE USER 'website'@'localhost' IDENTIFIED BY 'webpass';
GRANT ALL ON progettoDB.* TO 'website'@'localhost';

create table Gruppo (
	ID integer auto_increment primary key,
    nome varchar(255) not null unique,
    descrizione varchar(255) not null,
    `version` int unsigned NOT NULL DEFAULT '1'
);

create table Posizione (
	ID integer auto_increment primary key,
    luogo varchar(255) not null,
    edificio varchar(255) not null,
    piano varchar(255) not null,
    constraint posizione unique (luogo, edificio, piano),
    `version` int unsigned NOT NULL DEFAULT '1'
);

create table Aula (
	ID integer auto_increment primary key,
	nome varchar(255) not null unique,
    capienza integer not null,
	emailResponsabile varchar(255) not null,
	note varchar(255) not null, 
    numeroPreseRete integer not null,
    numeroPreseElettriche integer not null,
    gruppoID integer not null,
    posizioneID integer not null,
	`version` int unsigned NOT NULL DEFAULT '1'
);

create table Attrezzatura (
	ID integer auto_increment primary key,
    nome varchar(255) not null unique,
    `version` int unsigned NOT NULL DEFAULT '1'
);

create table Tipologia (
	ID integer auto_increment primary key,
	nome varchar(255) not null unique,
    `version` int unsigned NOT NULL DEFAULT '1'
);

create table Responsabile (
	ID integer not null auto_increment primary key,
	nome varchar(255) not null,
	email varchar(255) not null unique,
    `version` int unsigned NOT NULL DEFAULT '1'
);

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
create table Amministratore (
	ID integer auto_increment primary key,
	username varchar(255) not null unique,
	`password` varchar(300) not null,
	`version` int unsigned NOT NULL DEFAULT '1'
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES Amministratore WRITE;
/*!40000 ALTER TABLE Amministratore DISABLE KEYS */;
/*username password*/
INSERT INTO Amministratore VALUES (4,'username','d0b11597c8b2c78eed7249c406872ee7e1ff2cfd1bb2941891f2d65399fdcfd5eca0bc92b59cc668fb4a5b8e0425a4f5',1);
/*!40000 ALTER TABLE Amministratore ENABLE KEYS */;
UNLOCK TABLES;


create table Evento (
	ID integer auto_increment primary key,
	giorno date not null,
	oraInizio TIME not null,
	oraFine TIME not null,
	nome varchar(255) not null,
	descrizione varchar(255) not null,
	tipologia varchar(255) not null,
	responsabileID integer not null,
    aulaID integer not null,
    corsoID integer,
    `version` int unsigned NOT NULL DEFAULT '1'
);

create table Corso (
	ID integer auto_increment primary key,
	nome varchar(255) not null unique,
    `version` int unsigned NOT NULL DEFAULT '1'
);

create table Fornito (
	aulaID integer not null,
	attrezzaturaID integer not null,
	constraint chiaveFornito primary key (aulaID, attrezzaturaID)
    
);

create table Tiene (
	aulaID integer not null,
	eventoID integer not null,
	constraint chiaveTiene primary key (aulaID, eventoID)
);

create table Richiede (
	eventoID integer not null,
	corsoID  integer not null,
	constraint chiaveRichiede primary key (eventoID, corsoID)
);



alter table Tiene add foreign key (aulaID) references Aula(ID) ON UPDATE CASCADE ON DELETE RESTRICT, add foreign key (eventoID) references Evento(ID) ON UPDATE CASCADE ON DELETE CASCADE;
alter table Richiede add foreign key (eventoID) references Evento(ID) ON UPDATE CASCADE ON DELETE CASCADE, add foreign key (corsoID) references Corso(ID) ON UPDATE CASCADE ON DELETE RESTRICT;
alter table Evento add foreign key (responsabileID) references Responsabile(ID) ON UPDATE CASCADE ON DELETE CASCADE;
alter table Aula add foreign key (gruppoID) references Gruppo(ID) ON UPDATE CASCADE ON DELETE RESTRICT, add foreign key (PosizioneID) references Posizione(ID) ON UPDATE CASCADE ON DELETE CASCADE;
alter table Fornito add foreign key (aulaID) references Aula(ID) ON UPDATE CASCADE ON DELETE RESTRICT, add foreign key (attrezzaturaID) references Attrezzatura(ID) ON UPDATE CASCADE ON DELETE RESTRICT;

insert into Gruppo (nome, descrizione) values 
	("DISIM", "Dipartimento di Ingegneria e Scienze dell'Informazione e Matematica"), 
    ("ATENEO", "Aule senza dipartimento"),
    ("CLA", "Centro linguistico di ateneo"), 
    ("COSBE", "Polo laboratoriale didattico biologico-chimico"), 
    ("DICEAA", "Dipartimento Ingegneria civile edile-architettura-ambiente"), 
    ("DIIIE aule Roio", "Dipartimento di ingegneria industriale e dell'informazione e di economia"), 
    ("DIIIE aule Acquasanta", "Dipartimento di ingegneria industriale e dell'informazione e di economia"), 
    ("DISCAB", "Dipartimento scienze chimiche applicate e biotecnologiche"), 
    ("DSFC", "Dipartimento di scienze fisiche e chimiche"), 
    ("DSU", "Dipartimento di scienze umane"), 
    ("MESVA", "Medicina clinica sanità pubblica scienze della vita e dell'ambiente"), 
    ("MICROSCOPIA", "Centro di miscroscopia");

insert into Posizione (luogo, edificio, piano) values
    ("Coppito","Alan Touring (Coppito 0)", "terra"),
    ("Coppito","Alan Touring (Coppito 0)", "primo"),
	("Coppito","Coppito 1", "terra"),
    ("Coppito","Coppito 1", "primo"),
    ("Coppito","Coppito 1", "secondo"),
    ("Coppito","Coppito 2", "terra"),
    ("Coppito","Coppito 2", "primo"),
    ("Coppito","Coppito 2", "secondo"),
    ("Coppito","11 A", "terra"),
    ("Coppito","11 A", "primo"),
    ("Coppito","11 A", "secondo"),
    ("Coppito","11 A", "terzo"),
    ("Coppito","11 B", "terra"),
    ("Coppito","11 B", "primo"),
    ("Coppito","11 B", "secondo");
    
insert into Aula (nome, capienza, emailResponsabile, note, numeroPreseRete, numeroPreseElettriche, gruppoID, posizioneID ) values
	("C1.16",46, "mattia.peccerill0o@student.univaq.it","", 1, 4, 1, 6),
    ("0.6",12, "mattia.peccerill0o@student.univaq.it","i ragazzi tendono a perdersi", 3, 4, 1, 3),
    ("1.1",24, "mattia.peccerill0o@student.univaq.it","",3, 4, 1, 4),
    ("1.7 (Aula biancofiore)",100, "mattia.peccerill0o@student.univaq.it","", 5, 4, 1, 4),
    ("2.5",100, "mattia.peccerill0o@student.univaq.it","", 3, 4, 1, 5),
    ("Aula Rossa",100, "mattia.peccerill0o@student.univaq.it","", 1, 4, 1, 4),
	("C1.9",80, "mattia.peccerill0o@student.univaq.it","", 1, 4, 1, 6),
    ("C1.10",126, "mattia.peccerill0o@student.univaq.it","", 1, 4, 1, 6),
    ("A0.4",46, "mattia.peccerill0o@student.univaq.it","", 3, 4, 1, 1),
    ("A1.1",46, "mattia.peccerill0o@student.univaq.it","", 1, 4, 1, 2),
    ("A1.2",46, "mattia.peccerill0o@student.univaq.it","la tenda della finestra è rotta", 1, 4, 1, 2),
    ("A1.3",46, "mattia.peccerill0o@student.univaq.it","", 1, 4, 1, 2),
    ("A1.4",46, "mattia.peccerill0o@student.univaq.it","la tenda della finestra è rotta", 1, 4, 1, 2),
    ("A1.5",46, "mattia.peccerill0o@student.univaq.it","", 1, 4, 1, 2),
    ("A1.6",100, "mattia.peccerill0o@student.univaq.it","", 1, 4, 1, 2),
    ("A1.7",100, "mattia.peccerill0o@student.univaq.it","", 1, 4, 1, 2),
    ("Digital Class",20, "mattia.peccerill0o@student.univaq.it","infestazione di formiche all'ingresso", 1, 4, 1, 1);
    
insert into Attrezzatura (nome) values
	("proiettore"),
    ("schermo motorizzato"),
    ("schermo manuale"),
    ("imp.audio"),
    ("pc fisso"),
    ("mic.a filo"),
    ("mic. senza filo"),
    ("lavagna luminosa"),
    ("visual presenter"),
    ("imp. elettrico"),
    ("allestimento"),
	("lavagna");
    
insert into Tipologia (nome) values
	("seminario"),
    ("riunione"),
    ("lauree"),
    ("lezione"),
    ("esame"),
    ("parziale"),
    ("altro");
    
insert into Responsabile (nome, email) values
	("Alessia","alessia.sebastiano@student.univaq.it"),
    ("Andrea","andrea.dorazio@student.univaq.it"),
    ("Mattia","mattia.peccerillo@student.univaq.it");
    
insert into Amministratore(username, `password`) values
	("Tyler", "admin"),
    ("Timat", "admin"),
    ("Seba", "admin");
    
insert into Evento(giorno, oraInizio, oraFine, nome, descrizione, tipologia, responsabileID, aulaID, corsoID) values
("2023-07-04", "12:00", "12:30", "DT0180","docente della penna giuseppe", "lezione", 1,1,1),
    ("2023-06-16", "14:30", "15:00", "F0140","docente smriglio stefano", "lezione", 1,1,2),
    ("2023-06-15", "15:30", "17:00", "F0140","docente della penna giuseppe", "lezione", 1,1,1),
    ("2023-05-16", "16:30", "18:30", "DT0180","docente della penna giuseppe", "lezione", 1,1,1),
     ("2023-06-30", "16:30", "17:00", "DT0180","docente della penna giuseppe", "lezione", 1,1,1),
     ("2023-06-30", "17:00", "17:30", "DT0180","docente della penna giuseppe", "lezione", 1,1,1),
     ("2023-06-26", "17:00", "17:30", "DT0180","docente della penna giuseppe", "esame", 1,1,1),
      ("2023-06-30", "17:30", "18:00", "DT0180","docente della penna giuseppe", "lezione", 1,1,1),
    ("2023-07-01", "16:30", "17:00", "DT0180","docente della penna giuseppe", "lezione", 1,1,1);
    
insert into Corso(nome) values
	("Advanced database system (basi di dati II)"),
	("Advanced probability"),
    ("Web engineering"),
    ("Sviluppo web avanzato"),
    ("Analisi 1"),
    ("Ricerca Operativa"),
    ("Fisica");
    
insert into Fornito(aulaID, attrezzaturaID) values
(11,1),
(11,2),
(11,5),
(11,10),
(11,11),
(16,1),
(16,2),
(16,4),
(16,5),
(16,6),
(16,7),
(16,10),
(16,11),
(2,3),
(2,8),
(2,11);

insert into Tiene(aulaID, eventoID) values
(1, 1),
(2, 2),
(1, 3),
(1, 4);

insert into Richiede (eventoID, corsoID) values
(1,1),
(2,2),
(3,6),
(4,3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

SELECT * from evento order by giorno  



    

