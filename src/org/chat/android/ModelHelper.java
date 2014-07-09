package org.chat.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.chat.android.models.Attendance;
import org.chat.android.models.CHAAccessed;
import org.chat.android.models.Client;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthSelectRecorded;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.HealthTopic;
import org.chat.android.models.HealthTopicAccessed;
import org.chat.android.models.Household;
import org.chat.android.models.PageAssessment1;
import org.chat.android.models.PageSelect1;
import org.chat.android.models.PageText1;
import org.chat.android.models.PageVideo1;
import org.chat.android.models.Service;
import org.chat.android.models.ServiceAccessed;
import org.chat.android.models.TopicVideo;
import org.chat.android.models.Util;
import org.chat.android.models.Vaccine;
import org.chat.android.models.VaccineRecorded;
import org.chat.android.models.Video;
import org.chat.android.models.Visit;
import org.chat.android.models.Worker;

import android.content.Context;
import android.provider.Settings.Secure;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class ModelHelper {
	static String lang = Locale.getDefault().getLanguage();
	
	public static Date getLastSyncedAt(Context context, String direction) {
		Util u = null;
		Dao<Util, Integer> uDao;		
		DatabaseHelper uDbHelper = new DatabaseHelper(context);
		try {
			uDao = uDbHelper.getUtilDao();
			List<Util> uList = uDao.queryBuilder().query();
			Iterator<Util> iter = uList.iterator();
			while (iter.hasNext()) {
				u = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		if (direction == "pull") {
			return u.getLastPulledAt();
		} else if (direction == "push") {
			return u.getLastPushedAt();
		} else {
			return null;
		}
	}
	
	public static void setLastSyncedAt(Context context, Date d, String direction) throws SQLException {
		Util u = null;
		Dao<Util, Integer> uDao;
		DatabaseHelper uDbHelper = new DatabaseHelper(context);
		uDao = uDbHelper.getUtilDao();
		
		if (direction == "pull") {
			u = new Util(1, null, d);
		} else if (direction == "push") {
			u = new Util(1, d, null);
		}
		uDao.createOrUpdate(u);
	}
	
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
	
	public static Worker getWorkerForId(Context context, int workerId) {
		Worker worker = null;
		Dao<Worker, Integer> wDao;		
		DatabaseHelper wDbHelper = new DatabaseHelper(context);
		try {
			wDao = wDbHelper.getWorkersDao();
			worker = wDao.queryForId(workerId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return worker;
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
	
	public static Worker getWorkerForUsername(Context context, String username) {
		Worker worker = null;
		Dao<Worker, Integer> wDao;		
		DatabaseHelper wDbHelper = new DatabaseHelper(context);
		try {
			wDao = wDbHelper.getWorkersDao();
			List<Worker> wList = wDao.queryBuilder().where().eq("username",username).query();
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

	public static Household getHouseholdForId(Context context, int hhId) {
		Household household = null;
		Dao<Household, Integer> hDao;		
		DatabaseHelper hDbHelper = new DatabaseHelper(context);
		try {
			hDao = hDbHelper.getHouseholdsDao();
			household = hDao.queryForId(hhId);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return household;
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
	
	public static List<Household> getHouseholdsForWorkerId(Context context, int workerId) {
		List<Household> hList = null;
		Dao<Household, Integer> hDao;		
		DatabaseHelper hDbHelper = new DatabaseHelper(context);
		try {
			hDao = hDbHelper.getHouseholdsDao();
			hList = hDao.queryBuilder().where().eq("worker_id",workerId).query();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return hList;
	}
	
	public static List<Household> getAllHouseholds(Context context) {
		List<Household> hList = null;
		Dao<Household, Integer> hDao;		
		DatabaseHelper hDbHelper = new DatabaseHelper(context);
		try {
			hDao = hDbHelper.getHouseholdsDao();
			hList = hDao.queryBuilder().query();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return hList;
	}
	
	public static Client getClientForId(Context context, int clientId) {
		Client client = null;
		Dao<Client, Integer> cDao;		
		DatabaseHelper cDbHelper = new DatabaseHelper(context);
		try {
			cDao = cDbHelper.getClientsDao();
			List<Client> cList = cDao.queryBuilder().where().eq("id",clientId).query();
			Iterator<Client> iter = cList.iterator();
			while (iter.hasNext()) {
				client = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return client;
	}
	
	public static List<Client> getClientsForHousehold(Context context, int hhId) {
		List<Client> hhCList = new ArrayList<Client>();
		Dao<Client, Integer> cDao;		
		DatabaseHelper cDbHelper = new DatabaseHelper(context);
		try {
			cDao = cDbHelper.getClientsDao();
			hhCList = cDao.queryBuilder().where().eq("hh_id",hhId).query();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return hhCList;
	}
	
	public static List<Client> getAttendingClientsForVisitIdUnderAge(Context context, int visitId, int age) {
		List<Client> cList = new ArrayList<Client>();
		
    	// create the list of attending Clients
    	List<Integer> presentHHMembers = new ArrayList<Integer>();
        Dao<Attendance, Integer> aDao;
        List<Attendance> allAttendees = new ArrayList<Attendance>();
        DatabaseHelper attDbHelper = new DatabaseHelper(context);
        try {
			aDao = attDbHelper.getAttendanceDao();
			allAttendees = aDao.query(aDao.queryBuilder().prepare());
        	for (Attendance a : allAttendees) {
    			if (a.getVisitId() == visitId) {
    				presentHHMembers.add(a.getClientId());
    			}
        	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        Dao<Client, Integer> cDao;
        DatabaseHelper cDbHelper = new DatabaseHelper(context);
        try {
        	cDao = cDbHelper.getClientsDao();
        	for (Integer i : presentHHMembers) {
        		// get client for the id
        		Client c = cDao.queryForId(i);
        		if (c.getAge() <= age) {
    				cList.add(c);
    			}
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        return cList;
	}
	
	public static List<Attendance> getAttendanceForVisitId(final Context context, int visitId) {
		List<Attendance> aList = null;
		Dao<Attendance, Integer> aDao;		
		DatabaseHelper aDbHelper = new DatabaseHelper(context);
		try {
			aDao = aDbHelper.getAttendanceDao();
			aList = aDao.queryBuilder().where().eq("visit_id",visitId).query();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// sort the list by age TODO: FINISH ME
//		Collections.sort(aList, new Comparator<Attendance>() {
//			public double compare(Attendance a1, Attendance a2) {
//				double age1 = ModelHelper.getClientForId(context, a1.getClientId()).getAge();
//				double age2 = ModelHelper.getClientForId(context, a2.getClientId()).getAge();
//				return Double.compare(age1, age2);
//			}
//		});
		
		return aList;
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
        		// service names that are not displayed will always be in english (right?)
    			if (s.getName(lang).equals(sName)) {
    				service = s;
    			}
        	}
		} catch (SQLException e) {
			Log.e("Unable to locate service id from name:", sName);
			e.printStackTrace();
		}
        return service;
	}
	
	public static List<HealthTheme> getHealthThemes(Context context) {
		List<HealthTheme> themeList = null;
		Dao<HealthTheme, Integer> tDao;		
		DatabaseHelper tDbHelper = new DatabaseHelper(context);
		try {
			tDao = tDbHelper.getHealthThemesDao();
			themeList = tDao.queryBuilder().query();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return themeList;
	}
	
	public static HealthTheme getThemeForName(Context context, String themeName) {
		HealthTheme theme = null;
		Dao<HealthTheme, Integer> tDao;		
		DatabaseHelper tDbHelper = new DatabaseHelper(context);
		try {
			tDao = tDbHelper.getHealthThemesDao();
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
	
	public static List<HealthSelect> getSelectsForSubjectId(Context context, int subjectId) {
		List<HealthSelect> selectList = null;
		Dao<HealthSelect, Integer> hsDao;		
		DatabaseHelper hsDbHelper = new DatabaseHelper(context);
		try {
			hsDao = hsDbHelper.getHealthSelectsDao();
			QueryBuilder<HealthSelect, Integer> qb = hsDao.queryBuilder();
			qb.where().eq("subject_id",subjectId);
			selectList = qb.orderBy("id", true).query();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return selectList;
	}
	
	public static List<HealthTopic> getTopicsForThemeName(Context context, String themeName) {
		List<HealthTopic> topicList = null;
		Dao<HealthTopic, Integer> tDao;		
		DatabaseHelper tDbHelper = new DatabaseHelper(context);
		try {
			tDao = tDbHelper.getHealthTopicsDao();
			topicList = tDao.queryBuilder().where().eq("theme",themeName).query();
		} catch (SQLException e2) {
			Log.e("Topic does not exist in the DB: ", themeName);
			e2.printStackTrace();
		}
		
		return topicList;
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
	
	public static HealthSelect getHealthSelectForId(Context context, int id) {
		HealthSelect hs = null;
		Dao<HealthSelect, Integer> hsDao;		
		DatabaseHelper hsDbHelper = new DatabaseHelper(context);
		try {
			hsDao = hsDbHelper.getHealthSelectsDao();
			List<HealthSelect> selectList = hsDao.queryBuilder().where().eq("id",id).query();
			Iterator<HealthSelect> iter = selectList.iterator();
			while (iter.hasNext()) {
				hs = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return hs;
	}
	
	public static HealthSelectRecorded getHealthSelectRecordedForVisitIdAndTopicName(Context context, int visitId, String topicName) {
		Dao<HealthSelectRecorded, Integer> hsrDao;		
		DatabaseHelper hsrDbHelper = new DatabaseHelper(context);
		HealthSelectRecorded hsr = null;
		try {
			hsrDao = hsrDbHelper.getHealthSelectRecordedDao();
			List<HealthSelectRecorded> hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("topic",topicName).query();
			Iterator<HealthSelectRecorded> iter = hsrList.iterator();
			while (iter.hasNext()) {
				hsr = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return hsr;
	}
	
	public static List<HealthSelectRecorded> getHealthSelectRecordedsForVisitIdAndTopicNameAndClientId(Context context, int visitId, String topicName, int clientId) {
		Dao<HealthSelectRecorded, Integer> hsrDao;		
		DatabaseHelper hsrDbHelper = new DatabaseHelper(context);
		List<HealthSelectRecorded> hsrList = null;
		try {
			hsrDao = hsrDbHelper.getHealthSelectRecordedDao();
			hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("topic",topicName).and().eq("client_id",clientId).query();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return hsrList;
	}
	
	public static HealthSelectRecorded getHealthSelectRecordedsForVisitIdAndTopicNameAndSelectIdAndClientId(Context context, int visitId, String topicName, int selectId, int clientId) {
		Dao<HealthSelectRecorded, Integer> hsrDao;		
		DatabaseHelper hsrDbHelper = new DatabaseHelper(context);
		HealthSelectRecorded hsr = null;
		try {
			hsrDao = hsrDbHelper.getHealthSelectRecordedDao();
			List<HealthSelectRecorded> hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("topic",topicName).and().eq("select_id",selectId).and().eq("client_id",clientId).query();
			Iterator<HealthSelectRecorded> iter = hsrList.iterator();
			while (iter.hasNext()) {
				hsr = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return hsr;
	}
	
	public static List<ServiceAccessed> getServicesAccessedForVisitId(Context context, int visitId) {
		List<ServiceAccessed> saList = null;
		Dao<ServiceAccessed, Integer> saDao;		
		DatabaseHelper saDbHelper = new DatabaseHelper(context);
		try {
			saDao = saDbHelper.getServiceAccessedDao();
			saList = saDao.queryBuilder().where().eq("visit_id",visitId).query();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		return saList;		
	}
	
	public static List<HealthTopicAccessed> getHealthTopicsAccessedForVisitId(Context context, int visitId) {
		List<HealthTopicAccessed> htaList = null;
		Dao<HealthTopicAccessed, Integer> htaDao;		
		DatabaseHelper htaDbHelper = new DatabaseHelper(context);
		try {
			htaDao = htaDbHelper.getHealthTopicAccessedDao();
			htaList = htaDao.queryBuilder().where().eq("visit_id",visitId).query();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
		return htaList;		
	}
	
	public static CHAAccessed getCHAAccessedForVisitIdAndClientIdAndType(Context context, int visitId, int clientId, String type) {
		Dao<CHAAccessed, Integer> chaaDao;		
		DatabaseHelper chaaDbHelper = new DatabaseHelper(context);
		CHAAccessed chaa = null;
		try {
			chaaDao = chaaDbHelper.getCHAAccessedDao();
			List<CHAAccessed> chaaList = chaaDao.queryBuilder().where().eq("visit_id",visitId).and().eq("client_id",clientId).and().eq("type",type).query();
			Iterator<CHAAccessed> iter = chaaList.iterator();
			while (iter.hasNext()) {
				chaa = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return chaa;
	}
	
	public static Boolean getCHAAccessedCompleteForVisitIdAndClientIdAndType(Context context, int visitId, int clientId, String type) {
		List<CHAAccessed> chaaList = null;
		Dao<CHAAccessed, Integer> chaaDao;		
		DatabaseHelper chaaDbHelper = new DatabaseHelper(context);
		try {
			chaaDao = chaaDbHelper.getCHAAccessedDao();
			chaaList = chaaDao.queryBuilder().where().eq("visit_id",visitId).and().eq("client_id",clientId).and().eq("type",type).query();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		Boolean completeFlag = false;
		for (CHAAccessed chaa : chaaList) {
			if (chaa.getEndTime() != null) {
				completeFlag = true;
			}
		}
		
		return completeFlag;
	}
	
	public static void setCHAAccessed(Context context, CHAAccessed chaa) {
	    Dao<CHAAccessed, Integer> chaaDao;
	    DatabaseHelper chaaDbHelper = new DatabaseHelper(context);
	    try {
	    	chaaDao = chaaDbHelper.getCHAAccessedDao();
	    	chaaDao.update(chaa);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	}
	
	public static PageAssessment1 getPageAssessment1ForId(Context context, int id) {
		Dao<PageAssessment1, Integer> pa1Dao;		
		DatabaseHelper pa1DbHelper = new DatabaseHelper(context);
		PageAssessment1 pa1 = null;
		try {
			pa1Dao = pa1DbHelper.getPageAssessment1Dao();
			List<PageAssessment1> pa1List = pa1Dao.queryBuilder().where().eq("id",id).query();
			Iterator<PageAssessment1> iter = pa1List.iterator();
			while (iter.hasNext()) {
				pa1 = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return pa1;
	}
	
	public static List<Vaccine> getVaccinesForAge(Context context, double age) {
		List<Vaccine> vList = null;
		Dao<Vaccine, Integer> vDao;		
		DatabaseHelper vDbHelper = new DatabaseHelper(context);
		try {
			vDao = vDbHelper.getVaccinesDao();
			vList = vDao.queryBuilder().where().le("age",age).query();
		} catch (SQLException e2) {
			Log.e("Video does not exist in the DB: ", "");
			e2.printStackTrace();
		}
		
		return vList;		
	}
	
	public static VaccineRecorded getVaccineRecordedForClientIdAndVaccineId(Context context, int clientId, int vaccineId) {
		Dao<VaccineRecorded, Integer> vrDao;		
		DatabaseHelper vrDbHelper = new DatabaseHelper(context);
		VaccineRecorded vr = null;
		try {
			vrDao = vrDbHelper.getVaccineRecordedDao();
			List<VaccineRecorded> pvList = vrDao.queryBuilder().where().eq("client_id",clientId).and().eq("vaccine_id",vaccineId).query();
			Iterator<VaccineRecorded> iter = pvList.iterator();
			while (iter.hasNext()) {
				vr = iter.next();
			}
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		return vr;
	}
	
	public static Boolean getVaccineRecordedCompleteForClientId(Context context, int clientId) {
		Boolean missingVaccineFlag = false;
		
		Client c = getClientForId(context, clientId);
		List<Vaccine> vList = getVaccinesForAge(context, c.getAge());
		
		for (Vaccine vaccine : vList) {
			// will return null if it doesn't exist
			VaccineRecorded vr = getVaccineRecordedForClientIdAndVaccineId(context, clientId, vaccine.getId());
			if (vr == null) {
				missingVaccineFlag = true;
			}
		}
		
		if (missingVaccineFlag == true) {
			return false;
		} else {
			return true;
		}
	}
	
	
	/************************** SAVING **************************/
	public static void setVisitToDirtyAndSave(Context context, Visit v) {
		v.setDirty();
	    Dao<Visit, Integer> vDao;
	    DatabaseHelper vDbHelper = new DatabaseHelper(context);
	    try {
	    	vDao = vDbHelper.getVisitsDao();
	    	vDao.update(v);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
		
	}
	
	
	/*************************** OTHER ***************************/
	public static int generateId(Context context) {
    	String myUUID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);   
    	long seconds = System.currentTimeMillis() / 1000;
    	String secondsString = String.valueOf(seconds);
    	String idString = myUUID+secondsString;
    	int hashedIdString = idString.hashCode();
    	if (hashedIdString < 0) {
    		hashedIdString = hashedIdString * -1;
    	}
    	
    	Random randomNum = new Random();
    	int randomInt = randomNum.nextInt(99999);
    	int myId = hashedIdString + randomInt;
    	return myId;
    	// k, this is pretty over the top and silly, but functional for now
    	
    	
    	// feels odd to construct this all ourselves. An attempt to eliminating collisions while staying within int space
//    	String myUUID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
//    	int hashedUUID = myUUID.hashCode();
//    	if (hashedUUID < 0) {
//    		hashedUUID = hashedUUID * -1;
//    	}
//    	String hashedUUIDString = String.valueOf(hashedUUID);
//   
//    	long seconds = System.currentTimeMillis() / 1000;
//    	String secondsString = String.valueOf(seconds);
//    	
//    	String idString = hashedUUIDString+secondsString;
//    	int myId = Integer.parseInt(idString);     
    	
	}
	
	
}
