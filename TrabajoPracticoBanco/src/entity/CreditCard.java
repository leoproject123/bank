package entity;

public class CreditCard extends Product {

	private float creditLimit;

	public float getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(float limit) {
		this.creditLimit = limit;
	}

	@Override
	public float getInterestRate() {
		return 0.3f;
	}

}
