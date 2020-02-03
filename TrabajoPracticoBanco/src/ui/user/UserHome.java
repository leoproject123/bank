package ui.user;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ui.admin.CheckingAccountScreen;
import ui.admin.CreateProductScreen;
import ui.admin.CreateUserScreen;
import ui.admin.CreditCardScreen;
import ui.admin.DeleteProductScreen;
import ui.admin.SavingsAccountScreen;
import ui.admin.UsersTableScreen;
import ui.client.StatementScreen;
import ui.client.DepositScreen;
import ui.client.MainScreen;
import ui.client.PaymentScreen;
import ui.client.TransferScreen;
import ui.client.WithdrawalScreen;

public class UserHome extends JPanel {

	private Title title;
	private MainScreen mainScreen;
	private WithdrawalScreen withdrawalScreen;
	private TransferScreen transferScreen;
	private PaymentScreen paymentScreen;
	private DepositScreen depositScreen;
	private StatementScreen statementScreen;
	private InitialPanel initialPanel;
	private CreateUserScreen createUserScreen;
	private CreateProductScreen createProductScreen;
	private CreditCardScreen creditCardScreen;
	private SavingsAccountScreen savingsAccountScreen;
	private CheckingAccountScreen checkingAccountScreen;
	private DeleteProductScreen deleteProductScreen;
	private JPanel panel;
	private FooterButton footerButton;
	private boolean isFooterActive;
	private UsersTableScreen usersTableScreen;

	public JPanel screenBuilder(String option) {

		this.setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 20, 20, 20));

		footerButton = new FooterButton("MAIN SCREEN");
		isFooterActive = true;

		switch (option) {

		case "home page":
			title = new Title("BIENVENIDO");
			initialPanel = new InitialPanel();
			panel = initialPanel.buildPanelScreen();
			isFooterActive = false;
			break;

		case "main screen":
			title = new Title("SELECCION OPCION:");
			mainScreen = new MainScreen();
			panel = mainScreen.init();
			footerButton = new FooterButton("LOG OUT");
			break;

		case "withdrawal":
			title = new Title("SELECCIONE CUENTA / INTRODUZCA IMPORTE");
			withdrawalScreen = new WithdrawalScreen();
			panel = withdrawalScreen.init();
			break;

		case "transferencia":
			title = new Title("SELECCIONE CUENTAS / INTRODUZCA IMPORTE");
			transferScreen = new TransferScreen();
			panel = transferScreen.init();
			break;

		case "pago de cuentas":
			title = new Title("SELECCIONE SERVICIO A PAGAR / NUMERO DE CUENTA / IMPORTE");
			paymentScreen = new PaymentScreen();
			panel = paymentScreen.init();
			break;

		case "depositos":
			title = new Title("SELECCIONE CUENTA / IMPORTE A DEPOSITAR");
			depositScreen = new DepositScreen();
			panel = depositScreen.init();
			break;

		case "consultas":
			title = new Title("SELECCIONE CUENTA PARA CONSULTAR MOVIMIENTOS");
			statementScreen = new StatementScreen();
			panel = statementScreen;
			break;

		case "admin main panel":
			title = new Title("SELECT CLIENT");
			usersTableScreen = new UsersTableScreen();
			panel = usersTableScreen;
			footerButton = new FooterButton("LOG OUT");
			break;

		case "create user":
			title = new Title("USER CREDENTIALS");
			createUserScreen = new CreateUserScreen();
			panel = createUserScreen.init();
			footerButton = new FooterButton("ADMIN MAIN PANEL");
			break;

		case "product":
			title = new Title("ENTER INFORMATION");
			createProductScreen = new CreateProductScreen();
			panel = createProductScreen.init();
			footerButton = new FooterButton("ADMIN MAIN PANEL");
			break;

		case "admin credit card":
			title = new Title("ENTER INFORMATION");
			creditCardScreen = new CreditCardScreen();
			panel = creditCardScreen.init();
			footerButton = new FooterButton("ADMIN MAIN PANEL");
			break;

		case "admin savings account":
			title = new Title("ENTER INFORMATION");
			savingsAccountScreen = new SavingsAccountScreen();
			panel = savingsAccountScreen.init();
			footerButton = new FooterButton("ADMIN MAIN PANEL");
			break;

		case "admin checking account":
			title = new Title("ENTER INFORMATION");
			checkingAccountScreen = new CheckingAccountScreen();
			panel = checkingAccountScreen.init();
			footerButton = new FooterButton("ADMIN MAIN PANEL");
			break;

		case "delete product":
			title = new Title("ENTER INFORMATION");
			deleteProductScreen = new DeleteProductScreen();
			panel = deleteProductScreen.init();
			footerButton = new FooterButton("ADMIN MAIN PANEL");
		}

		this.add(title, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);

		if (isFooterActive) {
			this.add(footerButton.init(), BorderLayout.SOUTH);
		}

		return this;

	}

}
