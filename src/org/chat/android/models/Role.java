package org.chat.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "roles")
public class Role {
	@DatabaseField
	private int id;
    @DatabaseField
    private String role_name;

    /**
     * Default Constructor needed by ormlite
     */
    public Role() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param role_name
     */
    public Role(int id, String role_name) {
    	this.id = id;
        this.role_name = role_name;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Role(Role existingRoleModel) {
        this.id = existingRoleModel.id;
        this.role_name = existingRoleModel.role_name;
    }
    

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRoleName() {
		return role_name;
	}
	
	public void setRoleName(String role_name) {
		this.role_name = role_name;
	}
}