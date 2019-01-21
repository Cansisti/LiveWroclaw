package liveWroclaw;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SearchPanel extends Panel implements ActionListener, KeyListener, ItemListener {
    private static final long serialVersionUID = 1L;

    TextField searchbox;
    List list;
    List zespoly;
    List obiekty;
    Button bData;
    Button bCena;
    Button bOcen;
    Button bKup;
    Panel ctrl;
    
    ArrayList<Integer> idks;

    private StringTokenizer st;

    public SearchPanel() {
        setLayout( new BorderLayout() );
        idks = new ArrayList<>();

        searchbox = new TextField();
        ctrl = new Panel();
        list = new List(16);
        list.addItemListener( this);
        zespoly = new List(17);
        zespoly.addItemListener(this);
        obiekty = new List(17);
        obiekty.addItemListener( this);
        searchbox.addKeyListener(this);
        ctrl.setLayout( new GridLayout( 1, 3 ) );

        add( searchbox, BorderLayout.NORTH );
        add( list, BorderLayout.CENTER );
        add( ctrl, BorderLayout.SOUTH );
        
        bData = new Button("Szukaj po dacie");
        ctrl.add( bData );
        bData.addActionListener(this);

        bCena = new Button("Szukaj po cenie");
        ctrl.add( bCena );
        bCena.addActionListener(this);
        
        bOcen = new Button( "Oceń" );
        ctrl.add( bOcen );
        bOcen.addActionListener( this );
        
        bKup = new Button( "Kup bilety" );
        ctrl.add( bKup );
        bKup.addActionListener( this );

        search();
    }

    void search() {
        String wzor = searchbox.getText();
        System.out.println("Wyszukaj " + wzor);
        try {
            list.removeAll();
            idks.clear();
            Statement stmt = App.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, nazwa_obiektu, id_koncertu FROM koncerty\n" +
                    "        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu\n" +
                    "        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu\n" +
                    "        WHERE data_koncertu >= now() AND\n" +
                    "        il_pozostalych_biletow>0 AND\n" +
                    "        (nazwa_zespolu LIKE '%" + wzor + "%' \n" +
                    "        OR nazwa_obiektu LIKE '%" + wzor + "%'\n" +
                    "        OR kategoria LIKE '%" + wzor + "%');");

            while (rs.next()) {
            	list.add( rs.getString( 3 ) + " (" + rs.getString( 4 ) + "), " + rs.getString( 1 ) + " " + rs.getString( 5 ) + " (pozostało " + rs.getString( 2 ) + " biletów)" );
                idks.add( rs.getInt( "id_koncertu" ) );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void search2() {
        String wzor = searchbox.getText();
        System.out.println("Wyszukaj " + wzor);
        st = new StringTokenizer(wzor, ";");
        st.nextToken();
        String data1 = st.nextToken();
        String data2 = st.nextToken();


        try {
            list.removeAll();
            idks.clear();
            Statement stmt = App.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, nazwa_obiektu, id_koncertu FROM koncerty\n" +
                    "        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu\n" +
                    "        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu\n" +
                    "        WHERE data_koncertu >= now() AND\n" +
                    "        il_pozostalych_biletow>0 AND\n" +
                    "        data_koncertu >= " + data1 + " \n" +
                    "        AND data_koncertu <= " + data2 + ";");

            while (rs.next()) {
            	list.add( rs.getString( 3 ) + " (" + rs.getString( 4 ) + "), " + rs.getString( 1 ) + " " + rs.getString( 5 ) + " (pozostało " + rs.getString( 2 ) + " biletów)" );
                idks.add( rs.getInt( "id_koncertu" ) );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void search3() {
        String wzor = searchbox.getText();
        System.out.println("Wyszukaj " + wzor);
        try {
        	idks.clear();
            list.removeAll();
            Statement stmt = App.conn.createStatement();
            ResultSet rs = stmt.executeQuery("  SELECT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, nazwa_obiektu, id_koncertu FROM koncerty\n" +
                    "        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu\n" +
                    "        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu\n" +
                    "        WHERE data_koncertu >= now() AND\n" +
                    "        il_pozostalych_biletow>0 AND\n" +
                    "       akt_najtanszy_bilet <= " + wzor + ";");

            while (rs.next()) {
            	list.add( rs.getString( 3 ) + " (" + rs.getString( 4 ) + "), " + rs.getString( 1 ) + " " + rs.getString( 5 ) + " (pozostało " + rs.getString( 2 ) + " biletów)" );
                idks.add( rs.getInt( "id_koncertu" ) );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void podglad_zespolow() {
        try {
            zespoly.removeAll();
            Statement stmt = App.conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT nazwa_zespolu, kategoria, kom_zespolu_tekst, ocena_zespolu, AVG(ocena_zespolu) AS ocena_srednia\n" +
                    "    FROM zespoly JOIN komentarze_zespoly ON zespoly.id_zespolu = komentarze_zespoly.id_zespolu");
            while (rs.next()) {
                zespoly.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dodaj_kom_zespolu() {
        try {
//            String skomentarz = komentarz.getText();
//            int ocena;

            String snazwa = zespoly.getSelectedItem();
            String skomentarz = " ";
            int ocena = 0;

            CallableStatement cstmt = App.conn.prepareCall("call dodaj_kom_zespolu( ?, ?, ?)");
            cstmt.setString(1, snazwa);
            cstmt.setInt(2, ocena);
            cstmt.setString(3, skomentarz);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    void podglad_obiektow() {
        try {
            list.removeAll();
            Statement stmt = App.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nazwa_obiektu, adres, il_miejsc_siedzacych, il_miejsc_stojacych, kom_obiektu_tekst, ocena_obiektu, AVG(ocena_obiektu) AS ocena_srednia\n" +
                    "    FROM obiekty JOIN komentarze_obiektu o ON obiekty.id_obiektu = o.id_obiektu;");
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void dodaj_kom_obiektu() {
        try {
//            String skomentarz = komentarz.getText();
//            int ocena;

            String snazwa = obiekty.getSelectedItem();
            String skomentarz = "  ";
            int ocena = 0;

            CallableStatement cstmt = App.conn.prepareCall(" call dodaj_kom_obiektu( ?, ?, ?)");
            cstmt.setString(1, snazwa);
            cstmt.setInt(2, ocena);
            cstmt.setString(3, skomentarz);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
            search();
        }
    }

    @Override
    public void actionPerformed( ActionEvent arg0 ) {
        if (arg0.getSource() == bData) {
            search2();
        } else if (arg0.getSource() == bCena) {
            search3();
        } else if (arg0.getSource() == zespoly) {
            dodaj_kom_zespolu();
        } else if (arg0.getSource() == obiekty) {
            dodaj_kom_obiektu();
        } else if (arg0.getSource() == bKup) {
            new BuyDialog( idks.get( list.getSelectedIndex() ) );
        }
        else if( arg0.getSource() == bOcen ) {
        	new CommentDialog();
        }

    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
