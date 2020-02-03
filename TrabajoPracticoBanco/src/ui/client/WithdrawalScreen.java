package ui.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import entity.Product;
import exception.ServiceException;
import service.ProductService;
import service.TransactionService;
import ui.manager.PanelManager;
import ui.user.UserHome;

public class WithdrawalScreen extends JPanel {

	private JLabel account;
	private JLabel amount;
	private JComboBox accountSelect;
	private JTextField amountField;
	private JButton submit;
	private ComboBoxModel model;
	List<Product> products = null;

	public JPanel init() {

		this.setLayout(new BorderLayout());
		JPanel secondary = new JPanel();
		secondary.setLayout(new GridLayout(12, 0));

		ProductService productService = new ProductService();
		TransactionService transactionService = new TransactionService();

		try {
			products = productService.getByUserId(Integer.toString(PanelManager.loggedUser.getID()));
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		String labels[] = new String[products.size()];
		try {
			labels = productService.getAccountLabelsByUser(products, "--- seleccione cuenta ---");
		} catch (ServiceException e2) {
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Something went wrong!", JOptionPane.ERROR_MESSAGE);
		}

		account = new JLabel("Cuenta");
		accountSelect = new JComboBox(labels);
		amount = new JLabel("Importe");
		amountField = new JTextField("");
		submit = new JButton("INGRESAR");
		submit.setFont(new Font("Arial", Font.BOLD, submit.getFont().getSize()));

		secondary.add(account);
		secondary.add(accountSelect);
		secondary.add(amount);
		secondary.add(amountField);
		secondary.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

		this.add(secondary, BorderLayout.CENTER);
		this.add(submit, BorderLayout.SOUTH);

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				float value = 0.0f;

				try {
					value = Float.parseFloat(amountField.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Importe no valido", "Lo siento!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (accountSelect.getSelectedItem() == "--- seleccione cuenta ---" || value <= 0) {
					JOptionPane.showMessageDialog(null,
							"Selecciona una cuenta y complete los campos. (solo valores positivos)");
					return;
				}

				Product product = products.stream()
						.filter(prod -> prod.getAccountNumber()
								.equals(((String) accountSelect.getSelectedItem()).substring(0, 8)))
						.findAny().orElse(null);

				try {

					float withdrawalAmount = value;
					float balance = productService.getDebitBalance(product, withdrawalAmount);
					boolean enoughFunds = productService.areEnoughFunds(product, withdrawalAmount, balance);

					if (enoughFunds) {

						product.setBalance(balance);
						String originAccount = product.getAccountNumber();
						String originAccountType = product.getCode();

						productService.update(product);
						JOptionPane.showMessageDialog(null, "Extraccion realizada con exito");
						transactionService.create(originAccount, originAccountType, -withdrawalAmount, 1,
								PanelManager.loggedUser.getID());
						UserHome userHome = new UserHome();
						PanelManager.changePanel(userHome.screenBuilder("main screen"));

					} else {

						JOptionPane.showMessageDialog(null, "Not enough funds to withdraw");

					}
				} catch (ServiceException e1) {

					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		return this;
	}
}
