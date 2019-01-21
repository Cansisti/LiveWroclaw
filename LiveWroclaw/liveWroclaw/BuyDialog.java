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

public class BuyDialog extends WindowAdapter implements ActionListener, ItemListener {

	Dialog dialog;
	
	int id_koncertu;
	
	Choice typ;
	TextField ilosc;
	Button accept;
	Label status;
	
	BuyDialog( int idk ) {
		id_koncertu = idk;
		dialog = new Dialog( App.frame );
		dialog.addWindowListener( this );
		dialog.setBounds( 350, 150, 500, 200 );
		dialog.setLayout( new GridLayout( 6, 1 ) );
		
		status = new Label();
		typ = new Choice();
		ilosc = new TextField();
		accept = new Button( "Akceptuj" );

		accept.addActionListener( this );
		typ.add( "Siedzące" );
		typ.add( "Stojące" );
		typ.addItemListener( this );
		
		dialog.add( new Label( "Ilość:" ) );
		dialog.add( ilosc );
		dialog.add( new Label( "Typ: " ) );
		dialog.add( typ );
		dialog.add( status );
		dialog.add( accept );
		
		
		dialog.setVisible( true );
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		if( e.getSource() == accept ) {
			try {
				int ilosc_biletow = Integer.parseInt( ilosc.getText() );
				int rodzaj_miejsca = typ.getSelectedIndex(); // 0 to siedzące, 1 to stojące
				// id_koncertu jest dane
				// TODO
				// kupić bilety
			}
			catch( NumberFormatException ex ) {
				status.setText( "Ilość musi być liczbą" );
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO
		int cena = 0;
		if( arg0.getItem().equals( "siedzące" ) ) {
			// TODO
		}
		if( arg0.getItem().equals( "stojące" ) ) {
			// TODO
		}
		status.setText( "Cena: " + cena );
	}
	
	
	@Override
	public void windowClosing( WindowEvent e ) {
		dialog.dispose();
	}
}
