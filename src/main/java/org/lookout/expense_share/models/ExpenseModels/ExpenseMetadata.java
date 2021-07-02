package org.lookout.expense_share.models.ExpenseModels;

import java.util.Date;

public class ExpenseMetadata {
	
	//info about an expense
	
	private String name;
	private String notes;
	private Date creationDate;
    private String imgUrl;
    
    public ExpenseMetadata(String name, String notes, Date creationDate, String imgUrl) {
        this.name = name;
        this.notes = notes;
        this.creationDate = creationDate;
        this.imgUrl = imgUrl;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}   
}
