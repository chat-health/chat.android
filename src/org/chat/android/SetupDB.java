package org.chat.android;

import java.sql.SQLException;
import java.util.Date;

import org.chat.android.models.Util;

import android.os.Bundle;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

public class SetupDB extends OrmLiteBaseActivity<DatabaseHelper> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // UTIL - TODO: think about where to eventually move this, since it needs to fire on empty DB or something
        Date d = new Date();
        Util u1 = new Util(1, d, d);
        try {
        	Dao<Util, Integer> utilDao = getHelper().getUtilDao();
        	utilDao.create(u1);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }

	    Toast.makeText(getApplicationContext(), "I now do nothing!", Toast.LENGTH_SHORT).show();
	    finish();
    }
}
