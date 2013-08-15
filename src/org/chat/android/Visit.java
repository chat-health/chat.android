package org.chat.android;

import java.util.Date;
import java.util.GregorianCalendar;

import android.text.format.Time;

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
	private int _id;

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
    private Time start_time;
    @DatabaseField
    private Time end_time;
    @DatabaseField
    private Boolean video_watched;
    @DatabaseField
    private Boolean resource_accessed;
    @DatabaseField
    private String type;							// this may get moved to Service
    
    @ForeignCollectionField()						// TODO: revisit this when I understand what's going on
    ForeignCollection<Client> clients;

    
    
    /**
     * Default Constructor needed by ormlite
     * @param date
     */
    public Visit(Date date) {
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
    public Visit(int worker_id, String role, Date date, int hh_id, String type, double lat, double lon, Time startTime) {
    	this.hh_id = hh_id;
        this.worker_id = worker_id;
        this.role = role;
        this.date = date;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
        this.start_time = startTime;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Visit(Visit existingVisitModel) {
        this.worker_id = existingVisitModel.worker_id;
        this.hh_id = existingVisitModel.hh_id;
        this.type = existingVisitModel.type;
    }

	public int get_id() {
		return _id;
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
	
	public Time setStartTime() {
		return start_time;
	}

	public void setStartTime(Time startTime) {
		this.start_time = startTime;
	}
}
