package ui.user;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.Admin;
import entity.Client;
import entity.User;
import exception.InvalidPasswordException;
import exception.ServiceException;
import service.UserService;
import exception.InvalidUserException;
import ui.manager.PanelManager;

public class Login extends JPanel {

	private JLabel user;
	private JLabel password;
	private JTextField userField;
	private JTextField passwordField;
	private JButton submit;
	private JButton exit;

	public JPanel init() {

		this.setLayout(new BorderLayout());
		JPanel secondary = new JPanel();
		secondary.setLayout(new GridLayout(12, 0));

		user = new JLabel("Usuario");
		password = new JLabel("Password");
		userField = new JTextField("");
		passwordField = new JPasswordField("");

		secondary.add(user);
		secondary.add(userField);
		secondary.add(password);
		secondary.add(passwordField);
		secondary.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

		submit = new JButton("INGRESAR");
		exit = new JButton("SALIR");

		JPanel buttons = new JPanel(new FlowLayout());
		buttons.add(submit);
		buttons.add(exit);

		this.add(secondary, BorderLayout.CENTER);
		this.add(buttons, BorderLayout.SOUTH);

		UserService userService = new UserService();
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					User user = userService.validateCredentials(userField.getText(), passwordField.getText());
					PanelManager.loggedUser = user;
					UserHome userHome = new UserHome();

					if (user instanceof Admin) {
						PanelManager.changePanel(userHome.screenBuilder("admin main panel"));
					} else if (user instanceof Client) {
						PanelManager.changePanel(userHome.screenBuilder("main screen"));
					}
				} catch (InvalidPasswordException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				} catch (InvalidUserException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				} catch (ServiceException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		exit.addActionListener(ExitDialogue.getInstance());

		return this;

	}

}
