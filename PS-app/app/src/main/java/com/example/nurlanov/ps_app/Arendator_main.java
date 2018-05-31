package com.example.nurlanov.ps_app;


import android.Manifest;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.nurlanov.ps_app.BlankFragment.number_phone;


public class Arendator_main extends Fragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private MapView mView;
    private View v;
    private DatabaseReference dbArendator;
    Button save,choose,saveTime;
    EditText hour_ed,min_ed;
    private boolean markered = false;
    private DatabaseReference ref;
    private String phone_num = number_phone;
    private LatLng lng;
    private LatLng current = new LatLng(100,100);
    private List<Arendator_info> asd = new ArrayList<Arendator_info>();
    private Arendator_info aren;

    CheckBox checkBox;
    Marker marker;

    Runnable runnable;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.arendator_main, container, false);
        save = v.findViewById(R.id.save);
        saveTime = v.findViewById(R.id.saveTime);
        choose = v.findViewById(R.id.choose);

        hour_ed = v.findViewById(R.id.hour);
        min_ed = v.findViewById(R.id.min);
        checkBox = v.findViewById(R.id.emptyChoose);

        choose.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override

            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    final Calendar c = Calendar.getInstance();
                    final int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);


                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    hour_ed.setText(hourOfDay + "");
                                    min_ed.setText(minute + "");

                                }
                            }, hour, minute, false);
                    timePickerDialog.show();
                }
                else{
                    Toast.makeText(getActivity(),"Все консоли свободны",Toast.LENGTH_SHORT).show();
                }
            }

        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        getData();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markered) {
                    for (int i = 0; i < asd.size(); i++) {
                        if (asd.get(i).getNum().equals(phone_num)) {
                            aren = new Arendator_info(asd.get(i).getId().toString(), asd.get(i).getNamePS().toString(), asd.get(i).getPass().toString(), asd.get(i).getAddress().toString(), asd.get(i).getNum().toString(),
                                    asd.get(i).getHour(), asd.get(i).getMinute(), lng.latitude, lng.longitude, asd.get(i).getMesta(), true);

                        }
                    }
                    dbArendator = database.getReference().child("Arendator").child(phone_num);
                    dbArendator.setValue(aren);
                    Toast.makeText(getActivity(), "Ваш адрес сохранен", Toast.LENGTH_SHORT).show();
                    markered = false;
                }
                else{
                    Toast.makeText(getActivity(),"Отметьте местоположение", Toast.LENGTH_SHORT).show();
                }
            }

        });
        saveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkBox.isChecked()) {
                    for (int i = 0; i < asd.size(); i++) {
                        if (asd.get(i).getNum().equals(phone_num)) {
                            aren = new Arendator_info(asd.get(i).getId().toString(), asd.get(i).getNamePS().toString(),
                                    asd.get(i).getPass().toString(), asd.get(i).getAddress().toString(), asd.get(i).getNum().toString(),
                                    hour_ed.getText().toString(), min_ed.getText().toString(),
                                    asd.get(i).getLongitude(),asd.get(i).getLatitude(), asd.get(i).getMesta(), true);

                        }
                    }
                    dbArendator = database.getReference().child("Arendator").child(phone_num);
                    dbArendator.setValue(aren);
                    Toast.makeText(getActivity(), "Время сохранено", Toast.LENGTH_SHORT).show();



                }
                else{
                    Toast.makeText(getActivity(),"Все консоли свободны", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void getData() {
        ref.child("Arendator").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for(DataSnapshot child : children){
                    Arendator_info info = child.getValue(Arendator_info.class);
                    asd.add(info);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = (MapView) v.findViewById(R.id.map);
        if (mView != null) {
            mView.onCreate(null);
            mView.onResume();
            mView.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //MapsInitializer.initialize(getActivity());
        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(!markered){
                    marker = map.addMarker(new MarkerOptions().position(latLng));
                    lng = latLng;
                    markered = true;

                }else {
                    marker.remove();
                    marker = map.addMarker(new MarkerOptions().position(latLng));
                    lng = latLng;
                    markered = true;


                }

            }
        });

        LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final LocationManager manager = (LocationManager) getActivity().getSystemService( getActivity().LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            // Call your Alert message
            Toast.makeText(getActivity(),"GPS is Disable",Toast.LENGTH_SHORT).show();
        }else {
            if(location!=null) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                LatLng redmond = new LatLng(latitude, longitude);
                map.moveCamera(CameraUpdateFactory.newLatLng(redmond));
            }
        }

        map.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        map.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();

        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMinZoomPreference(11);

    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (map != null) {
            map.setMyLocationEnabled(true);
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(getActivity(), "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        map.moveCamera(CameraUpdateFactory.newLatLng(current));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    //showDefaultLocation();
                }
                return;
            }

        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    map.setMinZoomPreference(15);
                    return false;
                }
            };
    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    map.setMinZoomPreference(12);
                    current = new LatLng(location.getLatitude(),location.getLongitude());

                }
            };

    public Arendator_main() {
    }
}