package org.lookout.expense_share.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lookout.expense_share.models.User;
import org.lookout.expense_share.models.ExpenseModels.ExpenseTypes;
import org.lookout.expense_share.models.SplitModels.EqualSplit;
import org.lookout.expense_share.models.SplitModels.ExactAmountSplit;
import org.lookout.expense_share.models.SplitModels.PercentageSplit;
import org.lookout.expense_share.models.SplitModels.Split;

public class SplitFactory {

	//create split objects using Factory pattern
	
	public static List<Split> createSplitObjects(String type, double totalAmount, int noOfUsers, Map<User, Double> proportions) {
		List<Split> splits = new ArrayList<>();
		
		if(type.equalsIgnoreCase(ExpenseTypes.EQUAL.name())) {
			for(User user : proportions.keySet()) {
				splits.add(new EqualSplit(user, totalAmount/noOfUsers));
			}
		}
		else if(type.equalsIgnoreCase(ExpenseTypes.EXACTAMOUNT.name())) {
			for(Map.Entry<User, Double> proportion : proportions.entrySet()) {
				splits.add(new ExactAmountSplit(proportion.getKey(), proportion.getValue()));
			}
		}
		else if(type.equalsIgnoreCase(ExpenseTypes.PERCENTAGE.name())) {
			for(Map.Entry<User, Double> proportion : proportions.entrySet()) {
				splits.add(new PercentageSplit(proportion.getKey(), (proportion.getValue()/100)*totalAmount, proportion.getValue()));
			}
		}
		
		return splits;
	}

}
