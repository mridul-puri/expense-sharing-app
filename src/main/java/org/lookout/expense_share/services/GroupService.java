package org.lookout.expense_share.services;

import java.util.List;

import org.lookout.expense_share.dao.GroupDAO;
import org.lookout.expense_share.models.Group;
import org.lookout.expense_share.models.User;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
	
	public int createGroup(String name, List<User> members) {
		Group group = new Group(name, members);
		GroupDAO.addGroup(group);
        return group.getId();
    }
	
	public Group getGroupById(int id) {
		return GroupDAO.getGroupById(id);
	}
	
	public void deleteAllGroups() {
		GroupDAO.deleteAllGroups();
	}
	
	//getAllGroups()
	
	//updateGroup(id)
	
	//deleteGroup(id)
	
}
