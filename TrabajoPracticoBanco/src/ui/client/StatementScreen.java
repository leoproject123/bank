package ui.client;
//

//import java.awt.FlowLayout;
//import java.util.List;
//
//import javax.swing.BoxLayout;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//
//import basico.Producto;
//import exception.ServiceException;
//import servicio.MovimientoService;
//import servicio.ProductoService;
//import ui.UsuarioTableModel;
//import ui.manager.PanelManager;
//
//public class ConsultasScreen extends JPanel {
//	
//	private JTable tablaUsuarios;
//	private UsuarioTableModel modelo;
//	private JComboBox cuentaSelect;
//	private JButton consultar;
//	private JScrollPane scrollPaneParaTabla;
//	private JButton resumen;
//	private JLabel saldo;
//	private JTextField saldoCampo;
//	List<Producto> productos = null;
//	
//	
//	public JPanel init() {
//		
//		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		
//		ProductoService productoService = new ProductoService();
//
//		try {
//			productos = productoService.getByUserId(Integer.toString(PanelManager.loggedUser.getID()));			
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//		
//		String labels[] = new String[productos.size()];
//		labels = productoService.getAccountLabelsByUser(productos);
//		
//		modelo = new UsuarioTableModel();
//		tablaUsuarios = new JTable(modelo);
//		scrollPaneParaTabla = new JScrollPane(tablaUsuarios); 
//		cuentaSelect = new JComboBox(labels);
//
//		this.add(cuentaSelect);
//		this.add(scrollPaneParaTabla);
//		
//		JPanel saldos = new JPanel();
//		saldos.setLayout(new BoxLayout(saldos, BoxLayout.X_AXIS));
//		
//		saldo = new JLabel("Saldo: ");
//		saldoCampo= new JTextField ("");
//
//		saldos.add(saldo);
//		saldos.add(saldoCampo);
//		
//		this.add(saldos);
//		
//		consultar = new JButton("Consultar");
//		resumen = new JButton("Resumen");
//		
//		JPanel botones = new JPanel();
//		botones.setLayout(new FlowLayout());
//		botones.add(consultar);
//		botones.add(resumen);
//
//		this.add(botones);
//		
//		return this;
//	}
//}
//
//

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import entity.Product;
import entity.Transaction;
import exception.ServiceException;
import service.ProductService;
import service.TransactionService;
import ui.manager.PanelManager;
import ui.user.UserHome;

public class StatementScreen extends JPanel implements ActionListener {

	private JTable transactionTable;
	private UserTableModel model;
	private JButton submit;
	private JButton exportStatement;
	private JScrollPane tableScrollPane;
	private UserHome userHome;
	private JComboBox accountSelect;
	List<Product> products = null;

	public StatementScreen() {
		super();
		buildPanel();
	}

	private void buildPanel() {

		userHome = new UserHome();
		model = new UserTableModel();
		transactionTable = new JTable(model);
		transactionTable.getColumnModel().getColumn(0).setPreferredWidth(15);
		transactionTable.setPreferredScrollableViewportSize(new Dimension(452, 325));
		transactionTable.setFillsViewportHeight(true);

		tableScrollPane = new JScrollPane(transactionTable);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		ProductService productService = new ProductService();

		try {
			products = productService.getByUserId(Integer.toString(PanelManager.loggedUser.getID()));
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		String labels[] = new String[products.size()];
		try {
			labels = productService.getAccountLabelsByUser(products, "--- selecciona cuenta ---");
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
		}

		accountSelect = new JComboBox(labels);

		this.add(accountSelect);
		this.add(tableScrollPane);

		submit = new JButton("Consultar");
		submit.addActionListener(this);
		exportStatement = new JButton("Resumen");
		exportStatement.addActionListener(this);

		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(submit);
		buttons.add(exportStatement);

		this.add(buttons);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (accountSelect.getSelectedItem() == "--- selecciona cuenta ---") {
			JOptionPane.showMessageDialog(null, "Choose an account");
			return;
		}

		Product product = products.stream().filter(
				prod -> prod.getAccountNumber().equals(((String) accountSelect.getSelectedItem()).substring(0, 8)))
				.findAny().orElse(null);

		TransactionService transactionService = new TransactionService();

		if (e.getSource() == submit) {

			List<Transaction> users = new ArrayList<>();

			try {
				users = transactionService.getAllByAccountNumber(product.getAccountNumber());
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}

			model.setContenido(users);
			model.fireTableDataChanged();

		} else if (e.getSource() == exportStatement) {

			String[] options = { "Ordenado por tipo de transaccion", "Ordenado por fecha" };
			int option = JOptionPane.showOptionDialog(null, "Seleccione tipo de resumen", "Statement",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

			if (option == JOptionPane.YES_OPTION) {

				try {
					transactionService.exportTransactionsByType(product);
				} catch (ServiceException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				}
			} else {

				try {
					transactionService.exportTransactions(product);
				} catch (ServiceException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Lo siento!", JOptionPane.ERROR_MESSAGE);
				}
			}

			JOptionPane.showMessageDialog(null, "El resumen esta en su escritorio");
		}

	}
}
