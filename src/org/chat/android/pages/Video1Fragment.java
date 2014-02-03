package org.chat.android.pages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.chat.android.models.TopicVideo;
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
import android.widget.ImageView;
import android.widget.TextView;

public class Video1Fragment extends Fragment {
	Context context;
	int visitId = 0;
    TextView title = null;
    TextView content1 = null;
    List<ImageView> imgViewList = null;
    List<TextView> videoNameList = null;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_video1, container, false);
    	context = getActivity();
    	
    	visitId = getArguments().getInt("visitId"); 
    	
    	title = (TextView) view.findViewById(R.id.v1title);
    	content1 = (TextView) view.findViewById(R.id.v1content1);
    	
    	imgViewList = new ArrayList<ImageView>();
    	imgViewList.add((ImageView) view.findViewById(R.id.v1videoImage1));
    	imgViewList.add((ImageView) view.findViewById(R.id.v1videoImage2));
    	imgViewList.add((ImageView) view.findViewById(R.id.v1videoImage3));
    	imgViewList.add((ImageView) view.findViewById(R.id.v1videoImage4));
    	// make them not clickable until (and if) they've been pulled from DB - ie they should not be clickable if that image is not filled
    	for (ImageView i : imgViewList) {
    		i.setEnabled(false);
    	}
    	
    	videoNameList = new ArrayList<TextView>();
    	videoNameList.add((TextView) view.findViewById(R.id.v1videoText1));
    	videoNameList.add((TextView) view.findViewById(R.id.v1videoText2));
    	videoNameList.add((TextView) view.findViewById(R.id.v1videoText3));
    	videoNameList.add((TextView) view.findViewById(R.id.v1videoText4));
    	
    	String lang = Locale.getDefault().getLanguage();
    	
    	populateDisplayedFragment(getArguments().getString("themeName"), getArguments().getString("type"), getArguments().getInt("id"), lang);

    	return view;
    }

	public void populateDisplayedFragment(String themeName, String type, int pageContentId, String lang) {
		// get this particular page in this particular table/type
		Dao<PageVideo1, Integer> pvDao;		
		DatabaseHelper pvDbHelper = new DatabaseHelper(context);
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
		
		HealthTheme theme = ModelHelper.getThemeForName(context, themeName);
		int colorRef = Color.parseColor(theme.getColor());
		title.setTextColor(colorRef);
		
		// grab the videos for this page
		List<TopicVideo> topicVideos = ModelHelper.getVideoIdsForPageVideo1Id(context, pv.getId());				// this is from a bridge table, containing page_video1_ids and video_ids
		
		// populate the images and text from video list
		//v1videoImage1  v1videoText1
		for (int i = 0; i < topicVideos.size(); i++) {
			Video video = ModelHelper.getVideoForId(context, topicVideos.get(i).getVideoId());
			int screenshotId = getResources().getIdentifier(video.getScreenshot(), "drawable", context.getPackageName());
			imgViewList.get(i).setImageResource(screenshotId);
			imgViewList.get(i).setTag(video.getId());
			imgViewList.get(i).setEnabled(true);
			videoNameList.get(i).setText(video.getName());
		}	
    }
}
