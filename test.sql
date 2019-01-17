source tabele.sql
source proc.sql

call dodaj_wlasciciela( "Jaskier", "123", @ret ); select @ret;
select id_wlasciciela into @Jaskier from wlasciciele where login = "Jaskier"; select @Jaskier;
call dodaj_obiekt( "Sza³wia i Rozmaryn", @Jaskier, "Novigrad", 45, 20, "Jaskier", "123", @ret ); select @ret;
select id_obiektu into @SzaiRo from obiekty where nazwa_obiektu = "Sza³wia i Rozmaryn"; select @SzaiRo;
insert into zespoly values( null, "Jaskier i Priscilla", "Ballady" ); select * from zespoly;
select id_zespolu into @JaiPri from zespoly where nazwa_zespolu = "Jaskier i Priscilla"; select @JaiPri;
call dodaj_koncert( @SzaiRo, @JaiPri, "2019-05-21", "2019-04-21", "Jaskier", "123", @ret ); select @ret;
select id_koncertu into @JKoncert from koncerty where id_obiektu = @SzaiRo limit 1; select @JKoncert;
call dodaj_bilety( 30, @JKoncert, 100, 'siedzace', "Jaskier", "123", @ret ); select @ret;
-- call anuluj_koncert( @JKoncert, "Jaskier", "123", @ret ); select @ret;