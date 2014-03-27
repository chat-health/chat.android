package org.chat.android.models;

import java.util.Date;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "page_text1")
public class PageText1 {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
    @DatabaseField
    private String en_content1;
    @DatabaseField
    private String zu_content1;
    @DatabaseField
    private String en_content2;
    @DatabaseField
    private String zu_content2;    
    @DatabaseField
    private Date created_at;
    @DatabaseField
    private Date modified_at;
    
    /**
     * Default Constructor needed by ormlite
     */
    public PageText1() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param en_content1;
     * @param zu_content1;
     * @param en_content2;
     * @param zu_content2;
     * @param created_at
     * @param modified_at
     */
    public PageText1(int id, String en_content1, String zu_content1, String en_content2, String zu_content2, Date created_at, Date modified_at) {
    	this.id = id;
        this.en_content1 = en_content1;
        this.zu_content1 = zu_content1;
        this.en_content2 = en_content2;
        this.zu_content2 = zu_content2;
        this.created_at = created_at;
    	this.modified_at = modified_at;        
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
//    public PageText1(PageText1 existingPageText1Model) {
//        this.en_content1 = existingPageText1Model.en_content1;
//        this.en_content2 = existingPageText1Model.en_content2;
//        this.zu_content1 = existingPageText1Model.zu_content1;
//        this.zu_content2 = existingPageText1Model.zu_content2;
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
	
	public String getEnContent2() {
		return en_content2;
	}
	
	public void setEnContent2(String en_content2) {
		this.en_content2 = en_content2;
	}
	
	public void setZuContent1(String zu_content1) {
		this.zu_content1 = zu_content1;
	}
	
	public String getZuContent1() {
		return zu_content1;
	}
	
	public void setZuContent2(String zu_content2) {
		this.zu_content2 = zu_content2;
	}

	public String getZuContent2() {
		return zu_content2;
	}
	
	// and this, ladies and gentlemen, is an argument for JS > Java - this is one easy line in JS
	// TODO: figure out how to do this in a more elegant way, with casting
	public String getContent(String lang, String field) {
		if (lang.equals("en")) {
			if (field.equals("content1")) {
				return en_content1;
			} else if (field.equals("content2")) {
				return en_content2;
			} else {
				Log.e("Unknown field: ", field);
				return null;
			}
		} else if (lang.equals("zu")) {
			if (field.equals("content1")) {
				return zu_content1;
			} else if (field.equals("content2")) {
				return zu_content2;
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