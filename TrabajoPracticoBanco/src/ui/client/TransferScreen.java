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

public class TransferScreen extends JPanel {

	private JLabel originAccount;
	private JLabel destinationAccount;
	private JLabel amount;
	private JComboBox originAccountSelect;
	private JTextField destinationAccountSelect;
	private JTextField amountInput;
	private JButton submit;
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
			JOptionPane.showMessageDialog(null, e.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
		}

		String labels[] = new String[products.size()];
		try {
			labels = productService.getAccountLabelsByUser(products, "--- seleccione cuenta ---");
		} catch (ServiceException e2) {
			JOptionPane.showMessageDialog(null, e2.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
		}

		originAccount = new JLabel("Cuenta Origen");
		originAccountSelect = new JComboBox(labels);
		destinationAccount = new JLabel("Cuenta Destino");
		destinationAccountSelect = new JTextField("");
		amount = new JLabel("Importe");
		amountInput = new JTextField("");
		submit = new JButton("INGRESAR");
		submit.setFont(new Font("Arial", Font.BOLD, submit.getFont().getSize()));

		secondary.add(originAccount);
		secondary.add(originAccountSelect);
		secondary.add(destinationAccount);
		secondary.add(destinationAccountSelect);
		secondary.add(amount);
		secondary.add(amountInput);
		secondary.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

		this.add(secondary, BorderLayout.CENTER);
		this.add(submit, BorderLayout.SOUTH);

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				float value = 0.0f;

				try {
					value = Float.parseFloat(amountInput.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Importe no valido", "Lo siento!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (originAccountSelect.getSelectedItem() == "--- seleccione cuenta ---" || value <= 0) {
					JOptionPane.showMessageDialog(null,
							"Seleccione cuenta y complete los campos. (solo valores positivos)");
					return;
				}

				Product product = products.stream()
						.filter(prod -> prod.getAccountNumber()
								.equals(((String) originAccountSelect.getSelectedItem()).substring(0, 8)))
						.findAny().orElse(null);

				try {

					float transferAmount = Float.valueOf(amountInput.getText());
					float currentAmount = product.getBalance();
					float balance = productService.getDebitBalance(product, transferAmount);
					boolean enoughFunds = productService.areEnoughFunds(product, transferAmount, balance);

					if (enoughFunds) {

						product.setBalance(balance);

						String originAccount = product.getAccountNumber();
						String originAccountType = product.getCode();

						Product destinationProduct = productService
								.getByAccountNumber(destinationAccountSelect.getText());
						if (destinationProduct == null) {
							JOptionPane.showMessageDialog(null, "Cuenta de destino inexistente");
							product.setBalance(currentAmount);
							return;
						}
						productService.update(product);

						float destinationBalance = productService.getCreditBalance(destinationProduct, transferAmount);
						destinationProduct.setBalance(destinationBalance);
						productService.update(destinationProduct);

						transactionService.create(originAccount, originAccountType, -transferAmount, 2,
								PanelManager.loggedUser.getID());
						transactionService.create(destinationProduct.getAccountNumber(), destinationProduct.getCode(),
								transferAmount, 2, destinationProduct.getClientId());

						JOptionPane.showMessageDialog(null, "Transferencia realizada con exito");
						UserHome userHome = new UserHome();
						PanelManager.changePanel(userHome.screenBuilder("main screen"));

					} else {
						JOptionPane.showMessageDialog(null, "Saldo no disponible para transferir");
					}
				} catch (ServiceException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return this;
	}
}
