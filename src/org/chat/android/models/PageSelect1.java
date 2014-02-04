package org.chat.android.models;

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
//    @DatabaseField
//    private String image1;	
    @DatabaseField
    private String en_content1;
    @DatabaseField
    private String zu_content1;


    
    /**
     * Default Constructor needed by ormlite
     */
    public PageSelect1() {
    }

    
    /**
     * Constructor that instantiates the private member variable(s)
     * @param health_page_id
     */
    public PageSelect1(int id, String en_content1, String zu_content1) {
    	this.id = id;
    	//this.image1 = image1;
        this.en_content1 = en_content1;
        this.zu_content1 = zu_content1;
    }
    
    
    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public PageSelect1(PageSelect1 existingPageSelect1Model) {
    	//this.image1 = existingPageSelect1Model.image1;
        this.en_content1 = existingPageSelect1Model.en_content1; 
        this.zu_content1 = existingPageSelect1Model.zu_content1;
    }
    

	public int getId() {
		return id;
	}
//	public String getImage1() {
//		return image1;
//	}
	
//	public String getContent(String lang, String field) {
//		if (lang.equals("en")) {
//			if (field.equals("content1")) {
//				return en_content1;
//			}else if (field.equals("question1")) {
//				return en_question1;
//			} else if (field.equals("answer1")) {
//				return en_answer1;
//			} else if (field.equals("answer2")) {
//				return en_answer2;
//			} else if (field.equals("answer3")) {
//				return en_answer3;
//			} else if (field.equals("answer4")) {
//				return en_answer4;
//			} else {
//				Log.e("Unknown field: ", field);
//				return null;
//			}
//		} else if (lang.equals("zu")) {
//			if (field.equals("content1")) {
//				return zu_content1;
//			} else if (field.equals("question1")) {
//				return zu_question1;
//			} else if (field.equals("answer1")) {
//				return zu_answer1;
//			} else if (field.equals("answer2")) {
//				return zu_answer2;
//			} else if (field.equals("answer3")) {
//				return zu_answer3;
//			} else if (field.equals("answer4")) {
//				return zu_answer4;
//			} else {
//				Log.e("Unknown field: ", field);
//				return null;
//			}
//		} else {
//			Log.e("Unknown language: ", lang);
//			return null;
//		}
//	}
	
	public String getContent(String lang, String field) {
		if (lang.equals("en")) {
			return en_content1;
		} else if (lang.equals("zu")){
			return zu_content1;
		} else {
			Log.e("Unknown language:", lang);
			return null;
		}
	}
	
//	public void setImage(String image1) {
//		this.image1 = image1;
//	}
	public void setEnContent1(String en_content1) {
		this.en_content1 = en_content1;
	}
//	public void setEnQuestion1(String en_question1) {
//		this.en_question1 = en_question1;
//	}
//	public void setEnAnswer1(String en_answer1) {
//		this.en_answer1 = en_answer1;
//	}
//	public void setEnAnswer2(String en_answer2) {
//		this.en_answer2 = en_answer2;
//	}
//	public void setEnAnswer3(String en_answer3) {
//		this.en_answer3 = en_answer3;
//	}
//	public void setEnAnswer4(String en_answer4) {
//		this.en_answer4 = en_answer4;
//	}
	public void setZuContent1(String zu_content1) {
		this.zu_content1 = zu_content1;
	}
//	public void setZuQuestion(String zu_question1) {
//		this.zu_question1 = zu_question1;
//	}
//	public void setZuAnswer1(String zu_answer1) {
//		this.zu_answer1 = zu_answer1;
//	}
//	public void setZuAnswer2(String zu_answer2) {
//		this.zu_answer2 = zu_answer2;
//	}
//	public void setZuAnswer3(String zu_answer3) {
//		this.zu_answer3 = zu_answer3;
//	}
//	public void setZuAnswer4(String zu_answer4) {
//		this.zu_answer4 = zu_answer4;
//	}

}