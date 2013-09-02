package org.chat.android;

//import static org.chat.android.R.id.resource_name;

import java.util.ArrayList;

import org.chat.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ResourcesAdapter extends ArrayAdapter<String> {
	private LayoutInflater mInflater;
	private ArrayList<String> resourcesArray;

	public ResourcesAdapter(Context context, int layoutResourceId,
			ArrayList<String> resourcesArray) {
		super(context, layoutResourceId, resourcesArray);

		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resourcesArray = resourcesArray;
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
//	public View getView(int position, View convertView, ViewGroup parent) {
//		convertView = this.mInflater.inflate(
//				R.layout.services_overview_listview_row, null);
//
//		String s = resourcesArray.get(position);
//
//		TextView tv = null;
//
//		if (convertView != null) {
//			tv = (TextView) convertView.findViewById(R.id.resource_name);
//			tv.setText(s);
//		}
//
//		return convertView;
//	}

}

// MORE TO FIX HERE