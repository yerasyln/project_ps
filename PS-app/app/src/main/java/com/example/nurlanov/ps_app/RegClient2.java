package com.example.nurlanov.ps_app;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegClient2 extends Fragment {
    Button submitPass;
    EditText pass, passRepeat;
    DatabaseReference dbClient;
    String id = Registration_client.id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reg_client_2, container, false);
        submitPass = (Button)v.findViewById(R.id.reg_client_submit_pass);
        pass = (EditText)v.findViewById(R.id.reg_client_pass);
        passRepeat = (EditText)v.findViewById(R.id.reg_client_pass_repeat);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        submitPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbClient = database.getReference().child("Client").child(id);
                if(pass.getText().toString().equals(passRepeat.getText().toString())){
                    Client_info info_1 = new Client_info("0",pass.getText().toString(),id);
                            dbClient.setValue(info_1);
                    Client_main fragment = new Client_main();
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.addToBackStack("Tag").commit();
                }
                else{
                    Toast.makeText(getActivity(),"Пароли не совпадают!",Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }
}