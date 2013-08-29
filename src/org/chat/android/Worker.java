package org.chat.android;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "workers")
public class Worker {
	@DatabaseField(generatedId = true)
	private int worker_id;
    @DatabaseField
    private String first_name;
    @DatabaseField
    private String last_name;    
    @DatabaseField
    private String password;
    @DatabaseField
    private int role_id;
    @DatabaseField
    private String assigned_community;
//    @DatabaseField
//    private String username;
    
    
    /**
     * Default Constructor needed by ormlite
     */
    public Worker() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param first_name
     */
    public Worker(String first_name, String last_name, String password, int role_id, String assigned_community) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.role_id = role_id;
        this.assigned_community = assigned_community;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Worker(Worker existingWorkerModel) {
        this.first_name = existingWorkerModel.first_name;
        this.last_name = existingWorkerModel.last_name;
        this.password = existingWorkerModel.password;
        this.role_id = existingWorkerModel.role_id;
        this.assigned_community = existingWorkerModel.assigned_community;
    }
    

	public int getWorkerId() {
		return worker_id;
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
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getRoleId() {
		return role_id;
	}
	
	public void setRoleId(int role_id) {
		this.role_id = role_id;
	}
	
	public String getAssignedCommunity() {
		return assigned_community;
	}
	
	public void setAssignedCommunity(String assigned_community) {
		this.assigned_community = assigned_community;
	}
}