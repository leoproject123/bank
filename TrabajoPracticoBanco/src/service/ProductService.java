package service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

import dao.ProductDao;
import dao.implementation.ProductDaoImpl;
import entity.CheckingAccount;
import entity.CreditCard;
import entity.Product;
import entity.SavingsAccount;
import exception.DAOException;
import exception.ServiceException;

public class ProductService {

	ProductDao productoDao = new ProductDaoImpl();

	public void create(String accountNumber, String type, int clientId, float balance) throws ServiceException {

		Product product = null;

		switch (type) {
		case "SA":
			product = new SavingsAccount();
			product.setCode("SA");
			break;

		case "CA":
			product = new CheckingAccount();
			product.setCode("CA");
			break;

		}

		product.setAccountNumber(accountNumber);
		product.setClientId(clientId);
		product.setBalance(balance);

		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);

		product.setCreationDate(ts);

		try {
			productoDao.create(product);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void createCreditCard(String accountNumber, int clientId, float limit) throws ServiceException {

		Product creditCard = new CreditCard();

		creditCard.setCode("CC");
		creditCard.setAccountNumber(accountNumber);
		creditCard.setClientId(clientId);
		creditCard.setBalance(0);

		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);

		creditCard.setCreationDate(ts);
		((CreditCard) creditCard).setCreditLimit(limit);

		try {
			productoDao.createCreditCard(creditCard);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	public Product getByAccountNumber(String accountNumber) throws ServiceException {

		try {
			return productoDao.getByAccountNumber(accountNumber);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public void delete(String accountNumber) throws ServiceException {

		try {
			productoDao.delete(accountNumber);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public void deleteByUserId(int userId) throws ServiceException {

		try {
			productoDao.deleteByUserId(userId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public void update(Product product) throws ServiceException {

		try {
			productoDao.update(product);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public List<Product> getByUserId(String id) throws ServiceException {

		try {
			return productoDao.getByUserId(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public String[] getAccountLabelsByUser(List<Product> products, String message) throws ServiceException {

		String labels[] = new String[(products.size() + 1)];
		labels[0] = message;
		for (int i = 0; i < products.size(); i++) {
			Product item = products.get(i);
			if (item instanceof CreditCard && item.getBalance() <= 0) {
				labels[i + 1] = item.getAccountNumber() + " " + item.getCode() + " ($"
						+ String.format("%.2f", Math.abs(item.getBalance())) + ")";

			} else if (item instanceof CreditCard && item.getBalance() > 0) {
				labels[i + 1] = item.getAccountNumber() + " " + item.getCode() + " $"
						+ String.format("%.2f", item.getBalance()) + " -> user's favor";

			} else {
				labels[i + 1] = item.getAccountNumber() + " " + item.getCode() + " $"
						+ String.format("%.2f", item.getBalance());
			}
		}
		return labels;
	}

	public String generateAccountNumber() throws ServiceException {

		String accountNumber;

		int randomNumber = new Random().nextInt(99999999);
		String randomString = String.valueOf(randomNumber);

		while (randomString.length() < 8) {
			randomString = "0" + randomString;
		}

		try {
			while (productoDao.getByAccountNumber(randomString) != null) {
				randomNumber = new Random().nextInt(99999999);
				randomString = String.valueOf(randomNumber);

				while (randomString.length() < 8) {
					randomString = "0" + randomString;
				}
			}
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

		accountNumber = randomString;

		return accountNumber;
	}

	public float getDebitBalance(Product product, float amount) throws ServiceException {

		float currentBalance = product.getBalance();

		float balance = currentBalance - amount;

		return balance;
	}

	public float getCreditBalance(Product product, float amount) throws ServiceException {

		float currentBalance = product.getBalance();

		float balance = currentBalance + amount;

		return balance;
	}

	public boolean areEnoughFunds(Product product, float amount, float balance) throws ServiceException {
		boolean enoughFunds = false;
		float currentBalance = product.getBalance();

		if (product instanceof CreditCard) {
			float creditLimit = ((CreditCard) product).getCreditLimit();
			float fundsAvailable = creditLimit + currentBalance - amount;
			if (fundsAvailable >= 0) {
				enoughFunds = true;
			}
		} else if (!(product instanceof CreditCard) && balance >= 0) {
			enoughFunds = true;
		}

		return enoughFunds;
	}

}
