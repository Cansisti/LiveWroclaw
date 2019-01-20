DROP PROCEDURE IF EXISTS kup_bilety;
DROP PROCEDURE IF EXISTS dodaj_do_koszyka;
DROP TABLE IF EXISTS koszyk;


DELIMITER $$
CREATE PROCEDURE dodaj_do_koszyka(id int(12))
  BEGIN

    UPDATE bilety SET czy_dodac_do_koszyka = 1 WHERE id_biletu = id;

  END $$
DELIMITER ;




DELIMITER $$
CREATE PROCEDURE kup_bilety()

  BEGIN
    SET autocommit = 0;
    START TRANSACTION;


    CREATE TEMPORARY TABLE koszyk (
      id_b int(12),
      cena_b int(4)
    );

    INSERT INTO koszyk
    SELECT id_biletu, cena FROM bilety
    WHERE czy_dodac_do_koszyka = 1;

    SELECT SUM(cena_b) FROM koszyk INTO @p;

    ALTER TABLE koszyk ADD do_zaplaty smallint(4);

    INSERT INTO koszyk (do_zaplaty)
    VALUE (@p);

    SELECT * FROM koszyk;

    COMMIT;


  END $$
DELIMITER ;




CALL dodaj_do_koszyka(123);
CALL kup_bilety();