package org.chat.android.models;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "symptoms")
public class Symptom {
	@DatabaseField(index = true)
	private int id;
    @DatabaseField
	private String type;
    @DatabaseField
	private String en_content;
    @DatabaseField
	private String zu_content;


    /**
     * Default Constructor needed by ormlite
     */
    public Symptom() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param type
     * @param en_content
     * @param zu_content
     * 
     */
    public Symptom(int id, String type, String en_content, String zu_content) {
    	this.id = id;
    	this.type = type;
        this.en_content = en_content;
        this.zu_content = zu_content;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Symptom(Symptom existingServicesAccessedModel) {
    	this.id = existingServicesAccessedModel.id;
        this.type = existingServicesAccessedModel.type;
        this.en_content = existingServicesAccessedModel.en_content;
        this.zu_content = existingServicesAccessedModel.zu_content;
    }

	public int getId() {
		return id;
	}
	
    public void setId(int id) {
		this.id = id;
	}
    
    public String getType() {
		return type;
	}
	
    public void setType(String type) {
		this.type = type;
	}
	
	public String getEnContent() {
		return en_content;
	}
	
	public void setEnContent(String en_content) {
		this.en_content = en_content;
	}
	
	public String getZuContent() {
		return zu_content;
	}
	
	public void setZuContent(String zu_content) {
		this.zu_content = zu_content;
	}
	
	public String getContent(String lang) {
		if (lang == "en") {
			return en_content;
		} else if (lang == "zu") {
			return zu_content;
		} else {
			Log.e("Unknown language: ", lang);
			return null;
		}
	}
}