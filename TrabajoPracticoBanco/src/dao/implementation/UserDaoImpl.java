package dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.UserDao;
import database.DBManager;
import entity.Admin;
import entity.Client;
import entity.User;
import exception.DAOException;

public class UserDaoImpl extends DBManager implements UserDao {

	@Override
	public void create(User user) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "INSERT INTO USER (DATE, USER_NAME, PASSWORD, ROLE) VALUES(?,?,?,?)";
		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);

			pstmt.setTimestamp(1, user.getCreationDate());
			pstmt.setString(2, user.getUser());
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, user.getRole());

			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error creating user " + e);
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
	public void delete(int id) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "DELETE FROM USER WHERE ID = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);
			pstmt.setInt(1, id);

			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error deleting user " + e);
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
	public void update(User user) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "UPDATE USER SET USER_NAME = ?, PASSWORD = ?, ROLE = ? WHERE ID = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);

			pstmt.setString(1, user.getUser());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			pstmt.setInt(4, user.getID());

			pstmt.executeUpdate();

			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
				throw new DAOException("Error updating user information " + e);
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
	public User getById(int id) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "SELECT * FROM USER WHERE ID = ?";

		User user = null;

		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);
			pstmt.setInt(1, id);

			ResultSet result = pstmt.executeQuery();
			user = null;

			if (result.next()) {
				if (result.getString(5).equalsIgnoreCase("Cliente")) {
					user = new Client(result.getTimestamp(2), result.getString(3), result.getString(4),
							result.getString(5));
				} else {
					user = new Admin(result.getTimestamp(2), result.getString(3), result.getString(4),
							result.getString(5));
				}
				user.setID(id);
			}

			con.commit();
		} catch (SQLException e) {
			throw new DAOException("Error retrieving user by ID " + e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	@Override
	public List<User> getAll() throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "SELECT * FROM USER";
		List<User> resultList = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);
			ResultSet result = pstmt.executeQuery();
			resultList = new ArrayList<>();

			User user = null;

			while (result.next()) {
				if (result.getString(5).equalsIgnoreCase("Cliente")) {
					user = new Client(result.getTimestamp(2), result.getString(3), result.getString(4),
							result.getString(5));
				} else {
					user = new Admin(result.getTimestamp(2), result.getString(3), result.getString(4),
							result.getString(5));
				}
				user.setID(result.getInt(1));
				resultList.add(user);
			}

			con.commit();
		} catch (SQLException e) {
			throw new DAOException("Error retrieving all users " + e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultList;
	}

	@Override
	public List<User> getClients() throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "SELECT * FROM USER";
		List<User> resultList = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);

			ResultSet result = pstmt.executeQuery();
			resultList = new ArrayList<>();

			User user = null;

			while (result.next()) {
				if (result.getString(5).equalsIgnoreCase("Client")) {
					user = new Client(result.getTimestamp(2), result.getString(3), result.getString(4),
							result.getString(5));
					user.setID(result.getInt(1));
					resultList.add(user);
				}

			}
			con.commit();
		} catch (SQLException e) {
			throw new DAOException("Error retrieving all clients " + e);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultList;
	}

	public User getByUser(String userName) throws DAOException {

		Connection con = DBManager.connect();

		String psql2 = "SELECT * FROM USER WHERE USER_NAME = ?";

		User user = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(psql2);
			pstmt.setString(1, userName);

			ResultSet result = pstmt.executeQuery();

			if (result.next()) {
				if (result.getString(5).equalsIgnoreCase("Client")) {
					user = new Client(result.getTimestamp(2), result.getString(3), result.getString(4),
							result.getString(5));
				} else {
					user = new Admin(result.getTimestamp(2), result.getString(3), result.getString(4),
							result.getString(5));
				}
				user.setID(result.getInt(1));
			}

		} catch (SQLException e) {
			throw new DAOException("Error retrieving user " + e);

		}
		return user;
	}

}
