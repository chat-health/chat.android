package org.chat.android;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.Attendance;
import org.chat.android.models.Visit;
import org.chat.android.models.Worker;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;

public class RestoreVisitActivity extends Activity {
	// TODO Colin variables should have visibility set (private, public, protected)
	Context context;
	int visitId = 0;
	String workerName = "";
	String role = "";
	// since we aren't OrmLiteBaseActivity or BaseActivity we can't use getHelper()
    // so we use OpenHelperManager
    private DatabaseHelper databaseHelper = null;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		setContentView(R.layout.activity_restore_visit);

		Bundle b = getIntent().getExtras();
		visitId = b.getInt("visitId");					// what will visitId be if it doesn't exist? Needs to be 0 or needs an if wrapped around it. Then clean this (lots of dup in the two functions)

		workerName = b.getString("workerName");
		role = b.getString("role");
	}

	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

	public void resumeVisit(View v) {
		Intent i = new Intent(RestoreVisitActivity.this, HomeActivity.class);
		Bundle b = new Bundle();
		b.putString("workerName",workerName);
		b.putString("role",role);
		b.putInt("visitId",visitId);
		b.putBoolean("fromBack",true);
		i.putExtras(b);
		startActivity(i);
		finish();
	}

	public void openNewVisitWarning(View v) {
		// prompt about deleting uncompleted visits
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            setupNewVisit();
		            break;
		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String msg = getResources().getString(getResources().getIdentifier("restore_delete_text", "string", getPackageName()));
		builder.setMessage(msg)
			.setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
	}

	public void setupNewVisit() {
		int workerId = 0;
		workerId = ModelHelper.getWorkerForUsername(getHelper(), workerName).getId();

		// mark the old visit as 'complete', that is end_time = start_time - doing this instead of deleting old visits
		Visit visit = null;
		visit = ModelHelper.getVisitForId(getHelper(), visitId);
		Date endTime = null;
		endTime = visit.getStartTime();
		visit.setEndTime(endTime);

	    try {
	    	Dao<Visit, Integer> vDao = getHelper().getVisitsDao();
	    	vDao.update(visit);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }


		// delete old visits
//    	DatabaseHelper helper = OpenHelperManager.getHelper(getApplicationContext(), DatabaseHelper.class);
//    	Dao vDao;
//	    try {
//		    vDao = helper.getDao(Visit.class);
//		    DeleteBuilder<Visit, Integer> deleteBuilder = vDao.deleteBuilder();
//		    deleteBuilder.where().eq("worker_id",workerId).and().isNull("end_time");
//		    deleteBuilder.delete();
//    	} catch (SQLException e) {
//    	  	// TODO Auto-generated catch block
//    	  	e.printStackTrace();
//    	}



		// start a new visit
		Intent i = new Intent(RestoreVisitActivity.this, SetupVisitActivity.class);
		Bundle b = new Bundle();
		b.putString("workerName",workerName);
		b.putString("role",role);
		b.putInt("visitId",visitId);
		i.putExtras(b);
		startActivity(i);
		finish();
	}
}
