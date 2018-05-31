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


public class Registration_arendator extends Fragment {
    DatabaseReference dbArendator;
    private EditText namePs, pass, passRepeat, mestaPS, address;
    private Button submit;
    String id = Registration_client.id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration_arendator, container, false);

        namePs = (EditText)v.findViewById(R.id.reg_arend_name);
        pass = (EditText)v.findViewById(R.id.reg_arend_pass);
        passRepeat = (EditText)v.findViewById(R.id.reg_arend_pass_rep);
        mestaPS = (EditText)v.findViewById(R.id.reg_arend_mesta);
        address = (EditText)v.findViewById(R.id.reg_arend_address);

        submit = (Button)v.findViewById(R.id.reg_arend_submit);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbArendator = database.getReference().child("Arendator").child(id);
                if(pass.getText().toString().equals(passRepeat.getText().toString())){
                    Arendator_info info_1 = new Arendator_info("1", namePs.getText().toString(), pass.getText().toString(),
                            address.getText().toString(), id,null,null,0,0,Integer.parseInt(mestaPS.getText().toString()),true);
                    dbArendator.setValue(info_1);
                    Arendator_main fragment = new Arendator_main();
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.addToBackStack("Tag").commit();
                }
                else{
                    Toast.makeText(getActivity(),"Пароль повторно введен неправильно",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }
}
