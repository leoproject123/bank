package ui.admin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import exception.ServiceException;
import service.ProductService;
import ui.manager.PanelManager;
import ui.user.UserHome;

public class SavingsAccountScreen extends JPanel {

	private JLabel accountNumber;
	private JLabel generatedAccountNumber;
	private JLabel initialBalance;
	private JTextField initialBalanceField;
	private JButton submitButton;

	public JPanel init() {

		this.setLayout(new BorderLayout());
		JPanel secondary = new JPanel();
		secondary.setLayout(new GridLayout(12, 0));

		ProductService productService = new ProductService();
		String auxRandomNumber = null;
		try {
			auxRandomNumber = productService.generateAccountNumber();
		} catch (ServiceException e2) {
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
		}
		String randomAccountNumber = auxRandomNumber;

		accountNumber = new JLabel("Numero de Cuenta:");
		generatedAccountNumber = new JLabel(randomAccountNumber);
		generatedAccountNumber.setFont(new Font("Arial", Font.ITALIC, generatedAccountNumber.getFont().getSize()));
		initialBalance = new JLabel("Balance Inicial:");
		initialBalanceField = new JTextField("0");

		submitButton = new JButton("CREAR PRODUCTO");
		submitButton.setFont(new Font("Arial", Font.BOLD, submitButton.getFont().getSize()));

		secondary.add(accountNumber);
		secondary.add(generatedAccountNumber);
		secondary.add(initialBalance);
		secondary.add(initialBalanceField);
		secondary.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

		this.add(secondary, BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.SOUTH);

		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					if (Float.parseFloat(initialBalanceField.getText()) <= 0) {
						JOptionPane.showMessageDialog(null, "Solo valores positivos");
						return;
					}
					productService.create(randomAccountNumber, "SA", PanelManager.selectedUser.getID(),
							Float.parseFloat(initialBalanceField.getText()));
					JOptionPane.showMessageDialog(null, "Producto creado!");
					PanelManager.selectedUser = null;
					UserHome userHome = new UserHome();
					PanelManager.changePanel(userHome.screenBuilder("admin main panel"));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Importe incorrecto", "Lo siento!", JOptionPane.ERROR_MESSAGE);
				} catch (ServiceException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return this;
	}

}
