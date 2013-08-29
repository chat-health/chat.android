package org.chat.android;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "services")
public class Service {
	@DatabaseField()
	private int id;
	
    @DatabaseField(uniqueCombo = true)			// need to figure out the uniqueCombo and index here (and _id)
    private String service_name;

    @DatabaseField()
    private String service_type;				// this represents the service supertype ie (Emotional Well Being)
    
    @DatabaseField()
    private int role_id;						// who has access to this service
    
    /**
     * Default Constructor needed by ormlite
     */
    public Service() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param service_name
     */
    public Service(int id, String service_name, String service_type, int role_id) {
        this.id = id;
    	this.service_name = service_name;
    	this.service_type = service_type;
    	this.role_id = role_id;
    }

    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Service(Service existingServiceModel) {
        this.service_name = existingServiceModel.service_name;
    }

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getServiceName() {
		return service_name;
	}

	public void setServiceName(String service_name) {
		this.service_name = service_name;
	}
	
	public String getServiceType() {
		return service_type;
	}

	public void setServiceType(String service_type) {
		this.service_type = service_type;
	}
	
	public int getRoleId() {
		return role_id;
	}

	public void setRoleId(int role_id) {
		this.role_id = role_id;
	}
}
