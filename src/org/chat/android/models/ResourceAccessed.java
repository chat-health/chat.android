package org.chat.android.models;

import java.util.Date;

import org.chat.android.ModelHelper;
import org.chat.android.MyApplication;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "resources_accessed")
public class ResourceAccessed {
	@DatabaseField(generatedId = true)
	private int id;
    @DatabaseField
    private int resource_id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
    private int worker_id;
    @DatabaseField
    private Date date;
    @DatabaseField
    private Boolean newly_created;


    /**
     * Default Constructor needed by ormlite
     */
    public ResourceAccessed() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param resource_id
     * @param visit_id
     * @param worker_id
     * @param time
     * 
     */
    public ResourceAccessed(int resource_id, int visit_id, int worker_id, Date date) {
    	Context myContext = MyApplication.getAppContext();
    	this.id = ModelHelper.generateId(myContext);    	
    	this.resource_id = resource_id;
    	this.worker_id = worker_id;
    	this.visit_id = visit_id;
    	this.date = date;
    	this.newly_created = true;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public ResourceAccessed(ResourceAccessed existingResourcesAccessedModel) {
    	Context myContext = MyApplication.getAppContext();
    	this.id = ModelHelper.generateId(myContext);  
        this.resource_id = existingResourcesAccessedModel.resource_id;
        this.visit_id = existingResourcesAccessedModel.visit_id;
        this.worker_id = existingResourcesAccessedModel.worker_id;
        this.date = existingResourcesAccessedModel.date;
        this.newly_created = true;
    }
    
    public int getId() {
    	return id;
    }

	public int getResourceId() {
		return resource_id;
	}
	
	public void setResourceId(int resource_id) {
		this.resource_id = resource_id;
	}
	
	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
	}
	
	public int getWorkerId() {
		return worker_id;
	}
	
	public void setWorkerId(int worker_id) {
		this.worker_id = worker_id;
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