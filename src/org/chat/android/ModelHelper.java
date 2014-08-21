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

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;

public class ModelHelper {
	static String lang = Locale.getDefault().getLanguage();

	public static Date getLastSyncedAt(DatabaseHelper databaseHelper, String direction) {
		Util u = null;
		Dao<Util, Integer> uDao;
		try {
			uDao = databaseHelper.getUtilDao();
			List<Util> uList = uDao.queryBuilder().query();
			Iterator<Util> iter = uList.iterator();
			while (iter.hasNext()) {
				u = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
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

	public static void setLastSyncedAt(DatabaseHelper databaseHelper, Date d, String direction) throws SQLException {
		// retrieve Util dao
		Dao<Util, Integer> uDao = databaseHelper.getUtilDao();
		// used dao to retrieve util model with id =1 
		Util u = uDao.queryForId(1);

		if (direction == "pull") {
			u.setLastPulledAt(d);
		} else if (direction == "push") {
			u.setLastPushedAt(d);
		}
		uDao.createOrUpdate(u);
	}

	public static String getRecentUsername(DatabaseHelper databaseHelper) {
		String workerName = null;

		// grab the last visit
		Visit visit = null;
		Dao<Visit, Integer> vDao;
		try {
			// Yuck. There must be a much better way to do this
			vDao = databaseHelper.getVisitsDao();
			QueryBuilder<Visit, Integer> builder = vDao.queryBuilder();
			builder.limit(1);
			builder.orderBy("start_time", false);  // true for ascending, false for descending
			List<Visit> vList = vDao.query(builder.prepare());  // returns list of ten items
			Iterator<Visit> iter = vList.iterator();
			while (iter.hasNext()) {
				visit = iter.next();
			}

		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		// grab the worker from the last visit, if it exists
		if (visit != null) {
			Worker w = ModelHelper.getWorkerForId(databaseHelper, visit.getWorkerId());
			if (w != null) {
				workerName = w.getUsername();
			}
			
			return workerName;
		} else {
			return null;
		}
	}

	public static Visit getVisitForId(DatabaseHelper databaseHelper, int visitId) {
		Visit visit = null;
		Dao<Visit, Integer> vDao;
		try {
			vDao = databaseHelper.getVisitsDao();
			List<Visit> vList = vDao.queryBuilder().where().eq("id",visitId).query();
			Iterator<Visit> iter = vList.iterator();
			while (iter.hasNext()) {
				visit = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return visit;
	}

	public static Worker getWorkerForId(DatabaseHelper databaseHelper, int workerId) {
		Worker worker = null;
		try {
			Dao<Worker, Integer> wDao = databaseHelper.getWorkersDao();
			worker = wDao.queryForId(workerId);
		} catch (SQLException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		return worker;
	}

	public static Worker getWorkerForName(DatabaseHelper databaseHelper, String workerName) {
		Worker worker = null;
		try {
			Dao<Worker, Integer> wDao = databaseHelper.getWorkersDao();
			List<Worker> wList = wDao.queryBuilder().where().eq("first_name",workerName).query();
			Iterator<Worker> iter = wList.iterator();
			while (iter.hasNext()) {
				worker = iter.next();
			}
		} catch (SQLException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		return worker;
	}

	public static Worker getWorkerForUsername(DatabaseHelper databaseHelper, String username) {
		Worker worker = null;
		try {
			Dao<Worker, Integer> wDao = databaseHelper.getWorkersDao();
			List<Worker> wList = wDao.queryBuilder().where().eq("username",username).query();
			Iterator<Worker> iter = wList.iterator();
			while (iter.hasNext()) {
				worker = iter.next();
			}
		} catch (SQLException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		return worker;
	}

	public static Household getHouseholdForId(DatabaseHelper databaseHelper, int hhId) {
		Household household = null;
		try {
			Dao<Household, Integer> hDao = databaseHelper.getHouseholdsDao();
			household = hDao.queryForId(hhId);
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return household;
	}


	public static Household getHouseholdForName(DatabaseHelper databaseHelper, String hhName) {
		Household household = null;
		try {
			Dao<Household, Integer> hDao = databaseHelper.getHouseholdsDao();
			List<Household> hList = hDao.queryBuilder().where().eq("hh_name",hhName).query();
			Iterator<Household> iter = hList.iterator();
			while (iter.hasNext()) {
				household = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return household;
	}

	public static List<Household> getHouseholdsForWorkerId(DatabaseHelper databaseHelper, int workerId) {
		List<Household> hList = null;
		try {
			Dao<Household, Integer> hDao = databaseHelper.getHouseholdsDao();
			hList = hDao.queryBuilder().where().eq("worker_id",workerId).query();
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return hList;
	}

	public static List<Household> getAllHouseholds(DatabaseHelper databaseHelper) {
		List<Household> hList = null;
		Dao<Household, Integer> hDao;
		try {
			hDao = databaseHelper.getHouseholdsDao();
			hList = hDao.queryBuilder().query();
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return hList;
	}

	public static Client getClientForId(DatabaseHelper databaseHelper, int clientId) {
		Client client = null;
		Dao<Client, Integer> cDao;
		try {
			cDao = databaseHelper.getClientsDao();
			List<Client> cList = cDao.queryBuilder().where().eq("id",clientId).query();
			Iterator<Client> iter = cList.iterator();
			while (iter.hasNext()) {
				client = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return client;
	}

	public static List<Client> getClientsForHousehold(DatabaseHelper databaseHelper, int hhId) {
		List<Client> hhCList = new ArrayList<Client>();
		Dao<Client, Integer> cDao;
		try {
			cDao = databaseHelper.getClientsDao();
			hhCList = cDao.queryBuilder().orderBy("date_of_birth",true).where().eq("hh_id",hhId).query();
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return hhCList;
	}

	public static List<Client> getAttendingClientsForVisitIdUnderAge(DatabaseHelper databaseHelper, int visitId, int age) {
		List<Client> cList = new ArrayList<Client>();

    	// create the list of attending Clients
    	List<Integer> presentHHMembers = new ArrayList<Integer>();
        Dao<Attendance, Integer> aDao;
        List<Attendance> allAttendees = new ArrayList<Attendance>();
        try {
			aDao = databaseHelper.getAttendanceDao();
			allAttendees = aDao.query(aDao.queryBuilder().prepare());
        	for (Attendance a : allAttendees) {
    			if (a.getVisitId() == visitId) {
    				presentHHMembers.add(a.getClientId());
    			}
        	}
		} catch (SQLException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

        Dao<Client, Integer> cDao;
        try {
        	cDao = databaseHelper.getClientsDao();
        	for (Integer i : presentHHMembers) {
        		// get client for the id
        		Client c = cDao.queryForId(i);
        		if (c.getAge() <= age) {
    				cList.add(c);
    			}
    		}
		} catch (SQLException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

        // TODO: this is a nice to have

//        Collections.sort(cList, new Comparator<Client>(){
//            public int compare(Client c1, Client c2) {
//                return c1.getAge().compareTo(c2.getAge());
//            }
//        });


        return cList;
	}

	public static List<Attendance> getAttendanceForVisitId(final DatabaseHelper databaseHelper, int visitId) {
		List<Attendance> aList = null;
		Dao<Attendance, Integer> aDao;
		try {
			aDao = databaseHelper.getAttendanceDao();
			aList = aDao.queryBuilder().where().eq("visit_id",visitId).query();
		} catch (SQLException e2) {
			// Auto-generated catch block
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

	public static Service getServiceForName(DatabaseHelper databaseHelper, String sName, String language) {
    	Service service = null;
        Dao<Service, Integer> sDao;
        List<Service> allServices = new ArrayList<Service>();
        try {
			sDao = databaseHelper.getServicesDao();
			allServices = sDao.query(sDao.queryBuilder().prepare());
        	for (Service s : allServices) {
        		// service names that are not displayed will always be in english (right?)
    			if (s.getName(language).equals(sName)) {
    				service = s;
    			}
        	}
		} catch (SQLException e) {
			Log.e("Unable to locate service id from name:", sName);
			e.printStackTrace();
		}
        return service;
	}

	public static List<HealthTheme> getHealthThemes(DatabaseHelper databaseHelper) {
		List<HealthTheme> themeList = null;
		Dao<HealthTheme, Integer> tDao;
		try {
			tDao = databaseHelper.getHealthThemesDao();
			themeList = tDao.queryBuilder().query();
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return themeList;
	}

	public static HealthTheme getThemeForName(DatabaseHelper databaseHelper, String themeName) {
		HealthTheme theme = null;
		Dao<HealthTheme, Integer> tDao;
		try {
			tDao = databaseHelper.getHealthThemesDao();
			List<HealthTheme> tList = tDao.queryBuilder().where().eq("name",themeName).query();
			Iterator<HealthTheme> iter = tList.iterator();
			while (iter.hasNext()) {
				theme = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return theme;
	}

	public static List<HealthSelect> getSelectsForSubjectId(DatabaseHelper databaseHelper, int subjectId) {
		List<HealthSelect> selectList = null;
		Dao<HealthSelect, Integer> hsDao;
		try {
			hsDao = databaseHelper.getHealthSelectsDao();
			QueryBuilder<HealthSelect, Integer> qb = hsDao.queryBuilder();
			qb.where().eq("subject_id",subjectId);
			selectList = qb.orderBy("id", true).query();
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return selectList;
	}

	public static List<HealthTopic> getTopicsForThemeName(DatabaseHelper databaseHelper, String themeName) {
		List<HealthTopic> topicList = null;
		Dao<HealthTopic, Integer> tDao;
		try {
			tDao = databaseHelper.getHealthTopicsDao();
			topicList = tDao.queryBuilder().where().eq("theme",themeName).query();
		} catch (SQLException e2) {
			Log.e("Topic does not exist in the DB: ", themeName);
			e2.printStackTrace();
		}

		return topicList;
	}

	public static HealthTopic getTopicForName(DatabaseHelper databaseHelper, String topicName) {
		HealthTopic topic = null;
		Dao<HealthTopic, Integer> tDao;
		try {
			tDao = databaseHelper.getHealthTopicsDao();
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

	public static List<TopicVideo> getVideoIdsForPageVideo1Id(DatabaseHelper databaseHelper, int vId) {
		List<TopicVideo> tvList = null;
		Dao<TopicVideo, Integer> tvDao;
		try {
			tvDao = databaseHelper.getTopicVideosDao();
			tvList = tvDao.queryBuilder().where().eq("page_video1_id",vId).query();
		} catch (SQLException e2) {
			Log.e("Video does not exist in the DB: ", "");
			e2.printStackTrace();
		}

		return tvList;
	}

	public static Video getVideoForId(DatabaseHelper databaseHelper, int vId) {
		Video video = null;
		Dao<Video, Integer> vDao;
		try {
			vDao = databaseHelper.getVideosDao();
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

	public static PageText1 getPageText1ForId(DatabaseHelper databaseHelper, int pageContentId) {
		Dao<PageText1, Integer> ptDao;
		PageText1 pt = null;
		try {
			ptDao = databaseHelper.getPageText1Dao();
			List<PageText1> ptList = ptDao.queryBuilder().where().eq("id",pageContentId).query();
			Iterator<PageText1> iter = ptList.iterator();
			while (iter.hasNext()) {
				pt = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return pt;
	}

	public static PageSelect1 getPageSelect1ForId(DatabaseHelper databaseHelper, int pageContentId) {
		Dao<PageSelect1, Integer> psDao;
		PageSelect1 ps = null;
		try {
			psDao = databaseHelper.getPageSelect1Dao();
			List<PageSelect1> psList = psDao.queryBuilder().where().eq("id",pageContentId).query();
			Iterator<PageSelect1> iter = psList.iterator();
			while (iter.hasNext()) {
				ps = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return ps;
	}

	public static PageVideo1 getPageVideo1ForId(DatabaseHelper databaseHelper, int pageContentId) {
		Dao<PageVideo1, Integer> pvDao;
		PageVideo1 pv = null;
		try {
			pvDao = databaseHelper.getPageVideo1Dao();
			List<PageVideo1> pvList = pvDao.queryBuilder().where().eq("id",pageContentId).query();
			Iterator<PageVideo1> iter = pvList.iterator();
			while (iter.hasNext()) {
				pv = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return pv;
	}

	public static HealthSelect getHealthSelectForId(DatabaseHelper databaseHelper, int id) {
		HealthSelect hs = null;
		Dao<HealthSelect, Integer> hsDao;
		try {
			hsDao = databaseHelper.getHealthSelectsDao();
			List<HealthSelect> selectList = hsDao.queryBuilder().where().eq("id",id).query();
			Iterator<HealthSelect> iter = selectList.iterator();
			while (iter.hasNext()) {
				hs = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return hs;
	}

	public static List<HealthSelect> getHealthSelectsForSubjectId(DatabaseHelper databaseHelper, int subjectId) {
		List<HealthSelect> sList = null;
		Dao<HealthSelect, Integer> hsDao;

		try {
			hsDao = databaseHelper.getHealthSelectsDao();
			sList = hsDao.queryBuilder().where().eq("subject_id",subjectId).query();
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return sList;
	}

	public static HealthSelectRecorded getHealthSelectRecordedForVisitIdAndTopicName(DatabaseHelper databaseHelper, int visitId, String topicName) {
		Dao<HealthSelectRecorded, Integer> hsrDao;
		HealthSelectRecorded hsr = null;
		try {
			hsrDao = databaseHelper.getHealthSelectRecordedDao();
			List<HealthSelectRecorded> hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("topic",topicName).query();
			Iterator<HealthSelectRecorded> iter = hsrList.iterator();
			while (iter.hasNext()) {
				hsr = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return hsr;
	}

	public static List<HealthSelectRecorded> getHealthSelectRecordedsForVisitIdAndTopicNameAndClientId(DatabaseHelper databaseHelper, int visitId, String topicName, int clientId) {
		Dao<HealthSelectRecorded, Integer> hsrDao;
		List<HealthSelectRecorded> hsrList = null;
		try {
			hsrDao = databaseHelper.getHealthSelectRecordedDao();
			hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("topic",topicName).and().eq("client_id",clientId).query();
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return hsrList;
	}

	public static HealthSelectRecorded getHealthSelectRecordedsForVisitIdAndTopicNameAndSelectIdAndClientId(DatabaseHelper databaseHelper, int visitId, String topicName, int selectId, int clientId) {
		Dao<HealthSelectRecorded, Integer> hsrDao;
		HealthSelectRecorded hsr = null;
		try {
			hsrDao = databaseHelper.getHealthSelectRecordedDao();
			List<HealthSelectRecorded> hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("topic",topicName).and().eq("select_id",selectId).and().eq("client_id",clientId).query();
			Iterator<HealthSelectRecorded> iter = hsrList.iterator();
			while (iter.hasNext()) {
				hsr = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return hsr;
	}

	public static List<String> getAllHealthSelectContentForVisitIdAndClientId(DatabaseHelper databaseHelper, int visitId, int clientId) {
		List<String> hsContentList = new ArrayList<String>();

		// get the health select recordeds for this visit and client
		List<HealthSelectRecorded> hsrList = null;
		Dao<HealthSelectRecorded, Integer> hsrDao;
		try {
			hsrDao = databaseHelper.getHealthSelectRecordedDao();
			hsrList = hsrDao.queryBuilder().where().eq("visit_id",visitId).and().eq("client_id",clientId).and().eq("topic","assessment").query();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// WARNING: this does not work correctly yet - it still always returns EnContent1s, which is obviously wrong in that it ignores Content2 and Content
		// FIXME

		// get the corresponding health selects
		Dao<HealthSelect, Integer> hsDao;
		Dao<PageAssessment1, Integer> pa1Dao;
		for (HealthSelectRecorded hsr : hsrList) {
			try {
				hsDao = databaseHelper.getHealthSelectsDao();
				HealthSelect hs = hsDao.queryForId(hsr.getSelectId());
				pa1Dao = databaseHelper.getPageAssessment1Dao();
				PageAssessment1 pa1 = pa1Dao.queryForId(hs.getSubjectId());
				hsContentList.add(pa1.getEnContent1());
				hsContentList.add(hs.getEnContent());
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

		return hsContentList;
	}

	public static List<ServiceAccessed> getServicesAccessedForVisitId(DatabaseHelper databaseHelper, int visitId) {
		List<ServiceAccessed> saList = null;
		Dao<ServiceAccessed, Integer> saDao;
		try {
			saDao = databaseHelper.getServiceAccessedDao();
			saList = saDao.queryBuilder().where().eq("visit_id",visitId).query();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		return saList;
	}

	public static List<HealthTopicAccessed> getHealthTopicsAccessedForVisitId(DatabaseHelper databaseHelper, int visitId) {
		List<HealthTopicAccessed> htaList = null;
		Dao<HealthTopicAccessed, Integer> htaDao;
		try {
			htaDao = databaseHelper.getHealthTopicAccessedDao();
			htaList = htaDao.queryBuilder().where().eq("visit_id",visitId).query();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		return htaList;
	}

	public static CHAAccessed getCHAAccessedForVisitIdAndClientIdAndType(DatabaseHelper databaseHelper, int visitId, int clientId, String type) {
		Dao<CHAAccessed, Integer> chaaDao;
		CHAAccessed chaa = null;
		try {
			chaaDao = databaseHelper.getCHAAccessedDao();
			List<CHAAccessed> chaaList = chaaDao.queryBuilder().where().eq("visit_id",visitId).and().eq("client_id",clientId).and().eq("type",type).query();
			Iterator<CHAAccessed> iter = chaaList.iterator();
			while (iter.hasNext()) {
				chaa = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return chaa;
	}

	public static Boolean getCHAAccessedCompleteForVisitIdAndClientIdAndType(DatabaseHelper databaseHelper, int visitId, int clientId, String type) {
		List<CHAAccessed> chaaList = null;
		Dao<CHAAccessed, Integer> chaaDao;
		try {
			chaaDao = databaseHelper.getCHAAccessedDao();
			chaaList = chaaDao.queryBuilder().where().eq("visit_id",visitId).and().eq("client_id",clientId).and().eq("type",type).query();
		} catch (SQLException e2) {
			// Auto-generated catch block
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

	public static void setCHAAccessed(DatabaseHelper databaseHelper, CHAAccessed chaa) {
	    Dao<CHAAccessed, Integer> chaaDao;
	    try {
	    	chaaDao = databaseHelper.getCHAAccessedDao();
	    	chaaDao.update(chaa);
	    } catch (SQLException e1) {
	        // Auto-generated catch block
	        e1.printStackTrace();
	    }
	}

	public static PageAssessment1 getPageAssessment1ForId(DatabaseHelper databaseHelper, int id) {
		Dao<PageAssessment1, Integer> pa1Dao;
		PageAssessment1 pa1 = null;
		try {
			pa1Dao = databaseHelper.getPageAssessment1Dao();
			List<PageAssessment1> pa1List = pa1Dao.queryBuilder().where().eq("id",id).query();
			Iterator<PageAssessment1> iter = pa1List.iterator();
			while (iter.hasNext()) {
				pa1 = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return pa1;
	}

	public static List<Vaccine> getVaccinesForAge(DatabaseHelper databaseHelper, double age) {
		List<Vaccine> vList = null;
		Dao<Vaccine, Integer> vDao;
		try {
			vDao = databaseHelper.getVaccinesDao();
			vList = vDao.queryBuilder().where().le("age",age).query();
		} catch (SQLException e2) {
			Log.e("Video does not exist in the DB: ", "");
			e2.printStackTrace();
		}

		return vList;
	}

	public static VaccineRecorded getVaccineRecordedForClientIdAndVaccineId(DatabaseHelper databaseHelper, int clientId, int vaccineId) {
		Dao<VaccineRecorded, Integer> vrDao;
		VaccineRecorded vr = null;
		try {
			vrDao = databaseHelper.getVaccineRecordedDao();
			List<VaccineRecorded> pvList = vrDao.queryBuilder().where().eq("client_id",clientId).and().eq("vaccine_id",vaccineId).query();
			Iterator<VaccineRecorded> iter = pvList.iterator();
			while (iter.hasNext()) {
				vr = iter.next();
			}
		} catch (SQLException e2) {
			// Auto-generated catch block
			e2.printStackTrace();
		}

		return vr;
	}

	public static Boolean getVaccineRecordedCompleteForClientId(DatabaseHelper databaseHelper, int clientId) {
		Boolean missingVaccineFlag = false;

		Client c = getClientForId(databaseHelper, clientId);
		List<Vaccine> vList = getVaccinesForAge(databaseHelper, c.getAge());

		for (Vaccine vaccine : vList) {
			// will return null if it doesn't exist
			VaccineRecorded vr = getVaccineRecordedForClientIdAndVaccineId(databaseHelper, clientId, vaccine.getId());
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
	public static void setVisitToDirtyAndSave(DatabaseHelper databaseHelper, Visit v) {
		v.setDirty();
	    Dao<Visit, Integer> vDao;
	    try {
	    	vDao = databaseHelper.getVisitsDao();
	    	vDao.update(v);
	    } catch (SQLException e1) {
	        // Auto-generated catch block
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
