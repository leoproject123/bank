package dao;

import java.util.List;

import entity.Product;
import exception.DAOException;

public interface ProductDao {

	void create(Product producto) throws DAOException;

	public void createCreditCard(Product tarjetaDeCredito) throws DAOException;

	Product getByAccountNumber(String accountNumber) throws DAOException;

	void delete(String accountNumber) throws DAOException;

	void update(Product producto) throws DAOException;

	List<Product> getByUserId(String ID) throws DAOException;

	void deleteByUserId(int clientId) throws DAOException;

}