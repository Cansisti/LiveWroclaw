drop user if exists 'klient'@'localhost';
drop user if exists 'wlasciciel'@'localhost';
drop user if exists 'admin'@'localhost';

CREATE USER 'klient'@'localhost' IDENTIFIED BY 'klient';
grant select on livewroclaw2.obiekty to 'klient'@'localhost';
grant select on livewroclaw2.koncerty to 'klient'@'localhost';
grant select on livewroclaw2.bilety to 'klient'@'localhost';
grant insert on livewroclaw2.komentarze_zespoly to 'klient'@'localhost';
grant insert on livewroclaw2.komentarze_obiektu to 'klient'@'localhost';
grant select on livewroclaw2.zespoly to 'klient'@'localhost';
grant execute on procedure livewroclaw2.autoryzacja to 'klient'@'localhost';
grant execute on procedure livewroclaw2.kup_bilet2 to 'klient'@'localhost';

CREATE USER 'wlasciciel'@'localhost' IDENTIFIED BY 'wla7182311bvd1utdvu1d';
grant execute on procedure livewroclaw2.autoryzacja to 'wlasciciel'@'localhost';
grant execute on procedure livewroclaw2.dodaj_obiekt to 'wlasciciel'@'localhost';
grant execute on procedure livewroclaw2.dodaj_koncert to 'wlasciciel'@'localhost';
grant execute on procedure livewroclaw2.anuluj_koncert to 'wlasciciel'@'localhost';
grant execute on procedure livewroclaw2.dodaj_bilety to 'wlasciciel'@'localhost';
grant select on livewroclaw2.zespoly to 'wlasciciel'@'localhost';
grant insert on livewroclaw2.zespoly to 'wlasciciel'@'localhost';
grant select on livewroclaw2.obiekty to 'wlasciciel'@'localhost';
grant select on livewroclaw2.koncerty to 'wlasciciel'@'localhost';
grant select on livewroclaw2.bilety to 'wlasciciel'@'localhost';

CREATE user 'admin'@'localhost' IDENTIFIED BY 'asd9ebi1d97wbuscnaufb1cnbaybcs1';
grant execute on procedure livewroclaw2.dodaj_wlasciciela to 'admin'@'localhost';
grant select on livewroclaw2.zespoly to 'admin'@'localhost';
grant update on livewroclaw2.zespoly to 'admin'@'localhost';

flush privileges;