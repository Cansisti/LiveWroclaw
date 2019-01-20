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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AdminPanel extends Panel implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	List zespoly;
	Button add, update;
	Panel ctrl;
	TextField nazwa, kategoria;
	Label zid;
	
	ArrayList<Integer> zids;
	ArrayList<String> names;
	ArrayList<String> cats;
	
	public AdminPanel() {
		setLayout( new BorderLayout() );
		zids = new ArrayList<>();
		names = new ArrayList<>();
		cats = new ArrayList<>();
		
		zespoly = new List( 8 );
		add = new Button( "Dodaj właściciela" );
		ctrl = new Panel();
		nazwa = new TextField();
		kategoria = new TextField();
		zid = new Label();
		update = new Button( "Aktualizuj" );
		ctrl.setLayout( new GridLayout( 3, 2) );
		zespoly.addItemListener( this );
		add.addActionListener( this );
		update.addActionListener( this );
		
		ctrl.add( new Label( "Nazwa:" ) );
		ctrl.add( nazwa );
		ctrl.add( new Label( "Kategoria:" ) );
		ctrl.add( kategoria );
		ctrl.add( zid );
		ctrl.add( update );
		add( zespoly, BorderLayout.NORTH );
		add( ctrl, BorderLayout.CENTER );
		add( add, BorderLayout.SOUTH );
		
		try {
			Statement stmt = App.conn.createStatement();
			ResultSet rs = stmt.executeQuery( "select * from zespoly" );
			while( rs.next() ) {
				zespoly.add( rs.getString( "nazwa_zespolu" ) );
				zids.add( rs.getInt( "id_zespolu" ) );
				names.add( rs.getString( "nazwa_zespolu" ) );
				cats.add( rs.getString( "kategoria" ) );
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		if( e.getSource() == update ) {
			try {
				Statement stmt = App.conn.createStatement();
				stmt.executeUpdate( "update zespoly set nazwa_zespolu = '" + nazwa.getText() + "' and kategoria = '"
									+ kategoria.getText() + "' where id_zespolu = " + zids.get( zespoly.getSelectedIndex() ) );
			}
			catch( SQLException ex ) {
				ex.printStackTrace();
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
