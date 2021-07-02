package org.lookout.expense_share.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lookout.expense_share.dao.BalanceDAO;
import org.lookout.expense_share.models.User;
import org.lookout.expense_share.models.ExpenseModels.Expense;
import org.lookout.expense_share.models.SplitModels.Split;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

	public void updateBalances(Expense expense) {
		
		Map<Integer, Map<Integer, Double>> balanceSheet = BalanceDAO.getBalanceSheet();
		User payer = expense.paidBy;
		
		//iterate through the splits and store balance both ways (ie. user1 -> user 2 and user2 -> user1)
		
		for (Split split : expense.getSplits()) {
            int paidTo = split.getUser().getId();
            Map<Integer, Double> balances = balanceSheet.get(payer.getId());
            if (!balances.containsKey(paidTo)) {
                balances.put(paidTo, 0.0);
            }
            balances.put(paidTo, balances.get(paidTo) + split.getAmount());
            BalanceDAO.addToBalanceSheet(payer.getId(), balances);
            
            balances = balanceSheet.get(paidTo);
            if (!balances.containsKey(payer.getId())) {
                balances.put(payer.getId(), 0.0);
            }
            balances.put(payer.getId(), balances.get(payer.getId()) - split.getAmount());
            BalanceDAO.addToBalanceSheet(paidTo, balances);
        }
		
	}
	
	//fetch overall balance for a particular user
	
	public List<List<Integer>> fetchBalance(int userId) {
		List<List<Integer>> balance = new ArrayList<>();
		Map<Integer, Map<Integer, Double>> balanceSheet = BalanceDAO.getBalanceSheet();
        
        for (Map.Entry<Integer, Double> userBalance : balanceSheet.get(userId).entrySet()) {
            if (userBalance.getValue() != 0) {
                addToBalance(userId, userBalance.getKey(), userBalance.getValue(), balance);
            }
        }
        	
        return balance;
	}

    private void addToBalance(int user1, int user2, double amount, List<List<Integer>> balance) {
    	List<Integer> details = new ArrayList<>();
    	if (amount < 0) {
        	details.add(user1);
        	details.add(user2);
        } else if (amount > 0) {
        	details.add(user2);
        	details.add(user1);
        }
    	details.add((int)amount);
    	balance.add(details);
    }
    
}
