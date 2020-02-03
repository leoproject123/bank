package entity;

public class MoneyTransfer extends TypeOfTransaction {

	private Product destination;

	public Product getDestination() {
		return destination;
	}

	public void setDestination(Product destination) {
		this.destination = destination;
	}

}
