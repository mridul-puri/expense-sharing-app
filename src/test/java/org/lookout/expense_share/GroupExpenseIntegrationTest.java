package org.lookout.expense_share;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ExpenseShareApplication.class)
public class GroupExpenseIntegrationTest {

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
		groupService.deleteAllGroups();
	}
	
	
	@Test
	@DisplayName("Validate splitting an expense equally in a group")
	public void testGroupExpenseWithEqualAmounts() throws InvalidExpenseException, InvalidProportionsException {
		
		//prepare
		
		int userId1 = userService.createUser("Joey", "joey@lookout.com", "1965676893");
		int userId2 = userService.createUser("Chandler", "chandler@lookout.com", "1535632429");
		int userId3 = userService.createUser("Phoebe", "phoebe@lookout.com", "2563788998");
		
		User user1 = UserDAO.getUserById(userId1);
		User user2 = UserDAO.getUserById(userId2);
		User user3 = UserDAO.getUserById(userId3);
		
		List<User> members = new ArrayList<>();
		members.add(user1);
		members.add(user2);
		members.add(user3);
		
		int groupId = groupService.createGroup("Friends", members);
		
		ExpenseMetadata metadata = new ExpenseMetadata("Shopping", "CartItems", new Date(System.currentTimeMillis()), "http://www.newyork.com");

		Map<User, Double> proportions = new HashMap<>();
		proportions.put(user1, null);
		proportions.put(user2, null);
		proportions.put(user3, null);
		
		//act
		
		Pair<Integer, List<List<Object>>> expenseMapping = expenseService.createGroupExpense(600d, user2, user2, metadata, "EQUAL", groupId, proportions);
		int expenseId = expenseMapping.getLeft();
		List<List<Object>> debts = expenseMapping.getRight();
		
		//assert
		
		Expense expense = ExpenseDAO.getExpenseById(expenseId);
		Assert.assertNotNull(expense);
		List<Split> splits = expense.getSplits();
		
		for(int i = 0;i < splits.size();i++) {
			Assert.assertEquals(200d, splits.get(i).getAmount(), 0d);
		}
		
		Assert.assertEquals(4, debts.get(0).get(0)); //assert user who owes money
		Assert.assertEquals(5, debts.get(0).get(1)); //assert user who will recieve money
		Assert.assertEquals(400d, debts.get(0).get(2)); //assert debt amount
		
		Assert.assertEquals(6, debts.get(1).get(0)); //assert user who owes money
		Assert.assertEquals(5, debts.get(1).get(1)); //assert user who will recieve money
		Assert.assertEquals(400d, debts.get(1).get(2)); //assert debt amount
	}
	
	@Test
	@DisplayName("Validate splitting an expense un-equally in a group")
	public void testGroupExpenseWithExactAmounts() throws InvalidExpenseException, InvalidProportionsException {
		
		//prepare
		
		int userId1 = userService.createUser("Joey", "joey@lookout.com", "1965676893");
		int userId2 = userService.createUser("Chandler", "chandler@lookout.com", "1535632429");
		int userId3 = userService.createUser("Phoebe", "phoebe@lookout.com", "2563788998");
		
		User user1 = UserDAO.getUserById(userId1);
		User user2 = UserDAO.getUserById(userId2);
		User user3 = UserDAO.getUserById(userId3);
				
		List<User> members = new ArrayList<>();
		members.add(user1);
		members.add(user2);
		members.add(user3);
		
		int groupId = groupService.createGroup("Friends", members);
				
		ExpenseMetadata metadata = new ExpenseMetadata("Shopping", "CartItems", new Date(System.currentTimeMillis()), "http://www.newyork.com");
		
		Map<User, Double> proportions = new HashMap<>();
		proportions.put(user1, 400d);
		proportions.put(user2, 150d);
		proportions.put(user3, 50d);
		
		List<Double> proportionsDefined = new ArrayList<>();
		proportionsDefined.add(400d);
		proportionsDefined.add(150d);
		proportionsDefined.add(50d);
		
		//act
				
		Pair<Integer, List<List<Object>>> expenseMapping = expenseService.createGroupExpense(600d, user2, user2, metadata, "EXACTAMOUNT", groupId, proportions);
		int expenseId = expenseMapping.getLeft();
		List<List<Object>> debts = expenseMapping.getRight(); 
		
		//assert
				
		Expense expense = ExpenseDAO.getExpenseById(expenseId);
		Assert.assertNotNull(expense);
		
		List<Split> splits = expense.getSplits();
		List<Double> splitAmounts = new ArrayList<>();
		for(Split split : splits) {
			splitAmounts.add(split.getAmount());
		}

		Assert.assertTrue(splitAmounts.containsAll(proportionsDefined));
		
		Assert.assertEquals(10, debts.get(0).get(0)); //assert user who owes money
		Assert.assertEquals(11, debts.get(0).get(1)); //assert user who will recieve money
		Assert.assertEquals(800d, debts.get(0).get(2)); //assert debt amount
		
		Assert.assertEquals(12, debts.get(1).get(0)); //assert user who owes money
		Assert.assertEquals(11, debts.get(1).get(1)); //assert user who will recieve money
		Assert.assertEquals(100d, debts.get(1).get(2)); //assert debt amount
	}
	
	@Test
	@DisplayName("Validate splitting an expense in a group based on percentages")
	public void testGroupExpenseWithPercentages() throws InvalidExpenseException, InvalidProportionsException {
		
		//prepare
		
		int userId1 = userService.createUser("Joey", "joey@lookout.com", "1965676893");
		int userId2 = userService.createUser("Chandler", "chandler@lookout.com", "1535632429");
		int userId3 = userService.createUser("Phoebe", "phoebe@lookout.com", "2563788998");
		
		User user1 = UserDAO.getUserById(userId1);
		User user2 = UserDAO.getUserById(userId2);
		User user3 = UserDAO.getUserById(userId3);
						
		List<User> members = new ArrayList<>();
		members.add(user1);
		members.add(user2);
		members.add(user3);
						
		int groupId = groupService.createGroup("Friends", members);
						
		ExpenseMetadata metadata = new ExpenseMetadata("Shopping", "CartItems", new Date(System.currentTimeMillis()), "http://www.newyork.com");
				
		Map<User, Double> proportions = new HashMap<>();
		proportions.put(user1, 20d);
		proportions.put(user2, 30d);
		proportions.put(user3, 50d);	
		
		List<Double> proportionsDefined = new ArrayList<>();
		proportionsDefined.add(120d);
		proportionsDefined.add(180d);
		proportionsDefined.add(300d);
				
		//act
						
		Pair<Integer, List<List<Object>>> expenseMapping = expenseService.createGroupExpense(600d, user2, user2, metadata, "PERCENTAGE", groupId, proportions);
		int expenseId = expenseMapping.getLeft();
		List<List<Object>> debts = expenseMapping.getRight();

		//assert
						
		Expense expense = ExpenseDAO.getExpenseById(expenseId);
		Assert.assertNotNull(expense);
		
		List<Split> splits = expense.getSplits();
		List<Double> splitAmounts = new ArrayList<>();
		for(Split split : splits) {
			splitAmounts.add(split.getAmount());
		}

		Assert.assertTrue(splitAmounts.containsAll(proportionsDefined));
		
		Assert.assertEquals(3, debts.get(0).get(0)); //assert user who owes money
		Assert.assertEquals(2, debts.get(0).get(1)); //assert user who will recieve money
		Assert.assertEquals(600d, debts.get(0).get(2)); //assert debt amount
		
		Assert.assertEquals(1, debts.get(1).get(0)); //assert user who owes money
		Assert.assertEquals(2, debts.get(1).get(1)); //assert user who will recieve money
		Assert.assertEquals(240d, debts.get(1).get(2)); //assert debt amount
	}

	@Test
	@DisplayName("Validate splitting multiple expenses in a group")
	public void testAddMultipleExpense() throws InvalidExpenseException, InvalidProportionsException {
		
		//prepare
		
		int userId1 = userService.createUser("Joey", "joey@lookout.com", "1965676893");
		int userId2 = userService.createUser("Chandler", "chandler@lookout.com", "1535632429");
		int userId3 = userService.createUser("Phoebe", "phoebe@lookout.com", "2563788998");

		User user1 = UserDAO.getUserById(userId1);
		User user2 = UserDAO.getUserById(userId2);
		User user3 = UserDAO.getUserById(userId3);
		
		List<User> members = new ArrayList<>();
		members.add(user1);
		members.add(user2);
		members.add(user3);
		
		int groupId = groupService.createGroup("Friends", members);
		
		ExpenseMetadata metadata1 = new ExpenseMetadata("Shopping", "CartItems", new Date(System.currentTimeMillis()), "http://www.newyork.com");
		ExpenseMetadata metadata2 = new ExpenseMetadata("Party", "", new Date(System.currentTimeMillis()), "http://www.newyork.com");
		
		Map<User, Double> proportions1 = new HashMap<>();
		proportions1.put(user1, 50d);
		proportions1.put(user2, 25d);
		proportions1.put(user3, 25d);
		
		Map<User, Double> proportions2 = new HashMap<>();
		proportions2.put(user1, 100d);
		proportions2.put(user2, 30d);
		proportions2.put(user3, 70d);
		
		//act

		expenseService.createGroupExpense(100d, user1, user1, metadata1, "PERCENTAGE", groupId, proportions1);
		Pair<Integer, List<List<Object>>> expenseMapping = expenseService.createGroupExpense(200d, user2, user2, metadata2, "EXACTAMOUNT", groupId, proportions2);
		List<List<Object>> debts = expenseMapping.getRight();

		//assert
		
		Assert.assertEquals(8, debts.get(0).get(0)); //assert user who owes money
		Assert.assertEquals(7, debts.get(0).get(1)); //assert user who will recieve money
		Assert.assertEquals(50d, debts.get(0).get(2)); //assert debt amount
		
		Assert.assertEquals(9, debts.get(1).get(0)); //assert user who owes money
		Assert.assertEquals(7, debts.get(1).get(1)); //assert user who will recieve money
		Assert.assertEquals(50d, debts.get(1).get(2)); //assert debt amount
		
		Assert.assertEquals(9, debts.get(2).get(0)); //assert user who owes money
		Assert.assertEquals(8, debts.get(2).get(1)); //assert user who will recieve money
		Assert.assertEquals(190d, debts.get(2).get(2)); //assert debt amount
		
		Assert.assertEquals(7, debts.get(3).get(0)); //assert user who owes money
		Assert.assertEquals(8, debts.get(3).get(1)); //assert user who will recieve money
		Assert.assertEquals(100d, debts.get(3).get(2)); //assert debt amount
		
	}
	
}
