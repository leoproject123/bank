package entity;

import java.sql.Timestamp;

public abstract class User {

	private int id;
	private Timestamp creationDate;
	private String user;
	private String password;
	private String role;

	public User() {

	};

	public User(Timestamp creationDate, String user, String password, String role) {
		this.creationDate = creationDate;
		this.user = user;
		this.password = password;
		this.role = role;
	}

	public int getID() {
		return id;
	}

	public void setID(int iD) {
		id = iD;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String toString() {
		return "[ID=" + id + ", creationDate=" + creationDate + ", user=" + user + ", password=" + password + ", role="
				+ role + "]";
	}
}
