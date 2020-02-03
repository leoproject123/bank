package ui.admin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import exception.ServiceException;
import service.ProductService;
import ui.manager.PanelManager;
import ui.user.UserHome;

public class CreditCardScreen extends JPanel {

	private JLabel accountNumber;
	private JLabel generatedAccountNumber;
	private JLabel creditLimit;
	private JTextField creditLimitField;
	private JButton submitButton;

	public JPanel init() {

		this.setLayout(new BorderLayout());
		JPanel secondary = new JPanel();
		secondary.setLayout(new GridLayout(12, 0));

		ProductService productService = new ProductService();
		String auxRandomAccount = null;
		try {
			auxRandomAccount = productService.generateAccountNumber();
		} catch (ServiceException e2) {
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
		}
		String randomAccountNumber = auxRandomAccount;

		accountNumber = new JLabel("Numero de Cuenta:");
		generatedAccountNumber = new JLabel(randomAccountNumber);
		generatedAccountNumber.setFont(new Font("Arial", Font.ITALIC, generatedAccountNumber.getFont().getSize()));
		creditLimit = new JLabel("Limite de Credito:");
		creditLimitField = new JTextField("10000");

		submitButton = new JButton("CREAR PRODUCTO");
		submitButton.setFont(new Font("Arial", Font.BOLD, submitButton.getFont().getSize()));

		secondary.add(accountNumber);
		secondary.add(generatedAccountNumber);
		secondary.add(creditLimit);
		secondary.add(creditLimitField);
		secondary.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

		this.add(secondary, BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.SOUTH);

		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					if (Float.parseFloat(creditLimitField.getText()) <= 0) {
						JOptionPane.showMessageDialog(null, "Solo valores positivos");
						return;
					}
					productService.createCreditCard(randomAccountNumber, PanelManager.selectedUser.getID(),
							Float.parseFloat(creditLimitField.getText()));
					JOptionPane.showMessageDialog(null, "Producto creado!");
					PanelManager.selectedUser = null;
					UserHome userHome = new UserHome();
					PanelManager.changePanel(userHome.screenBuilder("admin main panel"));
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Importe no valido", "Lo siento!", JOptionPane.ERROR_MESSAGE);
				} catch (ServiceException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return this;
	}

}
