package org.lookout.expense_share.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.lookout.expense_share.dao.BalanceDAO;
import org.lookout.expense_share.dao.ExpenseDAO;
import org.lookout.expense_share.exceptions.InvalidExpenseException;
import org.lookout.expense_share.exceptions.InvalidProportionsException;
import org.lookout.expense_share.models.Group;
import org.lookout.expense_share.models.User;
import org.lookout.expense_share.models.ExpenseModels.Expense;
import org.lookout.expense_share.models.ExpenseModels.ExpenseMetadata;
import org.lookout.expense_share.models.SplitModels.Split;
import org.lookout.expense_share.utils.ExpenseFactory;
import org.lookout.expense_share.utils.SplitFactory;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
	
	BalanceService balanceService;
	SimplifyDebtService debtService;
	GroupService groupService;
	
	public ExpenseService(BalanceService balanceService, SimplifyDebtService debtService, GroupService groupService) {
		this.balanceService = balanceService;
		this.debtService = debtService;
		this.groupService = groupService;
	}
	
	public int createUserExpense(double amount, User addedBy, User paidBy, ExpenseMetadata metadata, 
			String type, int noOfUsers, Map<User, Double> proportions) throws InvalidExpenseException, InvalidProportionsException {
		
		//create an expense object
		
		Expense expense = ExpenseFactory.createExpenseObject(amount, addedBy, paidBy, metadata, type);

		if(expense == null)
			throw new InvalidExpenseException("The format of expense division is invalid");

		//create split object for each user in the expense
		
		List<Split> splits = SplitFactory.createSplitObjects(type, amount, noOfUsers, proportions);
		
		for(Split split : splits) {
			expense.addSplit(split);
		}
		
		if(!expense.validate())
			throw new InvalidProportionsException("Split proportions invalid");
		
		ExpenseDAO.addExpense(expense);
		
		//updating balance for expense in the balance sheet

		balanceService.updateBalances(expense);

		return expense.getId();
	}
	
	public Pair<Integer, List<List<Object>>> createGroupExpense(double amount, User addedBy, User paidBy, ExpenseMetadata metadata, 
			String type, int groupId, Map<User, Double> proportions) throws InvalidExpenseException, InvalidProportionsException {
		
		//create an expense object using Factory pattern
		
		Expense expense = ExpenseFactory.createExpenseObject(amount, addedBy, paidBy, metadata, type);

		if(expense == null)
			throw new InvalidExpenseException("The format of expense division is invalid");

		Group group = groupService.getGroupById(groupId);

		//create split object for each user in the expense
		
		List<Split> splits = SplitFactory.createSplitObjects(type, amount, group.getMembers().size(), proportions);
		
		for(Split split : splits) {
			expense.addSplit(split);
		}
			
		if(!expense.validate())
			throw new InvalidProportionsException("Split proportions invalid");
		ExpenseDAO.addExpense(expense);
		
		//updating balance for expense in the balance sheet
		
		balanceService.updateBalances(expense);
		
		Map<Integer, Map<Integer, Double>> graph = formGraph(group);

		return Pair.of(expense.getId(), debtService.simplifyDebt(graph));

	}
	
	private Map<Integer, Map<Integer, Double>> formGraph(Group group) {
		List<Integer> memberIds = new ArrayList<>();
		List<User> members = group.getMembers();
		for(int i = 0;i < members.size();i++) {
			memberIds.add(members.get(i).getId());
		}

		Map<Integer, Map<Integer, Double>> balanceSheetForGroup = new HashMap<>();
		for(Map.Entry<Integer, Map<Integer, Double>> entry : BalanceDAO.getBalanceSheet().entrySet()) {
			if(memberIds.contains(entry.getKey()))
					balanceSheetForGroup.put(entry.getKey(), entry.getValue());	
		}
		
		return balanceSheetForGroup;
	}
	
	public Expense getExpenseById(int id) {
		return ExpenseDAO.getExpenseById(id);
	}
	
}
