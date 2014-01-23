package org.chat.android.models;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "health_themes")
public class HealthTheme {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String en_observe_content;
    @DatabaseField
    private String en_record_content;
    @DatabaseField
    private String zu_observe_content;
    @DatabaseField
    private String zu_record_content;
    @DatabaseField
    private String color;   

    
    /**
     * Default Constructor needed by ormlite
     */
    public HealthTheme() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param health_page_id
     */
    public HealthTheme(int id, String name, String en_observe_content, String en_record_content, String zu_observe_content, String zu_record_content, String color) {
    	this.id = id;
    	this.name = name;
        this.en_observe_content = en_observe_content;
        this.en_record_content = en_record_content;
        this.zu_observe_content = zu_observe_content;
        this.zu_record_content = zu_record_content;
        this.color = color;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public HealthTheme(HealthTheme existingHealthThemeModel) {
    	this.name = existingHealthThemeModel.name;
        this.en_observe_content = existingHealthThemeModel.en_observe_content;
        this.en_record_content = existingHealthThemeModel.en_record_content;
        this.zu_record_content = existingHealthThemeModel.zu_record_content;
        this.zu_record_content = existingHealthThemeModel.zu_record_content;
        this.color = existingHealthThemeModel.color;
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
	
	public String getEnObserveContent() {
		return en_observe_content;
	}
	
	public void setEnObserveContent(String en_observe_content) {
		this.en_observe_content = en_observe_content;
	}
	
	public String getEnRecordContent() {
		return en_record_content;
	}
	
	public void setEnRecordContent(String en_record_content) {
		this.en_record_content = en_record_content;
	}
	
	public void setZuObserveContent(String zu_observe_content) {
		this.zu_observe_content = zu_observe_content;
	}
	
	public String getZuObserveContent() {
		return zu_observe_content;
	}
	
	public void setZuRecordContent(String zu_record_content) {
		this.zu_record_content = zu_record_content;
	}

	public String getZuRecordContent() {
		return zu_record_content;
	}
	
	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}
	
}