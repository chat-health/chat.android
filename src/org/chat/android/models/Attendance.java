package org.chat.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "attendance")
public class Attendance {
//	@DatabaseField(generatedId = true)
//	private int id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
	private int client_id;
    @DatabaseField
	private boolean dirty;

    /**
     * Default Constructor needed by ormlite
     */
    public Attendance() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param visit_id
     * @param client_id
     */
    public Attendance(int visit_id, int client_id) {
    	this.visit_id = visit_id;
        this.client_id = client_id;
        this.dirty = true;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Attendance(Attendance existingAttendanceModel) {
        this.visit_id = existingAttendanceModel.visit_id;
        this.client_id = existingAttendanceModel.client_id;
    }

//	public int getId() {
//		return id;
//	}

	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
		this.setDirty();
	}
	
	public int getClientId() {
		return client_id;
	}
	
	public void setClientId(int client_id) {
		this.client_id = client_id;
		this.setDirty();
	}
	
	public void setDirty() {
		this.dirty = true;
	}
	
	public boolean isDrity() {
		return this.dirty;
	}
}