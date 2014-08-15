package org.chat.android.models;

import java.util.Date;

import org.chat.android.DatabaseHelper;
import org.chat.android.ModelHelper;
import org.chat.android.MyApplication;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "cha_accessed")
public class CHAAccessed {
	@DatabaseField(id = true)
	private int id;
    @DatabaseField
    private int client_id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
    private String type;							// will be either health or immunization
    @DatabaseField
    private Date start_time;
    @DatabaseField
    private Date end_time;

    /**
     * Default Constructor needed by ormlite
     */
    public CHAAccessed() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param client_id
     * @param visit_id
     * @param type
     * @param start_time
     * 
     */
    public CHAAccessed(int client_id, int visit_id, String type, Date start_time, DatabaseHelper dbHelper) {
    	Context myContext = MyApplication.getAppContext();
        Visit v = ModelHelper.getVisitForId(dbHelper, visit_id);
        ModelHelper.setVisitToDirtyAndSave(dbHelper, v);
        this.id = ModelHelper.generateId(myContext);
        this.client_id = client_id;
    	this.visit_id = visit_id;
    	this.visit_id = visit_id;
    	this.type = type;
    	this.start_time = start_time;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public CHAAccessed(CHAAccessed existingServicesAccessedModel) {
        this.client_id = existingServicesAccessedModel.client_id;
        this.visit_id = existingServicesAccessedModel.visit_id;
        this.type = existingServicesAccessedModel.type;
        this.start_time = existingServicesAccessedModel.start_time;
        this.end_time = existingServicesAccessedModel.end_time;
    }

	public int getClientId() {
		return client_id;
	}
	
	public void setClientId(int client_id) {
		this.client_id = client_id;
	}	
	
	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Date getStartTime() {
		return start_time;
	}

	public void setStartTime(Date start_time) {
		this.start_time = start_time;
	}
	
	public Date getEndTime() {
		return end_time;
	}

	public void setEndTime(Date end_time, DatabaseHelper databaseHelper) {
		this.end_time = end_time;
//		Context myContext = MyApplication.getAppContext();
        Visit v = ModelHelper.getVisitForId(databaseHelper, this.visit_id);
        ModelHelper.setVisitToDirtyAndSave(databaseHelper, v);
	}
	
}