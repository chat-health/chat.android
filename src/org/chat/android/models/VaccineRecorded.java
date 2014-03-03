package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "vaccines_recorded")
public class VaccineRecorded {
	@DatabaseField
	private int vaccine_id;
    @DatabaseField
    private int client_id;
    @DatabaseField
    private int visit_id;
    @DatabaseField(dataType = DataType.DATE_STRING)
	private Date date;


    /**
     * Default Constructor needed by ormlite
     */
    public VaccineRecorded() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param vaccine_id
     * @param client_id
     * @param visit_id
     * @param date
     * 
     */
    public VaccineRecorded(int vaccine_id, int client_id, int visit_id, Date date) {
    	this.vaccine_id = vaccine_id;
    	this.client_id = client_id;
    	this.visit_id = visit_id;
        this.date = date;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public VaccineRecorded(VaccineRecorded existingServicesAccessedModel) {
    	this.vaccine_id = existingServicesAccessedModel.vaccine_id;
        this.client_id = existingServicesAccessedModel.client_id;
        this.visit_id = existingServicesAccessedModel.visit_id;
        this.date = existingServicesAccessedModel.date;
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
}