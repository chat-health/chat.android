package org.chat.android.models;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Armin Krauss on 2013-06-12.
 */
@DatabaseTable(tableName = "clients")
public class Client {
	@DatabaseField(index = true, uniqueCombo = true)
	private int id;
	@DatabaseField(index = true, uniqueCombo = true)
	private String last_name;
	@DatabaseField(index = true, uniqueCombo = true)
	private String first_name;
	@DatabaseField
	private int hh_id;	
	@DatabaseField
	private String gender;
	@DatabaseField
	private Date date_of_birth;

	/**
	 * Default Constructor needed by ormlite
	 */
	public Client() {
	}

	/**
	 * Constructor that instantiates the private member variable(s)
	 * 
	 * @param id
	 * @param last_name
	 * @param first_name
	 * @param hh_id
	 * @param gender
	 * @param date_of_birth
	 */
	public Client(int id, String first_name, String last_name, int hh_id, String gender, Date date_of_birth) {
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.hh_id = hh_id;
		this.gender = gender;
		this.date_of_birth = date_of_birth;
	}

	/**
	 * Copy constructor
	 * 
	 * @param existingListModel
	 *            - List model instance that is copied to new instance
	 */
	public Client(Client existingClientModel) {
		this.id = existingClientModel.id;
		this.last_name = existingClientModel.last_name;
		this.first_name = existingClientModel.first_name;
		this.hh_id = existingClientModel.hh_id;
		this.gender = existingClientModel.gender;
		this.date_of_birth = existingClientModel.date_of_birth;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String last_name) {
		this.last_name = last_name;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}
	
	public int getHhId() {
		return hh_id;
	}

	public void setHhId(int hh_id) {
		this.hh_id = hh_id;
	}	

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public Date getDateOfBirth() {
		return date_of_birth;
	}

	public void setDateOfBirth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	
	
	// WATCH FOR TIMEZONE
	// TODO: fix this pile of crap. Ignores leap years, many other issues - need better api instead of doing this nonsense manually
	public double getAge() {
		//String age_string;
    	Date today = new Date();
    	//double age = today.getYear() - date_of_birth.getYear();
    	double ageInMS = today.getTime() - date_of_birth.getTime();
    	double age = Math.floor(ageInMS / 31536000);
    	age = age/1000;				// LOLOLOLOL
    	
		return age;
	}
	
	public String getAgeString() {
		Date today = new Date();
    	int age = today.getYear() - date_of_birth.getYear();
    	
		return Integer.toString(age);
	}
}
