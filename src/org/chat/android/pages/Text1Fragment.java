package org.chat.android.pages;

import org.chat.android.R;
import org.chat.android.R.layout;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Text1Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_text1, container, false);

      return view;
    }
}