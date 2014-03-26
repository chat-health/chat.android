package org.chat.android.models;

import java.util.Date;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "page_video1")
public class PageVideo1 {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
    @DatabaseField
    private String en_content1;
    @DatabaseField
    private String zu_content1;
    @DatabaseField
	private Date created_at;
	@DatabaseField
	private Date modified_at;

    
    /**
     * Default Constructor needed by ormlite
     */
    public PageVideo1() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param en_content1
     * @param zu_content1
     * @param created_at
     * @param modified_at
     */
    public PageVideo1(int id, String en_content1, String zu_content1, Date created_at, Date modified_at) {
    	this.id = id;
        this.en_content1 = en_content1;
        this.zu_content1 = zu_content1;
        this.created_at = created_at;
    	this.modified_at = modified_at;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
//    public PageVideo1(PageVideo1 existingPageVideo1Model) {
//        this.en_content1 = existingPageVideo1Model.en_content1;
//        this.zu_content1 = existingPageVideo1Model.zu_content1;
//    }
    

	public int getId() {
		return id;
	}
	
	public String getEnContent1() {
		return en_content1;
	}
	
	public void setEnContent1(String en_content1) {
		this.en_content1 = en_content1;
	}
	
	public void setZuContent1(String zu_content1) {
		this.zu_content1 = zu_content1;
	}
	
	public String getZuContent1() {
		return zu_content1;
	}
	
	public String getContent(String lang, String field) {
		if (lang.equals("en")) {
			if (field.equals("content1")) {
				return en_content1;
			} else {
				Log.e("Unknown field: ", field);
				return null;
			}
		} else if (lang.equals("zu")) {
			if (field.equals("content1")) {
				return zu_content1;
			} else {
				Log.e("Unknown field: ", field);
				return null;
			}
		} else {
			Log.e("Unknown language: ", lang);
			return null;
		}
		
		
	}
}