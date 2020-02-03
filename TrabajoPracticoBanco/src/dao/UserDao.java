package dao;

import java.util.List;

import entity.User;
import exception.DAOException;

public interface UserDao {

	void create(User usuario) throws DAOException;

	void delete(int ID) throws DAOException;

	void update(User usuario) throws DAOException;

	User getById(int ID) throws DAOException;

	List<User> getAll() throws DAOException;

	List<User> getClients() throws DAOException;

	User getByUser(String user) throws DAOException;

}