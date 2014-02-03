package org.chat.android.pages;

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

import com.j256.ormlite.dao.Dao;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Text1Fragment extends Fragment {
	Context context;
    TextView content1 = null;
    TextView content2 = null;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_text1, container, false);
    	context = getActivity();
    	
		content1 = (TextView) view.findViewById(R.id.t1tv1);
		content2 = (TextView) view.findViewById(R.id.t1tv2);
		
		// determine language from current tablet settings
		String lang = Locale.getDefault().getLanguage();
        
	    populateDisplayedFragment(getArguments().getString("themeName"), getArguments().getString("type"), getArguments().getInt("id"), lang);
    	
    	return view;
    }
    
	public void populateDisplayedFragment(String themeName, String type, int pageContentId, String lang) {
		PageText1 pt = ModelHelper.getPageText1ForId(context, pageContentId);

		content1.setText(pt.getContent(lang, "content1"));
		content2.setText(pt.getContent(lang, "content2"));
		
		HealthTheme theme = ModelHelper.getThemeForName(getActivity(), themeName);
		int colorRef = Color.parseColor(theme.getColor());
		content1.setTextColor(colorRef);
    }
}