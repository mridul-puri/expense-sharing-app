package org.lookout.expense_share;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lookout.expense_share.dao.ExpenseDAO;
import org.lookout.expense_share.dao.UserDAO;
import org.lookout.expense_share.exceptions.InvalidExpenseException;
import org.lookout.expense_share.exceptions.InvalidProportionsException;
import org.lookout.expense_share.models.User;
import org.lookout.expense_share.models.ExpenseModels.Expense;
import org.lookout.expense_share.models.ExpenseModels.ExpenseMetadata;
import org.lookout.expense_share.models.SplitModels.Split;
import org.lookout.expense_share.services.BalanceService;
import org.lookout.expense_share.services.ExpenseService;
import org.lookout.expense_share.services.GroupService;
import org.lookout.expense_share.services.SimplifyDebtService;
import org.lookout.expense_share.services.UserService;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Assert;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ExpenseShareApplication.class)
public class UserExpenseIntegrationTest {

	private ExpenseService expenseService;
	private BalanceService balanceService;
	private SimplifyDebtService debtService;
	private UserService userService;
	private GroupService groupService;
	
	@BeforeEach
	public void setUp() {
		balanceService = new BalanceService();
		debtService = new SimplifyDebtService();
		groupService = new GroupService();
		expenseService = new ExpenseService(balanceService, debtService, groupService);
		userService = new UserService();
		userService.deleteAllUsers();
	}
	
	@Test
	@DisplayName("Validate splitting an expense equally between 2 users")
	public void testAddExpenseWithEqualAmounts() throws InvalidExpenseException, InvalidProportionsException {
		
		//prepare
		
		int userId1 = userService.createUser("Joey", "joey@lookout.com", "1965676893");
		int userId2 = userService.createUser("Chandler", "chandler@lookout.com", "1535632429");
		
		User user1 = UserDAO.getUserById(userId1);
		User user2 = UserDAO.getUserById(userId2);
		
		ExpenseMetadata metadata = new ExpenseMetadata("Grocery", "vegetables", new Date(System.currentTimeMillis()), "http://www.newyork.com");
		Map<User, Double> proportions = new HashMap<>();
		proportions.put(user1, null);
		proportions.put(user2, null);
		
		//act
		
		int expenseId = expenseService.createUserExpense(1000d, user1, user2, metadata, "EQUAL", 2, proportions);
		
		//assert
		
		Expense expense = ExpenseDAO.getExpenseById(expenseId);
		Assert.assertNotNull(expense);
		
		List<Split> splits = expense.getSplits();
		Assert.assertEquals(500d, splits.get(0).getAmount(), 0d);
		Assert.assertEquals(500d, splits.get(1).getAmount(), 0d);
	
	}
	
	@Test
	@DisplayName("Validate splitting an expense un-equally between 2 users")
	public void testAddExpenseWithExactAmounts() throws InvalidExpenseException, InvalidProportionsException {
		
		//prepare
		
		int userId1 = userService.createUser("Joey", "joey@lookout.com", "1965676893");
		int userId2 = userService.createUser("Chandler", "chandler@lookout.com", "1535632429");
		
		User user1 = UserDAO.getUserById(userId1);
		User user2 = UserDAO.getUserById(userId2);
		
		ExpenseMetadata metadata = new ExpenseMetadata("Grocery", "vegetables", new Date(System.currentTimeMillis()), "http://www.newyork.com");
		Map<User, Double> proportions = new HashMap<>();
		proportions.put(user1, 700d);
		proportions.put(user2, 300d);
		
		List<Double> proportionsDefined = new ArrayList<>();
		proportionsDefined.add(700d);
		proportionsDefined.add(300d);
		
		//act
		
		int expenseId = expenseService.createUserExpense(1000d, user1, user2, metadata, "EXACTAMOUNT", 2, proportions);
				
		//assert
		
		Expense expense = ExpenseDAO.getExpenseById(expenseId);
		Assert.assertNotNull(expense);
		
		List<Split> splits = expense.getSplits();
		List<Double> splitAmounts = new ArrayList<>();
		for(Split split : splits) {
			splitAmounts.add(split.getAmount());
		}

		Assert.assertTrue(splitAmounts.containsAll(proportionsDefined));	
	}
	
	@Test
	@DisplayName("Validate splitting an expense between 2 users based on percentages")
	public void testAddExpenseWithPercentages() throws InvalidExpenseException, InvalidProportionsException {
		
		//prepare
		
		int userId1 = userService.createUser("Joey", "joey@lookout.com", "1965676893");
		int userId2 = userService.createUser("Chandler", "chandler@lookout.com", "1535632429");
		
		User user1 = UserDAO.getUserById(userId1);
		User user2 = UserDAO.getUserById(userId2);
		
		ExpenseMetadata metadata = new ExpenseMetadata("Grocery", "vegetables", new Date(System.currentTimeMillis()), "http://www.newyork.com");
		Map<User, Double> proportions = new HashMap<>();
		proportions.put(user1, 20d);
		proportions.put(user2, 80d);
		
		List<Double> proportionsDefined = new ArrayList<>();
		proportionsDefined.add(200d);
		proportionsDefined.add(800d);
		
		//act
		
		int expenseId = expenseService.createUserExpense(1000d, user1, user2, metadata, "PERCENTAGE", 2, proportions);
						
		//assert
		
		Expense expense = ExpenseDAO.getExpenseById(expenseId);
		Assert.assertNotNull(expense);
		
		List<Split> splits = expense.getSplits();
		List<Double> splitAmounts = new ArrayList<>();
		for(Split split : splits) {
			splitAmounts.add(split.getAmount());
		}

		Assert.assertTrue(splitAmounts.containsAll(proportionsDefined));
	
	}
	
}
