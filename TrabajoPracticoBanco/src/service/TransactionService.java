package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.TransactionDao;
import dao.implementation.TransactionDaoImpl;
import entity.CheckingAccount;
import entity.Client;
import entity.CreditCard;
import entity.Deposit;
import entity.MoneyTransfer;
import entity.Payment;
import entity.Product;
import entity.SavingsAccount;
import entity.Transaction;
import entity.TypeOfTransaction;
import entity.Withdrawal;
import exception.DAOException;
import exception.ServiceException;

public class TransactionService {

	TransactionDao md = new TransactionDaoImpl();

	public List<Transaction> getAll(int userId) throws ServiceException {

		try {
			return md.getAll(userId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	public List<Transaction> getAllByAccountNumber(String accountNumber) throws ServiceException {

		try {
			return md.getAllByAccountNumber(accountNumber);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void delete(int transactionId) throws ServiceException {

		try {
			md.delete(transactionId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void update(Transaction transaction) throws ServiceException {

		try {
			md.update(transaction);
		} catch (DAOException e) {
			throw new ServiceException();
		}
	}

	public void create(String accountNumber, String typeOfProduct, float amount, int type, int clientId)
			throws ServiceException {

		TransactionDao td = new TransactionDaoImpl();
		Transaction transaction = new Transaction();

		Client user = new Client();
		user.setID(clientId);
		transaction.setUsuario(user);
		transaction.setAmount(amount);
		TypeOfTransaction typeOfTransaction = null;

		switch (type) {
		case 1:
			typeOfTransaction = new Withdrawal();
			break;
		case 2:
			typeOfTransaction = new MoneyTransfer();
			break;
		case 3:
			typeOfTransaction = new Payment();
			break;
		case 4:
			typeOfTransaction = new Deposit();
			break;
		}

		typeOfTransaction.setID(type);
		transaction.setType(typeOfTransaction);
		Product product = null;

		switch (typeOfProduct) {
		case "SA":
			product = new SavingsAccount();
			break;
		case "CA":
			product = new CheckingAccount();
			break;
		case "CC":
			product = new CreditCard();
			break;
		}

		product.setAccountNumber(accountNumber);
		product.setCode(typeOfProduct);
		transaction.setProduct(product);

		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		transaction.setDate(ts);

		try {
			td.create(transaction);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void exportTransactions(Product product) throws ServiceException {

		TransactionService transactionService = new TransactionService();
		List<Transaction> transactions = new ArrayList<>();
		transactions = transactionService.getAllByAccountNumber(product.getAccountNumber());
		String typeOfAccount = "";

		if (product instanceof SavingsAccount) {
			typeOfAccount = "Savings Account";
		} else if (product instanceof CheckingAccount) {
			typeOfAccount = "Checking Account";
		} else if (product instanceof CreditCard) {
			typeOfAccount = "Credit Card";
		}

		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("/Users/leo/Desktop/statement.txt", false));
			bw.newLine();
			bw.append("Leo's Bank\n\n" + typeOfAccount + "\n\nAccount Number: " + product.getAccountNumber() + "\n\n");
			bw.append("ID\tCreation Date\t\tDescription\tDebit\tCredit\n\n");
			bw.append("-----------------------------------------------------------------\n\n");

			for (Transaction trs : transactions) {

				int id = trs.getID();
				Date creationDate = trs.getDate();
				DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - hh:mm aaa");
				String dateFormatted = formatter.format(creationDate);
				String description = trs.getType().getDescription();
				float amount = trs.getAmount();
				float debit = 0;
				float credit = 0;

				if (amount < 0) {
					debit = amount;
				} else if (amount > 0) {
					credit = amount;
				}

				if (debit != 0) {
					bw.append(id + "\t" + dateFormatted + "\t" + description + " \t" + Math.abs(debit) + "\n");
				} else if (credit != 0) {
					bw.append(id + "\t" + dateFormatted + "\t" + description + " \t\t" + credit + "\n");
				}
				bw.append("\n");
			}

			bw.append("-----------------------------------------------------------------\n\n");
			String balance;
			if (product.getBalance() < 0) {
				balance = "($" + String.format("%.2f", Math.abs(product.getBalance())) + ")";
			} else {
				balance = "$" + String.format("%.2f", product.getBalance());
			}
			bw.append("BALANCE:\t" + balance);

			if (product instanceof CreditCard) {
				bw.append("\tCredit Limit:\t$" + String.format("%.2f", ((CreditCard) product).getCreditLimit()));
			}

			bw.close();
		} catch (IOException e1) {
			throw new ServiceException(e1);
		}
	}

	public void exportTransactionsByType(Product product) throws ServiceException {

		TransactionService transactionService = new TransactionService();
		List<Transaction> transactions = new ArrayList<>();
		transactions = transactionService.getAllByAccountNumber(product.getAccountNumber());
		String typeOfAccount = "";

		if (product instanceof SavingsAccount) {
			typeOfAccount = "Savings Account";
		} else if (product instanceof CheckingAccount) {
			typeOfAccount = "Checking Account";
		} else if (product instanceof CreditCard) {
			typeOfAccount = "Credit Card";
		}

		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("/Users/leo/Desktop/statement.txt", false));
			bw.newLine();
			bw.append("Leo's Bank\n\n" + typeOfAccount + "\n\nAccount Number: " + product.getAccountNumber() + "\n\n");
			bw.append("\n\t\t     --- WITHDRAWALS ---\n\n");
			bw.append("ID\tCreation Date\t\tDescription\tDebit\tCredit\n\n");
			bw.append("-----------------------------------------------------------------\n\n");

			for (Transaction trs : transactions) {

				if (trs.getType() instanceof Withdrawal) {

					int id = trs.getID();
					Date creationDate = trs.getDate();
					DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - hh:mm aaa");
					String dateFormatted = formatter.format(creationDate);
					String description = trs.getType().getDescription();
					float amount = trs.getAmount();
					float debit = 0;
					float credit = 0;

					if (amount < 0) {
						debit = amount;
					} else if (amount > 0) {
						credit = amount;
					}

					if (debit != 0) {
						bw.append(id + "\t" + dateFormatted + "\t" + description + " \t" + Math.abs(debit) + "\n");
					} else if (credit != 0) {
						bw.append(id + "\t" + dateFormatted + "\t" + description + " \t\t" + credit + "\n");
					}
					bw.append("\n");
				}
			}

			bw.append("\n\t\t     --- TRANSFERS ---\n\n");
			bw.append("ID\tCreation Date\t\tDescription\tDebit\tCredit\n\n");
			bw.append("-----------------------------------------------------------------\n\n");

			for (Transaction trs : transactions) {

				if (trs.getType() instanceof MoneyTransfer) {

					int id = trs.getID();
					Date creationDate = trs.getDate();
					DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - hh:mm aaa");
					String dateFormatted = formatter.format(creationDate);
					String description = trs.getType().getDescription();
					float amount = trs.getAmount();
					float debit = 0;
					float credit = 0;

					if (amount < 0) {
						debit = amount;
					} else if (amount > 0) {
						credit = amount;
					}

					if (debit != 0) {
						bw.append(id + "\t" + dateFormatted + "\t" + description + " \t" + Math.abs(debit) + "\n");
					} else if (credit != 0) {
						bw.append(id + "\t" + dateFormatted + "\t" + description + " \t\t" + credit + "\n");
					}
					bw.append("\n");
				}
			}

			bw.append("\n\t\t     --- PAYMENTS ---\n\n");
			bw.append("ID\tCreation Date\t\tDescription\tDebit\tCredit\n\n");
			bw.append("-----------------------------------------------------------------\n\n");

			for (Transaction trs : transactions) {

				if (trs.getType() instanceof Payment) {

					int id = trs.getID();
					Date creationDate = trs.getDate();
					DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - hh:mm aaa");
					String dateFormatted = formatter.format(creationDate);
					String description = trs.getType().getDescription();
					float amount = trs.getAmount();
					float debit = 0;
					float credit = 0;

					if (amount < 0) {
						debit = amount;
					} else if (amount > 0) {
						credit = amount;
					}

					if (debit != 0) {
						bw.append(id + "\t" + dateFormatted + "\t" + description + " \t" + Math.abs(debit) + "\n");
					} else if (credit != 0) {
						bw.append(id + "\t" + dateFormatted + "\t" + description + " \t\t" + credit + "\n");
					}
					bw.append("\n");
				}
			}

			bw.append("\n\t\t     --- DEPOSITS ---\n\n");
			bw.append("ID\tCreation Date\t\tDescription\tDebit\tCredit\n\n");
			bw.append("-----------------------------------------------------------------\n\n");

			for (Transaction trs : transactions) {

				if (trs.getType() instanceof Deposit) {

					int id = trs.getID();
					Date creationDate = trs.getDate();
					DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd - hh:mm aaa");
					String dateFormatted = formatter.format(creationDate);
					String description = trs.getType().getDescription();
					float amount = trs.getAmount();
					float debit = 0;
					float credit = 0;

					if (amount < 0) {
						debit = amount;
					} else if (amount > 0) {
						credit = amount;
					}

					if (debit != 0) {
						bw.append(id + "\t" + dateFormatted + "\t" + description + " \t" + Math.abs(debit) + "\n");
					} else if (credit != 0) {
						bw.append(id + "\t" + dateFormatted + "\t" + description + " \t\t" + credit + "\n");
					}
					bw.append("\n");
				}
			}

			bw.append("-----------------------------------------------------------------\n\n");
			String balance;
			if (product.getBalance() < 0) {
				balance = "($" + String.format("%.2f", Math.abs(product.getBalance())) + ")";
			} else {
				balance = "$" + String.format("%.2f", product.getBalance());
			}
			bw.append("BALANCE:\t" + balance);

			if (product instanceof CreditCard) {
				bw.append("\tCredit Limit:\t$" + String.format("%.2f", ((CreditCard) product).getCreditLimit()));
			}

			bw.close();
		} catch (IOException e1) {
			throw new ServiceException(e1);
		}
	}

}
