package org.chat.android.models;


import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "vaccines")
public class Vaccine {
	@DatabaseField(index = true)
	private int id;
    @DatabaseField
    private double age;							// age that vaccine should have been administered, in years
    @DatabaseField
	private String display_age;					// same, but what will appear to the user (ie "Birth", "6 weeks", "12 years")
    @DatabaseField
	private String short_name;
    @DatabaseField
	private String long_name;
    @DatabaseField
	private Date created_at;
	@DatabaseField
	private Date modified_at;


    /**
     * Default Constructor needed by ormlite
     */
    public Vaccine() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param age
     * @param display_age
     * @param short_name
     * @param long_name
     * @param created_at
     * @param modified_at
     * 
     */
    public Vaccine(int id, double age, String display_age, String short_name, String long_name, Date created_at, Date modified_at) {
    	this.id = id;
    	this.age = age;
    	this.display_age = display_age;
        this.short_name = short_name;
        this.long_name = long_name;
        this.created_at = created_at;
    	this.modified_at = modified_at;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
//    public Vaccine(Vaccine existingServicesAccessedModel) {
//    	this.id = existingServicesAccessedModel.id;
//        this.age = existingServicesAccessedModel.age;
//        this.display_age = existingServicesAccessedModel.display_age;
//        this.short_name = existingServicesAccessedModel.short_name;
//        this.long_name = existingServicesAccessedModel.long_name;
//    }

	public int getId() {
		return id;
	}
	
    public void setId(int id) {
		this.id = id;
	}
    
    public double getAge() {
		return age;
	}
	
    public void setAge(double age) {
		this.age = age;
	}
    
    public String getDisplayAge() {
		return display_age;
	}
	
    public void setDisplayAge(String display_age) {
		this.display_age = display_age;
	}
	
	public String getShortName() {
		return short_name;
	}
	
	public void setShortName(String short_name) {
		this.short_name = short_name;
	}
	
	public String getLongName() {
		return long_name;
	}
	
	public void setLongName(String long_name) {
		this.long_name = long_name;
	}
}