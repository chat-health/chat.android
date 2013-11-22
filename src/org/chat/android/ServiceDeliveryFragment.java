//package org.chat.android;
//
//import android.app.FragmentTransaction;
//import android.app.ListFragment;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//
//public class ServiceDeliveryFragment extends ListFragment {
//    boolean mDualPane;
//    int mCurCheckPosition = 0;
//
//    @Override
//    public void onActivityCreated(Bundle savedState) {
//        super.onActivityCreated(savedState);
//        
//        String serviceCategory = ((ServiceDeliveryActivity)getActivity()).serviceCategory;
//	    String[] services;
//	    if (serviceCategory.equals("B - Material Well Being")) {
//	    	services = getResources().getStringArray(R.array.material_well_being_service_array);
//	    } else if (serviceCategory.equals("C - Cognitive Well Being")) {
//	    	services = getResources().getStringArray(R.array.cognitive_well_being_service_array);
//	    } else if (serviceCategory.equals("D - Emotional Well Being")) {
//	    	services = getResources().getStringArray(R.array.emotional_well_being_service_array);
//	    } else if (serviceCategory.equals("E - Physical Well Being")) {
//	    	services = getResources().getStringArray(R.array.physical_well_being_service_array);
//	    } else if (serviceCategory.equals("S - Documents and Grants")) {
//	    	services = getResources().getStringArray(R.array.documents_and_grants_service_array);
//	    } else if (serviceCategory.equals("T - Change of Status")) {
//	    	services = getResources().getStringArray(R.array.change_of_status_service_array);
//	    } else {
//	    	Toast.makeText(getActivity(), "ERROR: Unknown service group", Toast.LENGTH_SHORT).show();
//	    	services = getResources().getStringArray(R.array.material_well_being_service_array);
//	    }
//	    setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, services));
//
//        // Check to see if we have a frame in which to embed the details
//        // fragment directly in the containing UI.
//        View detailsFrame = getActivity().findViewById(R.id.details);
//        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
//
//        if (savedState != null) {
//            // Restore last state for checked position.
//            mCurCheckPosition = savedState.getInt("curChoice", 0);
//        }
//
//        if (mDualPane) {
//            // In dual-pane mode, list view highlights selected item.
//            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//            // Make sure our UI is in the correct state.
//            showDetails(mCurCheckPosition);
//        }
//        
//     
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("curChoice", mCurCheckPosition);
//    }
//
//    @Override
//    public void onListItemClick(ListView l, View v, int pos, long id) {
//        showDetails(pos);
//    }
//
//    /**
//     * Helper function to show the details of a selected item, either by
//     * displaying a fragment in-place in the current UI, or starting a
//     * whole new activity in which it is displayed.
//     */
//    void showDetails(int index) {
//        mCurCheckPosition = index;
//
//        if (mDualPane) {
//            // We can display everything in-place with fragments.
//            // Have the list highlight this item and show the data.
//            getListView().setItemChecked(index, true);
//
//            // Check what fragment is shown, replace if needed.
//            ServiceDetailsFragment details = (ServiceDetailsFragment)getFragmentManager().findFragmentById(R.id.details);
//            if (details == null || details.getShownIndex() != index) {
//                // Make new fragment to show this selection.
//                details = ServiceDetailsFragment.newInstance(index);
//
//                // Execute a transaction, replacing any existing
//                // fragment with this one inside the frame.
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.details, details);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.commit();
//            }
//
//        }
////        else {
////            // Otherwise we need to launch a new activity to display
////            // the dialog fragment with selected text.
////            Intent intent = new Intent();
////            intent.setClass(getActivity(), DetailsActivity.class);
////            intent.putExtra("index", index);
////            startActivity(intent);
////        }
//    }
//}
//
