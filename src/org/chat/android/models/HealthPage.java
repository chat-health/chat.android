package org.chat.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "health_pages")
public class HealthPage {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
    @DatabaseField
    private int subtopic_id;
    @DatabaseField
    private int page_number;
    @DatabaseField
    private String type;


    
    /**
     * Default Constructor needed by ormlite
     */
    public HealthPage() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param subtopic_id
     */
    public HealthPage(int id, int subtopic_id, int page_number, String type) {
    	this.id = id;
        this.subtopic_id = subtopic_id;
        this.page_number = page_number;
        this.type = type;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public HealthPage(HealthPage existingHealthPageModel) {
        this.subtopic_id = existingHealthPageModel.subtopic_id;
        this.page_number = existingHealthPageModel.page_number;
        this.type = existingHealthPageModel.type;
    }
    

	public int getId() {
		return id;
	}
	
	public int getSubtopicId() {
		return subtopic_id;
	}
	
	public void setSubtopicId(int subtopic_id) {
		this.subtopic_id = subtopic_id;
	}
	
	public int getPageNumber() {
		return page_number;
	}
	
	public void setPageNumber(int page_number) {
		this.page_number = page_number;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}