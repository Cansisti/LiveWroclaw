package liveWroclaw;

import java.awt.BorderLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;

public class SearchPanel extends Panel implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;

    TextField searchbox;
    List list;

    public SearchPanel() {
        setLayout(new BorderLayout());

        searchbox = new TextField();
        list = new List(17);

        searchbox.addKeyListener(this);

        add(searchbox, BorderLayout.NORTH);
        add(list, BorderLayout.CENTER);
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {

            String wzor = searchbox.getText();

            System.out.println("Wyszukaj: " + wzor);
            // TODO
            // Tutaj procedura wyszukiwania
            // searchbox.getText() zwraca tekst wyszukiwania
            // na razie spróbujmy z samym wyszukiwaniem
            // list.add( ~~String~~ ) dodaje element do listy


//			try {
//				CallableStatement cstmt = App.conn.prepareCall( "call wyszukaj(?)" );
//				cstmt.setString( 1, wzor );
//				cstmt.executeUpdate();
//
//				ResultSet rs = cstmt.executeQuery();
//				list.add(rs.getString(1));
//				//System.out.println("result:" + rs.getString(1));
//
//				list.add(cstmt.toString());
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}

            try {
                list.removeAll();
                Statement stmt = App.conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT data_koncertu, il_pozostalych_biletow, nazwa_zespolu, kategoria, nazwa_obiektu FROM koncerty\n" +
                        "        JOIN zespoly ON koncerty.id_zespolu = zespoly.id_zespolu\n" +
                        "        JOIN obiekty ON koncerty.id_obiektu = obiekty.id_obiektu\n" +
                        "        WHERE data_koncertu<=CURDATE() AND\n" +
                        "        il_pozostalych_biletow>0 AND\n" +
                        "        (nazwa_zespolu LIKE '%" + wzor + "%' \n" +
                        "        OR nazwa_obiektu LIKE '%" + wzor + "%'\n" +
                        "        OR kategoria LIKE '%" + wzor + "%');");

                while (rs.next()) {
                    list.add(rs.getString(1));
                    //System.out.println(rs.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

}
