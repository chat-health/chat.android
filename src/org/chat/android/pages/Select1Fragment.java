package org.chat.android.pages;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.chat.android.DatabaseHelper;
import org.chat.android.R;
import org.chat.android.R.layout;
import org.chat.android.models.PageSelect1;
import org.chat.android.models.PageText1;

import com.j256.ormlite.dao.Dao;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class Select1Fragment extends Fragment {
	ImageView image1 = null;
    TextView content1 = null;
    TextView question1 = null;
    RadioButton answer1 = null;
    RadioButton answer2 = null;
    RadioButton answer3 = null;
    RadioButton answer4 = null;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_select1, container, false);
    	
    	image1 = (ImageView) view.findViewById(R.id.s1i1);
		content1 = (TextView) view.findViewById(R.id.s1tv1);
		question1 = (TextView) view.findViewById(R.id.s1tv2);
		answer1 = (RadioButton) view.findViewById(R.id.s1rb1);
		answer2 = (RadioButton) view.findViewById(R.id.s1rb2);
		answer3 = (RadioButton) view.findViewById(R.id.s1rb3);
		answer4 = (RadioButton) view.findViewById(R.id.s1rb4);
		
		// determine language from current tablet settings
		String lang = Locale.getDefault().getLanguage();
		
		String type = getArguments().getString("type");
		int id = getArguments().getInt("id");
        
	    populateDisplayedFragment(type, id, lang);
	    //populateDisplayedFragment2("select1", 1, lang);
    	
    	return view;
    }
    
	public void populateDisplayedFragment(String type, int pageContentId, String lang) {
		// get this particular page in this particular table/type
		Dao<PageSelect1, Integer> psDao;		
		DatabaseHelper psDbHelper = new DatabaseHelper(getActivity());
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

		image1.setImageResource(getActivity().getResources().getIdentifier("drawable/" + ps.getImage1(), null, getActivity().getPackageName()));
		content1.setText(ps.getContent(lang, "content1"));
		question1.setText(ps.getContent(lang, "question1"));
		answer1.setText(ps.getContent(lang, "answer1"));
		answer2.setText(ps.getContent(lang, "answer2"));
		answer3.setText(ps.getContent(lang, "answer3"));
		answer4.setText(ps.getContent(lang, "answer4"));
    }
}