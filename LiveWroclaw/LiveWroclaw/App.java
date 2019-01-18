package LiveWroclaw;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class App extends WindowAdapter implements ActionListener {
	
	Frame frame;
	Connection conn;
	Properties prop;
	
	public App() {
		frame = new Frame( "LiveWrocław" );
        frame.setBounds( 300, 100, 1024/2, 768/2 );
        frame.addWindowListener( this );
        frame.setVisible( true );
        
        prop = new Properties();
        prop.put( "user", "debug" );
        prop.put( "password", "debug" );
        prop.put( "serverTimezone", "UTC" );
        prop.put( "useSSL", "FALSE" );
        prop.put( "allowPublicKeyRetrieval", "TRUE" );
        
        try {
			conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/livewroclaw2", prop );
		} catch( SQLException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
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
