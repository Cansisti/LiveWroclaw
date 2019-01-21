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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		typ.add( "Wybierz" );
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
				if( oce < 1 || oce > 5 ) {
					status.setText( "Ocena musi być z zakresu 1-5" );
					return;
				}
				String komentarz = tekst.getText();
				String nazwa = lista.getSelectedItem(); // odpowiednio nazwa zespołu lub obiektu
				Statement stmt = App.conn.createStatement();
				if( co_oceniamy == 1 ) {
					stmt.executeUpdate( "insert into komentarze_obiektu values("
							+ "null,"
							+ " (select id_obiektu from obiekty where nazwa_obiektu = '" + nazwa + "'),"
							+ oce + ",'"
							+ komentarz + "' )" );
				}
				else if( co_oceniamy == 2 ) {
					stmt.executeUpdate( "insert into komentarze_zespoly values("
							+ "null,"
							+ " (select id_zespolu from zespoly where nazwa_zespolu = '" + nazwa + "'),"
							+ oce + ",'"
							+ komentarz + "' )" );
				}
				dialog.dispose();
			}
			catch( NumberFormatException ex ) {
				status.setText( "Ocena musi być liczbą" );
			}
			catch( SQLException ex2 ) {
				ex2.printStackTrace();
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		lista.removeAll();
		if( arg0.getItem().equals( "Zespół" ) ) {
			try {
				Statement stmt = App.conn.createStatement();
				ResultSet rs = stmt.executeQuery( "select * from zespoly order by nazwa_zespolu" );
				while( rs.next() ) {
					lista.add( rs.getString( "nazwa_zespolu" ) );
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if( arg0.getItem().equals( "Obiekt" ) ) {
			try {
				Statement stmt = App.conn.createStatement();
				ResultSet rs = stmt.executeQuery( "select * from obiekty order by nazwa_obiektu" );
				while( rs.next() ) {
					lista.add( rs.getString( "nazwa_obiektu" ) );
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void windowClosing( WindowEvent e ) {
		dialog.dispose();
	}
}