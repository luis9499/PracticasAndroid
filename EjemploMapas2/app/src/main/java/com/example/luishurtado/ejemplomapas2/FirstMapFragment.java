package com.example.luishurtado.ejemplomapas2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstMapFragment extends SupportMapFragment {


    public FirstMapFragment() {
        // Required empty public constructor
    }
    public static FirstMapFragment newInstance(){
        return new FirstMapFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater,container,savedInstanceState);
        return root;
    }

}
