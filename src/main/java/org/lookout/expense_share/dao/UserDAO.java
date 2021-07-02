package org.lookout.expense_share.dao;

import java.util.HashMap;
import java.util.Map;

import org.lookout.expense_share.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
	
	//maintain a mapping of user id and user info
	
	public static Map<Integer, User> list_of_users = new HashMap<>();
	
	public static void addUser(User user) {
		list_of_users.put(user.getId(), user);
	}
	
	public static User getUserById(int id) {
		return list_of_users.get(id);
	}
	
	public static void deleteAllUsers() {
		list_of_users.clear();
	}
}
