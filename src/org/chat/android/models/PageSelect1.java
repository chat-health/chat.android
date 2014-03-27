package org.chat.android.models;

import java.util.Date;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "page_select1")
public class PageSelect1 {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
    @DatabaseField
    private String en_content;
    @DatabaseField
    private String zu_content;
    @DatabaseField
    private Date created_at;
    @DatabaseField
    private Date modified_at;

    
    /**
     * Default Constructor needed by ormlite
     */
    public PageSelect1() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param id
     * @param en_content
     * @param zu_content
     * @param created_at
     * @param modified_at
     */
    public PageSelect1(int id, String en_content, String zu_content, Date created_at, Date modified_at) {
    	this.id = id;
        this.en_content = en_content;
        this.zu_content = zu_content;
        this.created_at = created_at;
    	this.modified_at = modified_at;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
//    public PageSelect1(PageSelect1 existingPageSelect1Model) {
//        this.en_content = existingPageSelect1Model.en_content; 
//        this.zu_content = existingPageSelect1Model.zu_content;
//    }
    

	public int getId() {
		return id;
	}
	
	public String getContent(String lang, String field) {				// FIXME - content1 -> content
		if (lang.equals("en")) {
			return en_content;
		} else if (lang.equals("zu")){
			return zu_content;
		} else {
			Log.e("Unknown language:", lang);
			return null;
		}
	}
	
	public void setEnContent1(String en_content) {
		this.en_content = en_content;
	}

	public void setZuContent1(String zu_content) {
		this.zu_content = zu_content;
	}

}