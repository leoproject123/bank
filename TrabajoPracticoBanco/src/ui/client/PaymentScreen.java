package ui.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
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

public class PaymentScreen extends JPanel {

	private JLabel service;
	private JComboBox serviceSelect;
	private JLabel account;
	private JComboBox accountSelect;
	private JLabel amount;
	private JTextField amountField;
	private JButton submit;
	List<Product> products = null;

	public JPanel init() {

		this.setLayout(new BorderLayout());
		JPanel secondary = new JPanel();
		secondary.setLayout(new GridLayout(12, 0));

		ProductService productService = new ProductService();
		TransactionService transferService = new TransactionService();
		try {
			products = productService.getByUserId(Integer.toString(PanelManager.loggedUser.getID()));
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
		}

		String labels[] = new String[products.size()];
		try {
			labels = productService.getAccountLabelsByUser(products, "--- seleccione cuenta ---");
		} catch (ServiceException e2) {
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
		}

		String[] availableServicesToPay = { "--- seleccione servicio ---", "UP", "Edenor", "Aysa", "Claro",
				"Cablevision", "Liberty", "Telecom" };

		service = new JLabel("Servicio");
		serviceSelect = new JComboBox(availableServicesToPay);
		account = new JLabel("Cuenta");
		accountSelect = new JComboBox(labels);
		amount = new JLabel("Importe");
		amountField = new JTextField("");
		submit = new JButton("INGRESAR");
		submit.setFont(new Font("Arial", Font.BOLD, submit.getFont().getSize()));

		secondary.add(service);
		secondary.add(serviceSelect);
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

				if (accountSelect.getSelectedItem() == "--- seleccione cuenta ---"
						|| serviceSelect.getSelectedItem() == "--- seleccione servicio ---" || value <= 0) {
					JOptionPane.showMessageDialog(null,
							"Seleccione cuenta/servicio y complete todos los campos. (solo valores positivos)");
					return;
				}

				Product product = products.stream()
						.filter(prod -> prod.getAccountNumber()
								.equals(((String) accountSelect.getSelectedItem()).substring(0, 8)))
						.findAny().orElse(null);

				try {

					float paymentAmount = value;
					float balance = productService.getDebitBalance(product, paymentAmount);
					boolean enoughFunds = productService.areEnoughFunds(product, paymentAmount, balance);

					if (enoughFunds) {

						product.setBalance(balance);

						String originAccount = product.getAccountNumber();
						String originAccountType = product.getCode();

						productService.update(product);
						JOptionPane.showMessageDialog(null, "pago realizado con exito");
						transferService.create(originAccount, originAccountType, -paymentAmount, 3,
								PanelManager.loggedUser.getID());
						UserHome userHome = new UserHome();
						PanelManager.changePanel(userHome.screenBuilder("main screen"));

					} else {
						JOptionPane.showMessageDialog(null, "Saldo no disponible para abonar");
					}
				} catch (ServiceException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return this;
	}
}
