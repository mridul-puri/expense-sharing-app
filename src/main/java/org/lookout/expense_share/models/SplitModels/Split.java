package org.lookout.expense_share.models.SplitModels;

import org.lookout.expense_share.models.User;

public abstract class Split {
	
	//model for splitting an expense
	
	public User user;
	public double amount;

    public Split(User user, double amount) {
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
