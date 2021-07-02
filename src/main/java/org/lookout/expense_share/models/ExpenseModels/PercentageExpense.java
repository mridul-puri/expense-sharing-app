package org.lookout.expense_share.models.ExpenseModels;

import org.lookout.expense_share.models.User;
import org.lookout.expense_share.models.SplitModels.Split;
import org.lookout.expense_share.models.SplitModels.PercentageSplit;

public class PercentageExpense extends Expense {

	public PercentageExpense(double amount, User addedBy, User paidBy, ExpenseMetadata metadata) {
        super(amount, addedBy, paidBy, metadata);
    }

    @Override
    public boolean validate() {
        for (Split split : this.splits) {
            if (!(split instanceof PercentageSplit)) {
                return false;
            }
        }

        double totalPercent = 100;
        double sumSplitPercent = 0;
        for (Split split : this.splits) {

        	PercentageSplit exactSplit = (PercentageSplit) split;

            sumSplitPercent += exactSplit.getPercent();

        }

        if (totalPercent != sumSplitPercent) {

            return false;
        }

        return true;
    }
}
