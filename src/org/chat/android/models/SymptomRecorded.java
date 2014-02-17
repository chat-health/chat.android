package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "symptom_recorded")
public class SymptomRecorded {
	@DatabaseField
	private int symptom_id;
    @DatabaseField
    private int client_id;
    @DatabaseField
    private int visit_id;

    /**
     * Default Constructor needed by ormlite
     */
    public SymptomRecorded() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param symptom_id
     * @param client_id
     * @param visit_id
     * 
     */
    public SymptomRecorded(int symptom_id, int client_id, int visit_id) {
    	this.symptom_id = symptom_id;
    	this.client_id = client_id;
    	this.visit_id = visit_id;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public SymptomRecorded(SymptomRecorded existingServicesAccessedModel) {
    	this.symptom_id = existingServicesAccessedModel.symptom_id;
        this.client_id = existingServicesAccessedModel.client_id;
        this.visit_id = existingServicesAccessedModel.visit_id;
    }

	public int getVaccineId() {
		return symptom_id;
	}
	
    public void setVaccineId(int symptom_id) {
		this.symptom_id = symptom_id;
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
}