Expense Sharing Application
---------------------------------------------------------------------------------------------------

This is a Maven project written in Java using Spring Boot framework
---------------------------------------------------------------------------------------------------

Artifact name : expense-share-app-1.0.zip
----------------------------------------------------------------------------------------------------

This is a Test Driven Application. 

Test Classes : 

1) GroupExpenseIntegrationTest : 4 IT test cases to validate the functionality around groups
2) UserExpenseIntegrationTest : 3 IT test cases to validate the one to one functionality
3) SimplifyDebtUnitTest : 1 Unit Test to validate the debt simplification algorithm


For executing test cases : 

	As a pre-requisite, please install maven in your system (https://maven.apache.org/install.html). 
	Then,
	- Go inside the folder "expense-share-codebase"
	- give the command "mvn test" in the commandline (There are 8 tests in total)
-------------------------------------------------------------------------------------------------------------------------------------------------------
Data Layer : Created an in-memory DAO around Expenses, Groups and Users. Also kept a "balance sheet" map for storing debt of every user with every user
-------------------------------------------------------------------------------------------------------------------------------------------------------

Functional Requirements :

1) System should support creating a user
2) System should support creating a group with multiple users
3) User should be able to add expense with other users
4) User should be able to add expense in a group
5) An expense can be split in 3 ways : 
	. Equally among users
	. By percentage/ratio among users
	. By supplying an exact amount for each user
6) System should support simplifaction of expenses between users
7) System should support simplification of expenses in a group
----------------------------------------------------------------------------------------------------