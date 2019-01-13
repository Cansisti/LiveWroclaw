CREATE DATABASE livewroclaw2;

USE livewroclaw2;

CREATE TABLE bilety (
  id_biletu      int(12) NOT NULL UNIQUE,
  id_koncertu    int(12) NOT NULL UNIQUE,
  cena           int(4)  NOT NULL,
  rodzaj_miejsca enum ('siedzace', 'stojace'),
  czy_sprzedany  boolean,
  PRIMARY KEY (id_biletu),
  FOREIGN KEY (id_koncertu) REFERENCES koncerty (id_koncertu)
);

CREATE TABLE koncerty (
  id_koncertu            int(12) NOT NULL UNIQUE,
  id_obiektu             int(12) NOT NULL UNIQUE,
  id_zespolu             int(12) NOT NULL UNIQUE,
  data_koncertu          date,
  data_sprzedarzy        date,
  il_miejsc_siedzacych   smallint(5) unsigned,
  il_miejsc_stojacych    smallint(5) unsigned,
  il_pozostalych_biletow smallint(5) unsigned,
  PRIMARY KEY (id_koncertu),
  FOREIGN KEY (id_obiektu) REFERENCES obiekty (id_obiektu)
);

CREATE TABLE obiekty (
  id_obiektu           int(12) NOT NULL UNIQUE,
  id_wlasciciela       int(12) NOT NULL UNIQUE,
  adres                varchar(50),
  il_miejsc_siedzacych smallint(5) unsigned,
  il_miejsc_stojacych  smallint(5) unsigned,
  PRIMARY KEY (id_obiektu),
  FOREIGN KEY (id_wlasciciela) REFERENCES wlasciciele (id_wlasciciela)
);

CREATE TABLE wlasciciele (
  id_wlasciciela int(12) NOT NULL UNIQUE,
  login          varchar(20),
  haslo          varchar(20),
  PRIMARY KEY (id_wlasciciela)
);

CREATE TABLE zespoly (
  id_zespolu           int(12) NOT NULL UNIQUE,
  nazwa_zespolu varchar(40) NOT NULL UNIQUE,
  kategoria varchar(100),
  PRIMARY KEY (id_zespolu)
);

CREATE TABLE komentarze_koncerty (
  id_kom_koncertu int(12) NOT NULL UNIQUE,
  id_koncertu int(12) NOT NULL UNIQUE,
  ocena_koncertu smallint(3),
  PRIMARY KEY (id_kom_koncertu),
  FOREIGN KEY (id_koncertu) REFERENCES koncerty (id_koncertu)
);

CREATE TABLE komentarze_obiektu (
  id_kom_obiektu int(12) NOT NULL UNIQUE,
  id_obiektu int(12) NOT NULL UNIQUE,
  ocena_obiektu smallint(3),
  PRIMARY KEY (id_kom_obiektu),
  FOREIGN KEY (id_obiektu) REFERENCES obiekty (id_obiektu)
);

ALTER TABLE koncerty
ADD FOREIGN KEY (id_zespolu) REFERENCES zespoly(id_zespolu);

ALTER TABLE komentarze_koncerty
ADD kom_koncertu_tekst varchar(200);

ALTER TABLE komentarze_obiektu
ADD kom_obiektu_tekst varchar(200);