package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "workers")
public class Worker {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
    @DatabaseField
    private String first_name;
    @DatabaseField
    private String last_name;
    @DatabaseField
    private String username;
    @DatabaseField
    private String password;
    @DatabaseField
    private String role_name;
    @DatabaseField
    private String assigned_community;
    @DatabaseField
    private int phone_number;
    @DatabaseField
	private Date created_at;
	@DatabaseField
	private Date modified_at;
    
    
    /**
     * Default Constructor needed by ormlite
     */
    public Worker() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param first_name
     * @param last_name
     * @param username
     * @param password
     * @param role_name
     * @param assigned_community
     * @param phone_number
     * @param created_at
     * @param modified_at
     */
    public Worker(int id, String first_name, String last_name, String username, String password, String role_name, String assigned_community, int phone_number, Date created_at, Date modified_at) {
    	this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.role_name = role_name;
        this.assigned_community = assigned_community;
        this.phone_number = phone_number;
        this.created_at = created_at;
    	this.modified_at = modified_at;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
//    public Worker(Worker existingWorkerModel) {
//        this.first_name = existingWorkerModel.first_name;
//        this.last_name = existingWorkerModel.last_name;
//        this.password = existingWorkerModel.password;
//        this.role_name = existingWorkerModel.role_name;
//        this.assigned_community = existingWorkerModel.assigned_community;
//    }
    

	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return first_name;
	}
	
	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}
	
	public String getLastName() {
		return last_name;
	}
	
	public void setLastName(String last_name) {
		this.last_name = last_name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRoleName() {
		return role_name;
	}
	
	public void setRoleName(String role_name) {
		this.role_name = role_name;
	}
	
	public String getAssignedCommunity() {
		return assigned_community;
	}
	
	public void setAssignedCommunity(String assigned_community) {
		this.assigned_community = assigned_community;
	}
	
	public int getPhoneNumber() {
		return phone_number;
	}
	
	public void setPhoneNumber(int phone_number) {
		this.phone_number = phone_number;
	}
	
	public String toString() {
		return "Worker: " + getId() + getFirstName();
	}
}