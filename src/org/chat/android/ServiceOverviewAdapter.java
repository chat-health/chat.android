//package org.chat.android;
//
//import static org.chat.android.R.id.service_type_name;
//
//import java.util.ArrayList;
//
//import org.chat.android.Service;
//import org.chat.android.R;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//
//public class ServiceOverviewAdapter extends ArrayAdapter<String> {
//	private LayoutInflater mInflater;
//    private ArrayList<String> servicesArray;
//
//    public ServiceOverviewAdapter(Context context, int layoutResourceId, ArrayList<String> servicesArray) {
//        super(context, layoutResourceId, servicesArray);
//
//        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.servicesArray = servicesArray;
//    }
//
//    /*
//	 * we are overriding the getView method here - this is what defines how each
//	 * list item will look.
//	 */
//    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView = this.mInflater.inflate(R.layout.service_delivery_listview_row, null);
//        
//        String s = servicesArray.get(position);
//        
//        TextView tv = null;
//
//        if (convertView != null) {
//            tv = (TextView)convertView.findViewById(service_type_name);
//            tv.setText(s);
//        }        
// 
//        return convertView;
//    }
//    
//}
//
//
//// MORE TO FIX HERE