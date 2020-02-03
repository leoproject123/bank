package ui.manager;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entity.User;
import ui.user.UserHome;

public class PanelManager {

	private static UserHome usuarioHome;
	private static JFrame frame;
	public static User loggedUser;
	public static User selectedUser;

	public void buildManager() {

		frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setResizable(false);
		frame.setTitle("Leo's Bank");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		usuarioHome = new UserHome();
		changePanel(usuarioHome.screenBuilder("home page"));

	}

	public void showFrame() {
		frame.setVisible(true);
	}

	public static void changePanel(JPanel panel) {

		frame.getContentPane().removeAll();
		frame.getContentPane().add(panel);
		frame.getContentPane().validate();
	}

}
