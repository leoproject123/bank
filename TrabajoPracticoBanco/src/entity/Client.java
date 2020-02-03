package entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Client extends User {

	private List<Transaction> transactions;
	private List<Product> products;

	public Client() {
		super();
	};

	public Client(Timestamp creationDate, String user, String password, String role) {
		super(creationDate, user, password, role);
		this.transactions = new ArrayList<>();
		this.products = new ArrayList<>();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProductos(List<Product> products) {
		this.products = products;
	}

}
