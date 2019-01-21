DROP DATABASE IF EXISTS livewroclaw2;
CREATE DATABASE livewroclaw2;

USE livewroclaw2;

CREATE TABLE wlasciciele (
  id_wlasciciela int(12)     NOT NULL UNIQUE AUTO_INCREMENT,
  login          varchar(20) NOT NULL UNIQUE,
  haslo          varchar(70),
  PRIMARY KEY (id_wlasciciela)
);

CREATE TABLE obiekty (
  id_obiektu           int(12)     NOT NULL UNIQUE AUTO_INCREMENT,
  id_wlasciciela       int(12)     NOT NULL,
  nazwa_obiektu        varchar(50) not null unique,
  adres                varchar(50),
  il_miejsc_siedzacych smallint(5) unsigned,
  il_miejsc_stojacych  smallint(5) unsigned,
  PRIMARY KEY (id_obiektu),
  FOREIGN KEY (id_wlasciciela) REFERENCES wlasciciele (id_wlasciciela)
);

CREATE TABLE koncerty (
  id_koncertu            int(12) NOT NULL UNIQUE AUTO_INCREMENT,
  id_obiektu             int(12) NOT NULL,
  id_zespolu             int(12) NOT NULL,
  data_koncertu          date,
  data_sprzedarzy        date,
  il_miejsc_siedzacych   smallint(5) unsigned,
  il_miejsc_stojacych    smallint(5) unsigned,
  il_pozostalych_biletow smallint(5) unsigned,
  akt_najtanszy_bilet    int(4),
  PRIMARY KEY (id_koncertu),
  FOREIGN KEY (id_obiektu) REFERENCES obiekty (id_obiektu)
);

CREATE TABLE bilety (
  id_biletu      int(12) NOT NULL UNIQUE AUTO_INCREMENT,
  id_koncertu    int(12) NOT NULL,
  cena           int(4)  NOT NULL,
  rodzaj_miejsca enum ('siedzace', 'stojace'),
  czy_sprzedany  boolean,
  PRIMARY KEY (id_biletu)
);

CREATE TABLE zespoly (
  id_zespolu    int(12)     NOT NULL UNIQUE AUTO_INCREMENT,
  nazwa_zespolu varchar(40) NOT NULL UNIQUE,
  kategoria     varchar(100),
  PRIMARY KEY (id_zespolu)
);

CREATE TABLE komentarze_zespoly (
  id_kom_zespolu    int(12) NOT NULL UNIQUE AUTO_INCREMENT,
  id_zespolu        int(12) NOT NULL,
  ocena_zespolu     smallint(3),
  kom_zespolu_tekst varchar(200),
  PRIMARY KEY (id_kom_zespolu)
);

CREATE TABLE komentarze_obiektu (
  id_kom_obiektu    int(12) NOT NULL UNIQUE AUTO_INCREMENT,
  id_obiektu        int(12) NOT NULL,
  ocena_obiektu     smallint(3),
  kom_obiektu_tekst varchar(200),
  PRIMARY KEY (id_kom_obiektu)
);

ALTER TABLE koncerty
  ADD FOREIGN KEY (id_zespolu) REFERENCES zespoly (id_zespolu);
