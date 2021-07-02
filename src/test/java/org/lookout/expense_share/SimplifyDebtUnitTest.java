package org.lookout.expense_share;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lookout.expense_share.services.SimplifyDebtService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ExpenseShareApplication.class)
public class SimplifyDebtUnitTest {

	private SimplifyDebtService debtService;
	
	@BeforeEach
	public void setUp() {
		debtService = new SimplifyDebtService();
	}
	
	@Test
	@DisplayName("Validate simplification of multiple debts")
	public void testSimplifyDebt() {
		
		//prepare 
		
		Map<Integer, Map<Integer, Double>> debtGraph = new HashMap<>();
		Map<Integer, Double> debt1 = new HashMap<>();
		Map<Integer, Double> debt2 = new HashMap<>();
		Map<Integer, Double> debt3 = new HashMap<>();
		
		debt1.put(1, 0d);
		debt1.put(2, 1000d);
		debt1.put(3, 2000d);
		
		debt2.put(1, 0d);
		debt2.put(2, 0d);
		debt2.put(3, 5000d);
		
		debt3.put(1, 0d);
		debt3.put(2, 0d);
		debt3.put(3, 0d);
		
		debtGraph.put(1, debt1);
		debtGraph.put(2, debt2);
		debtGraph.put(3, debt3);
        
        //act
        
        List<List<Object>> debt = debtService.simplifyDebt(debtGraph);
        
        //assert
        
        Assert.assertEquals(3, debt.get(0).get(0)); //assert who owes money
        Assert.assertEquals(2, debt.get(0).get(1)); //assert who will recieve money
        Assert.assertEquals(4000d, debt.get(0).get(2)); //assert debt amount
        
        Assert.assertEquals(3, debt.get(1).get(0)); //assert who owes money
        Assert.assertEquals(1, debt.get(1).get(1)); //assert who will recieve money
        Assert.assertEquals(3000d, debt.get(1).get(2)); //assert debt amount
        
	}
}
