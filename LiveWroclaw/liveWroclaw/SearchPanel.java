package liveWroclaw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.StringTokenizer;

public class SearchPanel extends Panel implements ActionListener, KeyListener, ItemListener {
    private static final long serialVersionUID = 1L;

    TextField searchbox;
    List list;
    //    List koncerty;
    List zespoly;
    List obiekty;
    JButton bData;
    JButton bCena;
    JButton bZespoly;
    JButton bObiekty;

    private StringTokenizer st;


    public SearchPanel() {
        setLayout(new BorderLayout());

        searchbox = new TextField();

        list = new List(17);
        list.addItemListener( this);

//        koncerty = new List(17);
//        //koncerty.addItemListener( this );
        zespoly = new List(17);
        zespoly.addItemListener(this);
        obiekty = new List(17);
        obiekty.addItemListener( this);


        searchbox.addKeyListener(this);

        add(searchbox, BorderLayout.NORTH);

        add(list, BorderLayout.CENTER);
//        add(koncerty, BorderLayout.CENTER);
        add(zespoly, BorderLayout.CENTER);
        add(obiekty, BorderLayout.CENTER);


        search();

        /******/
        bData = new JButton("Szukaj po dacie");
        bData.setBounds(50, 50, 30, 10);
        add(bData);
        bData.addActionListener(this);

        bCena = new JButton("Szukaj po cenie");
        bCena.setBounds(50, 60, 30, 10);
        add(bCena);
        bCena.addActionListener(this);

        bZespoly = new JButton("Podglad zespolow");
        bZespoly.setBounds(50, 70, 30, 10);
        add(bZespoly);
        bZespoly.addActionListener(this);

        bObiekty = new JButton("Podglad obiektow");
        bObiekty.setBounds(50, 80, 30, 10);
        add(bObiekty);
        bObiekty.addActionListener(this);


    }

    void search() {
        String wzor = searchbox.getText();
        System.out.println("Wyszukaj " + wzor);
        try {
            list.removeAll();
            Statement stmt = App.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, nazwa_obiektu FROM koncerty\n" +
                    "        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu\n" +
                    "        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu\n" +
                    "        WHERE data_koncertu >= now() AND\n" +
                    "        il_pozostalych_biletow>0 AND\n" +
                    "        (nazwa_zespolu LIKE '%" + wzor + "%' \n" +
                    "        OR nazwa_obiektu LIKE '%" + wzor + "%'\n" +
                    "        OR kategoria LIKE '%" + wzor + "%');");

            while (rs.next()) {
                list.add(rs.getString(1));
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
            Statement stmt = App.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, nazwa_obiektu FROM koncerty\n" +
                    "        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu\n" +
                    "        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu\n" +
                    "        WHERE data_koncertu >= now() AND\n" +
                    "        il_pozostalych_biletow>0 AND\n" +
                    "        data_koncertu >= " + data1 + " \n" +
                    "        AND data_koncertu <= " + data2 + ";");

            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void search3() {
        String wzor = searchbox.getText();
        System.out.println("Wyszukaj " + wzor);
        try {
            list.removeAll();
            Statement stmt = App.conn.createStatement();
            ResultSet rs = stmt.executeQuery("  SELECT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, nazwa_obiektu FROM koncerty\n" +
                    "        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu\n" +
                    "        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu\n" +
                    "        WHERE data_koncertu >= now() AND\n" +
                    "        il_pozostalych_biletow>0 AND\n" +
                    "       akt_najtanszy_bilet <= " + wzor + ";");

            while (rs.next()) {
                list.add(rs.getString(1));
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
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == bData) {
            search2();
        } else if (arg0.getSource() == bCena) {
            search3();
        } else if (arg0.getSource() == bZespoly) {
            podglad_zespolow();
        } else if (arg0.getSource() == bObiekty) {
            podglad_obiektow();
        } else if (arg0.getSource() == zespoly) {
            dodaj_kom_zespolu();
        } else if (arg0.getSource() == obiekty) {
            dodaj_kom_obiektu();
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
