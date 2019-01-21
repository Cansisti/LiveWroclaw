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
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

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
                // id_koncertu jest dane
                // TODO
                // kupić bilety

                CallableStatement cstmt = App.conn.prepareCall("call kup_bilet2( ?, ?, ?)");
                cstmt.setInt(1, id_koncertu);
                cstmt.setInt(2, rodzaj_miejsca);
                cstmt.setInt(3, ilosc_biletow);
                cstmt.executeUpdate();


//                CallableStatement cstmt = App.conn.prepareCall("call kup_bilet( ?, ?, ?)");
//                int c = 0;
//                cstmt.registerOutParameter(1, Types.INTEGER);
//                cstmt.execute();
//                c = cstmt.getInt(1);
//                System.out.println(c);


            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                status.setText("Ilość musi być liczbą");
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent arg0) {
        // TODO

        int cena = 0;
        if (arg0.getItem().equals("Siedzące")) {
            // TODO
            typ.select(0);

        }
        if (arg0.getItem().equals("Stojące")) {
            // TODO
            typ.select(1);

        }
        status.setText("Cena: " + cena);
    }


    @Override
    public void windowClosing(WindowEvent e) {
        dialog.dispose();
    }
}
