package org.chat.android.models;

import java.util.Date;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "services")
public class Service {
	@DatabaseField(id = true, index = true, uniqueCombo = true)
	private int id;
    @DatabaseField
    private String en_name;
    @DatabaseField
    private String zu_name;
    @DatabaseField()
    private String type;						// this represents the service supertype ie (Emotional Well Being)
    @DatabaseField()
    private String role;						// who has access to this service
    @DatabaseField()
    private String instructions;						// additional info - usually becomes the hint in the Other list, and will often be null
    @DatabaseField
	private Date created_at;
	@DatabaseField
	private Date modified_at;

    /**
     * Default Constructor needed by ormlite
     */
    public Service() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param service_name
     */
    public Service(int id, String en_name, String zu_name, String type, String role, String instructions, Date created_at, Date modified_at) {
        this.id = id;
    	this.en_name = en_name;
    	this.zu_name = zu_name;
    	this.type = type;
    	this.role = role;
    	this.instructions = instructions;
    	this.created_at = created_at;
    	this.modified_at = modified_at;
    }

    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
//    public Service(Service existingServiceModel) {
//        this.name = existingServiceModel.name;
//    }

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getName(String lang) {
		if (lang.equals("en")) {
			return en_name;
		} else if (lang.equals("zu")) {
			return zu_name;
		} else {
			Log.e("Unknown language: ", lang);
			return null;
		}
	}

	public void setEnName(String en_name) {
		this.en_name = en_name;
	}
	
	public void setZuName(String zu_name) {
		this.zu_name = zu_name;
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
