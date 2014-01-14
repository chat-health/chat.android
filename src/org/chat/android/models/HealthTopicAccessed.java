package org.chat.android.models;

import java.util.Date;

import android.text.format.Time;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "health_topic_accessed")
public class HealthTopicAccessed {
	@DatabaseField(generatedId = true)
	private int id;
    @DatabaseField
    private int topic_id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
    private int hh_id;
    @DatabaseField
    private String topic_name;

    /**
     * Default Constructor needed by ormlite
     */
    public HealthTopicAccessed() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param topic_id
     * @param visit_id
     * @param hh_id
     * @param topic_name
     * 
     */
    public HealthTopicAccessed(int topic_id, int visit_id, int hh_id, String topic_name) {
    	this.topic_id = topic_id;
    	this.visit_id = visit_id;
    	this.hh_id = hh_id;
    	this.topic_name = topic_name;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public HealthTopicAccessed(HealthTopicAccessed existingServicesAccessedModel) {
        this.topic_id = existingServicesAccessedModel.topic_id;
        this.visit_id = existingServicesAccessedModel.visit_id;
        this.hh_id = existingServicesAccessedModel.hh_id;
        this.topic_name = existingServicesAccessedModel.topic_name;
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
	
	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
	}
	
	public int getHouseholdId() {
		return hh_id;
	}
	
	public void setHouseholdId(int hh_id) {
		this.hh_id = hh_id;
	}
	
	public String getTopicName() {
		return topic_name;
	}
	
	public void setTopicName(String topic_name) {
		this.topic_name = topic_name;
	}
	
}