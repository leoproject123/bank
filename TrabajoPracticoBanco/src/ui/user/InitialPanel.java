package ui.user;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class InitialPanel extends JPanel {

	private Login login;
	private JLabel copyright;

	public JPanel buildPanelScreen() {

		setBorder(new EmptyBorder(20, 20, 0, 20));

		this.setLayout(new BorderLayout());
		login = new Login();
		login.init();
		this.add(login, BorderLayout.CENTER);

		copyright = new JLabel("© Leo's bank. All rights reserved.");
		this.add(copyright, BorderLayout.SOUTH);

		return this;

	}
}
