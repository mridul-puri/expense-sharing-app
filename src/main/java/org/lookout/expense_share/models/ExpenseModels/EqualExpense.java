package org.lookout.expense_share.models.ExpenseModels;

import org.lookout.expense_share.models.User;
import org.lookout.expense_share.models.SplitModels.Split;
import org.lookout.expense_share.models.SplitModels.EqualSplit;

public class EqualExpense extends Expense {
	
	public EqualExpense(double amount, User addedBy, User paidBy, ExpenseMetadata metadata) {
        super(amount, addedBy, paidBy, metadata);
    }

    @Override
    public boolean validate() {
        for (Split split : this.splits) {
            if (!(split instanceof EqualSplit)) {
                return false;
            }
        }

        return true;
    }
}
