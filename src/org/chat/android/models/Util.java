package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "util")
public class Util {
	@DatabaseField
	private Date last_pushed_at;
    @DatabaseField
    private Date last_pulled_at;


    /**
     * Default Constructor needed by ormlite
     */
    public Util() {
    }
    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param last_pushed_at
     * @param last_pulled_at
     * 
     */
    public Util(Date last_pushed_at, Date last_pulled_at) {
    	this.last_pushed_at = last_pushed_at;
    	this.last_pulled_at = last_pulled_at;
    }
    
    public Date getLastPushedAt() {
		return last_pushed_at;
	}
	
	public void setLastPushedAt(Date last_pushed_at) {
		this.last_pushed_at = last_pushed_at;
	}
    
	public Date getLastPulledAt() {
		return last_pulled_at;
	}
	
	public void setLastPulledAt(Date last_pulled_at) {
		this.last_pulled_at = last_pulled_at;
	}
    
}