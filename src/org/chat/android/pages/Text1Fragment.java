package org.chat.android.pages;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.chat.android.DatabaseHelper;
import org.chat.android.R;
import org.chat.android.R.layout;
import org.chat.android.models.PageText1;

import com.j256.ormlite.dao.Dao;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Text1Fragment extends Fragment {
    TextView content1 = null;
    TextView content2 = null;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_text1, container, false);
    	
		content1 = (TextView) view.findViewById(R.id.t1tv1);
		content2 = (TextView) view.findViewById(R.id.t1tv2);
		
		// determine language from current tablet settings
		String lang = Locale.getDefault().getLanguage();
        
	    populateDisplayedFragment(getArguments().getString("type"), getArguments().getInt("id"), lang);
    	
    	return view;
    }
    
	public void populateDisplayedFragment(String type, int pageContentId, String lang) {
		// get this particular page in this particular table/type
		Dao<PageText1, Integer> ptDao;		
		DatabaseHelper ptDbHelper = new DatabaseHelper(getActivity());
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

		content1.setText(pt.getContent(lang, "content1"));
		content2.setText(pt.getContent(lang, "content2"));
    }
}