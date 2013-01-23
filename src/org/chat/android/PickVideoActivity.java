package org.chat.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
}
