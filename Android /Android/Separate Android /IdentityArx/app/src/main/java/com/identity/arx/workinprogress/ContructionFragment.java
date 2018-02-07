package com.identity.arx.workinprogress;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.identity.arx.R;


public class ContructionFragment extends Fragment {
    private ImageView imageView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Profile");
        View view = inflater.inflate(R.layout.activity_contruction, container, false);
        this.imageView = (ImageView) view.findViewById(R.id.contructionImageview);
        this.imageView.setImageResource(R.drawable.construct);
        return view;
    }
}
