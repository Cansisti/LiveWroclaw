CREATE USER IF NOT EXISTS 'klient'@'localhost' IDENTIFIED BY 'klient';
GRANT ALL PRIVILEGES ON livewroclaw2.* TO 'klient'@'localhost';

CREATE USER IF NOT EXISTS 'wlasciciel'@'localhost' IDENTIFIED BY 'wla7182311bvd1utdvu1d';
GRANT ALL PRIVILEGES ON livewroclaw2.* TO 'wlasciciel'@'localhost';

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'asd9ebi1d97wbuscnaufb1cnbaybcs1';
GRANT ALL PRIVILEGES ON livewroclaw2.* TO 'admin'@'localhost';

/*CREATE USER IF NOT EXISTS 'wlasciciel'@'localhost' IDENTIFIED BY 'abc123';
GRANT ALL PRIVILEGES ON livewroclaw2.* TO 'wlasciciel'@'localhost';*/