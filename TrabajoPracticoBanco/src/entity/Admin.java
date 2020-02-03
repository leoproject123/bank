package entity;

import java.sql.Timestamp;

public class Admin extends User {

	public Admin() {
		super();
	}

	public Admin(Timestamp creationDate, String user, String password, String role) {
		super(creationDate, user, password, role);
	}

}
