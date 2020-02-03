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

import service.ProductService;
import service.UserService;
import ui.manager.PanelManager;
import ui.user.UserHome;

public class CreateProductScreen extends JPanel {

	private JLabel type;
	private JComboBox typeSelect;
	private JButton submitButton;

	public JPanel init() {

		this.setLayout(new BorderLayout());
		JPanel secondary = new JPanel();
		secondary.setLayout(new GridLayout(12, 0));

		UserHome userHome = new UserHome();
		String[] labels = { "--- seleccione tipo ---", "cuenta corriente", "caja de ahorro", "tarjeta de credito" };

		type = new JLabel("Tipo:");
		typeSelect = new JComboBox(labels);

		submitButton = new JButton("SIGUIENTE");
		submitButton.setFont(new Font("Arial", Font.BOLD, submitButton.getFont().getSize()));

		secondary.add(type);
		secondary.add(typeSelect);
		secondary.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

		this.add(secondary, BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.SOUTH);

		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (typeSelect.getSelectedItem() == "--- seleccione tipo ---") {
					JOptionPane.showMessageDialog(null, "Seleccione tipo de cuenta");
					return;
				}

				if ((String) typeSelect.getSelectedItem() == "tarjeta de credito") {

					PanelManager.changePanel(userHome.screenBuilder("admin credit card"));

				} else if ((String) typeSelect.getSelectedItem() == "caja de ahorro") {

					PanelManager.changePanel(userHome.screenBuilder("admin savings account"));

				} else if ((String) typeSelect.getSelectedItem() == "cuenta corriente") {

					PanelManager.changePanel(userHome.screenBuilder("admin checking account"));

				}
			}
		});

		return this;
	}
}
