package org.lookout.expense_share.models.SplitModels;

import org.lookout.expense_share.models.User;

public class ExactAmountSplit extends Split {
	
    public ExactAmountSplit(User user, double amount) {
        super(user, amount);
    }
    
}
