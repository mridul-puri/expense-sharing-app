package org.lookout.expense_share.dao;

import java.util.HashMap;
import java.util.Map;

import org.lookout.expense_share.models.ExpenseModels.Expense;
import org.springframework.stereotype.Repository;

@Repository
public class ExpenseDAO {
	
	//maintain a mapping of expense id and expense info
	
	public static Map<Integer, Expense> list_of_expenses = new HashMap<>();
	
	public static void addExpense(Expense expense) {
		list_of_expenses.put(expense.getId(), expense);
	}
	
	public static Expense getExpenseById(int id) {
		return list_of_expenses.get(id);
	}
	
}
