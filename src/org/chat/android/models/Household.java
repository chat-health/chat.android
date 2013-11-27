package org.chat.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by colin
 */
@DatabaseTable(tableName = "households")
public class Household {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
    @DatabaseField
    private String hh_name;
    @DatabaseField
    private String community;
    @DatabaseField
    private int worker_id;

    /**
     * Default Constructor needed by ormlite
     */
    public Household() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param hh_name
     * @param community
     * @param worker_id
     */
    public Household(int id, String hh_name, String community, int worker_id) {
    	this.id = id;
    	this.hh_name = hh_name;
    	this.community = community;
        this.worker_id = worker_id;
    }

    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Household(Household existingHouseholdModel) {
        this.id = existingHouseholdModel.id;
        this.hh_name = existingHouseholdModel.hh_name;
        this.community = existingHouseholdModel.community; 
        this.worker_id = existingHouseholdModel.worker_id;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getHhName() {
		return hh_name;
	}

	public void setHhName(String hh_name) {
		this.hh_name = hh_name;
	}
	
	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}
	
	public int getWorkerId() {
		return worker_id;
	}

	public void setWorkerId(int worker_id) {
		this.worker_id = worker_id;
	}

}
