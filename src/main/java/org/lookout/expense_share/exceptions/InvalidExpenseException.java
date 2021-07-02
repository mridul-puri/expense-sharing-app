package org.lookout.expense_share.exceptions;

public class InvalidExpenseException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidExpenseException(String msg) {
		super(msg);
	}
	
}
