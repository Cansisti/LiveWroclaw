package liveWroclaw;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class BuyDialog extends WindowAdapter implements ActionListener, ItemListener {

    Dialog dialog;

    int id_koncertu;

    Choice typ;
    TextField ilosc;
    Button accept;
    Label status;

    BuyDialog(int idk) {
        id_koncertu = idk;
        dialog = new Dialog(App.frame);
        dialog.addWindowListener(this);
        dialog.setBounds(350, 150, 500, 200);
        dialog.setLayout(new GridLayout(6, 1));

        status = new Label();
        typ = new Choice();
        ilosc = new TextField();
        accept = new Button("Akceptuj");

        accept.addActionListener(this);
        typ.add("Siedzące");
        typ.add("Stojące");
        typ.addItemListener(this);

        dialog.add(new Label("Ilość:"));
        dialog.add(ilosc);
        dialog.add(new Label("Typ: "));
        dialog.add(typ);
        dialog.add(status);
        dialog.add(accept);


        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == accept) {
            try {
                int ilosc_biletow = Integer.parseInt(ilosc.getText());
                int rodzaj_miejsca = typ.getSelectedIndex(); // 0 to siedzące, 1 to stojące

                CallableStatement cstmt = App.conn.prepareCall("call kup_bilet2( ?, ?, ?)");
                cstmt.setInt(1, id_koncertu);
                cstmt.setInt(2, rodzaj_miejsca);
                cstmt.setInt(3, ilosc_biletow);
                cstmt.executeUpdate();


            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                status.setText("Ilość musi być liczbą");
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent arg0) {
        int cena = 0;

        int il = Integer.parseInt(ilosc.getText());

        if (arg0.getItem().equals("Siedzące")) {

            try {
                Statement stmt = App.conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DISTINCT cena FROM bilety WHERE rodzaj_miejsca = 'siedzace' ");
                cena = rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (arg0.getItem().equals("Stojące")) {
            try {
                Statement stmt = App.conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT DISTINCT cena FROM bilety WHERE rodzaj_miejsca = 'stojace' ");
                cena = rs.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        status.setText("Cena: " + cena*il);
    }


    @Override
    public void windowClosing(WindowEvent e) {
        dialog.dispose();
    }
}
