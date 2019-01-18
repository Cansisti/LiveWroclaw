package LiveWroclaw;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagePanel extends Panel implements ActionListener {
	private static final long serialVersionUID = 1L;

	int id;
	
	Button test;
	
	public ManagePanel( int _id ) {
		id = _id;
		setLayout( new BorderLayout() );
		
		test = new Button( "test" );
		
		add( test, BorderLayout.NORTH );
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
