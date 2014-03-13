package org.chat.android;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.chat.android.models.Client;
import org.chat.android.models.HealthSelect;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.Household;
import org.chat.android.models.PageAssessment1;
import org.chat.android.models.PageSelect1;
import org.chat.android.models.PageText1;
import org.chat.android.models.PageVideo1;
import org.chat.android.models.Resource;
import org.chat.android.models.Role;
import org.chat.android.models.Service;
import org.chat.android.models.TopicVideo;
import org.chat.android.models.Util;
import org.chat.android.models.Vaccine;
import org.chat.android.models.Video;
import org.chat.android.models.HealthTopic;
import org.chat.android.models.HealthPage;
import org.chat.android.models.Worker;

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
        
        // UTIL - TODO: think about where to eventually move this, since it needs to fire on empty DB or something
        Date d = new Date();
        Util u1 = new Util(d, d);
        Dao<Util, Integer> utilDao;
        DatabaseHelper utilDbHelper = new DatabaseHelper(context);
        try {
        	utilDao = utilDbHelper.getUtilDao();
        	utilDao.create(u1);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
        
        // CLIENTS
//		int hhId = 1;			// for John Doe household
//		//Date date1 = new Date(1980, 12, 29);		// TODO: switch Date objects out in all cases - it's a goddamned mess
//		//Calendar date1 = new GregorianCalendar(1980,12,29);
//		Date date1 = new Date(93, 11, 11);
//		Client c1 = new Client(1, "John", "Doe", hhId, "male", date1);
//		Client c2 = new Client(2, "Jane", "Jacobs", hhId, "female", date1);
//		Date date2 = new Date(113, 11, 5);
//		Client c3 = new Client(3, "Davey", "Jones", hhId, "male", date2);
//		
////		GregorianCalendar dob = new GregorianCalendar(1980, 12, 29);
////		Client c1 = new Client(1, "John", "Doe", hhId, "male", dob);
////		Client c2 = new Client(2, "Jane", "Jacobs", hhId, "female", dob);
////		Client c3 = new Client(3, "Davey", "Jones", hhId, "male", dob);
//		
//	    Dao<Client, Integer> clientDao1;
//	    Dao<Client, Integer> clientDao2;
//	    Dao<Client, Integer> clientDao3;
//	    DatabaseHelper clientDbHelper = new DatabaseHelper(context);
//	    try {
//	        clientDao1 = clientDbHelper.getClientsDao();
//	        clientDao1.create(c1);
//	        clientDao2 = clientDbHelper.getClientsDao();
//	        clientDao2.create(c2);
//	        clientDao3 = clientDbHelper.getClientsDao();
//	        clientDao3.create(c3);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }
//	    
//	    
//	    // HOUSEHOLDS
//	    Household h1 = new Household(1, "John Doe", "snathing", 1000);
//	    Dao<Household, Integer> hDao1;
//	    DatabaseHelper householdDbHelper = new DatabaseHelper(context);
//	    try {
//	        hDao1 = householdDbHelper.getHouseholdsDao();
//	        hDao1.create(h1);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }
	    
	    
	    // ROLES
//	    Role r1 = new Role(1, "volunteer");
//	    Role r2 = new Role(2, "councelor");
//	    Dao<Role, Integer> rDao1;
//	    Dao<Role, Integer> rDao2;
//	    DatabaseHelper roleDbHelper = new DatabaseHelper(context);
//	    try {
//	        rDao1 = roleDbHelper.getRolesDao();
//	        rDao1.create(r1);
//	        rDao2 = roleDbHelper.getRolesDao();
//	        rDao2.create(r2);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }
	    
	    
	    // SERVICES
//	    String type = "Material Well Being";
//	    String role = "Home Care Volunteer";
//	    Service s1 = new Service(1, "B1 Household (Re)Assessment - Checklist (Staff Only)", type, role, null);
//	    Service s2 = new Service(2, "B2 Emergency Food parcel/voucher provision", type, role, null);
//	    Service s3 = new Service(3, "B3 Household Equipment provision", type, role, null);
//	    Service s4 = new Service(4, "B4 Clothing distribution", type, role, null);
//	    Service s5 = new Service(5, "B5 Blanket / Bedding Distribution", type, role, null);
//	    Service s6 = new Service(6, "B6 Household Maintenance", type, role, null);
//	    Service s7 = new Service(7, "B7 Monitoring - Grant Usage", type, role, null);
//	    type = "Cognitive Well Being";
//	    Service s8 = new Service(8, "C1 Advise/Assistance in applying for a School Fee Exemption", type, role, null);
//	    Service s9 = new Service(9, "C2 School Uniform Provision", type, role, null);
//	    Service s10 = new Service(10, "C3 School Stationary Provision", type, role, null);
//	    Service s11 = new Service(11, "C4 Monitoring - School Attendance and Performance (School Visits Only)", type, role, null);
//
//	    Dao<Service, Integer> sDao1;
//	    Dao<Service, Integer> sDao2;
//	    Dao<Service, Integer> sDao3;
//	    Dao<Service, Integer> sDao4;
//	    Dao<Service, Integer> sDao5;
//	    Dao<Service, Integer> sDao6;
//	    Dao<Service, Integer> sDao7;
//	    Dao<Service, Integer> sDao8;
//	    Dao<Service, Integer> sDao9;
//	    Dao<Service, Integer> sDao10;
//	    Dao<Service, Integer> sDao11;
//
//	    DatabaseHelper serviceDbHelper = new DatabaseHelper(context);
//	    try {
//	        sDao1 = serviceDbHelper.getServicesDao();
//	        sDao1.create(s1);
//	        sDao2 = serviceDbHelper.getServicesDao();
//	        sDao2.create(s2);
//	        sDao3 = serviceDbHelper.getServicesDao();
//	        sDao3.create(s3);
//	        sDao4 = serviceDbHelper.getServicesDao();
//	        sDao4.create(s4);
//	        sDao5 = serviceDbHelper.getServicesDao();
//	        sDao5.create(s5);
//	        sDao6 = serviceDbHelper.getServicesDao();
//	        sDao6.create(s6);
//	        sDao7 = serviceDbHelper.getServicesDao();
//	        sDao7.create(s7);
//	        sDao8 = serviceDbHelper.getServicesDao();
//	        sDao8.create(s8);
//	        sDao9 = serviceDbHelper.getServicesDao();
//	        sDao9.create(s9);
//	        sDao10 = serviceDbHelper.getServicesDao();
//	        sDao10.create(s10);
//	        sDao11 = serviceDbHelper.getServicesDao();
//	        sDao11.create(s11);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }
	    
	    
//	    // WORKERS
//	    Worker w1 = new Worker(1, "colin", "mccann", "chat", "volunteer", "snathing");
//	    Dao<Worker, Integer> wDao1;
//	    DatabaseHelper workerDbHelper = new DatabaseHelper(context);
//	    try {
//	        wDao1 = workerDbHelper.getWorkersDao();
//	        wDao1.create(w1);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }
	    
	    
	    // VIDEOS
//	    Video vid1 = new Video("Psychosocial Support", "pss_animatic.mp4", "two_years_up_screenshot");
//	    Video vid2 = new Video("Nutrition", "nutrition_animatic.mp4", "two_years_up_screenshot");
//	    Video vid3 = new Video("Nutrition (0-9 months)", "nutrition_0-9_months.mp4", "one_to_nine_months_screenshot");
//	    Video vid4 = new Video("Nutrition (2+ years)", "nutrition_2_years_up.mp4", "one_to_nine_months_screenshot");
//	    Dao<Video, Integer> vidDao1;
//	    Dao<Video, Integer> vidDao2;
//	    Dao<Video, Integer> vidDao3;
//	    Dao<Video, Integer> vidDao4;
//	    DatabaseHelper videoDbHelper = new DatabaseHelper(context);
//	    try {
//	        vidDao1 = videoDbHelper.getVideosDao();
//	        vidDao1.create(vid1);
//	        vidDao2 = videoDbHelper.getVideosDao();
//	        vidDao2.create(vid2);
//	        vidDao3 = videoDbHelper.getVideosDao();
//	        vidDao3.create(vid3);
//	        vidDao4 = videoDbHelper.getVideosDao();
//	        vidDao4.create(vid4);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    } 

	    
	    // RESOURCES
//	    Resource res1 = new Resource("Infant feeding for newborns and infants up to 6 months", "0-6mos.pdf");
//	    Resource res2 = new Resource("Feeding for infants 6 months and older", "6-12mos.pdf");
//	    Dao<Resource, Integer> resDao1;
//	    Dao<Resource, Integer> resDao2;
//	    DatabaseHelper resDbHelper = new DatabaseHelper(context);
//	    try {
//	        resDao1 = resDbHelper.getResourcesDao();
//	        resDao1.create(res1);
//	        resDao2 = resDbHelper.getResourcesDao();
//	        resDao2.create(res2);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }
	    
	    
	    // HEALTH THEMES
//	    HealthTheme theme1 = new HealthTheme(1, "HIV", "Some HIV related observe content", "Some HIV related record content", "Zulu stuff", "Zulu stuff", "#a01718");
//	    HealthTheme theme2 = new HealthTheme(2, "Severe Childhood Illnesses", "Some Severe Childhood Illnesses related observe content", "Some Severe Childhood Illnesses related record content", "Zulu stuff", "Zulu stuff", "#ebbb28");
//	    HealthTheme theme3 = new HealthTheme(3, "Nutrition", "Some Nutrition related observe content", "Some Nutrition related record content", "Zulu stuff", "Zulu stuff", "#5b943d");
//	    HealthTheme theme4 = new HealthTheme(4, "Psychosocial Support", "Look for signs that the parent/carer spends time focusing on the child and its interests", "Does the child seem happy and active and willing to talk with home visitor?", "Zulu stuff", "Zulu stuff", "#4ec5c7");
//	    Dao<HealthTheme, Integer> themeDao1;
//	    Dao<HealthTheme, Integer> themeDao2;
//	    Dao<HealthTheme, Integer> themeDao3;
//	    Dao<HealthTheme, Integer> themeDao4;
//	    DatabaseHelper themeDbHelper = new DatabaseHelper(context);
//	    try {
//	    	themeDao1 = themeDbHelper.getHealthThemeDao();
//	        themeDao1.create(theme1);
//	        themeDao2 = themeDbHelper.getHealthThemeDao();
//	        themeDao2.create(theme2);
//	        themeDao3 = themeDbHelper.getHealthThemeDao();
//	        themeDao3.create(theme3);
//	        themeDao4 = themeDbHelper.getHealthThemeDao();
//	        themeDao4.create(theme4);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }
	    
	    
//	    // HEALTH SELECTS
//	    //HealthSelect(int id (auto-gen), int subject_id, String en_content, String zu_content)
//	    // THEME
//	    HealthSelect select4 = new HealthSelect(4, "GOOD  Child seems happy, hopeful, and content.", "Zulu stuff");
//	    HealthSelect select3 = new HealthSelect(4, "FAIR  Child is mostly happy but occasionally he/she is anxious, or withdrawn.", "Zulu stuff");
//	    HealthSelect select2 = new HealthSelect(4, "BAD  Child is often withdrawn, irritable, anxious, unhappy, or sad.", "Zulu stuff");
//	    HealthSelect select1 = new HealthSelect(4, "VERY BAD  Child seems hopeless, sad, withdrawn, wishes could die, or wants to be left alone.", "Zulu stuff");
//	    
//	    // HEALTH EDUCATION DELIVERY
//	    // generic HIV
//	    HealthSelect select5 = new HealthSelect(101, "VERY BAD  Child seems hopeless, sad, withdrawn, wishes could die, or wants to be left alone.", "Zulu stuff");
//	    HealthSelect select6 = new HealthSelect(101, "BAD  Child is often withdrawn, irritable, anxious, unhappy, or sad.", "Zulu stuff");
//	    HealthSelect select7 = new HealthSelect(101, "FAIR  Child is mostly happy but occasionally he/she is anxious, or withdrawn.", "Zulu stuff");
//	    HealthSelect select8 = new HealthSelect(101, "GOOD  Child seems happy, hopeful, and content.", "Zulu stuff");
//	    // Psychosocial (0-5)
//	    HealthSelect select9 = new HealthSelect(102, "GOOD  Child likes to play with peers and participates in group or family activities.", "Zulu stuff");
//	    HealthSelect select10 = new HealthSelect(102, "FAIR Child has minor problems getting along with others and argues or gets into fights sometimes.", "Zulu stuff");
//	    HealthSelect select11 = new HealthSelect(102, "BAD  Child is disobedient to adults and frequently does not interact well with peers, guardian, or others.", "Zulu stuff");
//	    HealthSelect select12 = new HealthSelect(102, "VERY BAD  Child has behavioural problems, including stealing, and/or other risky or disruptive behaviour.", "Zulu stuff");
//	    
//	    // CHA HEALTH DELIVERY
//	    HealthSelect select13 = new HealthSelect(1001, "Yes", "Zulu yes");
//	    HealthSelect select14 = new HealthSelect(1001, "No", "Zulu no");
//	    HealthSelect select15 = new HealthSelect(1001, "Greater than 21 days", "Zulu");
//	    HealthSelect select16 = new HealthSelect(1001, "Less than 21 days", "Zulu");
//	    HealthSelect select17 = new HealthSelect(1002, "Yes", "Zulu yes");
//	    HealthSelect select18 = new HealthSelect(1002, "No", "Zulu no");
//	    HealthSelect select19 = new HealthSelect(1002, "Greater than 14 days", "Zulu");
//	    HealthSelect select20 = new HealthSelect(1002, "Less than 14 days", "Zulu");
//	    HealthSelect select21 = new HealthSelect(1003, "Yes", "Zulu yes");
//	    HealthSelect select22 = new HealthSelect(1003, "No", "Zulu no");
//	    
//	    Dao<HealthSelect, Integer> selectDao1;
//	    Dao<HealthSelect, Integer> selectDao2;
//	    Dao<HealthSelect, Integer> selectDao3;
//	    Dao<HealthSelect, Integer> selectDao4;
//	    Dao<HealthSelect, Integer> selectDao5;
//	    Dao<HealthSelect, Integer> selectDao6;
//	    Dao<HealthSelect, Integer> selectDao7;
//	    Dao<HealthSelect, Integer> selectDao8;
//	    Dao<HealthSelect, Integer> selectDao9;
//	    Dao<HealthSelect, Integer> selectDao10;
//	    Dao<HealthSelect, Integer> selectDao11;
//	    Dao<HealthSelect, Integer> selectDao12;
//	    Dao<HealthSelect, Integer> selectDao13;
//	    Dao<HealthSelect, Integer> selectDao14;
//	    Dao<HealthSelect, Integer> selectDao15;
//	    Dao<HealthSelect, Integer> selectDao16;
//	    Dao<HealthSelect, Integer> selectDao17;
//	    Dao<HealthSelect, Integer> selectDao18;
//	    Dao<HealthSelect, Integer> selectDao19;
//	    Dao<HealthSelect, Integer> selectDao20;
//	    Dao<HealthSelect, Integer> selectDao21;
//	    Dao<HealthSelect, Integer> selectDao22;
//	    DatabaseHelper selectDbHelper = new DatabaseHelper(context);
//	    try {
//	    	selectDao1 = selectDbHelper.getHealthSelectDao();
//	    	selectDao1.create(select1);
//	    	selectDao2 = selectDbHelper.getHealthSelectDao();
//	    	selectDao2.create(select2);
//	    	selectDao3 = selectDbHelper.getHealthSelectDao();
//	    	selectDao3.create(select3);
//	    	selectDao4 = selectDbHelper.getHealthSelectDao();
//	    	selectDao4.create(select4);
//	    	selectDao5 = selectDbHelper.getHealthSelectDao();
//	    	selectDao5.create(select5);
//	    	selectDao6 = selectDbHelper.getHealthSelectDao();
//	    	selectDao6.create(select6);
//	    	selectDao7 = selectDbHelper.getHealthSelectDao();
//	    	selectDao7.create(select7);
//	    	selectDao8 = selectDbHelper.getHealthSelectDao();
//	    	selectDao8.create(select8);
//	    	selectDao9 = selectDbHelper.getHealthSelectDao();
//	    	selectDao9.create(select9);
//	    	selectDao10 = selectDbHelper.getHealthSelectDao();
//	    	selectDao10.create(select10);
//	    	selectDao11 = selectDbHelper.getHealthSelectDao();
//	    	selectDao11.create(select11);
//	    	selectDao12 = selectDbHelper.getHealthSelectDao();
//	    	selectDao12.create(select12);
//	    	selectDao13 = selectDbHelper.getHealthSelectDao();
//	    	selectDao13.create(select13);
//	    	selectDao14 = selectDbHelper.getHealthSelectDao();
//	    	selectDao14.create(select14);
//	    	selectDao15 = selectDbHelper.getHealthSelectDao();
//	    	selectDao15.create(select15);
//	    	selectDao16 = selectDbHelper.getHealthSelectDao();
//	    	selectDao16.create(select16);
//	    	selectDao17 = selectDbHelper.getHealthSelectDao();
//	    	selectDao17.create(select17);
//	    	selectDao18 = selectDbHelper.getHealthSelectDao();
//	    	selectDao18.create(select18);
//	    	selectDao19 = selectDbHelper.getHealthSelectDao();
//	    	selectDao19.create(select19);
//	    	selectDao20 = selectDbHelper.getHealthSelectDao();
//	    	selectDao20.create(select20);
//	    	selectDao21 = selectDbHelper.getHealthSelectDao();
//	    	selectDao21.create(select21);
//	    	selectDao22 = selectDbHelper.getHealthSelectDao();
//	    	selectDao22.create(select22);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }
	    
	    
	    // HEALTH TOPICS
	    //HealthTopic(int id, String name, String theme)
	    HealthTopic sub1 = new HealthTopic(1, "What is HIV?", "HIV");
	    HealthTopic sub2 = new HealthTopic(2, "CD4, Viral load", "HIV");
	    HealthTopic sub3 = new HealthTopic(3, "ARVs", "HIV");
	    HealthTopic sub4 = new HealthTopic(4, "Helping children to take their ARVs", "HIV");
	    HealthTopic sub5 = new HealthTopic(5, "General signs and illnesses", "disease");
	    HealthTopic sub6 = new HealthTopic(6, "Illness: new-born", "disease");
	    HealthTopic sub7 = new HealthTopic(7, "Respiratory: 0 to 6 months", "disease");
	    HealthTopic sub8 = new HealthTopic(8, "Respiratory 6+ months", "disease");
	    HealthTopic sub9 = new HealthTopic(9, "Diarrhoea", "disease");
	    HealthTopic sub10 = new HealthTopic(10, "Malnutrition", "disease");
	    HealthTopic sub11 = new HealthTopic(11, "General (0 to 5 years)", "development");
	    HealthTopic sub12 = new HealthTopic(12, "0 to 9 months", "development");
	    HealthTopic sub13 = new HealthTopic(13, "9 months to 2 years", "development");
	    HealthTopic sub14 = new HealthTopic(14, "2 to 5 years", "development");
	    Dao<HealthTopic, Integer> subDao1;
	    Dao<HealthTopic, Integer> subDao2;
	    Dao<HealthTopic, Integer> subDao3;
	    Dao<HealthTopic, Integer> subDao4;
	    Dao<HealthTopic, Integer> subDao5;
	    Dao<HealthTopic, Integer> subDao6;
	    Dao<HealthTopic, Integer> subDao7;
	    Dao<HealthTopic, Integer> subDao8;
	    Dao<HealthTopic, Integer> subDao9;
	    Dao<HealthTopic, Integer> subDao10;
	    Dao<HealthTopic, Integer> subDao11;
	    Dao<HealthTopic, Integer> subDao12;
	    Dao<HealthTopic, Integer> subDao13;
	    Dao<HealthTopic, Integer> subDao14;
	    
	    DatabaseHelper subDbHelper = new DatabaseHelper(context);
	    try {
	        subDao1 = subDbHelper.getHealthTopicsDao();
	        subDao1.create(sub1);
	        subDao2 = subDbHelper.getHealthTopicsDao();
	        subDao2.create(sub2);
	        subDao3 = subDbHelper.getHealthTopicsDao();
	        subDao3.create(sub3);
	        subDao4 = subDbHelper.getHealthTopicsDao();
	        subDao4.create(sub4);
	        subDao5 = subDbHelper.getHealthTopicsDao();
	        subDao5.create(sub5);
	        subDao6 = subDbHelper.getHealthTopicsDao();
	        subDao6.create(sub6);
	        subDao7 = subDbHelper.getHealthTopicsDao();
	        subDao7.create(sub7);
	        subDao8 = subDbHelper.getHealthTopicsDao();
	        subDao8.create(sub8);
	        subDao9 = subDbHelper.getHealthTopicsDao();
	        subDao9.create(sub9);
	        subDao10 = subDbHelper.getHealthTopicsDao();
	        subDao10.create(sub10);
	        subDao11 = subDbHelper.getHealthTopicsDao();
	        subDao11.create(sub11);
	        subDao12 = subDbHelper.getHealthTopicsDao();
	        subDao12.create(sub12);
	        subDao13 = subDbHelper.getHealthTopicsDao();
	        subDao13.create(sub13);
	        subDao14 = subDbHelper.getHealthTopicsDao();
	        subDao14.create(sub14);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }	    
	    
	    
	    // HEALTH PAGES
	    //HealthPage(int id, int topic_id, int page_number, String type, int page_content_id)
	    // all page_content_ids should be in the 100s, most likely
	    HealthPage page1 = new HealthPage(1, 1, "text1", 1);
	    HealthPage page2 = new HealthPage(1, 2, "select1", 101);
	    HealthPage page3 = new HealthPage(1, 3, "text1", 2);
	    HealthPage page4 = new HealthPage(1, 4, "text1", 2);
	    HealthPage page5 = new HealthPage(2, 1, "text1", 1);
	    HealthPage page6 = new HealthPage(3, 1, "text1", 1);
	    HealthPage page7 = new HealthPage(4, 1, "text1", 1);
	    HealthPage page8 = new HealthPage(4, 2, "select1", 101);
	    // Psychosocial (0-5)
	    HealthPage page9 = new HealthPage(11, 1, "text1", 3);
	    HealthPage page10 = new HealthPage(11, 2, "select1", 102);
	    HealthPage page11 = new HealthPage(11, 3, "video1", 1);
	    HealthPage page12 = new HealthPage(11, 4, "text1", 4);
	    
	    Dao<HealthPage, Integer> pageDao1;
	    Dao<HealthPage, Integer> pageDao2;
	    Dao<HealthPage, Integer> pageDao3;
	    Dao<HealthPage, Integer> pageDao4;
	    Dao<HealthPage, Integer> pageDao5;
	    Dao<HealthPage, Integer> pageDao6;
	    Dao<HealthPage, Integer> pageDao7;
	    Dao<HealthPage, Integer> pageDao8;
	    
	    Dao<HealthPage, Integer> pageDao9;
	    Dao<HealthPage, Integer> pageDao10;
	    Dao<HealthPage, Integer> pageDao11;
	    Dao<HealthPage, Integer> pageDao12;
	    
	    DatabaseHelper pageDbHelper = new DatabaseHelper(context);
	    try {
	        pageDao1 = pageDbHelper.getHealthPagesDao();
	        pageDao1.create(page1);
	        pageDao2 = pageDbHelper.getHealthPagesDao();
	        pageDao2.create(page2);
	        pageDao3 = pageDbHelper.getHealthPagesDao();
	        pageDao3.create(page3);
	        pageDao4 = pageDbHelper.getHealthPagesDao();
	        pageDao4.create(page4);
	        pageDao5 = pageDbHelper.getHealthPagesDao();
	        pageDao5.create(page5);
	        pageDao6 = pageDbHelper.getHealthPagesDao();
	        pageDao6.create(page6);
	        pageDao7 = pageDbHelper.getHealthPagesDao();
	        pageDao7.create(page7);
	        pageDao8 = pageDbHelper.getHealthPagesDao();
	        pageDao8.create(page8);
	        pageDao9 = pageDbHelper.getHealthPagesDao();
	        pageDao9.create(page9);
	        pageDao10 = pageDbHelper.getHealthPagesDao();
	        pageDao10.create(page10);
	        pageDao11 = pageDbHelper.getHealthPagesDao();
	        pageDao11.create(page11);
	        pageDao12 = pageDbHelper.getHealthPagesDao();
	        pageDao12.create(page12);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
	    
	    // HEALTH CONTENT PAGES
	    PageText1 pt1 = new PageText1(1, "¥ bla bla a bit of stuff\n¥ a second line\n¥ a third line", "some more stuff for the second paragraph", "now some zulu content", "para 2 zulu content");
	    PageText1 pt2 = new PageText1(2, "another page of text stuff, check it", "some more stuff for the second paragraph", "now some zulu content for the second text1 page", "para 2 zulu content");
	    // Psychosocial (0-5)
	    // Ask
	    PageText1 pt3 = new PageText1(3, "Ask", "Do you spend time with your child?\nWhat do you do with your child?\nHow does your child respond?\nDo you feel that this child needs to be punished often?\nIf age appropriate - Does the child play with other children or have close friends?\nDoes he/she enjoy being with other children?\nDoes he/she fight with other children?", "Ask", "now some zulu content");
	    // Discussion
	    PageText1 pt4 = new PageText1(4, "Discussion", "What important information have you learned?\nDo you agree with this?\nHave you ever experienced this?\nWhat does this mean for you in your life?\nWhat do you think you can do about it?\nAre there barriers that will make this difficult? What can help you to overcome them?", "Discussion", "some zulu content");

	    Dao<PageText1, Integer> ptDao1;
	    Dao<PageText1, Integer> ptDao2;
	    Dao<PageText1, Integer> ptDao3;
	    Dao<PageText1, Integer> ptDao4;
	    DatabaseHelper ptDbHelper = new DatabaseHelper(context);
	    try {
	        ptDao1 = ptDbHelper.getPageText1Dao();
	        ptDao1.create(pt1);
	        ptDao2 = ptDbHelper.getPageText1Dao();
	        ptDao2.create(pt2);
	        ptDao3 = ptDbHelper.getPageText1Dao();
	        ptDao3.create(pt3);
	        ptDao4 = ptDbHelper.getPageText1Dao();
	        ptDao4.create(pt4);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
	    PageSelect1 ps1 = new PageSelect1(101, "bla bla a bit of stuff", "this is a question in zulu!");
	    // Psychosocial (0-5)
	    PageSelect1 ps2 = new PageSelect1(102, "", "");
	    Dao<PageSelect1, Integer> psDao1;
	    Dao<PageSelect1, Integer> psDao2;
	    DatabaseHelper psDbHelper = new DatabaseHelper(context);
	    try {
	        psDao1 = psDbHelper.getPageSelect1Dao();
	        psDao1.create(ps1);
	        psDao2 = psDbHelper.getPageSelect1Dao();
	        psDao2.create(ps2);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
	    //PageVideo1(int id, String en_content1, String zu_content1)
	    // Psychosocial (0-5)
	    PageVideo1 pv1 = new PageVideo1(1, "* Responsiveness\n* Affirmation\n* Positive discipline\n* Short and long term effect of carer-child relationship", "some zulu stuff");
	    Dao<PageVideo1, Integer> pvDao1;
	    DatabaseHelper pvDbHelper = new DatabaseHelper(context);
	    try {
	        pvDao1 = pvDbHelper.getPageVideo1Dao();
	        pvDao1.create(pv1);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
	    //TopicVideo(int page_video1_id, int video_id) 
	    // Psychosocial (0-5)
	    TopicVideo tv1 = new TopicVideo(1, 1);
	    TopicVideo tv2 = new TopicVideo(1, 2);
	    TopicVideo tv3 = new TopicVideo(1, 3);
	    TopicVideo tv4 = new TopicVideo(1, 4);
	    Dao<TopicVideo, Integer> tvDao1;
	    Dao<TopicVideo, Integer> tvDao2;
	    Dao<TopicVideo, Integer> tvDao3;
	    Dao<TopicVideo, Integer> tvDao4;
	    DatabaseHelper tvDbHelper = new DatabaseHelper(context);
	    try {
	        tvDao1 = tvDbHelper.getTopicVideosDao();
	        tvDao1.create(tv1);
	        tvDao2 = tvDbHelper.getTopicVideosDao();
	        tvDao2.create(tv2);
	        tvDao3 = tvDbHelper.getTopicVideosDao();
	        tvDao3.create(tv3);
	        tvDao4 = tvDbHelper.getTopicVideosDao();
	        tvDao4.create(tv4);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
	    
	    // CHILD HEALTH ASSESSMENT BRANCH
//	    PageAssessment1 pa1 = new PageAssessment1(1001, "Ask", "Does the child have a cough?", "Zulu stuff", "How long has the child been coughing?", "Zulu stuff", null,  null);
//	    PageAssessment1 pa2 = new PageAssessment1(1003, "Ask", "Has the child had convulsions?", "Zulu stuff", null, null, null,  null);
//	    Dao<PageAssessment1, Integer> pa1Dao;
//	    Dao<PageAssessment1, Integer> pa2Dao;
//	    DatabaseHelper pa1DbHelper = new DatabaseHelper(context);
//	    try {
//	    	pa1Dao = pa1DbHelper.getPageAssessment1Dao();
//	    	pa1Dao.create(pa1);
//	    	pa2Dao = pa1DbHelper.getPageAssessment1Dao();
//	    	pa2Dao.create(pa2);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }	    

	    
	    //public Vaccine(int id, int age, String display_age, String vaccine_short, String vaccine_long) 
	    Vaccine v1 = new Vaccine(1, 0, "Birth", "BCG", "Bacilli Calmete-Guerin (Anti-tuberculosis vaccine)");
	    Vaccine v2 = new Vaccine(2, 0, "Birth", "TOPV", "Trivalent oral polio vacine");
	    Vaccine v3 = new Vaccine(3, 0.1154, "6 weeks", "TOPV", "Trivalent oral polio vacine");
	    Vaccine v4 = new Vaccine(4, 0.1154, "6 weeks", "RV", "Rotavirus vaccine");
	    Vaccine v5 = new Vaccine(5, 0.1154, "6 weeks", "DTP-IPV/Hib", "Diptheria, tetanus, pertussis vaccine, inactivated polio vaccine, Haemophilus influenza type b vaccine");
	    Vaccine v6 = new Vaccine(6, 0.1154, "6 weeks", "Hepatitis B", "Hepatitis B");
	    Vaccine v7 = new Vaccine(7, 0.1154, "6 weeks", "PCV7", "7-valent pneumococcal vaccine");
	    Vaccine v8 = new Vaccine(8, 0.1923, "10 weeks", "DTP-IPV/Hib", "Diptheria, tetanus, pertussis vaccine, inactivated polio vaccine, Haemophilus influenza type b vaccine");
	    Vaccine v9 = new Vaccine(9, 0.1923, "10 weeks", "Hepatitis B", "Hepatitis B");
	    Vaccine v10 = new Vaccine(11, 0.2692, "14 weeks", "RV", "Rotavirus vaccine");
	    Vaccine v11 = new Vaccine(10, 0.2692, "14 weeks", "DTP-IPV/Hib", "Diptheria, tetanus, pertussis vaccine, inactivated polio vaccine, Haemophilus influenza type b vaccine");
	    Vaccine v12 = new Vaccine(12, 0.2692, "14 weeks", "Hepatitis B", "Hepatitis B");
	    Vaccine v13 = new Vaccine(13, 0.2692, "14 weeks", "PCV7", "7-valent pneumococcal vaccine");
	    Dao<Vaccine, Integer> vDao1;
	    Dao<Vaccine, Integer> vDao2;
	    Dao<Vaccine, Integer> vDao3;
	    Dao<Vaccine, Integer> vDao4;
	    Dao<Vaccine, Integer> vDao5;
	    Dao<Vaccine, Integer> vDao6;
	    Dao<Vaccine, Integer> vDao7;
	    Dao<Vaccine, Integer> vDao8;
	    Dao<Vaccine, Integer> vDao9;
	    Dao<Vaccine, Integer> vDao10;
	    Dao<Vaccine, Integer> vDao11;
	    Dao<Vaccine, Integer> vDao12;
	    Dao<Vaccine, Integer> vDao13;
	    DatabaseHelper vDbHelper = new DatabaseHelper(context);
	    try {
	        vDao1 = vDbHelper.getVaccineDao();
	        vDao1.create(v1);
	        vDao2 = vDbHelper.getVaccineDao();
	        vDao2.create(v2);
	        vDao3 = vDbHelper.getVaccineDao();
	        vDao3.create(v3);
	        vDao4 = vDbHelper.getVaccineDao();
	        vDao4.create(v4);
	        vDao5 = vDbHelper.getVaccineDao();
	        vDao5.create(v5);
	        vDao6 = vDbHelper.getVaccineDao();
	        vDao6.create(v6);
	        vDao7 = vDbHelper.getVaccineDao();
	        vDao7.create(v7);
	        vDao8 = vDbHelper.getVaccineDao();
	        vDao8.create(v8);
	        vDao9 = vDbHelper.getVaccineDao();
	        vDao9.create(v9);
	        vDao10 = vDbHelper.getVaccineDao();
	        vDao10.create(v10);
	        vDao11 = vDbHelper.getVaccineDao();
	        vDao11.create(v11);
	        vDao12 = vDbHelper.getVaccineDao();
	        vDao12.create(v12);
	        vDao13 = vDbHelper.getVaccineDao();
	        vDao13.create(v13);
	    } catch (SQLException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
	    Toast.makeText(getApplicationContext(), "DB populated!", Toast.LENGTH_SHORT).show();
	    finish();
    }
}
