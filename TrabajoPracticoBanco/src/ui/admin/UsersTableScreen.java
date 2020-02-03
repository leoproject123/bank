package ui.admin;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import entity.Product;
import entity.User;
import exception.ServiceException;
import service.ProductService;
import service.UserService;
import ui.manager.PanelManager;
import ui.user.UserHome;

public class UsersTableScreen extends JPanel implements ActionListener {

	private JTable userTable;
	private UsersTableModel model;
	private JScrollPane tableScrollPane;
	private JButton createUser;
	private JButton deleteUser;
	private JButton createProduct;
	private JButton deleteProduct;
	private UserHome userHome;

	public UsersTableScreen() {
		super();
		buildPanel();
	}

	private void buildPanel() {

		userHome = new UserHome();
		model = new UsersTableModel();
		userTable = new JTable(model);
		userTable.getColumnModel().getColumn(0).setPreferredWidth(15);
		userTable.setPreferredScrollableViewportSize(new Dimension(452, 325));
		userTable.setFillsViewportHeight(true);

		tableScrollPane = new JScrollPane(userTable);

		this.add(tableScrollPane);

		createUser = new JButton("Crear");
		createUser.addActionListener(this);
		this.add(createUser);

		deleteUser = new JButton("Borrar");
		deleteUser.addActionListener(this);
		this.add(deleteUser);

		createProduct = new JButton("Crear Producto");
		createProduct.addActionListener(this);
		this.add(createProduct);

		deleteProduct = new JButton("Borrar Producto");
		deleteProduct.addActionListener(this);
		this.add(deleteProduct);

		displayClients(model);
	}

	private void displayClients(UsersTableModel modelo) {

		UserService userService = new UserService();
		List<User> users = new ArrayList<>();

		try {
			users = userService.getClients();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		modelo.setContent(users);
		modelo.fireTableDataChanged();
	}

	public void actionPerformed(ActionEvent e) {

		UserService userService = new UserService();
		int selectedRow = this.userTable.getSelectedRow();
		User user = null;

		if (selectedRow >= 0) {
			user = this.model.getContent().get(selectedRow);
			PanelManager.selectedUser = user;
		}

		if (e.getSource() == createUser) {

			PanelManager.changePanel(userHome.screenBuilder("create user"));

		} else if (e.getSource() == deleteUser) {

			if (selectedRow >= 0) {
				this.model.getContent().remove(selectedRow);
				ProductService productService = new ProductService();

				try {
					userService.delete(user.getID());
					productService.deleteByUserId(PanelManager.selectedUser.getID());
				} catch (ServiceException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				}

				PanelManager.selectedUser = null;
				model.fireTableDataChanged();

			} else {
				JOptionPane.showMessageDialog(null, "Por favor, seleccione usuario");
			}

		} else if (e.getSource() == createProduct) {

			if (selectedRow >= 0) {
				PanelManager.changePanel(userHome.screenBuilder("product"));
			} else {
				JOptionPane.showMessageDialog(null, "Por favor, seleccione usuario");
			}
		} else if (e.getSource() == deleteProduct) {
			ProductService productService = new ProductService();
			List<Product> products = null;
			try {
				products = productService.getByUserId(Integer.toString(PanelManager.selectedUser.getID()));
			} catch (ServiceException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
			} catch (NullPointerException e2) {
				JOptionPane.showMessageDialog(null, "Seleccion usuario", "Lo siento!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (products.size() <= 0) {
				JOptionPane.showMessageDialog(null, "Este usuario no tiene productos para borrar");
			} else {
				PanelManager.changePanel(userHome.screenBuilder("delete product"));
			}
		}
	}

}
