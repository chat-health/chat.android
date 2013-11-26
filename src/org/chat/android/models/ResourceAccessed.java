package org.chat.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "resources_accessed")
public class ResourceAccessed {
    @DatabaseField
    private int resource_id;
    @DatabaseField
    private int visit_id;


    /**
     * Default Constructor needed by ormlite
     */
    public ResourceAccessed() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param resource_id
     * @param visit_id
     * 
     */
    public ResourceAccessed(int resource_id, int visit_id) {
    	this.resource_id = resource_id;
    	this.visit_id = visit_id;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public ResourceAccessed(ResourceAccessed existingResourcesAccessedModel) {
        this.resource_id = existingResourcesAccessedModel.resource_id;
        this.visit_id = existingResourcesAccessedModel.visit_id;
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
}