package org.chat.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Armin Krauss on 2013-06-12.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 18;
    private Dao<Visit, Integer> visitsDao = null;
    private Dao<Client, Integer> clientsDao = null;
    private Dao<Household, Integer> householdsDao = null;
    private Dao<Attendance, Integer> attendanceDao = null;
    private Dao<Role, Integer> roleDao = null;
    private Dao<Worker, Integer> workerDao = null;
    private Dao<Video, Integer> videoDao = null;
    private Dao<VideoAccessed, Integer> videoAccessedDao = null;
    

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
//        super(context, databaseName, factory, databaseVersion);
//    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Visit.class);
            TableUtils.createTable(connectionSource, Client.class);
            TableUtils.createTable(connectionSource, Household.class);
            TableUtils.createTable(connectionSource, Attendance.class);
            TableUtils.createTable(connectionSource, Role.class);
            TableUtils.createTable(connectionSource, Worker.class);
            TableUtils.createTable(connectionSource, Video.class);
            TableUtils.createTable(connectionSource, VideoAccessed.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create databases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, Visit.class, true);
            TableUtils.dropTable(connectionSource, Client.class, true);
            TableUtils.dropTable(connectionSource, Household.class, true);
            TableUtils.dropTable(connectionSource, Attendance.class, true);
            TableUtils.dropTable(connectionSource, Role.class, true);
            TableUtils.dropTable(connectionSource, Worker.class, true);
            TableUtils.dropTable(connectionSource, Video.class, true);
            TableUtils.dropTable(connectionSource, VideoAccessed.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new " + newVer, e);
        }    	 
    }

    /**
     * Functions that returns the Data Access Object DAO00
     */
    public Dao<Visit, Integer> getVisitsDao() throws SQLException {
        if (visitsDao == null) {
        	visitsDao = getDao(Visit.class);
        }
        return visitsDao;
    }
    public Dao<Client, Integer> getClientsDao() throws SQLException {
        if (clientsDao == null) {
        	clientsDao = getDao(Client.class);
        }
        return clientsDao;
    }
    public Dao<Household, Integer> getHouseholdsDao() throws SQLException {
        if (householdsDao == null) {
        	householdsDao = getDao(Household.class);
        }
        return householdsDao;
    }
    public Dao<Attendance, Integer> getAttendanceDao() throws SQLException {
        if (attendanceDao == null) {
            attendanceDao = getDao(Attendance.class);
        }
        return attendanceDao;
    }
    public Dao<Role, Integer> getRolesDao() throws SQLException {
        if (roleDao == null) {
            roleDao = getDao(Role.class);
        }
        return roleDao;
    }
    public Dao<Worker, Integer> getWorkersDao() throws SQLException {
        if (workerDao == null) {
            workerDao = getDao(Worker.class);
        }
        return workerDao;
    }
    public Dao<VideoAccessed, Integer> getVideosAccessedDao() throws SQLException {
        if (videoAccessedDao == null) {
        	videoAccessedDao = getDao(VideoAccessed.class);
        }
        return videoAccessedDao;
    }
    public Dao<Video, Integer> getVideosDao() throws SQLException {
        if (videoDao == null) {
        	videoDao = getDao(Video.class);
        }
        return videoDao;
    }    

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        clientsDao = null;
    }
}
