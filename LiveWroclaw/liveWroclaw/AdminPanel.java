package liveWroclaw;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class AdminPanel extends Panel implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	List zespoly;
	Button add, update;
	Panel ctrl1, ctrl2;
	TextField nazwa, kategoria, login, passwd;
	Label zid, ret;
	
	ArrayList<Integer> zids;
	ArrayList<String> names;
	ArrayList<String> cats;
	
	public AdminPanel() {
		setLayout( new BorderLayout() );
		zids = new ArrayList<>();
		names = new ArrayList<>();
		cats = new ArrayList<>();
		
		ctrl2 = new Panel();
		zespoly = new List( 8 );
		add = new Button( "Dodaj właściciela" );
		ctrl1 = new Panel();
		nazwa = new TextField();
		kategoria = new TextField();
		login = new TextField();
		passwd = new TextField();
		zid = new Label();
		update = new Button( "Aktualizuj" );
		ret = new Label();
		
		ctrl1.setLayout( new GridLayout( 3, 2) );
		zespoly.addItemListener( this );
		add.addActionListener( this );
		update.addActionListener( this );
		ctrl2.setLayout( new GridLayout( 3, 2) );
		passwd.setEchoChar( '#' );
		
		ctrl1.add( new Label( "Nazwa:" ) );
		ctrl1.add( nazwa );
		ctrl1.add( new Label( "Kategoria:" ) );
		ctrl1.add( kategoria );
		ctrl1.add( zid );
		ctrl1.add( update );
		ctrl2.add( new Label( "Nowy login:" ) );
		ctrl2.add( login );
		ctrl2.add( new Label( "Nowe hasło:" ) );
		ctrl2.add( passwd );
		ctrl2.add( ret );
		ctrl2.add( add );
		add( zespoly, BorderLayout.NORTH );
		add( ctrl1, BorderLayout.CENTER );
		add( ctrl2, BorderLayout.SOUTH );
		
		reload();
	}
	
	void reload() {
		try {
			int x = zespoly.getSelectedIndex();
			Statement stmt = App.conn.createStatement();
			ResultSet rs = stmt.executeQuery( "select * from zespoly order by nazwa_zespolu" );
			zespoly.removeAll();
			zids.clear();
			names.clear();
			cats.clear();
			while( rs.next() ) {
				zespoly.add( rs.getString( "nazwa_zespolu" ) );
				zids.add( rs.getInt( "id_zespolu" ) );
				names.add( rs.getString( "nazwa_zespolu" ) );
				cats.add( rs.getString( "kategoria" ) );
			}
			zespoly.select( x );
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		if( e.getSource() == update ) {
			try {
				Statement stmt = App.conn.createStatement();
				stmt.executeUpdate( "update zespoly set nazwa_zespolu = '" + nazwa.getText() + "', kategoria = '"
									+ kategoria.getText() + "' where id_zespolu = " + zids.get( zespoly.getSelectedIndex() ) );
			}
			catch( SQLException ex ) {
				ex.printStackTrace();
			}
			reload();
		}
		if( e.getSource() == add ) {
			try {
				CallableStatement cstmt = App.conn.prepareCall( "call dodaj_wlasciciela( ?, ?, ? )" );
				cstmt.setString( 1, login.getText() );
				cstmt.setString( 2, passwd.getText() );
				cstmt.registerOutParameter( 3, Types.VARCHAR );
				cstmt.executeQuery();
				if( cstmt.getString( 3 ).equals( "login alerady in use" ) ) {
					ret.setText( "Login już zajęty" );
				}
				else {
					ret.setText( "" );
					login.setText( "" );
				}
				passwd.setText( "" );
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void itemStateChanged( ItemEvent e ) {
		zid.setText( "ID: " + zids.get( zespoly.getSelectedIndex() ).toString() );
		nazwa.setText( names.get( zespoly.getSelectedIndex() ) );
		kategoria.setText( cats.get( zespoly.getSelectedIndex() ) );
	}
}
