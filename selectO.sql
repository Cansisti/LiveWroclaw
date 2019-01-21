SELECT nazwa_obiektu FROM obiekty;

USE livewroclaw2;


DELIMITER $$
CREATE PROCEDURE podglad_obiektu(nazwa varchar(50))

  BEGIN
    SET @nazwa = nazwa;
    SET @query = NULL;

    SET @query = CONCAT('
    SELECT nazwa_obiektu, adres, il_miejsc_siedzacych, il_miejsc_stojacych, kom_obiektu_tekst, ocena_obiektu, AVG(ocena_obiektu) AS ocena_srednia
    FROM obiekty JOIN komentarze_obiektu o ON obiekty.id_obiektu = o.id_obiektu
    WHERE nazwa_obiektu LIKE ''', @nazwa, ''';
    ');

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

  END $$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE dodaj_kom_obiektu(nazwa varchar(50), ocena smallint(3), kom varchar(200))

  BEGIN
    SET @nazwa = nazwa;
    SET @query = NULL;

    CALL podglad_obiektu(@nazwa);
    INSERT INTO komentarze_obiektu values (null, get_id_obiektu(@nazwa), ocena, kom);

  END $$
DELIMITER ;


DELIMITER $$
CREATE FUNCTION get_id_obiektu(nazwa varchar(50))
  RETURNS int(12)
NOT DETERMINISTIC
  BEGIN
    DECLARE select_var int(12);
    SET @nazwa = nazwa;
    SET select_var = (SELECT id_obiektu FROM obiekty WHERE nazwa_obiektu LIKE @nazwa);
    RETURN select_var;
  END$$
DELIMITER ;

/********/

CALL podglad_obiektu('abc');
DROP PROCEDURE IF EXISTS podglad_obiektu;


CALL dodaj_kom_obiektu('abc', 3, 'kjfnsdcb');
DROP PROCEDURE IF EXISTS dodaj_kom_obiektu;

SELECT get_id_obiektu('abc');
DROP FUNCTION IF EXISTS get_id_obiektu;




    SELECT nazwa_obiektu, adres, il_miejsc_siedzacych, il_miejsc_stojacych, kom_obiektu_tekst, ocena_obiektu, AVG(ocena_obiektu) AS ocena_srednia
    FROM obiekty JOIN komentarze_obiektu o ON obiekty.id_obiektu = o.id_obiektu
    WHERE nazwa_obiektu LIKE ''', @nazwa, ''';