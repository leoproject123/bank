package ui.user;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import ui.manager.PanelManager;

public class FooterButton {

	private String label;
	private JButton footerButton;
	private UserHome userHome;

	public FooterButton(String label) {
		this.label = label;
	}

	public JButton init() {
		userHome = new UserHome();
		footerButton = new JButton(label);
		footerButton.setForeground(Color.BLUE);
		footerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (label.equalsIgnoreCase("main screen")) {

					PanelManager.changePanel(userHome.screenBuilder("main screen"));

				} else if (label.equalsIgnoreCase("log out")) {

					PanelManager.loggedUser = null;
					PanelManager.changePanel(userHome.screenBuilder("home page"));

				} else if (label.equalsIgnoreCase("admin main panel")) {

					PanelManager.changePanel(userHome.screenBuilder("admin main panel"));

				}

			}

		});
		return footerButton;
	}
}
