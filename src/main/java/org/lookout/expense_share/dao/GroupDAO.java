package org.lookout.expense_share.dao;

import java.util.HashMap;
import java.util.Map;

import org.lookout.expense_share.models.Group;
import org.springframework.stereotype.Repository;

@Repository
public class GroupDAO {
	
	//maintain a mapping of group id and group info
	
	public static Map<Integer, Group> list_of_groups = new HashMap<>();
	
	public static void addGroup(Group group) {
		list_of_groups.put(group.getId(), group);
	}
	
	public static Group getGroupById(int id) {
		return list_of_groups.get(id);
	}
	
	public static void deleteAllGroups() {
		list_of_groups.clear();
	}
}
