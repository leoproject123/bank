package dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.TransactionDao;
import database.DBManager;
import entity.CheckingAccount;
import entity.Client;
import entity.CreditCard;
import entity.Deposit;
import entity.MoneyTransfer;
import entity.Payment;
import entity.SavingsAccount;
import entity.Transaction;
import entity.TypeOfTransaction;
import entity.User;
import entity.Withdrawal;
import exception.DAOException;

public class TransactionDaoImpl extends DBManager implements TransactionDao {

	@Override
	public void create(Transaction transaction) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "INSERT INTO TRANSACTION (DATE, AMOUNT, TYPE, PRODUCT, USER, ACCOUNT_NUMBER) VALUES(?,?,?,?,?,?)";

		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);

			pstmt.setTimestamp(1, transaction.getDate());
			pstmt.setFloat(2, transaction.getAmount());
			pstmt.setInt(3, transaction.getType().getID());
			pstmt.setString(4, transaction.getProduct().getCode());
			pstmt.setInt(5, transaction.getUsuario().getID());
			pstmt.setString(6, transaction.getProduct().getAccountNumber());

			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error generating transaction" + e);
			} catch (SQLException x) {
				x.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(Transaction transaction) throws DAOException {

		Connection con = DBManager.connect();

		String sql = "UPDATE TRANSACTION SET AMOUNT= ?, TYPE= ?, PRODUCT= ?, ACCOUNT_NUMBER= ? WHERE ID= ?";

		try {
			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setDouble(1, transaction.getAmount());
			stmt.setInt(2, transaction.getType().getID());
			stmt.setString(3, transaction.getProduct().getCode());
			stmt.setString(4, transaction.getProduct().getAccountNumber());
			stmt.setInt(5, transaction.getID());

			stmt.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error updating transaction" + e);
			} catch (SQLException x) {
				x.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Transaction> getAll(int userId) throws DAOException {

		String sql = "SELECT * FROM TRANSACTION WHERE USER = ?";
		Connection con = DBManager.connect();

		List<Transaction> result = null;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			result = new ArrayList<>();

			while (rs.next()) {

				Transaction mov = new Transaction();
				mov.setAmount(rs.getFloat(3));

				TypeOfTransaction tp = null;

				switch (rs.getInt(4)) {
				case 1:
					tp = new Withdrawal();
					break;
				case 2:
					tp = new MoneyTransfer();
					break;
				case 3:
					tp = new Payment();
					break;
				case 4:
					tp = new Deposit();
					break;
				}

				tp.setID(rs.getInt(4));
				mov.setType(tp);
				mov.setID(rs.getInt(1));
				mov.setDate(rs.getTimestamp(2));

				String prodId = rs.getString(5);

				switch (prodId) {
				case "SA":
					mov.setProduct(new SavingsAccount());
					break;
				case "CA":
					mov.setProduct(new CheckingAccount());
					break;
				case "CC":
					mov.setProduct(new CreditCard());
					break;
				}
				mov.getProduct().setCode(prodId);
				mov.getProduct().setAccountNumber(rs.getString(7));
				result.add(mov);
				User user = new Client();
				mov.setUsuario(user);
				mov.getUsuario().setID(userId);
				;

			}

		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error retrieving all transactions" + e);
			} catch (SQLException x) {
				x.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public List<Transaction> getAllByAccountNumber(String accountNumber) throws DAOException {

		String sql = "SELECT * FROM TRANSACTION WHERE ACCOUNT_NUMBER = ?";
		Connection con = DBManager.connect();

		List<Transaction> result = null;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, accountNumber);
			ResultSet rs = ps.executeQuery();
			result = new ArrayList<>();

			while (rs.next()) {

				Transaction transaction = new Transaction();
				transaction.setAmount(rs.getFloat(3));

				TypeOfTransaction tp = null;
				switch (rs.getInt(4)) {
				case 1:
					tp = new Withdrawal();
					break;
				case 2:
					tp = new MoneyTransfer();
					break;
				case 3:
					tp = new Payment();
					break;
				case 4:
					tp = new Deposit();
					break;
				}

				tp.setID(rs.getInt(4));
				transaction.setType(tp);
				transaction.setID(rs.getInt(1));
				transaction.setDate(rs.getTimestamp(2));
				String prodId = rs.getString(5);

				switch (prodId) {
				case "SA":
					transaction.setProduct(new SavingsAccount());
					break;
				case "CA":
					transaction.setProduct(new CheckingAccount());
					break;
				case "CC":
					transaction.setProduct(new CreditCard());
					break;
				}
				transaction.getProduct().setCode(prodId);
				transaction.getProduct().setAccountNumber(rs.getString(7));

				User usuario = new Client();
				transaction.setUsuario(usuario);
				transaction.getUsuario().setID(rs.getInt(6));

				result.add(transaction);

			}
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error retrieving all transactions from a user " + e);
			} catch (SQLException x) {
				x.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public void delete(int transactionId) throws DAOException {

		Connection con = DBManager.connect();
		String sql = "DELETE FROM TRANSACTION WHERE ID = ?";

		try {

			PreparedStatement stmt = con.prepareStatement(sql);

			stmt.setInt(1, transactionId);
			stmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error deleting transaction " + e);
			} catch (SQLException x) {
				x.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
