package org.lookout.expense_share.services;

import org.lookout.expense_share.dao.BalanceDAO;
import org.lookout.expense_share.dao.UserDAO;
import org.lookout.expense_share.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	public int createUser(String name, String email, String phone) {
		User user = new User(name, email, phone);
		UserDAO.addUser(user);
        BalanceDAO.createBalanceForUser(user);
        return user.getId();
    }
	
	public User getUserById(int id) {
		return UserDAO.getUserById(id);
	}
	
	public void deleteAllUsers() {
		UserDAO.deleteAllUsers();
	}
	
	//getAllUsers()
	
	//updateUser(id)
	
	//deleteUser(id)
	
}
