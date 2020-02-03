package service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import dao.UserDao;
import dao.implementation.UserDaoImpl;
import entity.Client;
import entity.User;
import exception.InvalidPasswordException;
import exception.DAOException;
import exception.ServiceException;
import exception.InvalidUserException;

public class UserService {

	UserDao usuarioDao = new UserDaoImpl();

	public void create(String userName, String password, String role) throws ServiceException {

		User user = new Client();

		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);

		user.setCreationDate(ts);
		user.setUser(userName);
		user.setPassword(password);
		user.setRole(role);

		try {
			usuarioDao.create(user);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

	public void delete(int id) throws ServiceException {

		try {
			usuarioDao.delete(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public void update(User user) throws ServiceException {

		try {
			usuarioDao.update(user);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public User getById(int id) throws ServiceException {

		try {
			return usuarioDao.getById(id);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public List<User> getAll() throws ServiceException {

		try {
			return usuarioDao.getAll();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public List<User> getClients() throws ServiceException {

		try {
			return usuarioDao.getClients();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

	}

	public User validateCredentials(String userName, String password)
			throws InvalidPasswordException, InvalidUserException, ServiceException {

		User user;
		try {
			user = usuarioDao.getByUser(userName);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		if (user != null) {
			if (user.getPassword().equals(password)) {
				return user;
			} else {
				throw new InvalidPasswordException("error contarsena!");
			}
		} else {
			throw new InvalidUserException("Usuario no existe");
		}

	}

	public boolean checkForRepeated(String userName) throws ServiceException {

		boolean repeated = false;
		List<User> usersFromDatabase = null;

		try {
			usersFromDatabase = getAll();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		for (User user : usersFromDatabase) {
			if (user.getUser().equals(userName)) {
				repeated = true;
			}
		}
		return repeated;
	}
}
