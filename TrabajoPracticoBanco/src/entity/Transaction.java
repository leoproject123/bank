package entity;

import java.sql.Timestamp;

public class Transaction {

	private int ID;
	private Timestamp date;
	private float amount;
	private TypeOfTransaction type;
	private Product product;
	private User user;

	public User getUsuario() {
		return user;
	}

	public void setUsuario(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public TypeOfTransaction getType() {
		return type;
	}

	public void setType(TypeOfTransaction type) {
		this.type = type;
	}

	public String toString() {
		return "[ID=" + ID + ", date=" + date + ", amount=" + amount + ", type=" + type.getID() + ", product="
				+ product.getCode() + ", usuario=" + user.getID() + ", nro cuenta=" + product.getAccountNumber() + "]";
	}

}
