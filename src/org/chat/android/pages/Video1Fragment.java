package org.chat.android.pages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.chat.android.DatabaseHelper;
import org.chat.android.ModelHelper;
import org.chat.android.R;
import org.chat.android.R.layout;
import org.chat.android.models.HealthTheme;
import org.chat.android.models.PageText1;
import org.chat.android.models.PageVideo1;
import org.chat.android.models.Video;
import org.chat.android.models.VideoAccessed;

import com.j256.ormlite.dao.Dao;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Video1Fragment extends Fragment {
    TextView title = null;
    TextView content1 = null;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_video1, container, false);
    	
    	title = (TextView) view.findViewById(R.id.v1title);
    	content1 = (TextView) view.findViewById(R.id.v1content1);
    	
    	String lang = Locale.getDefault().getLanguage();
    	
    	populateDisplayedFragment(getArguments().getString("themeName"), getArguments().getString("type"), getArguments().getInt("id"), lang);

    	return view;
    }

	public void populateDisplayedFragment(String themeName, String type, int pageContentId, String lang) {
		// get this particular page in this particular table/type
		Dao<PageVideo1, Integer> pvDao;		
		DatabaseHelper pvDbHelper = new DatabaseHelper(getActivity());
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

		content1.setText(pv.getContent(lang, "content1"));
		
		HealthTheme theme = ModelHelper.getThemeForName(getActivity(), themeName);
		int colorRef = Color.parseColor(theme.getColor());
		title.setTextColor(colorRef);
    }
	
//	public void playVideo (View v) {
//		Context context = getApplicationContext();
//		
//    	// figure out which video to play by determining which button was pressed
//    	int chosenVideoId = 0;
//    	int buttonId = v.getId();
////    	switch (buttonId) {
////	        case R.id.button_video_1:
////	        	chosenVideoId = 1;
////	            break;
////	        case R.id.button_video_2:
////	        	chosenVideoId = 2;
////	            break;
////	        case R.id.button_video_3:
////	        	chosenVideoId = 3;
////	            break;
////	        case R.id.button_video_4:
////	        	chosenVideoId = 4;
////	            break;
////	        case R.id.image_video_1:
////	        	chosenVideoId = 1;
////	            break;
////	        case R.id.image_video_2:
////	        	chosenVideoId = 2;
////	            break;
////	        case R.id.image_video_3:
////	        	chosenVideoId = 3;
////	            break;
////	        case R.id.image_video_4:
////	        	chosenVideoId = 4;
////	            break;	            
////	        default:
////	        	chosenVideoId = 1;
////	            break;
////	    }
//    	
//    	// record which video was played in videos_accessed table
//	    VideoAccessed va = new VideoAccessed(chosenVideoId, visitId);
//	    Dao<VideoAccessed, Integer> vaDao;
//	    DatabaseHelper vaDbHelper = new DatabaseHelper(context);
//	    try {
//	        vaDao = vaDbHelper.getVideosAccessedDao();
//	        vaDao.create(va);
//	    } catch (SQLException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }
//    	
//		// get the video_uri
//	    String videoURI = "";
//		Dao<Video, Integer> vidDao;		
//		DatabaseHelper vidDbHelper = new DatabaseHelper(context);
//		try {
//			vidDao = vidDbHelper.getVideosDao();
//			List<Video> vidList = vidDao.queryBuilder().where().eq("id",chosenVideoId).query();
//			Iterator<Video> iter = vidList.iterator();
//			while (iter.hasNext()) {
//				Video vid = iter.next();
//				videoURI = vid.getURI();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//    	// determining path to sdcard (readable by video player)
//    	File sdCard = Environment.getExternalStorageDirectory();
//    	// adding chat dir to path (copyAsset func ensures dir exists)
//        File dir = new File (sdCard.getAbsolutePath() + "/chat");
//        // copy video from within the APK to the sdcard/chat dir
//        // copyAsset(videoName, dir);
//        
//        // create file that points at video in sdcard dir (to retrieve URI)
//        File myvid = new File(dir, videoURI);
//        
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setDataAndType(Uri.fromFile(myvid), "video/*");
//        startActivity(intent);
//    }
//    
//    private void copyAsset(String fileToCopy, File targetDir) {
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//          in = getAssets().open(fileToCopy);
//          
//          if(targetDir.isDirectory() != true) {
//        	  targetDir.mkdirs();
//          }
//          
//          out = new FileOutputStream(new File(targetDir, fileToCopy));
//          copyFile(in, out);
//          in.close();
//          in = null;
//          out.flush();
//          out.close();
//          out = null;
//        } catch(IOException e) {
//            Log.e("tag", "Failed to copy asset file: " + fileToCopy, e);
//        }   
//    }
//    
//    private void copyFile(InputStream in, OutputStream out) throws IOException {
//        byte[] buffer = new byte[1024];
//        int read;
//        while((read = in.read(buffer)) != -1){
//        	out.write(buffer, 0, read);
//        }
//    }
}
