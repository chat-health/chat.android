//package org.chat.android;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//public class CircleDetailActivity extends FragmentActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_circle_detail);
//                if (savedInstanceState == null) {
//            Bundle arguments = new Bundle();
//            arguments.putString(CircleDetailFragment.ARG_ITEM_ID, getIntent()
//                    .getStringExtra(CircleDetailFragment.ARG_ITEM_ID));
//            CircleDetailFragment fragment = new CircleDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.circle_detail_container, fragment).commit();
//        }
//    }
//}