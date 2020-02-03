package dao;

import java.util.List;

import entity.Transaction;
import exception.DAOException;

public interface TransactionDao {

	void create(Transaction mov) throws DAOException;

	void update(Transaction mov) throws DAOException;

	List<Transaction> getAll(int idUsuario) throws DAOException;

	void delete(int movId) throws DAOException;

	List<Transaction> getAllByAccountNumber(String accountNumber) throws DAOException;

}