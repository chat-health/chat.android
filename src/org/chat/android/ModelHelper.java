package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chat.android.models.Attendance;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.HealthTopic;
import org.chat.android.models.Household;
import org.chat.android.models.PageSelect1;
import org.chat.android.models.PageText1;
import org.chat.android.models.PageVideo1;
import org.chat.android.models.Service;
import org.chat.android.models.TopicVideo;
import org.chat.android.models.Video;
import org.chat.android.models.Visit;
import org.chat.android.models.Worker;

import android.R.array;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.j256.ormlite.dao.Dao;

public class ModelHelper {
	public static Visit getVisitForId(Context context, int visitId) {
		Visit visit = null;
		Dao<Visit, Integer> vDao;		
		DatabaseHelper vDbHelper = new DatabaseHelper(context);
		try {
			vDao = vDbHelper.getVisitsDao();
			List<Visit> vList = vDao.queryBuilder().where().eq("id",visitId).query();
			Iterator<Visit> iter = vList.iterator();
			while (iter.hasNext()) {
				visit = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return visit;
	}


	public static Worker getWorkerForName(Context context, String workerName) {
		Worker worker = null;
		Dao<Worker, Integer> wDao;		
		DatabaseHelper wDbHelper = new DatabaseHelper(context);
		try {
			wDao = wDbHelper.getWorkersDao();
			List<Worker> wList = wDao.queryBuilder().where().eq("first_name",workerName).query();
			Iterator<Worker> iter = wList.iterator();
			while (iter.hasNext()) {
				worker = iter.next();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return worker;
	}


	public static Household getHouseholdForName(Context context, String hhName) {
		Household household = null;
		Dao<Household, Integer> hDao;		
		DatabaseHelper hDbHelper = new DatabaseHelper(context);
		try {
			hDao = hDbHelper.getHouseholdsDao();
			List<Household> hList = hDao.queryBuilder().where().eq("hh_name",hhName).query();
			Iterator<Household> iter = hList.iterator();
			while (iter.hasNext()) {
				household = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return household;
	}
	
	
	public static Attendance getAttendanceForVisitId(Context context, int visitId) {
		Attendance attendance = null;
		Dao<Attendance, Integer> aDao;		
		DatabaseHelper aDbHelper = new DatabaseHelper(context);
		try {
			aDao = aDbHelper.getAttendanceDao();
			List<Attendance> aList = aDao.queryBuilder().where().eq("visit_id",visitId).query();
			Iterator<Attendance> iter = aList.iterator();
			while (iter.hasNext()) {
				attendance = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return attendance;
	}
	
	
	public static Service getServiceForName(Context context, String sName) {
    	Service service = null;
        Dao<Service, Integer> sDao;
        List<Service> allServices = new ArrayList<Service>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        try {
			sDao = dbHelper.getServicesDao();
			allServices = sDao.query(sDao.queryBuilder().prepare());
        	for (Service s : allServices) {
    			if (s.getName().equals(sName)) {
    				service = s;
    			}
        	}
		} catch (SQLException e) {
			Log.e("Unable to locate service id from name:", sName);
			e.printStackTrace();
		}
        return service;
	}
	
	
	public static HealthTheme getThemeForName(Context context, String themeName) {
		HealthTheme theme = null;
		Dao<HealthTheme, Integer> tDao;		
		DatabaseHelper tDbHelper = new DatabaseHelper(context);
		try {
			tDao = tDbHelper.getHealthThemeDao();
			List<HealthTheme> tList = tDao.queryBuilder().where().eq("name",themeName).query();
			Iterator<HealthTheme> iter = tList.iterator();
			while (iter.hasNext()) {
				theme = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return theme;
	}
	
	
	public static HealthTopic getTopicForName(Context context, String topicName) {
		HealthTopic topic = null;
		Dao<HealthTopic, Integer> tDao;		
		DatabaseHelper tDbHelper = new DatabaseHelper(context);
		try {
			tDao = tDbHelper.getHealthTopicsDao();
			List<HealthTopic> tList = tDao.queryBuilder().where().eq("name",topicName).query();
			Iterator<HealthTopic> iter = tList.iterator();
			while (iter.hasNext()) {
				topic = iter.next();
			}
		} catch (SQLException e2) {
			Log.e("Topic does not exist in the DB: ", topicName);
			e2.printStackTrace();
		}
		
		return topic;
	}
	
	public static List<TopicVideo> getVideoIdsForPageVideo1Id(Context context, int vId) {
		List<TopicVideo> tvList = null;
		Dao<TopicVideo, Integer> tvDao;		
		DatabaseHelper tvDbHelper = new DatabaseHelper(context);
		try {
			tvDao = tvDbHelper.getTopicVideosDao();
			tvList = tvDao.queryBuilder().where().eq("page_video1_id",vId).query();
//			Iterator<TopicVideo> iter = tvList.iterator();
//			while (iter.hasNext()) {
//				video = iter.next();
//			}
		} catch (SQLException e2) {
			Log.e("Video does not exist in the DB: ", "");
			e2.printStackTrace();
		}
		
		return tvList;
	}
	
	public static Video getVideoForId(Context context, int vId) {
		Video video = null;
		Dao<Video, Integer> vDao;		
		DatabaseHelper vDbHelper = new DatabaseHelper(context);
		try {
			vDao = vDbHelper.getVideosDao();
			List<Video> vList = vDao.queryBuilder().where().eq("id",vId).query();
			Iterator<Video> iter = vList.iterator();
			while (iter.hasNext()) {
				video = iter.next();
			}
		} catch (SQLException e2) {
			Log.e("Video does not exist in the DB: ", "");
			e2.printStackTrace();
		}
		
		return video;
	}
	
	public static PageText1 getPageText1ForId(Context context, int pageContentId) {
		Dao<PageText1, Integer> ptDao;		
		DatabaseHelper ptDbHelper = new DatabaseHelper(context);
		PageText1 pt = null;
		try {
			ptDao = ptDbHelper.getPageText1Dao();
			List<PageText1> ptList = ptDao.queryBuilder().where().eq("id",pageContentId).query();
			Iterator<PageText1> iter = ptList.iterator();
			while (iter.hasNext()) {
				pt = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return pt;
	}
	
	public static PageSelect1 getPageSelect1ForId(Context context, int pageContentId) {
		Dao<PageSelect1, Integer> psDao;		
		DatabaseHelper psDbHelper = new DatabaseHelper(context);
		PageSelect1 ps = null;
		try {
			psDao = psDbHelper.getPageSelect1Dao();
			List<PageSelect1> psList = psDao.queryBuilder().where().eq("id",pageContentId).query();
			Iterator<PageSelect1> iter = psList.iterator();
			while (iter.hasNext()) {
				ps = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return ps;
	}
	
	public static PageVideo1 getPageVideo1ForId(Context context, int pageContentId) {
		Dao<PageVideo1, Integer> pvDao;		
		DatabaseHelper pvDbHelper = new DatabaseHelper(context);
		PageVideo1 pv = null;
		try {
			pvDao = pvDbHelper.getPageVideo1Dao();
			List<PageVideo1> pvList = pvDao.queryBuilder().where().eq("id",pageContentId).query();
			Iterator<PageVideo1> iter = pvList.iterator();
			while (iter.hasNext()) {
				pv = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return pv;
	}
	
}
