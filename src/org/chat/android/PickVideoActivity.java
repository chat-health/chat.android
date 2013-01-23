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
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class PickVideoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_video);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pick_video, menu);
        return true;
    }
    
    public void playVideo (View v) {
    	//Uri intentUri = Uri.parse(URL);
    	//Uri videoUri = Uri.parse("android.resource://org.chat.android/raw/viralload.mp4");
    	//Uri videoUri = Uri.parse("file:///assets/viralload");
    	//String videoUri = "android.resource://" + getPackageName() + "/" + R.raw.viralload;
//    	String videoUri = "file:///" + getPackageName() + "/assets/viralload";
    	//String videoUri = "file:///android_asset/viralload";
    	copyAsset("viralload.mp4");
    	//String videoUri = "file:///sdcard/viralload";
    	
    	File sdCard = Environment.getExternalStorageDirectory(); 

        File dir = new File (sdCard.getAbsolutePath() + "/chat");
        if(dir.isDirectory() != true) {
      	  dir.mkdirs();
        }
        File myvid = new File(dir, "viralload.mp4");
    	
        
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(Uri.fromFile(myvid), "video/mp4");
        startActivity(intent);

    }
    
    private void copyAsset(String fileToCopy) {
        InputStream in = null;
        OutputStream out = null;
        try {
          in = getAssets().open(fileToCopy);
          
          File sdCard = Environment.getExternalStorageDirectory(); 

          File dir = new File (sdCard.getAbsolutePath() + "/chat");
          if(dir.isDirectory() != true) {
        	  dir.mkdirs();
          }
          
          out = new FileOutputStream(new File(dir, fileToCopy));
          copyFile(in, out);
          in.close();
          in = null;
          out.flush();
          out.close();
          out = null;
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + fileToCopy, e);
        }   
    }
    
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }
    
}
