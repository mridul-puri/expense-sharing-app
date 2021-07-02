package org.lookout.expense_share.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SimplifyDebtService {

	private List<List<Object>> simplifiedDebts = new ArrayList<>();
	
    public List<List<Object>> simplifyDebt(Map<Integer, Map<Integer, Double>> graph) {
    	
    	//create a map of user id and net amount to be paid to that user [credit(user) - debit(user)]
    	
        Map<Integer, Double> amountToBePaid = new HashMap<>();

        for(Map.Entry<Integer, Map<Integer, Double>> entry : graph.entrySet()) {
        	Map<Integer, Double> paidToList = entry.getValue();
        	for(Map.Entry<Integer, Double> paidTo : paidToList.entrySet()) {
        		if(paidTo.getKey() != entry.getKey()) {
        			double netAmt = paidTo.getValue() - graph.get(paidTo.getKey()).get(entry.getKey());
        			amountToBePaid.put(entry.getKey(), amountToBePaid.getOrDefault(entry.getKey(), 0d) + netAmt);
        		}
        	}
        }

        resolveDebts(amountToBePaid);

        return simplifiedDebts;
    }
    
    private void resolveDebts(Map<Integer, Double> amountToBePaid) {
    	
    	//find tha maximum and minimum amounts to be given to any user
    	
        int maxCredit = getUserWithMaxAmount(amountToBePaid);
        int maxDebit = getUserWithMinAmount(amountToBePaid);

        //if both amounts are 0, it means all amounts are settled

        if (amountToBePaid.get(maxCredit) == 0 && amountToBePaid.get(maxDebit) == 0)
            return;
     
        //find minimum of the two amounts and then update amounts to be paid
        
        double min = findMin(-1 * amountToBePaid.get(maxDebit), amountToBePaid.get(maxCredit));
        amountToBePaid.put(maxCredit, amountToBePaid.get(maxCredit) - min);
        amountToBePaid.put(maxDebit, amountToBePaid.get(maxDebit) + min);
     
        //add the simplified debt to the resultant list (User with max debit pays "min" amount to user with max credit)

        List<Object> debt = new ArrayList<>();
        debt.add(maxDebit);
        debt.add(maxCredit);
        debt.add(min);

        simplifiedDebts.add(debt);
        
        //make a recursive call to see if any more debts can be simplified. Loop will terminate when both max credit and max debit become 0
        
        resolveDebts(amountToBePaid);
    }
    
    private int getUserWithMaxAmount(Map<Integer, Double> amountToBePaid) {
    	int userIdWithMaxAmt = 1;
        double maxAmt = Double.NEGATIVE_INFINITY;
        for (Map.Entry<Integer, Double> entry : amountToBePaid.entrySet()) {
        	if (entry.getValue() > maxAmt) {
        		maxAmt = entry.getValue();
        		userIdWithMaxAmt = entry.getKey();
        	}
                
        }
         
        return userIdWithMaxAmt;
    }
     
	private int getUserWithMinAmount(Map<Integer, Double> amountToBePaid)
    {
		int userIdWithMinAmt = 1;
        double minAmt = Double.POSITIVE_INFINITY;
        for (Map.Entry<Integer, Double> entry : amountToBePaid.entrySet()) {
            if (entry.getValue() < minAmt) {
            	minAmt = entry.getValue();
            	userIdWithMinAmt = entry.getKey();
            }
        }

        return userIdWithMinAmt;
    }
     
    private double findMin(double x, double y) {
        return (x < y) ? x: y;
    }
     
}
