package org.chat.android.models;

import java.util.Date;
import java.util.UUID;

import android.content.Context;
import android.provider.Settings.Secure;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by colin
 */
@DatabaseTable(tableName = "visits")
public class Visit {
	@DatabaseField
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
    @DatabaseField
	private boolean dirty;
    
    /**
     * Default Constructor needed by ormlite
     */
    public Visit() {
    	this.dirty = true;
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
    public Visit(Context context, int worker_id, String role, Date date, int hh_id, String type, double lat, double lon, Date start_time) {
    	String myUUID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
   
    	long seconds = System.currentTimeMillis() / 1000;
    	
    	//int myId = (int) (myUUID.hashCode() + seconds);
    	
    	String secondsString = String.valueOf(seconds);
    	String idString = myUUID+secondsString;
    	int hashedIdString = idString.hashCode();
    	int myId = hashedIdString;
    	// k, this isn't great IMO. Functional for now
    	
    	
    	this.id = myId;
    	this.hh_id = hh_id;
        this.worker_id = worker_id;
        this.role = role;
        this.date = date;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
        this.start_time = start_time;
        this.dirty = true;
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
        this.dirty = true;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.setDirty();
	}
	
	public int getHhId() {
		return hh_id;
	}

	public void setHhId(int hh_id) {
		this.hh_id = hh_id;
		this.setDirty();
	}
	
	public int getWorkerId() {
		return worker_id;
	}

	public void setWorkerId(int worker_id) {
		this.worker_id = worker_id;
		this.setDirty();
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
		this.setDirty();
	}
	
	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
		this.setDirty();
	}
	
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
		this.setDirty();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.setDirty();
	}
	
	public Date getStartTime() {
		return start_time;
	}

	public void setStartTime(Date start_time) {
		this.start_time = start_time;
		this.setDirty();
	}
	
	public Date getEndTime() {
		return end_time;
	}

	public void setEndTime(Date end_time) {
		this.end_time = end_time;
		this.setDirty();
	}
	
	public void setDirty() {
		this.dirty = true;
	}
	
	/** 
	 * This function reverses the dirty flag and should be used
	 * by the SyncAdapter to avoid syncing documents that have been synced and not changed inbetween.
	 */
	public void makeClean() {
		this.dirty = false;
	}
	
	public boolean isDirty() {
		return this.dirty;
	}
}
