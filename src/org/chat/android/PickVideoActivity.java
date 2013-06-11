package org.chat.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class PickVideoActivity extends Activity {

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_videos);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_pick_video, menu);
//        return true;
//    }
//    
//    public void playVideo (View v) {
//    	// 1) Figure out which video to play by determining which button was pressed
//    	// TODO: please add switch statement when adding button and video
//    	String videoName = "";
//    	int buttonId = v.getId();
//    	switch (buttonId) {
//	        case R.id.button_video_1:
//	        	videoName = "pss_animatic.mp4";
//	            break;
//	        case R.id.button_video_2:
//	        	videoName = "nutrition_animatic.mp4";
//	            break;
//	        case R.id.button_video_3:
//	        	videoName = "nutrition_0-9_months.mp4";
//	            break;
//	        case R.id.button_video_4:
//	        	videoName = "nutrition_2_years_up.mp4";
//	            break;
//	        case R.id.image_video_1:
//	        	videoName = "pss_animatic.mp4";
//	            break;
//	        case R.id.image_video_2:
//	        	videoName = "nutrition_animatic.mp4";
//	            break;
//	        case R.id.image_video_3:
//	        	videoName = "nutrition_0-9_months.mp4";
//	            break;
//	        case R.id.image_video_4:
//	        	videoName = "nutrition_2_years_up.mp4";
//	            break;	            
//	        default:
//	        	videoName = "viralload.mp4";
//	            break;
//	    }
//        
//    	// determining path to sdcard (readable by video player)
//    	File sdCard = Environment.getExternalStorageDirectory();
//    	// adding chat dir to path (copyAsset func ensures dir exists)
//        File dir = new File (sdCard.getAbsolutePath() + "/chat");
//        // copy video from within the APK to the sdcard/chat dir
//        // copyAsset(videoName, dir);
//        
//        // create file that points at video in sdcard dir (to retrieve URI)
//        File myvid = new File(dir, videoName);
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
//          out.write(buffer, 0, read);
//        }
//    }
    
}
