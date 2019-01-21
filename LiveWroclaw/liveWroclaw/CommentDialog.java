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

public class CommentDialog extends WindowAdapter implements ActionListener, ItemListener {

	Dialog dialog;
	
	Choice typ;
	Choice lista;
	TextField tekst;
	TextField ocena;
	Button accept;
	Label status;
	
	CommentDialog() {
		dialog = new Dialog( App.frame );
		dialog.addWindowListener( this );
		dialog.setBounds( 350, 150, 500, 300 );
		dialog.setLayout( new GridLayout( 8, 1 ) );
		
		status = new Label();
		typ = new Choice();
		lista = new Choice();
		tekst = new TextField();
		ocena = new TextField();
		accept = new Button( "Akceptuj" );

		accept.addActionListener( this );
		typ.add( "Obiekt" );
		typ.add( "Zespół" );
		typ.addItemListener( this );
		
		dialog.add( typ );
		dialog.add( lista );
		dialog.add( new Label( "Ocena:" ) );
		dialog.add( ocena );
		dialog.add( new Label( "Komentarz:" ) );
		dialog.add( tekst );
		dialog.add( status );
		dialog.add( accept );
		
		
		dialog.setVisible( true );
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		if( e.getSource() == accept ) {
			try {
				int oce = Integer.parseInt( ocena.getText() );
				int co_oceniamy = typ.getSelectedIndex(); // 0 to obiekt, 1 to zespół
				// można dodać sprawdzenie czy ocena mieści się w skali
				String komentarz = tekst.getText();
				String nazwa = lista.getSelectedItem(); // odpowiednio nazwa zespołu lub obiektu
				// TODO dodać ocenę
			}
			catch( NumberFormatException ex ) {
				status.setText( "Ocena musi być liczbą" );
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		if( arg0.getItem().equals( "Zespół" ) ) {
			// TODO załaduj w lista nazwy zespołów
		}
		if( arg0.getItem().equals( "Obiekt" ) ) {
			// TODO załaduj w lista nazwy obiektów
		}
	}
	
	
	@Override
	public void windowClosing( WindowEvent e ) {
		dialog.dispose();
	}
}