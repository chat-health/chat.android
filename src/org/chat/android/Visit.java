package org.chat.android;

import java.util.Date;
import java.util.GregorianCalendar;

import android.text.format.Time;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by colin
 */
//@DatabaseTable(tableName = "visits")
public class Visit {
	@DatabaseField(generatedId = true)
	private int _id;

	// check the var types on these - also just sanity check these
    @DatabaseField(index = true, uniqueCombo=true)
    private String staff_ID;
    @DatabaseField(index = true, uniqueCombo=true)
    private String HHID;
    @DatabaseField()
    private Date date;
    @DatabaseField
    private String role;    
    @DatabaseField
    private double lat;
    @DatabaseField
    private double lon;
//    @DatabaseField
//    private something clients_present;
    @DatabaseField
    private String type;
    @DatabaseField
    private Time start_time;
    @DatabaseField
    private Time end_time;
//    @DatabaseField
//    private array? videos_watched; // this should likely be another table with reference IDs?

    /**
     * Default Constructor needed by ormlite
     * @param userName 
     * @param date
     */
    public Visit(String userName, Date date) {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param HHID
     * @param staff_ID
     * @param role      
     * @param date 
     * @param type
     * @param lon 
     * @param lat 
     * @param start_time
     */
    public Visit(String HHID, String staff_ID, String role, Date date, String type, double lat, double lon, Time startTime) {
    	this.HHID = HHID;
        this.staff_ID = staff_ID;
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
        this.staff_ID = existingVisitModel.staff_ID;
        this.HHID = existingVisitModel.HHID;
        this.type = existingVisitModel.type;
    }

	public int get_id() {
		return _id;
	}

	public String getStaff_ID() {
		return staff_ID;
	}

	public void setStaff_ID(String staff_ID) {
		this.staff_ID = staff_ID;
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
	
	public String getHHID() {
		return HHID;
	}

	public void setHHID(String HHID) {
		this.HHID = HHID;
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
