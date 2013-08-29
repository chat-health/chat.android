package org.chat.android;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "roles")
public class Role {
	@DatabaseField
	private int role_id;
    @DatabaseField
    private String role_name;

    /**
     * Default Constructor needed by ormlite
     */
    public Role() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param role_id
     * @param role_name
     */
    public Role(int role_id, String role_name) {
    	this.role_id = role_id;
        this.role_name = role_name;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Role(Role existingRoleModel) {
        this.role_id = existingRoleModel.role_id;
        this.role_name = existingRoleModel.role_name;
    }
    

	public int getRoleId() {
		return role_id;
	}
	
	public void setRoleId(int role_id) {
		this.role_id = role_id;
	}
	
	public String getRoleName() {
		return role_name;
	}
	
	public void setRoleName(String role_name) {
		this.role_name = role_name;
	}
}