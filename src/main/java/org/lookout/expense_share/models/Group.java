package org.lookout.expense_share.models;

import java.util.List;

public class Group {

	//model of a group
	
	private static int inc = 0;
	public int id;
	public String name;
	public List<User> members;
	
	public Group(String name, List<User> members) {
		++inc;
		this.id = inc;
		this.name = name;
		this.members = members;
	}
	
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getMembers() {
		return members;
	}

	public void addMember(User user) {
		members.add(user);
	}
	
}
