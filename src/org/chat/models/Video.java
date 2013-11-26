package org.chat.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "videos")
public class Video {
    @DatabaseField
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String uri;


    /**
     * Default Constructor needed by ormlite
     */
    public Video() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param name
     * @param uri
     * 
     */
    public Video(int id, String name, String uri) {
    	this.id = id;
    	this.name = name;
    	this.uri = uri;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Video(Video existingVideosAccessedModel) {
        this.id = existingVideosAccessedModel.id;
        this.name = existingVideosAccessedModel.name;
        this.uri = existingVideosAccessedModel.uri;
    }

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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