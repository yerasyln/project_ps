package com.example.nurlanov.ps_app;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static android.content.Context.MODE_PRIVATE;


public class RegChoose extends Fragment {
    Button arendator;
    Button client;
    SharedPreferences.Editor sPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reg_choose, container, false);
        arendator = (Button)v.findViewById(R.id.arendator);
        client = (Button)v.findViewById(R.id.client);
        sPref= getActivity().getPreferences(MODE_PRIVATE).edit();


        arendator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPref.putString("0","0");
                sPref.commit();
                Registration_client fragment = new Registration_client();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.addToBackStack("Tag").commit();
            }
        });
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPref.putString("0","1");
                sPref.commit();
                Registration_client fragment = new Registration_client();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,fragment);
                fragmentTransaction.addToBackStack("Tag").commit();
            }
        });
        return v;
    }
}
