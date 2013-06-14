package org.chat.android;

import java.util.GregorianCalendar;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by Armin Krauss on 2013-06-12.
 */
@DatabaseTable(tableName = "clients")
public class Client {
	@DatabaseField(generatedId = true)
	private int _id;
	
    @DatabaseField(index = true, uniqueCombo=true)
    private String last_name;
    @DatabaseField(index = true, uniqueCombo=true)
    private String first_name;
    @DatabaseField(uniqueCombo=true)
    private GregorianCalendar birthday;

    /**
     * Default Constructor needed by ormlite
     */
    public Client() {
    }

    /**
     * Constructor that instantiates the private member variable(s)
     * @param last_name
     * @param first_name
     */
    public Client(String last_name, String first_name, GregorianCalendar birthday) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.birthday = birthday;
    }

    /**
     * Copy constructor
     * @param existingListModel - List model instance that is copied to new instance
     */
    public Client(Client existingClientModel) {
        this.last_name = existingClientModel.last_name;
        this.first_name = existingClientModel.first_name;
        this.birthday = existingClientModel.birthday;
    }

	public int get_id() {
		return _id;
	}

	public String getLastname() {
		return last_name;
	}

	public void setLastname(String last_name) {
		this.last_name = last_name;
	}
	
	public String getFirstname() {
		return first_name;
	}

	public void setFirstname(String first_name) {
		this.first_name = first_name;
	}
	
	public GregorianCalendar getBirthday() {
		return birthday;
	}

	public void setBirthday(GregorianCalendar birthday) {
		this.birthday = birthday;
	}
}
