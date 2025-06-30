package windowtest;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Label;
import java.awt.ScrollPane;
import javax.swing.JLayeredPane;

public class windowtests extends JPanel
{

	/**
	 * Create the panel.
	 */
	public windowtests()
	{
		setLayout(null);
		
		JButton btnNewButton = new JButton("Load");
		btnNewButton.setIcon(new ImageIcon("/Users/Oliver/Documents/Programming/Java/UOBCWK/CWK 5 - SY/panda2cw5/src/solution/resources/load.jpg"));
		btnNewButton.setForeground(Color.DARK_GRAY);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(140, 115, 160, 54);
		add(btnNewButton);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(6, 313, 468, -306);
		add(layeredPane);

	}
}
