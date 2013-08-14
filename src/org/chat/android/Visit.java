package org.chat.android;

import java.util.Date;
import java.util.GregorianCalendar;

import android.text.format.Time;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by colin
 */
@DatabaseTable(tableName = "visits")
public class Visit {
	@DatabaseField(generatedId = true)
	private int _id;

    @DatabaseField
    private int hhid;
    @DatabaseField
    private Date date;
    @DatabaseField
    private int worker_id;
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

    /**
     * Default Constructor needed by ormlite
     * @param userName 
     * @param date
     */
    public Visit(String userName, Date date) {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param HHName
     * @param workerName
     * @param role      
     * @param date 
     * @param type
     * @param lon 
     * @param lat 
     * @param start_time
     */
    public Visit(String HHName, String workerName, String role, Date date, String type, double lat, double lon, Time startTime) {
    	this.HHName = HHName;
        this.workerName = workerName;
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
        this.workerName = existingVisitModel.workerName;
        this.HHName = existingVisitModel.HHName;
        this.type = existingVisitModel.type;
    }

	public int get_id() {
		return _id;
	}

	public String getHHName() {
		return HHName;
	}

	public void setHHName(String HHName) {
		this.HHName = HHName;
	}
	
	public String getworkerName() {
		return workerName;
	}

	public void setworkerName(String workerName) {
		this.workerName = workerName;
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
