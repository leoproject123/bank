package ui.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import ui.manager.PanelManager;
import ui.user.UserHome;

public class MainScreen extends JPanel {

	private JButton withdrawal;
	private JButton transfer;
	private JButton payment;
	private JButton deposit;
	private JButton statement;

	public JPanel init() {

		this.setLayout(new GridLayout(0, 1));

		withdrawal = new JButton("Extraccion");
		transfer = new JButton("Transferencia");
		payment = new JButton("Pago de cuentas");
		deposit = new JButton("Depositos");
		statement = new JButton("Consultas");

		this.add(withdrawal);
		this.add(transfer);
		this.add(payment);
		this.add(deposit);
		this.add(statement);

		UserHome userHome = new UserHome();

		withdrawal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				PanelManager.changePanel(userHome.screenBuilder("withdrawal"));
			}
		});

		transfer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				PanelManager.changePanel(userHome.screenBuilder("transferencia"));

			}
		});

		payment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				PanelManager.changePanel(userHome.screenBuilder("pago de cuentas"));
			}
		});

		deposit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				PanelManager.changePanel(userHome.screenBuilder("depositos"));
			}
		});

		statement.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				PanelManager.changePanel(userHome.screenBuilder("consultas"));
			}
		});
		return this;
	}
}
