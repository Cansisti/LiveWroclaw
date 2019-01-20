package liveWroclaw;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.Robot;
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
        
        search();
    }

    void search() {
    	String wzor = searchbox.getText();
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
    
    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
        	search();
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
