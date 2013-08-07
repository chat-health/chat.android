package org.chat.android;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


// we may need to modify this to serviceType or some other superordinate category - which will somehow need to be included in the DB (they defintely don't have this concept)


@DatabaseTable(tableName = "services")
public class Service {
	@DatabaseField(generatedId = true)
	private int _id;
	
    @DatabaseField(index = true, uniqueCombo=true)
    private String service_name;

    /**
     * Default Constructor needed by ormlite
     */
    public Service() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param service_name
     */
    public Service(String service_name) {
        this.service_name = service_name;
    }

    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Service(Service existingServiceModel) {
        this.service_name = existingServiceModel.service_name;
    }

	public int get_id() {
		return _id;
	}

	public String getServiceName() {
		return service_name;
	}

	public void setServiceName(String service_name) {
		this.service_name = service_name;
	}
}
