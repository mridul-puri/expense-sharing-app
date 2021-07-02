package org.lookout.expense_share.dao;

import java.util.HashMap;
import java.util.Map;

import org.lookout.expense_share.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class BalanceDAO {
	
	//maintaining a balance sheet of the format <paidBy, <paidTo, amount>>
	
	public static Map<Integer, Map<Integer, Double>> balanceSheet = new HashMap<>();
	
	public static Map<Integer, Map<Integer, Double>> getBalanceSheet() {
		return balanceSheet;
	}
	
	public static void createBalanceForUser(User user) {
		balanceSheet.put(user.getId(), new HashMap<Integer, Double>());
	}
	
	public static void addToBalanceSheet(Integer userId, Map<Integer, Double> balance) {
		balanceSheet.put(userId, balance);
	}
}
