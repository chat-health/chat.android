package org.chat.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "services")
public class Service {
	@DatabaseField(index = true, uniqueCombo = true)
	private int id;
    @DatabaseField(index = true, uniqueCombo = true)
    private String name;
    @DatabaseField()
    private String type;						// this represents the service supertype ie (Emotional Well Being)
    @DatabaseField()
    private String role;						// who has access to this service
    @DatabaseField()
    private String instructions;						// additional info - usually becomes the hint in the Other list, and will often be null

    /**
     * Default Constructor needed by ormlite
     */
    public Service() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param service_name
     */
    public Service(int id, String name, String type, String role, String instructions) {
        this.id = id;
    	this.name = name;
    	this.type = type;
    	this.role = role;
    	this.instructions = instructions;
    }

    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Service(Service existingServiceModel) {
        this.name = existingServiceModel.name;
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
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
}
