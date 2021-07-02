package org.lookout.expense_share.models.ExpenseModels;

import org.lookout.expense_share.models.User;
import org.lookout.expense_share.models.SplitModels.Split;
import org.lookout.expense_share.models.SplitModels.ExactAmountSplit;

public class ExactAmountExpense extends Expense {
	
	public ExactAmountExpense(double amount, User addedBy, User paidBy, ExpenseMetadata metadata) {
        super(amount, addedBy, paidBy, metadata);
    }

    @Override
    public boolean validate() {
        for (Split split : this.splits) {
            if (!(split instanceof ExactAmountSplit)) {
                return false;
            }
        }
        
        double totalAmount = getAmount();
        double sumSplitAmount = 0;
        
        for (Split split : this.splits) {
            ExactAmountSplit exactSplit = (ExactAmountSplit) split;
            sumSplitAmount += exactSplit.getAmount();
        }

        if (totalAmount != sumSplitAmount) {
            return false;
        }
        
        return true;
    }
}
