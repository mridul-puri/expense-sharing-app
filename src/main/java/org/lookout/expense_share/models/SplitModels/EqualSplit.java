package org.lookout.expense_share.models.SplitModels;

import org.lookout.expense_share.models.User;

public class EqualSplit extends Split {
	
	public EqualSplit(User user, double amount) {
        super(user, amount);
    }
	
}
