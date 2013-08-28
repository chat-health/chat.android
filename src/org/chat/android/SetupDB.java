package org.chat.android;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;

public class SetupDB extends Activity {
	
    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Context context = getApplicationContext();
		int hhId = 123;
		Client c1 = new Client(1, "John", "Doe", hhId, "male");
		Client c2 = new Client(2, "Jane", "Jacobs", hhId, "female");
		Client c3 = new Client(3, "Davey", "Jones", hhId, "male");
		
		
	    Dao<Client, Integer> clientDao1;
	    DatabaseHelper dbHelper1 = new DatabaseHelper(context);
	    try {
	        clientDao1 = dbHelper1.getClientsDao();
	        clientDao1.create(c1);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    Dao<Client, Integer> clientDao2;
	    DatabaseHelper dbHelper2 = new DatabaseHelper(context);
	    try {
	        clientDao2 = dbHelper2.getClientsDao();
	        clientDao2.create(c2);
	    } catch (SQLException e2) {
	        // TODO Auto-generated catch block
	        e2.printStackTrace();
	    }
	    Dao<Client, Integer> clientDao3;
	    DatabaseHelper dbHelper3 = new DatabaseHelper(context);
	    try {
	        clientDao3 = dbHelper3.getClientsDao();
	        clientDao3.create(c3);
	    } catch (SQLException e3) {
	        // TODO Auto-generated catch block
	        e3.printStackTrace();
	    }
	    
	    Toast.makeText(getApplicationContext(), "DB populated!", Toast.LENGTH_SHORT).show();
	    finish();
    }
}
