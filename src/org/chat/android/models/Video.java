package org.chat.android.models;

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
    @DatabaseField()
    private String uri;
    @DatabaseField
    private String screenshot;


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
     * @param screenshot
     * 
     */
    public Video(int id, String name, String uri, String screenshot) {
    	this.id = id;
    	this.name = name;
    	this.uri = uri;
    	this.screenshot = screenshot;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Video(Video existingVideosAccessedModel) {
        this.id = existingVideosAccessedModel.id;
        this.name = existingVideosAccessedModel.name;
        this.uri = existingVideosAccessedModel.uri;
        this.screenshot = existingVideosAccessedModel.screenshot;
    }

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
	
	public String getScreenshot() {
		return screenshot;
	}
	
	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}	
}