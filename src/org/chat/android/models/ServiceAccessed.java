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
@DatabaseTable(tableName = "services_accessed")
public class ServiceAccessed {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private int service_id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
	private int client_id;
    @DatabaseField
	private String ad_info;
    @DatabaseField
	private Date date;
    @DatabaseField
	private boolean newly_created;					// don't think we need a dirty flag, since there's no update on these

    /**
     * Default Constructor needed by ormlite
     */
    public ServiceAccessed() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param service_id
     * @param visit_id
     * @param client_id
     * @param ad_info
     * @param date
     * @param newly_created
     * 
     */
    public ServiceAccessed(int service_id, int visit_id, int client_id, String ad_info, Date date) {
    	Context myContext = MyApplication.getAppContext();
    	this.id = ModelHelper.generateId(myContext);
    	this.service_id = service_id;
    	this.visit_id = visit_id;
        this.client_id = client_id;
        this.ad_info = ad_info;
        this.date = date;
        this.newly_created = true;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public ServiceAccessed(ServiceAccessed existingServicesAccessedModel) {
    	Context myContext = MyApplication.getAppContext();
    	this.id = ModelHelper.generateId(myContext);
        this.service_id = existingServicesAccessedModel.service_id;
        this.visit_id = existingServicesAccessedModel.visit_id;
        this.client_id = existingServicesAccessedModel.client_id;
        this.ad_info = existingServicesAccessedModel.ad_info;
        this.date = existingServicesAccessedModel.date;
        this.newly_created = true;
    }
    
    public int getId() {
    	return id;
    }

	public int getServiceId() {
		return service_id;
	}
	
	public void setServiceId(int service_id) {
		this.service_id = service_id;
	}
	
	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
	}
	
	public int getClientId() {
		return client_id;
	}
	
	public void setClientId(int client_id) {
		this.client_id = client_id;
	}
	
	public String getAdInfo() {
		return ad_info;
	}
	
	public void setAdInfo(String ad_info) {
		this.ad_info = ad_info;
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