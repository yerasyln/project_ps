package com.example.nurlanov.ps_app;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;


public class Registration_client extends Fragment {
    private EditText name, number, smsCode, repeatPass;
    private Button submit,getCode;
    private FirebaseAuth mAuth;
    String codeSent;
    static String id;
    SharedPreferences pref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View v = inflater.inflate(R.layout.registration_client, container, false);
        number = (EditText) v.findViewById(R.id.reg_client_num);
        getCode = (Button)v.findViewById(R.id.reg_client_get_sms);
        submit = (Button) v.findViewById(R.id.reg_client_submit);
        smsCode = (EditText)v.findViewById(R.id.reg_client_sms_code);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInCode();
                id = ""+number.getText().toString();

            }
        });
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPhoneNumberVerification(number.getText().toString());
                Toast.makeText(getActivity(),"На номер "+number.getText().toString()+" выслан смс код",Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);       // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }
    };

    private void signInCode() {
        String code = smsCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pref = getActivity().getPreferences(MODE_PRIVATE);
                            String choose = pref.getString("0","");
                            if(choose.equals("0")) {
                                    Registration_arendator fragment = new Registration_arendator();
                                    android.app.FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container, fragment);
                                    fragmentTransaction.addToBackStack("Tag").commit();
                            }
                            else{
                                RegClient2 fragment = new RegClient2();
                                android.app.FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container, fragment);
                                fragmentTransaction.addToBackStack("Tag").commit();
                            }


                        } else {
                            Toast.makeText(getActivity(),"Смс код введен неправильно",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
