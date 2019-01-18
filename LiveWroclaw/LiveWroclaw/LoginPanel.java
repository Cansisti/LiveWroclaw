package LiveWroclaw;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class LoginPanel extends Panel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	TextField login;
	TextField password;
	Button apply;
	App app;
	
	public LoginPanel( App _app ) {
		app = _app;
		setLayout( new BorderLayout() );
		
		login = new TextField();
		password = new TextField();
		apply = new Button( "Login" );
		
		login.setPreferredSize( new Dimension( 220, 0 ) );
		apply.setPreferredSize( new Dimension( 70, 0 ) );
		password.setEchoChar( '#' );
		apply.addActionListener( this );
		
		add( login, BorderLayout.WEST );
		add( password, BorderLayout.CENTER );
		add( apply, BorderLayout.EAST );
		
	}

	@Override
	public void actionPerformed( ActionEvent eve ) {
		if( eve.getSource() == apply ) {
			try {
				if( apply.getLabel().equals( "Logout" ) ) {
					App.connect( "klient", "klient" );
					login.setEditable( true );
					login.setText( "" );
					password.setVisible( true );
					apply.setLabel( "Login" );
					System.out.println( "Connected as klient" );
					app.setPanel( new SearchPanel() );
					return;
				}
				String slogin = login.getText();
				String spasswd = password.getText();
				if( slogin.equals( "admin" ) ) {
					if( spasswd.equals( "LiveWroclaw" ) ) {
						App.connect( "admin", "asd9ebi1d97wbuscnaufb1cnbaybcs1" );
						System.out.println( "Connected as admin" );
						return;
					}
				}
				CallableStatement cstmt = App.conn.prepareCall( "call autoryzacja( ?, ?, ?, ?)" );
				cstmt.setString( 1, slogin );
				cstmt.setString( 2, spasswd );
				cstmt.registerOutParameter( 3, Types.INTEGER );
				cstmt.registerOutParameter( 4, Types.VARCHAR );
				cstmt.executeUpdate();
				
				int id = cstmt.getInt( 3 );
				String ret = cstmt.getString( 4 );
				
				if( ret.equals( "success" ) ) {
					App.conn.close();
					App.connect( "wlasciciel", "wla7182311bvd1utdvu1d" );
					System.out.println( "Connected as wlasciciel" );
					login.setEditable( false );
					password.setText( "" );
					password.setVisible( false );
					apply.setLabel( "Logout" );
					app.setPanel( new ManagePanel( id ) );
					return;
				}
				else {
					System.out.println( "Authentication failed" );
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
