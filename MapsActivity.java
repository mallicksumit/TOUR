package com.example.kon_boot.tour;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

    public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            com.google.android.gms.location.LocationListener,
    GoogleMap.OnMarkerClickListener{

        private GoogleMap mMap;
        double latitude;
        double longitude;
        double searchlat;
        double searchlong;
        private int PROXIMITY_RADIUS = 10000;
        GoogleApiClient mGoogleApiClient;
        Location mLastLocation,current,pasloc;
        Marker mCurrLocationMarker;
        double latestlat;
        double latestlong;
        String type;
        LocationRequest mLocationRequest;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission();
            }

            //show error dialog if Google Play Services not available
            if (!isGooglePlayServicesAvailable()) {
                Log.d("onCreate", "Google Play Services not available. Ending Test case.");
                finish();
            } else {
                Log.d("onCreate", "Google Play Services available. Continuing.");
            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        private boolean isGooglePlayServicesAvailable() {
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            int result = googleAPI.isGooglePlayServicesAvailable(this);
            if(result != ConnectionResult.SUCCESS) {
                if(googleAPI.isUserResolvableError(result)) {
                    googleAPI.getErrorDialog(this, result,
                            0).show();
                }
                return false;
            }
            return true;
        }

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }

            Button btnRestaurant = (Button) findViewById(R.id.btnRestaurant);
            Button btnHospital = (Button) findViewById(R.id.btnHospital);
            Button btnSchool = (Button) findViewById(R.id.btnSchool);
            Button btncurrent =findViewById(R.id.btncurrent);


        }
        public void Onclicksearch(View view)
        {
            switch(view.getId()){
                case R.id.btnsearch:

                    EditText tf_location =  findViewById(R.id.editloc);

                    String location = tf_location.getText().toString();

                    List<Address> addressList;



                    if(!location.equals(""))

                    {

                        Geocoder geocoder = new Geocoder(this);


                        try {

                            addressList = geocoder.getFromLocationName(location, 5);


                            if(addressList != null)

                            {

                                for(int i = 0;i<addressList.size();i++)

                                {
                                    searchlat= addressList.get(i).getLatitude();
                                    searchlong=addressList.get(i).getLongitude();
                                    latestlat=searchlat;
                                    latestlong=searchlong;
                                    Log.d("searchlat", "" +searchlat);
                                    Log.d("searchlat", "" +searchlong);
                                    LatLng latLng = new LatLng(addressList.get(i).getLatitude() , addressList.get(i).getLongitude());

                                    MarkerOptions markerOptions = new MarkerOptions();

                                    markerOptions.position(latLng);

                                    markerOptions.title(location);

                                    mMap.addMarker(markerOptions);

                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                                }

                            }

                        } catch (IOException e) {

                            e.printStackTrace();

                        }

                    }

                    break;
                case R.id.btnRestaurant:

                    Log.d("SUmit", "" +latitude);
                        if(latestlat==latitude&&latestlong==longitude){
                            type ="restaurant";
                        build_retrofit_and_get_response("restaurant",latitude,longitude);

                        }
                        if(latestlong==searchlong&&latestlat==searchlat) {
                            type ="restaurant";

                            build_retrofit_and_get_response("restaurant", searchlat, searchlong);
                        }
                    Toast.makeText(MapsActivity.this, "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnSchool:
                    if(latestlat==latitude&&latestlong==longitude){
                         type ="school";
                    build_retrofit_and_get_response("school",latitude,longitude);}
                    if(latestlong==searchlong&&latestlat==searchlat){
                         type ="school";
                        build_retrofit_and_get_response("school",searchlat,searchlong);
                    Toast.makeText(MapsActivity.this, "Showing Nearby Schools", Toast.LENGTH_SHORT).show();}
                    break;
                case R.id.btnHospital:
                    if(latestlat==latitude&&latestlong==longitude) {
                        type ="hospital";
                        build_retrofit_and_get_response("hospital", latitude, longitude);

                    }
                    if(latestlong==searchlong&&latestlat==searchlat){
                        type ="hospital";
                        build_retrofit_and_get_response("hospital",searchlat,searchlong);

                    Toast.makeText(MapsActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();}
                    break;

                case R.id.btncurrent:

                    onLocationChanged(current);


            }

        }

        private void build_retrofit_and_get_response(String type, final double latc, final double longc) {

            String url = "https://maps.googleapis.com/maps/";

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final RetrofitMaps service = retrofit.create(RetrofitMaps.class);


            Call<MainPLaces> call = service.getNearbyPlaces(type, latc + "," + longc, PROXIMITY_RADIUS);
            call.enqueue(new Callback<MainPLaces>() {
                @Override
                public void onResponse(Call<MainPLaces> call, Response<MainPLaces> response) {
                    try {
                        mMap.clear();
                        // This loop will go through all the results and add marker on each location.
                        for (int i = 0; i < response.body().getResults().size(); i++) {
                            Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                            Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                            String placeName = response.body().getResults().get(i).getName();
                            String vicinity = response.body().getResults().get(i).getVicinity();
                            MarkerOptions markerOptions = new MarkerOptions();
                            LatLng latLng = new LatLng(lat, lng);
                            // Position of Marker on Map
                            markerOptions.position(latLng);
                            // Adding Title to the Marker
                            markerOptions.title(placeName + " : " + vicinity);

                            // Adding Marker to the Camera.
                            Marker m = mMap.addMarker(markerOptions);
                            float result[] = new float[10];
                            Location.distanceBetween(latc,longc,lat,lng,result);
                            markerOptions.snippet("Distance"+result[0]);
                            mMap.addMarker(markerOptions);
                            // Adding colour to the marker
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            // move map camera
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                        }
                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                    }
                }


                @Override
                public void onFailure(Call<MainPLaces> call, Throwable t) {
                    Log.d("onFailure", t.toString());

                }


            });


        }

        protected synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        @Override
        public void onConnected(Bundle bundle) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onLocationChanged(Location location) {

            Log.d("onLocationChanged", "entered");

            mLastLocation = location;
            current =mLastLocation;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            //Place current location marker
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latestlat=latitude;
            latestlong=longitude;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");

            // Adding colour to the marker
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

            // Adding Marker to the Map
            mCurrLocationMarker = mMap.addMarker(markerOptions);

            //move map camera
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
            mMap.animateCamera(cameraUpdate);

            if(mGoogleApiClient!=null)
            {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            }
            Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

            Log.d("onLocationChanged", "Exit");
        }

        @Override
        protected void onStop() {
            Intent intent =new Intent(MapsActivity.this,RecyclerAdapter.class);
            Intent myintent=new Intent(MapsActivity.this, MainActivity.class);
            myintent.putExtra("latitude",latestlat);
            myintent.putExtra("longitude",latestlong);
            myintent.putExtra("type",type);
            startActivity(myintent);
            super.onStop();
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }


        public void onProviderEnabled(String provider) {

        }


        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

        public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
        public boolean checkLocationPermission(){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Asking user if explanation is needed
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an explanation to the user asynchronously -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;
            } else {
                return true;
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_LOCATION: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted. Do the
                        // contacts-related task you need to do.
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            if (mGoogleApiClient == null) {
                                buildGoogleApiClient();
                            }
                            mMap.setMyLocationEnabled(true);
                        }

                    } else {

                        // Permission denied, Disable the functionality that depends on this permission.
                        Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                // other 'case' lines to check for other permissions this app might request.
                // You can add here other case statements according to your requirement.
            }
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            return false;
        }
    }
