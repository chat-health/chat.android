package org.chat.android.models;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "health_selects_recorded")
public class HealthSelectRecorded {
	@DatabaseField(unique = true, index = true, generatedId = true)
	private int id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
    private int select_id;
    @DatabaseField
    private String theme;
    
    /**
     * Default Constructor needed by ormlite
     */
    public HealthSelectRecorded() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param health_page_id
     */
    public HealthSelectRecorded(int visit_id, int select_id, String theme) {
    	this.visit_id = visit_id;
        this.select_id = select_id;
        this.theme = theme;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public HealthSelectRecorded(HealthSelectRecorded existingHealthSelectRecordedModel) {
    	this.visit_id = existingHealthSelectRecordedModel.visit_id;
        this.select_id = existingHealthSelectRecordedModel.select_id;
        this.theme = existingHealthSelectRecordedModel.theme;
    }
    

	public int getId() {
		return id;
	}
	
	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
	}
	
	public void setSelectId(int select_id) {
		this.select_id = select_id;
	}
	
	public int getSelectId() {
		return select_id;
	}
	
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	public String getTheme() {
		return theme;
	}
	
}