package org.chat.android.models;

import java.util.Date;

import org.chat.android.ModelHelper;
import org.chat.android.MyApplication;

import android.content.Context;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "vaccines_recorded")
public class VaccineRecorded {
	@DatabaseField(id = true)
	private int id;
	@DatabaseField
	private int vaccine_id;
    @DatabaseField
    private int client_id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
	private Date date;
    @DatabaseField
	private Boolean newly_created;


    /**
     * Default Constructor needed by ormlite
     */
    public VaccineRecorded() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param vaccine_id
     * @param client_id
     * @param visit_id
     * @param date
     * @param newly_created
     * 
     */
    public VaccineRecorded(int vaccine_id, int client_id, int visit_id, Date date) {
    	Context myContext = MyApplication.getAppContext();
    	this.id = ModelHelper.generateId(myContext);
    	this.vaccine_id = vaccine_id;
    	this.client_id = client_id;
    	this.visit_id = visit_id;
        this.date = date;
        this.newly_created = true;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public VaccineRecorded(VaccineRecorded existingServicesAccessedModel) {
    	Context myContext = MyApplication.getAppContext();
    	this.id = ModelHelper.generateId(myContext);
    	this.vaccine_id = existingServicesAccessedModel.vaccine_id;
        this.client_id = existingServicesAccessedModel.client_id;
        this.visit_id = existingServicesAccessedModel.visit_id;
        this.date = existingServicesAccessedModel.date;
        this.newly_created = true;
    }

    public int getId() {
    	return id;
    }
    
	public int getVaccineId() {
		return vaccine_id;
	}
	
    public void setVaccineId(int vaccine_id) {
		this.vaccine_id = vaccine_id;
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
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Boolean getNewlyCreatedStatus() {
		return newly_created;
	}
	
	public void setNewlyCreatedStatus() {
		this.newly_created = true;
	}
	
	public void makeClean() {
		this.newly_created = false;
	}
}