package liveWroclaw;


import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchPanel extends Panel implements ActionListener {
	private static final long serialVersionUID = 1L;

	TextField searchbox;
	
	public SearchPanel() {
		setLayout( new BorderLayout() );
		
		searchbox = new TextField();
		
		add( searchbox, BorderLayout.NORTH );
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
	
}
