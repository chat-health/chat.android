package org.chat.android.models;

import java.util.Date;

import org.chat.android.DatabaseHelper;
import org.chat.android.ModelHelper;
import org.chat.android.MyApplication;

import android.content.Context;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "attendance")
public class Attendance {
	@DatabaseField(id = true)
	private int id;
    @DatabaseField
    private int visit_id;
    @DatabaseField
	private int client_id;
    @DatabaseField
	private Boolean newly_created;

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
    public Attendance(int visit_id, int client_id, DatabaseHelper dbHelper) {
        // this may be trouble with the sync adapter? PLEASE TEST ME
        // https://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android
        Context myContext = MyApplication.getAppContext();
        Visit v = ModelHelper.getVisitForId(dbHelper, visit_id);
        ModelHelper.setVisitToDirtyAndSave(dbHelper, v);
        this.id = ModelHelper.generateId(myContext);
    	this.visit_id = visit_id;
        this.client_id = client_id;
        this.newly_created = true;
    }
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Attendance(Attendance existingAttendanceModel) {
        this.visit_id = existingAttendanceModel.visit_id;
        this.client_id = existingAttendanceModel.client_id;
    }

	public int getId() {
		return id;
	}

	public int getVisitId() {
		return visit_id;
	}
	
	public void setVisitId(int visit_id) {
		this.visit_id = visit_id;
	}
	
	public int getClientId() {
		return client_id;
	}
	
	public void setClientId(int client_id) {
		this.client_id = client_id;
	}
	
	public Boolean getNewlyCreatedStatus() {
		return newly_created;
	}
	
	public void setNewlyCreatedStatus() {
		this.newly_created = true;
	}
	
	public void makeClean() {
		this.newly_created = false;
	}
}