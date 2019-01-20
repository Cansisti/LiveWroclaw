package liveWroclaw;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JLabel;

public class ManagePanel extends Panel implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;

	int id;
	String login;
	String passwd;
	
	Panel ctrl;
	Panel view;
	
	List list;
	JLabel desc;
	Button add;
	Button rem;
	
	int lvl;
	
	int obj;
	ArrayList<Integer> kon;
	
	public ManagePanel( int _id, String _l, String _p ) {
		id = _id;
		login = _l;
		passwd = _p;
		lvl = 0;
		obj = 0;
		kon = new ArrayList<>();
		setLayout( new BorderLayout() );
		
		ctrl = new Panel();
		view = new Panel();
		
		list = new List( 18 );
		desc = new JLabel();
		add = new Button( "Dodaj" );
		rem = new Button( "Usuń" );
		
		ctrl.setLayout( new GridLayout( 1, 2 ) );
		view.setLayout( new GridLayout( 1, 2 ) );
		add.addActionListener( this );
		rem.addActionListener( this );
		list.addItemListener( this );
		
		ctrl.add( add );
		ctrl.add( rem );
		view.add( list );
		view.add( desc );
		add( view, BorderLayout.NORTH );
		add( ctrl, BorderLayout.SOUTH );
		
		reload();
	}
	
	@Override
	public void actionPerformed( ActionEvent eve ) {
		if( eve.getSource() == add ) {
			if( obj > 0 ) obj *= -1;
			else lvl++;
			new AddDialog( App.frame );
			reload();
		}
		if( eve.getSource() == rem ) {
			if( obj > 0 ) {
				desc.setText( "Najpierw wybierz koncert" );
			}
			else if( obj < 0 ) {
				int ix = list.getSelectedIndex() - 1;
				try {
					CallableStatement cstmt = App.conn.prepareCall( "call anuluj_koncert( ?, ?, ?, ? )" );
					cstmt.setInt( 1, kon.get( ix ) );
					cstmt.setString( 2, login );
					cstmt.setString( 3, passwd );
					cstmt.registerOutParameter( 4, Types.VARCHAR );
					cstmt.executeUpdate();
					if( cstmt.getString( 4 ).equals( "sale already started" ) ) {
						desc.setText( "Sprzedaż już się rozpoczęła" );
						return;
					}
					else if( cstmt.getString( 4 ).equals( "authentication failed" ) ) {
						desc.setText( "Odmowa dostępu!" );
						return;
					}
					reload();
					obj *= -1;
				} catch (SQLException e) {
					desc.setText( "Błąd bazy danych 0x1" );
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void itemStateChanged( ItemEvent e ) {
		if( list.getSelectedItem().equals( ".." ) ) {
			lvl--;
		}
		else {
			lvl++;
		}
		reload();
	}
	
	void reload() {
		try {
			switch( lvl ) {
			case 0: {
				obj = 0;
				list.removeAll();
				Statement stmt = App.conn.createStatement();
				ResultSet rs = stmt.executeQuery( "select nazwa_obiektu from obiekty where id_wlasciciela = " + id );
				while( rs.next() ) {
					list.add( rs.getString( 1 ) );
				}
				desc.setText( "" );
				break;
			}
			case 1: {
				Statement stmt;
				ResultSet rs;
				String sel = list.getSelectedItem();
				if( obj < 0 ) {
					stmt = App.conn.createStatement();
					rs = stmt.executeQuery( "select * from obiekty where id_obiektu = " + obj * -1 );
					if( rs.next() ) sel = rs.getString( "nazwa_obiektu" );
				}
				list.removeAll();
				list.add( ".." );
				stmt = App.conn.createStatement();
				rs = stmt.executeQuery( "select nazwa_zespolu, data_koncertu, id_koncertu from "
												+ "(select id_obiektu from obiekty where nazwa_obiektu = '" + sel + "' ) o "
												+ "join koncerty k on o.id_obiektu = k.id_obiektu "
												+ "join zespoly z on k.id_zespolu = z.id_zespolu" );
				kon.clear();
				while( rs.next() ) {
					list.add( rs.getString( 1 ) + ", " + rs.getString( 2 ) );
					kon.add( rs.getInt( 3 ) );
				}
				
				stmt = App.conn.createStatement();
				rs = stmt.executeQuery( "select * from obiekty where nazwa_obiektu = '" + sel + "'" );
				if( rs.next() ) {
					desc.setText( "<html>" + rs.getString( "nazwa_obiektu" ) + "<br>"
								+ rs.getString( "adres" ) + "<br>"
								+ "Miejsc siedzących: " + rs.getString( "il_miejsc_siedzacych" ) + "<br>" 
								+ "Miejsc stojących: " + rs.getString( "il_miejsc_stojacych" ) + "<br>" );
					obj = rs.getInt( "id_obiektu" );
				}
				else desc.setText( "Błąd bazy danych 0x2" );
				break;
			}
			case 2: {
				if( obj > 0 ) obj *= -1;
				lvl--;
				int ix = list.getSelectedIndex() - 1;
				Statement stmt = App.conn.createStatement();
				ResultSet rs = stmt.executeQuery( "select * from koncerty k join zespoly z on k.id_zespolu = z.id_zespolu where id_koncertu = " + kon.get( ix ) );
				if( rs.next() ) {
					desc.setText( "<html>" + rs.getString( "nazwa_zespolu" ) + "<br>"
							+ "Data koncertu: " + rs.getString( "data_koncertu" ) + "<br>"
							+ "Początek sprzedaży: " + rs.getString( "data_sprzedarzy" ) + "<br>" 
							+ "Miejsc siedzących: " + rs.getString( "il_miejsc_siedzacych" ) + "<br>" 
							+ "Miejsc stojących: " + rs.getString( "il_miejsc_stojacych" ) + "<br>" 
							+ "Pozostało miejsc: " + rs.getString( "il_pozostalych_biletow" ) + "<br>" );
				}
				else desc.setText( "Błąd bazy danych 0x3" );
				break;
			}
			}
		} catch( SQLException e1 ) {
			e1.printStackTrace();
			return;
		}
	}

	class AddDialog extends WindowAdapter implements ItemListener {
		
		Dialog dialog;
		Choice type;
		Panel panel;
		
		AddDialog( Frame owner ) {
			dialog = new Dialog( owner, "Dodaj", true );
			dialog.addWindowListener( this );
			dialog.setLayout( new BorderLayout() );
			dialog.setBounds( 350, 150, 500, 400 );
			
			type = new Choice();
			
			type.addItemListener( this );
			type.add( "Wybierz, co dodać" );
			type.add( "Dodaj obiekt" );
			type.add( "Dodaj koncert" );
			type.add( "Dodaj bilety" );
			
			dialog.add( type, BorderLayout.NORTH );
			dialog.setVisible( true );
		}
		
		@Override
		public void windowClosing( WindowEvent e ) {
			dialog.dispose();
		}

		@Override
		public void itemStateChanged( ItemEvent e ) {
			if( panel != null ) dialog.remove( panel );
			if( e.getItem().equals( "Dodaj obiekt" ) ) {
				panel = new DodajObiekt();
				dialog.add( panel );
			}
			else if( e.getItem().equals( "Dodaj koncert" ) ) {
				panel = new DodajKoncert();
				dialog.add( panel );
			}
			else if( e.getItem().equals( "Dodaj bilety" ) ) {
				panel = new DodajBilety();
				dialog.add( panel );
			}
			dialog.validate();
		}
		
		class DodajObiekt extends Panel implements ActionListener {
			private static final long serialVersionUID = 1L;
			
			TextField nazwa, adres, msie, msto;
			Button add, cancel;
			Label status;
			
			DodajObiekt() {
				setLayout( new GridLayout( 11, 1 ) );
				
				status = new Label();
				nazwa = new TextField();
				adres = new TextField();
				msie = new TextField();
				msto = new TextField();
				add = new Button( "Dodaj" );
				cancel = new Button( "Anuluj" );
				
				add.addActionListener( this );
				cancel.addActionListener( this );
				
				add( new Label( "Nazwa:" ) );
				add( nazwa );
				add( new Label( "Adres:" ) );
				add( adres );
				add( new Label( "Miejsc siedzących:" ) );
				add( msie );
				add( new Label( "Miejsc stojących:" ) );
				add( msto );
				add( status );
				add( add );
				add( cancel );
			}

			@Override
			public void actionPerformed( ActionEvent e ) {
				if( e.getSource() == add ) {
					String snazwa = nazwa.getText();
					String sadres = adres.getText();
					int imsie, imsto;
					try {
						imsie = Integer.parseInt( msie.getText() );
						imsto = Integer.parseInt( msto.getText() );
					}
					catch( NumberFormatException ex ) {
						status.setText( "Niepoprawny format" );
						return;
					}
					try {
						CallableStatement cstmt = App.conn.prepareCall( "call dodaj_obiekt( ?, ?, ?, ?, ?, ?, ?, ? )" );
						cstmt.setString( 1, snazwa );
						cstmt.setInt( 2, id );
						cstmt.setString( 3, sadres );
						cstmt.setInt( 4, imsie );
						cstmt.setInt( 5, imsto );
						cstmt.setString( 6, login );
						cstmt.setString( 7, passwd );
						cstmt.registerOutParameter( 8, Types.VARCHAR );
						cstmt.executeUpdate();
						String ret = cstmt.getString( 8 );
						if( ret.equals( "success" ) ) {
							dialog.dispose();
						}
						else {
							status.setText( "Odmowa dostępu!" );
						}
					} catch( SQLException e1 ) {
						status.setText( "Błąd bazy danych 0x4" );
						e1.printStackTrace();
						return;
					}
					
				}
			}
			
		}
		
		class DodajKoncert extends Panel implements ActionListener {
			private static final long serialVersionUID = 1L;
			
			Choice obiekt;
			TextField zespol, data, pocz;
			Button add, cancel;
			Label status;
			
			ArrayList<Integer> obj;
			
			DodajKoncert() {
				setLayout( new GridLayout( 11, 1 ) );
				obj = new ArrayList<>();
				
				status = new Label();
				obiekt = new Choice();
				zespol = new TextField();
				data = new TextField();
				pocz = new TextField();
				add = new Button( "Dodaj" );
				cancel = new Button( "Anuluj" );
				
				add.addActionListener( this );
				cancel.addActionListener( this );
				
				add( new Label( "Obiekt:" ) );
				add( obiekt );
				add( new Label( "Zespół:" ) );
				add( zespol );
				add( new Label( "Data:" ) );
				add( data );
				add( new Label( "Początek sprzedaży: " ) );
				add( pocz );
				add( status );
				add( add );
				add( cancel );
				
				try {
					Statement stmt = App.conn.createStatement();
					ResultSet rs = stmt.executeQuery( "select nazwa_obiektu, id_obiektu from obiekty where id_wlasciciela = " + id );
					while( rs.next() ) {
						obiekt.add( rs.getString( 1 ) );
						obj.add( rs.getInt( 2 ) );
					}
				} catch (SQLException e) {
					status.setText( "Błąd bazy danych 0x5" );
					e.printStackTrace();
				}
			}

			@Override
			public void actionPerformed( ActionEvent e ) {
				if( e.getSource() == add ) {
					try {
						int idz = 0;
						Statement stmt = App.conn.createStatement();
						ResultSet rs = stmt.executeQuery( "select id_zespolu from zespoly where nazwa_zespolu = '" + zespol.getText() + "'" );
						if( rs.next() ) idz = rs.getInt( 1 );
						else {
							stmt = App.conn.createStatement();
							stmt.executeUpdate( "insert into zespoly values( null, '" + zespol.getText() + "', null )" );
							stmt = App.conn.createStatement();
							rs = stmt.executeQuery( "select id_zespolu from zespoly where nazwa_zespolu = '" + zespol.getText() + "'" );
							if( rs.next() ) idz = rs.getInt( 1 );
							else {
								status.setText( "Nie udało się dodać zespołu" );
								return;
							}
						}
						
						CallableStatement cstmt = App.conn.prepareCall( "call dodaj_koncert( ?, ?, ?, ?, ?, ?, ? )" );
						cstmt.setInt( 1, obj.get( obiekt.getSelectedIndex() ) );
						cstmt.setInt( 2, idz );
						cstmt.setString( 3, data.getText() );
						cstmt.setString( 4, pocz.getText() );
						cstmt.setString( 5, login );
						cstmt.setString( 6, passwd );
						cstmt.registerOutParameter( 7, Types.VARCHAR );
						cstmt.executeUpdate();
						String ret = cstmt.getString( 7 );
						if( ret.equals( "success" ) ) {
							dialog.dispose();
						}
						else {
							if( ret.equals( "incorrect values" ) ) status.setText( "Podane daty są sprzeczne, przeszłe, lub zajęte" );
							else status.setText( "Odmowa dostępu!" );
						}
					} catch( SQLException e1 ) {
						status.setText( "Błąd bazy danych 0x6 (podano nieprawidłowe dane)" );
						e1.printStackTrace();
						return;
					}
					
				}
			}
			
		}
		
		class DodajBilety extends Panel implements ActionListener {
			private static final long serialVersionUID = 1L;
			
			Choice koncert, typ;
			TextField ilosc, cena;
			Button add, cancel;
			Label status;
			
			ArrayList<Integer> kon;
			
			DodajBilety() {
				setLayout( new GridLayout( 11, 1 ) );
				kon = new ArrayList<>();
				
				
				status = new Label();
				koncert = new Choice();
				typ = new Choice();
				ilosc = new TextField();
				cena = new TextField();
				add = new Button( "Dodaj" );
				cancel = new Button( "Anuluj" );
				
				add.addActionListener( this );
				cancel.addActionListener( this );
				typ.add( "Siedzące" );
				typ.add( "Stojące" );
				
				add( new Label( "Koncert:" ) );
				add( koncert );
				add( new Label( "Ilość:" ) );
				add( ilosc );
				add( new Label( "Cena:" ) );
				add( cena );
				add( new Label( "Typ: " ) );
				add( typ );
				add( status );
				add( add );
				add( cancel );
				
				try {
					Statement stmt = App.conn.createStatement();
					ResultSet rs = stmt.executeQuery( "select nazwa_zespolu, data_koncertu, id_koncertu, nazwa_obiektu from "
													+ "obiekty o "
													+ "join koncerty k on o.id_obiektu = k.id_obiektu "
													+ "join zespoly z on k.id_zespolu = z.id_zespolu" );
					while( rs.next() ) {
						koncert.add( rs.getString( 1 ) + ", " + rs.getString( 2 ) + " " + rs.getString( 4 ) );
						kon.add( rs.getInt( 3 ) );
					}
				} catch (SQLException e) {
					status.setText( "Błąd bazy danych 0x5" );
					e.printStackTrace();
				}
			}

			@Override
			public void actionPerformed( ActionEvent e ) {
				if( e.getSource() == add ) {
					try {
						CallableStatement cstmt = App.conn.prepareCall( "call dodaj_bilety( ?, ?, ?, ?, ?, ?, ? )" );
						cstmt.setInt( 1, Integer.parseInt( ilosc.getText() ) );
						cstmt.setInt( 2, kon.get( koncert.getSelectedIndex() ) );
						cstmt.setInt( 3, Integer.parseInt( cena.getText() ) );
						cstmt.setString( 4, typ.getSelectedItem() );
						cstmt.setString( 5, login );
						cstmt.setString( 6, passwd );
						cstmt.registerOutParameter( 7, Types.VARCHAR );
						cstmt.executeUpdate();
						String ret = cstmt.getString( 7 );
						if( ret.equals( "success" ) ) {
							dialog.dispose();
						}
						else {
							if( ret.equals( "num too big" ) ) status.setText( "Przekroczono deklarowaną liczbę miejsc" );
							else if( ret.equals( "authorization failes" ) ) status.setText( "Odmowa dostępu!" );
							else status.setText( "Błędne dane" );
						}
					} catch( SQLException e1 ) {
						status.setText( "Błąd bazy danych 0x7" );
						e1.printStackTrace();
						return;
					}
					catch( NumberFormatException ex ) {
						status.setText( "Niepoprawny format" );
						ex.printStackTrace();
						return;
					}
				}
				
			}
			
		}
		
	}
	
}
