package org.chat.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "health_pages")
public class HealthPage {
	@DatabaseField(generatedId = true)
	private int id;
    @DatabaseField
    private int topic_id;
    @DatabaseField
    private int page_number;
    @DatabaseField
    private String type;
    @DatabaseField
    private int	page_content_id;

    
    /**
     * Default Constructor needed by ormlite
     */
    public HealthPage() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param topic_id
     */
    public HealthPage(int topic_id, int page_number, String type, int page_content_id) {
        this.topic_id = topic_id;
        this.page_number = page_number;
        this.type = type;
        this.page_content_id = page_content_id;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public HealthPage(HealthPage existingHealthPageModel) {
        this.topic_id = existingHealthPageModel.topic_id;
        this.page_number = existingHealthPageModel.page_number;
        this.type = existingHealthPageModel.type;
        this.page_content_id = existingHealthPageModel.page_content_id;
    }
    

	public int getId() {
		return id;
	}
	
	public int getTopicId() {
		return topic_id;
	}
	
	public void setTopicId(int topic_id) {
		this.topic_id = topic_id;
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
	
	public int getPageContentId() {
		return page_content_id;
	}
	
	public void setPageContentId(int page_content_id) {
		this.page_content_id = page_content_id;
	}
}