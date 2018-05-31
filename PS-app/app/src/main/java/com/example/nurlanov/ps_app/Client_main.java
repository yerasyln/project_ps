package com.example.nurlanov.ps_app;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Client_main extends Fragment implements OnMapReadyCallback{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private MapView mView;
    private View v;
    private Marker marker;
    private LatLng lng;
    private List <Arendator_info> asd = new ArrayList<Arendator_info>();
    private List <LatLng> latLngs = new ArrayList<LatLng>();
    private DatabaseReference ref;
    private Button search;

    LatLng current = new LatLng(100,100);
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.client_main, container, false);
        search = (Button)v.findViewById(R.id.search);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        getData();



        return v;
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = (MapView) v.findViewById(R.id.mapClient);
        if (mView != null) {
            mView.onCreate(null);
            mView.onResume();
            mView.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LocationManager lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<asd.size(); i++){
                    lng = new LatLng(asd.get(i).getLongitude(),asd.get(i).getLatitude());
                    latLngs.add(lng);
                    if(lng!=null){
                        marker = map.addMarker(new MarkerOptions().position(lng).title(asd.get(i).getNamePS()));
                        }

                }
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.marker_dialog);
                dialog.setTitle("Info");
                Button ok = (Button)dialog.findViewById(R.id.okay);
                TextView name = dialog.findViewById(R.id.name1_info);
                TextView addres = dialog.findViewById(R.id.address1_info);
                final TextView number = dialog.findViewById(R.id.number1_info);
                TextView place = dialog.findViewById(R.id.mesta1_info);
                TextView empty = dialog.findViewById(R.id.empty1_info);
                TextView when = dialog.findViewById(R.id.hour1_info);

                number.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number.getText().toString()));
                        startActivity(intent);
                    }
                });

                for(int i = 0;i<asd.size();i++){
                    if(marker.getPosition().latitude==asd.get(i).getLongitude()&&marker.getPosition().longitude==asd.get(i).getLatitude()){
                        name.setText(asd.get(i).getNamePS());
                        addres.setText(asd.get(i).getAddress());
                        number.setText(asd.get(i).getNum());
                        place.setText(asd.get(i).getMesta()+"");
                        when.setText(asd.get(i).getHour()+":"+asd.get(i).getMinute());
                        empty.setText(asd.get(i).isEmpty()+"");
                    }
                }

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return false;
            }
        });

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
                    map.setMinZoomPreference(1);
                    return false;
                }
            };
    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    map.setMinZoomPreference(1);
                    current = new LatLng(location.getLatitude(),location.getLongitude());
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    map.addCircle(circleOptions);
                }
            };
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
}
