package org.chat.android.models;

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
    private String en_content2;
    @DatabaseField
    private String zu_content1;
    @DatabaseField
    private String zu_content2;    

    
    /**
     * Default Constructor needed by ormlite
     */
    public PageText1() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param health_page_id
     */
    public PageText1(int id, String en_content1, String en_content2, String zu_content1, String zu_content2) {
    	this.id = id;
        this.en_content1 = en_content1;
        this.en_content2 = en_content2;
        this.zu_content1 = zu_content1;
        this.zu_content2 = zu_content2;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public PageText1(PageText1 existingPageText1Model) {
        this.en_content1 = existingPageText1Model.en_content1;
        this.en_content2 = existingPageText1Model.en_content2;
        this.zu_content2 = existingPageText1Model.zu_content2;
        this.zu_content2 = existingPageText1Model.zu_content2;
    }
    

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
}