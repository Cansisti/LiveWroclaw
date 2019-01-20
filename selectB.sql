#podgląd koncertu
DROP PROCEDURE IF EXISTS podglad_koncertu;

DELIMITER $$
CREATE PROCEDURE podglad_koncertu(id int(12))

  BEGIN

    SELECT data_koncertu,
           nazwa_zespolu,
           kategoria,
           nazwa_obiektu,
           adres,
           k.il_miejsc_stojacych,
           k.il_miejsc_siedzacych,
           data_sprzedarzy,
           il_pozostalych_biletow,
           akt_najtanszy_bilet
    FROM koncerty k
           JOIN zespoly z ON k.id_zespolu = z.id_zespolu
           JOIN obiekty o ON k.id_obiektu = o.id_obiektu
    WHERE id = id_koncertu;

  END $$
DELIMITER ;



#podglad wszystkich biletow na dany koncert
DROP PROCEDURE IF EXISTS podglad_biletow;


DELIMITER $$
CREATE PROCEDURE podglad_biletow(id int(12))

  BEGIN

    SELECT id_biletu, data_koncertu, rodzaj_miejsca, cena
    FROM koncerty JOIN bilety ON bilety.id_koncertu = koncerty.id_koncertu
    WHERE bilety.id_koncertu = id AND czy_sprzedany = 0;

  END $$
DELIMITER ;


/***************/

CALL podglad_koncertu(123);
CALL podglad_biletow(123);