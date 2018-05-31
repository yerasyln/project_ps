package com.example.nurlanov.ps_app;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BlankFragment extends Fragment {
    Button log_in;
    Button sign_up;
    EditText num, pass;
    String passFromDb = "";
    String numFromDb = "";
    DatabaseReference ref;
    String id = "";
    CheckBox checkBox;
    static String number_phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_blank, container, false);
        log_in = (Button) v.findViewById(R.id.log_in);
        sign_up = (Button) v.findViewById(R.id.sign_up);
        checkBox = (CheckBox)v.findViewById(R.id.checkClient);
        num = (EditText) v.findViewById(R.id.log_num);
        pass = (EditText) v.findViewById(R.id.log_pass);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkBox.isChecked()) {
                    ref = database.getReference("Client");
                }
                else if(checkBox.isChecked()){
                    ref = database.getReference("Arendator");
                }
                ref.child(num.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        number_phone = num.getText().toString();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.getKey().equals("pass")) {
                                passFromDb = data.getValue(String.class);
                            }else if(data.getKey().equals("num")){
                                numFromDb = data.getValue(String.class);
                            }
                            else if(data.getKey().equals("id")){
                                id = data.getValue(String.class);
                            }


                        }
                        if (!passFromDb.equals(pass.getText().toString())) {
                            Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }else if(!numFromDb.equals(num.getText().toString())){
                            Toast.makeText(getActivity(), "Incorrect Number", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Correct Password", Toast.LENGTH_SHORT).show();
                            Arendator_main fragment = new Arendator_main();
                            Client_main fragment1 = new Client_main();
                            android.app.FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            if(id.equals("1")) {
                                fragmentTransaction.replace(R.id.container, fragment);
                            }
                            else{
                                fragmentTransaction.replace(R.id.container, fragment1);
                            }
                            fragmentTransaction.addToBackStack("Tag").commit();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Incorrect Number", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                RegChoose fragment = new RegChoose();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack("Tag").commit();
            }
        });
        return v;
    }
}
