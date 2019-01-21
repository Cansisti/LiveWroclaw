USE livewroclaw2;

DROP PROCEDURE IF EXISTS kup_bilet;


DELIMITER $$
CREATE FUNCTION kup_bilet(id int(12), rodzaj enum ('stojace', 'siedzace'), ilosc smallint(1))

  RETURNS int(4)
  NOT DETERMINISTIC

BEGIN
  DECLARE suma int(12);
  SET @i = 0;

  SET suma = (SELECT SUM(cena) FROM bilety WHERE rodzaj_miejsca = rodzaj AND czy_sprzedany = 0);

  SET autocommit = 0;
  START TRANSACTION;

  WHILE @i <= ilosc
  DO
  UPDATE bilety
    JOIN koncerty ON bilety.id_koncertu = koncerty.id_koncertu
  SET czy_sprzedany = 1
  WHERE rodzaj_miejsca = rodzaj AND czy_sprzedany = 0 AND bilety.id_koncertu = id ;

  END WHILE;

  COMMIT;

  RETURN suma;

END $$
DELIMITER ;

/****************/
DROP PROCEDURE IF EXISTS kup_bilet2;


DELIMITER $$
CREATE PROCEDURE kup_bilet2(id int(12), rodzaj enum ('stojace', 'siedzace'), ilosc smallint(1))

BEGIN
  SET @i = 0;

  SET autocommit = 0;
  START TRANSACTION;

  WHILE @i <= ilosc
  DO
  UPDATE bilety
    JOIN koncerty ON bilety.id_koncertu = koncerty.id_koncertu
  SET czy_sprzedany = 1
  WHERE rodzaj_miejsca = rodzaj AND czy_sprzedany = 0 AND bilety.id_koncertu = id ;
  END WHILE;

  COMMIT;

END $$
DELIMITER ;