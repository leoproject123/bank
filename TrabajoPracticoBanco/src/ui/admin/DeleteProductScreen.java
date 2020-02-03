package ui.admin;

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

import entity.Product;
import exception.ServiceException;
import service.ProductService;
import ui.manager.PanelManager;
import ui.user.UserHome;

public class DeleteProductScreen extends JPanel {

	private JLabel selectAccount;
	private JComboBox accounts;
	private JButton submitButton;
	List<Product> products = null;

	public JPanel init() {

		this.setLayout(new BorderLayout());
		JPanel secondary = new JPanel();
		secondary.setLayout(new GridLayout(12, 0));

		UserHome userHome = new UserHome();
		ProductService productService = new ProductService();

		String labels[] = null;

		try {
			products = productService.getByUserId(Integer.toString(PanelManager.selectedUser.getID()));
			labels = new String[products.size()];
			labels = productService.getAccountLabelsByUser(products, "--- seleccione producto ---");
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
		}

		selectAccount = new JLabel("Seleccione cuenta a eliminar");
		accounts = new JComboBox(labels);
		submitButton = new JButton("ELIMINAR");
		submitButton.setFont(new Font("Arial", Font.BOLD, submitButton.getFont().getSize()));

		secondary.add(selectAccount);
		secondary.add(accounts);
		secondary.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

		this.add(secondary, BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.SOUTH);

		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (accounts.getSelectedItem() == "--- seleccione producto ---") {
					JOptionPane.showMessageDialog(null, "Seleccione una cuenta");
					return;
				}

				Product account = products.stream().filter(
						prod -> prod.getAccountNumber().equals(((String) accounts.getSelectedItem()).substring(0, 8)))
						.findAny().orElse(null);

				try {
					productService.delete(account.getAccountNumber());
					JOptionPane.showMessageDialog(null, "product successfully deleted");
					PanelManager.selectedUser = null;
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
