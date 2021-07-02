package org.lookout.expense_share.models;

public class User {
	
	//model of a User
	
	private static int inc = 0;
	public int id;
	public String name;
	public String email;
	public String phone;

    public User(String name, String email, String phone) {
    	++inc;
        this.id = inc;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    
}
