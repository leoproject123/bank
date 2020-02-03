package ui.admin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.Client;
import entity.User;
import exception.ServiceException;
import service.UserService;
import ui.manager.PanelManager;
import ui.user.UserHome;

public class CreateUserScreen extends JPanel {

	private JLabel userId;
	private JLabel user;
	private JLabel userPassword;
	private JLabel userIdMessage;
	private JTextField userField;
	private JTextField userPasswordField;
	private JButton submitButton;

	public JPanel init() {

		this.setLayout(new BorderLayout());
		JPanel secondary = new JPanel();
		secondary.setLayout(new GridLayout(12, 0));

		UserService userService = new UserService();

		userId = new JLabel("ID:");
		userIdMessage = new JLabel("Generado automaticamente");
		userIdMessage.setFont(new Font("Arial", Font.ITALIC, userIdMessage.getFont().getSize()));
		user = new JLabel("Usuario:");
		userField = new JTextField("");
		userPassword = new JLabel("Password:");
		userPasswordField = new JPasswordField("");

		submitButton = new JButton("ENVIAR");
		submitButton.setFont(new Font("Arial", Font.BOLD, submitButton.getFont().getSize()));

		secondary.add(userId);
		secondary.add(userIdMessage);
		secondary.add(user);
		secondary.add(userField);
		secondary.add(userPassword);
		secondary.add(userPasswordField);
		secondary.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

		this.add(secondary, BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.SOUTH);

		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					if (userField.getText().equals("") || userPasswordField.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Por favor, ingrese usuario y password validos");
						return;
					}

					if (userService.checkForRepeated(userField.getText())) {
						JOptionPane.showMessageDialog(null, "El nombre " + userField.getText() + " no esta disponible");
						return;
					}

					userService.create(userField.getText(), userPasswordField.getText(), "client");
					JOptionPane.showMessageDialog(null, "User succesfully created");
					UserHome userHome = new UserHome();
					PanelManager.changePanel(userHome.screenBuilder("admin main panel"));
				} catch (ServiceException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return this;

	}

}
