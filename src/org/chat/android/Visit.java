package org.chat.android;

import java.util.GregorianCalendar;
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
    @DatabaseField(uniqueCombo=true)
    private GregorianCalendar date;
    @DatabaseField
    private double lat;
    @DatabaseField
    private double lon;
//    @DatabaseField
//    private something clients_present;
    @DatabaseField
    private String type;
    @DatabaseField
    private GregorianCalendar start_time;
    @DatabaseField
    private GregorianCalendar end_time;
//    @DatabaseField
//    private array? videos_watched; // this should likely be another table with reference IDs?

    /**
     * Default Constructor needed by ormlite
     */
    public Visit() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param last_name
     * @param HHID
     */
    public Visit(String staff_ID, String HHID, String type) {
        this.staff_ID = staff_ID;
        this.HHID = HHID;
        this.type = type;
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
	
	public String getHHID() {
		return HHID;
	}

	public void setHHID(String HHID) {
		this.HHID = HHID;
	}
	
	public String gettype() {
		return type;
	}

	public void settype(String type) {
		this.type = type;
	}
}
