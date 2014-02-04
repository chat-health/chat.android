package org.chat.android.models;

import java.util.Date;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by colin
 */
@DatabaseTable(tableName = "visits")
public class Visit {
	@DatabaseField(generatedId = true)
	private int id;
    @DatabaseField
    private int worker_id;
    @DatabaseField
	private String role;
    @DatabaseField
    private Date date;
    @DatabaseField
    private int hh_id;
    @DatabaseField
    private double lat;
    @DatabaseField
    private double lon;   
    @DatabaseField
    private Date start_time;
    @DatabaseField
    private Date end_time;
    @DatabaseField
    private String type;							// this may get moved to Service
    
    
    /**
     * Default Constructor needed by ormlite
     */
    public Visit() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param hh_id
     * @param worker_id
     * @param role      
     * @param date 
     * @param type
     * @param lon 
     * @param lat 
     * @param start_time
     */
    public Visit(int worker_id, String role, Date date, int hh_id, String type, double lat, double lon, Date start_time) {
    	this.hh_id = hh_id;
        this.worker_id = worker_id;
        this.role = role;
        this.date = date;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
        this.start_time = start_time;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Visit(Visit existingVisitModel) {
    	this.id = existingVisitModel.id;
    	this.hh_id = existingVisitModel.hh_id;
    	this.worker_id = existingVisitModel.worker_id;
        this.role = existingVisitModel.role;
        this.date = existingVisitModel.date;
        this.type = existingVisitModel.type;
        this.lat = existingVisitModel.lat;
        this.lon = existingVisitModel.lon;
        this.start_time = existingVisitModel.start_time;
        this.end_time = existingVisitModel.end_time;
    }

	public int getId() {
		return id;
	}

	// used for testing - should not generally be called
	public void setId(int id) {
		this.id = id;
	}
	
	public int getHhId() {
		return hh_id;
	}

	public void setHhId(int hh_id) {
		this.hh_id = hh_id;
	}
	
	public int getWorkerId() {
		return worker_id;
	}

	public void setWorkerId(int worker_id) {
		this.worker_id = worker_id;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Date setStartTime() {
		return start_time;
	}

	public void setStartTime(Date start_time) {
		this.start_time = start_time;
	}
	
	public Date setEndTime() {
		return end_time;
	}

	public void setEndTime(Date end_time) {
		this.end_time = end_time;
	}
}
