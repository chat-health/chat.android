package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "resources")
public class Resource {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField()
    private String uri;
    @DatabaseField
	private Date created_at;
	@DatabaseField
	private Date modified_at;


    /**
     * Default Constructor needed by ormlite
     */
    public Resource() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param name
     * @param uri
     * @param created_at
     * @param modified_at
     * 
     */
    public Resource(int id, String name, String uri, Date created_at, Date modified_at) {
    	this.id = id;
    	this.name = name;
    	this.uri = uri;
    	this.created_at = created_at;
    	this.modified_at = modified_at;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
//    public Resource(Resource existingResourcesAccessedModel) {
//        this.id = existingResourcesAccessedModel.id;
//        this.name = existingResourcesAccessedModel.name;
//        this.uri = existingResourcesAccessedModel.uri;
//    }

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getURI() {
		return uri;
	}
	
	public void setURI(String uri) {
		this.uri = uri;
	}	
}