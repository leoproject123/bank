package dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ProductDao;
import database.DBManager;
import entity.CheckingAccount;
import entity.CreditCard;
import entity.Product;
import entity.SavingsAccount;
import exception.DAOException;

public class ProductDaoImpl implements ProductDao {

	public void create(Product product) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "INSERT INTO PRODUCT (NUMBER, TYPE, CREATION_DATE, CLIENT_ID, BALANCE) VALUES(?,?,?,?,?)";

		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);

			pstmt.setString(1, product.getAccountNumber());
			pstmt.setString(2, product.getCode());
			pstmt.setTimestamp(3, product.getCreationDate());
			pstmt.setInt(4, product.getClientId());
			pstmt.setFloat(5, product.getBalance());

			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error creating product " + e);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void createCreditCard(Product product) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "INSERT INTO PRODUCT (NUMBER, TYPE, CREATION_DATE, CLIENT_ID, BALANCE, CREDIT_LIMIT) VALUES(?,?,?,?,?,?)";

		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);

			pstmt.setString(1, product.getAccountNumber());
			pstmt.setString(2, product.getCode());
			pstmt.setTimestamp(3, product.getCreationDate());
			pstmt.setInt(4, product.getClientId());
			pstmt.setFloat(5, product.getBalance());
			pstmt.setFloat(6, ((CreditCard) product).getCreditLimit());

			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				e.printStackTrace();
				throw new DAOException("Error creating credit card " + e);
			} catch (SQLException e1) {
				e1.printStackTrace();
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
	public Product getByAccountNumber(String accountNumber) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "SELECT * FROM PRODUCT WHERE NUMBER = ?";

		Product producto = null;

		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);

			pstmt.setString(1, accountNumber);

			ResultSet result = pstmt.executeQuery();
			producto = null;

			if (result.next()) {

				String code = result.getString(2);

				switch (code) {
				case "SA":
					producto = new SavingsAccount();
					producto.setCode("SA");
					break;

				case "CA":
					producto = new CheckingAccount();
					producto.setCode("CA");
					break;

				case "CC":
					producto = new CreditCard();
					producto.setCode("CC");
					break;
				}

				producto.setAccountNumber(result.getString(1));
				producto.setCreationDate(result.getTimestamp(3));
				producto.setClientId(result.getInt(4));
				producto.setBalance(result.getFloat(5));

			}

			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error retrieving products by account number " + e);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return producto;
	}

	@Override
	public void delete(String accountNumber) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "DELETE FROM PRODUCT WHERE NUMBER = ?";

		try {
			PreparedStatement pstm = con.prepareStatement(psql2);
			pstm.setString(1, accountNumber);
			pstm.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error deleting product " + e);
			} catch (SQLException e1) {
				e1.printStackTrace();
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
	public void deleteByUserId(int clientId) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "DELETE FROM PRODUCT WHERE client_id = ?";

		try {
			PreparedStatement pstm = con.prepareStatement(psql2);
			pstm.setInt(1, clientId);
			pstm.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error deleting products " + e);
			} catch (SQLException e1) {
				e1.printStackTrace();
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
	public void update(Product producto) throws DAOException {

		Connection con = DBManager.connect();

		String stmt = "UPDATE PRODUCT SET TYPE = ?, CLIENT_ID = ? , BALANCE = ? WHERE NUMBER = ?";

		try {
			PreparedStatement pstm = con.prepareStatement(stmt);

			pstm.setString(1, producto.getCode());
			pstm.setInt(2, producto.getClientId());
			pstm.setFloat(3, producto.getBalance());
			pstm.setString(4, producto.getAccountNumber());

			pstm.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error updating product " + e);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Product> getByUserId(String ID) throws DAOException {

		ArrayList<Product> productList = new ArrayList<Product>(); // check if it's working

		Connection con = DBManager.connect();

		String psql2 = "SELECT * FROM PRODUCT WHERE CLIENT_ID = ?";

		Product product = null;

		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);

			pstmt.setString(1, ID);

			ResultSet result = pstmt.executeQuery();
			product = null;

			while (result.next()) {

				String codigo = result.getString(2);

				switch (codigo) {
				case "SA":
					product = new SavingsAccount();
					product.setCode("SA");
					break;

				case "CA":
					product = new CheckingAccount();
					product.setCode("CA");
					break;

				case "CC":
					product = new CreditCard();
					product.setCode("CC");
					((CreditCard) product).setCreditLimit(result.getFloat(6));
					break;
				}

				product.setAccountNumber(result.getString(1));
				product.setCreationDate(result.getTimestamp(3));
				product.setClientId(result.getInt(4));
				product.setBalance(result.getFloat(5));

				productList.add(product);
			}
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error retrieving product by ID " + e);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return productList;
	}
}
