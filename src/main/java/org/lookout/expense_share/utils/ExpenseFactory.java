package org.lookout.expense_share.utils;

import org.lookout.expense_share.models.User;
import org.lookout.expense_share.models.ExpenseModels.EqualExpense;
import org.lookout.expense_share.models.ExpenseModels.ExactAmountExpense;
import org.lookout.expense_share.models.ExpenseModels.Expense;
import org.lookout.expense_share.models.ExpenseModels.ExpenseMetadata;
import org.lookout.expense_share.models.ExpenseModels.ExpenseTypes;
import org.lookout.expense_share.models.ExpenseModels.PercentageExpense;

public class ExpenseFactory {
	
	//create Expense objects using Factory pattern
	
	public static Expense createExpenseObject(double amount, User addedBy, User paidBy, ExpenseMetadata metadata, String type) {
		if(type.equalsIgnoreCase(ExpenseTypes.EQUAL.name())) {
			return new EqualExpense(amount, addedBy, paidBy, metadata);
		}
		else if(type.equalsIgnoreCase(ExpenseTypes.EXACTAMOUNT.name())) {
			return new ExactAmountExpense(amount, addedBy, paidBy, metadata);
		}
		else if(type.equalsIgnoreCase(ExpenseTypes.PERCENTAGE.name())){
			return new PercentageExpense(amount, addedBy, paidBy, metadata);
		}
		return null;
	}

}
