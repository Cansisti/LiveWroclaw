DELIMITER $$

CREATE PROCEDURE wyszukaj(wzor varchar(30))

  BEGIN
    SET @wzorzec = wzor;
    SET @query = NULL;

    SET @query = CONCAT('SELECT DISTINCT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, adres, cena, rodzaj_miejsca FROM koncerty
        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu
        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu
        JOIN bilety ON bilety.id_koncertu = koncerty.id_koncertu
        WHERE data_koncertu<=CURDATE() AND
        il_pozostalych_biletow>0 AND
        (nazwa_zespolu LIKE ''% ',   @wzorzec , '%''
        OR kategoria LIKE ''% ',   @wzorzec , '%'' );' );

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

  END $$
DELIMITER ;



DELIMITER $$

CREATE PROCEDURE wyszukaj2(data1 date, data2 date)

  BEGIN
    SET @data1 = data1;
    SET @data2 = data2;
    SET @query = NULL;

    SET @query = CONCAT('SELECT DISTINCT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, adres, cena, rodzaj_miejsca   FROM koncerty
        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu
        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu
        JOIN bilety ON bilety.id_koncertu = koncerty.id_koncertu
        WHERE data_koncertu<=CURDATE() AND
        il_pozostalych_biletow>0 AND
        data_koncertu >= ' , @data1 , ' AND
        data_koncertu <= ' , @data2 , '
        ;' );

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

  END $$
DELIMITER ;




DELIMITER $$

CREATE PROCEDURE wyszukaj3(max_cena int)

  BEGIN
    SET @max_cena = max_cena;
    SET @query = NULL;

    SET @query = CONCAT('SELECT DISTINCT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, adres, cena, rodzaj_miejsca  FROM koncerty
        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu
        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu
        JOIN bilety ON bilety.id_koncertu = koncerty.id_koncertu
        WHERE data_koncertu<=CURDATE() AND
        il_pozostalych_biletow>0 AND
        cena < ' , @max_cena , '
        ;' );

    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

  END $$
DELIMITER ;