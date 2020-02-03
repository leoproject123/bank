package entity;

public abstract class TypeOfTransaction {

	private int id;
	private String description;

	public int getID() {
		return id;
	}

	public void setID(int iD) {
		id = iD;
	}

	public String getDescription() {

		switch (id) {
		case 1:
			description = "Withdrawal";
			break;
		case 2:
			description = "Transfer";
			break;
		case 3:
			description = "Payment";
			break;
		case 4:
			description = "Deposit";
			break;
		}

		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
