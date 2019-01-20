package liveWroclaw;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class App extends WindowAdapter implements ActionListener {
	
	static Frame frame;
	static Connection conn;
	Panel panel;
	LoginPanel loginPanel;
	
	public App() {
		frame = new Frame( "LiveWrocław" );
        frame.setBounds( 300, 100, 1024/2, 768/2 );
        frame.addWindowListener( this );
        frame.setLayout( new BorderLayout() );
        
        loginPanel = new LoginPanel( this );
        
        frame.add( loginPanel, BorderLayout.SOUTH );
        
        frame.setVisible( true );
        
        try {
        	connect( "klient", "klient" );
        	System.out.println( "Connected as klient" );
        	setPanel( new SearchPanel() );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}
	
	public void setPanel( Panel p ) {
		if( panel != null ) {
			frame.remove( panel );
		}
		panel = p;
		frame.add( panel, BorderLayout.NORTH );
		frame.setVisible( true );
	}

	@Override
	public void actionPerformed( ActionEvent arg0 ) {
		
	}
	
	public static void connect( String user, String passwd ) throws SQLException {
		Properties prop = new Properties();
        prop.put( "user", user );
        prop.put( "password", passwd );
        prop.put( "serverTimezone", "UTC" );
        prop.put( "useSSL", "FALSE" );
        prop.put( "allowPublicKeyRetrieval", "TRUE" );
        
        conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/livewroclaw2", prop );
	}
	
	@Override
	public void windowClosing( WindowEvent e ) {
		try {
			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.exit( 0 );
	}
	
	public static void main( String[] args ) {
    	new App();
    }
}
