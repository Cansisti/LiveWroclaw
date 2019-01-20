#SELECT @@GLOBAL.sql_mode;
#SET @@GLOBAL.sql_mode= 'YOUR_VALUE';


INSERT INTO koncerty (akt_najtanszy_bilet)
SELECT MIN(cena) AS min_cena
FROM bilety JOIN koncerty ON bilety.id_koncertu = koncerty.id_koncertu;

/*********/

DELIMITER $$
CREATE PROCEDURE insert_najtanszy_bilet(id_koncertu int(12))

  BEGIN
    SET @id = id_koncertu;

    INSERT INTO koncerty (akt_najtanszy_bilet)
    SELECT MIN(cena) AS min_cena
    FROM bilety JOIN koncerty ON bilety.id_koncertu = koncerty.id_koncertu
    WHERE koncerty.id_koncertu = @id;

  END;
DELIMITER ;
