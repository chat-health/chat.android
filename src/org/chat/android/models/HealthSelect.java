package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "health_selects")
public class HealthSelect {
	@DatabaseField(id = true)
	private int id;
    @DatabaseField
    private int subject_id;				// refers to theme or topic, 1-99 for theme, 100+ for topic, 1000 for page_assessment? (CHECKME)
    @DatabaseField
    private String en_content;
    @DatabaseField
    private String zu_content;
    @DatabaseField
	private Date created_at;
	@DatabaseField
	private Date modified_at;

    
    /**
     * Default Constructor needed by ormlite
     */
    public HealthSelect() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param subject_id
     * @param en_content
     * @param zu_content
     * @param created_at
     * @param modified_at
     */
    public HealthSelect(int id, int subject_id, String en_content, String zu_content, Date created_at, Date modified_at) {
    	this.id = id;
    	this.subject_id = subject_id;
        this.en_content = en_content;
        this.zu_content = zu_content;
        this.created_at = created_at;
    	this.modified_at = modified_at;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
//    public HealthSelect(HealthSelect existingHealthSelectModel) {
//    	this.id = existingHealthSelectModel.id;
//    	this.subject_id = existingHealthSelectModel.subject_id;
//        this.en_content = existingHealthSelectModel.en_content;
//        this.zu_content = existingHealthSelectModel.zu_content;
//    }
    

	public int getId() {
		return id;
	}
	
	public int getSubjectId() {
		return subject_id;
	}
	
	public void setSubjectId(int subject_id) {
		this.subject_id = subject_id;
	}
	
	public String getEnContent() {
		return en_content;
	}
	
	public void setEnContent(String en_content) {
		this.en_content = en_content;
	}
	
	public void setZuContent(String zu_content) {
		this.zu_content = zu_content;
	}
	
	public String getZuContent() {
		return zu_content;
	}
	
	public String getContent(String lang) {
		if (lang.equals("zu")) {
			return zu_content;
		} else {
			return en_content;
		}
	}
	
}