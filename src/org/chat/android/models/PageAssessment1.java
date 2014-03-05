package org.chat.android.models;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by colin
 */
@DatabaseTable(tableName = "page_assessment1")
public class PageAssessment1 {
	@DatabaseField(id = true, unique = true, index = true)
	private int id;
	@DatabaseField
	private String type;
    @DatabaseField
    private String en_content1;
    @DatabaseField
    private String zu_content1;
    @DatabaseField
    private String en_content2;
    @DatabaseField
    private String zu_content2;
    @DatabaseField
    private String en_content3;
    @DatabaseField
    private String zu_content3;

    
    /**
     * Default Constructor needed by ormlite
     */
    public PageAssessment1() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param health_page_id
     */
    public PageAssessment1(int id, String type, String en_content1, String zu_content1, String en_content2, String zu_content2, String en_content3, String zu_content3) {
    	this.id = id;
    	this.type = type;
        this.en_content1 = en_content1;
        this.zu_content1 = zu_content1;
        this.en_content2 = en_content2;
        this.zu_content2 = zu_content2;
        this.en_content3 = en_content3;
        this.zu_content3 = zu_content3;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public PageAssessment1(PageAssessment1 existingPageAssessment1Model) {
    	this.type = existingPageAssessment1Model.type;
        this.en_content1 = existingPageAssessment1Model.en_content1;
        this.zu_content1 = existingPageAssessment1Model.zu_content1;
        this.en_content2 = existingPageAssessment1Model.en_content2;
        this.zu_content2 = existingPageAssessment1Model.zu_content2;
        this.en_content3 = existingPageAssessment1Model.en_content3;
        this.zu_content3 = existingPageAssessment1Model.zu_content3;
    }
    

	public int getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
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

	public String getEnContent2() {
		return en_content2;
	}
	
	public void setEnContent2(String en_content2) {
		this.en_content2 = en_content2;
	}
	
	public void setZuContent2(String zu_content2) {
		this.zu_content2 = zu_content2;
	}

	public String getZuContent2() {
		return zu_content2;
	}
	
	public String getEnContent3() {
		return en_content3;
	}
	
	public void setEnContent3(String en_content3) {
		this.en_content3 = en_content3;
	}
	
	public void setZuContent3(String zu_content3) {
		this.zu_content3 = zu_content3;
	}

	public String getZuContent3() {
		return zu_content3;
	}
	
	// and this, ladies and gentlemen, is an argument for JS > Java - this is one easy line in JS
	// TODO: figure out how to do this in a more elegant way, with casting
	public String getContent(String lang, String field) {
		if (lang.equals("en")) {
			if (field.equals("content1")) {
				return en_content1;
			} else if (field.equals("content2")) {
				return en_content2;
			} else if (field.equals("content3")) {
				return en_content3;
			} else {
				Log.e("Unknown field: ", field);
				return null;
			}
		} else if (lang.equals("zu")) {
			if (field.equals("content1")) {
				return zu_content1;
			} else if (field.equals("content2")) {
				return zu_content2;
			} else if (field.equals("content3")) {
				return zu_content3;
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