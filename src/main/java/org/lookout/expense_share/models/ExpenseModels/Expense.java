package org.lookout.expense_share.models.ExpenseModels;

import java.util.ArrayList;
import java.util.List;

import org.lookout.expense_share.models.User;
import org.lookout.expense_share.models.SplitModels.Split;

public abstract class Expense {

	//model of an expense
	
	private static int inc = 0;
	public int id;
	public double amount;
	public User addedBy;
	public User paidBy;
	public ExpenseMetadata metadata;
	public List<Split> splits;
	
	public Expense(double amount, User addedBy, User paidBy, ExpenseMetadata metadata) {
		++inc;
		this.id = inc;
		this.amount = amount;
		this.addedBy = addedBy;
		this.paidBy = paidBy;
		this.metadata = metadata;
		this.splits = new ArrayList<>();
	}
	
	public int getId() {
		return id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public User getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(User addedBy) {
		this.addedBy = addedBy;
	}

	public User getPaidBy() {
		return paidBy;
	}

	public void setPaidBy(User paidBy) {
		this.paidBy = paidBy;
	}

	public ExpenseMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(ExpenseMetadata metadata) {
		this.metadata = metadata;
	}

	public void addSplit(Split split) {
		this.splits.add(split);
	}

	public List<Split> getSplits() {
		return splits;
	}

	public abstract boolean validate();
    
}
