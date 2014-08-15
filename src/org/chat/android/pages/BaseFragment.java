package org.chat.android.pages;

import org.chat.android.DatabaseHelper;
import android.app.Fragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class BaseFragment extends Fragment {
	// since we aren't OrmLiteBaseActivity or BaseActivity we can't use getHelper()
    // so we use OpenHelperManager
    protected DatabaseHelper databaseHelper = null;
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        releaseHelper();
    }
	
    protected DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }
    
    protected void releaseHelper() {
    	if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
