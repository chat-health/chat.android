package org.chat.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "health_topics")
public class HealthTopic {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String theme;

    
    /**
     * Default Constructor needed by ormlite
     */
    public HealthTopic() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param name
     */
    public HealthTopic(int id, String name, String theme) {
    	this.id = id;
        this.name = name;
        this.theme = theme;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public HealthTopic(HealthTopic existingHealthTopicModel) {
        this.name = existingHealthTopicModel.name;
        this.theme = existingHealthTopicModel.theme;
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
	
	public String getTheme() {
		return theme;
	}
	
	public void setTheme(String theme) {
		this.theme = theme;
	}
}