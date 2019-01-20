package liveWroclaw;

import java.awt.BorderLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SearchPanel extends Panel implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;

	TextField searchbox;
	List list;
	
	public SearchPanel() {
		setLayout( new BorderLayout() );
		
		searchbox = new TextField();
		list = new List( 17 );
		
		searchbox.addKeyListener( this );
		
		add( searchbox, BorderLayout.NORTH );
		add( list, BorderLayout.CENTER );
	}
	
	@Override
	public void keyReleased( KeyEvent ke ) {
		if( ke.getKeyCode() == KeyEvent.VK_ENTER ) {
			System.out.println( "Wyszukaj: " + searchbox.getText() );
			// TODO
			// Tutaj procedura wyszukiwania
			// searchbox.getText() zwraca tekst wyszukiwania
			// na razie spróbujmy z samym wyszukiwaniem
			// list.add( ~~String~~ ) dodaje element do listy
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

	@Override
	public void keyPressed( KeyEvent ke ) {
	}

	@Override
	public void keyTyped( KeyEvent ke ) {
	}
	
}
