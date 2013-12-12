package org.chat.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "health_subtopics")
public class HealthSubtopic {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String topic;

    
    /**
     * Default Constructor needed by ormlite
     */
    public HealthSubtopic() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param name
     */
    public HealthSubtopic(int id, String name, String topic) {
    	this.id = id;
        this.name = name;
        this.topic = topic;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public HealthSubtopic(HealthSubtopic existingHealthSubtopicModel) {
        this.name = existingHealthSubtopicModel.name;
        this.topic = existingHealthSubtopicModel.topic;
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
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
}