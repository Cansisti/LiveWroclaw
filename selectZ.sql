#podgląd wszystkich zespołow - czy opakowac w procedure?
SELECT nazwa_zespolu, kategoria FROM zespoly;

#podgląd konkretnego zepolu - wersja na zmienionych tabelach
DELIMITER $$

CREATE PROCEDURE podglad_zespolu(nazwa varchar(40))

  BEGIN
    SET @nazwa = nazwa;
    SET @query = NULL;

    SET @query = CONCAT('
    SELECT nazwa_zespolu, kategoria, kom_zespolu_tekst, ocena_zespolu, AVG(ocena_zespolu) AS ocena_srednia
    FROM zespoly JOIN komentarze_zespoly ON zespoly.id_zespolu = komentarze_zespoly.id_zespolu
    WHERE nazwa_zespolu LIKE ''', @nazwa, ''';
    ');

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

  END $$
DELIMITER ;


DELIMITER $$

CREATE PROCEDURE dodaj_kom_zespolu(nazwa varchar(40), ocena smallint(3), kom varchar(200))

  BEGIN
    SET @nazwa = nazwa;
    SET @query = NULL;

    CALL podglad_zespolu(@nazwa);
    INSERT INTO komentarze_zespoly values (null, get_id_zespolu(@nazwa), ocena, kom);

  END $$
DELIMITER ;


DELIMITER $$
CREATE FUNCTION get_id_zespolu(nazwa varchar(40))
  RETURNS int(12)
NOT DETERMINISTIC
  BEGIN
    DECLARE select_var int(12);
    SET @nazwa = nazwa;
    SET select_var = (SELECT id_zespolu FROM zespoly WHERE nazwa_zespolu LIKE @nazwa);
    RETURN select_var;
  END$$
DELIMITER ;


CALL podglad_zespolu('abc');
DROP PROCEDURE IF EXISTS podglad_zespolu;


CALL dodaj_kom_zespolu('abc', 3, 'kjfnsdcb');
DROP PROCEDURE IF EXISTS dodaj_kom_zespolu;

SELECT get_id_zespolu('abc');