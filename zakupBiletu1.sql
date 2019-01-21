USE livewroclaw2;

/****************/
DROP PROCEDURE IF EXISTS kup_bilet2;


DELIMITER $$
CREATE PROCEDURE kup_bilet2(id int(12), rodzaj enum ('stojace', 'siedzace'), ilosc int )

BEGIN
  declare i int default 0;

  START TRANSACTION;

  WHILE i < ilosc
  DO
  UPDATE bilety
    JOIN koncerty ON bilety.id_koncertu = koncerty.id_koncertu
  SET czy_sprzedany = 1
  WHERE rodzaj_miejsca = rodzaj AND czy_sprzedany = 0 AND bilety.id_koncertu = id ;

  set i = i+1;
  
  END WHILE;
  
  UPDATE koncerty
  SET il_pozostalych_biletow = il_pozostalych_biletow - ilosc
  WHERE id_koncertu = id ;

  COMMIT;

END $$
DELIMITER ;