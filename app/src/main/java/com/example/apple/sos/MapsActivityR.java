package com.example.apple.sos;
        import android.*;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.net.Uri;
        import android.os.Build;
        import android.provider.Settings;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;
        import android.view.View;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.io.IOException;
        import java.util.List;
        import java.util.Locale;


public class MapsActivityR extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private DatabaseReference databaseReference,rootRef;
    private LatLng recLocation;
    Intent intent;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                Log.i("10", "HEllo");
                configure_location();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Login.firebaseauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        rootRef = FirebaseDatabase.getInstance().getReference().child("P8mq1MnrOYYP3Rm6ZhraslZ5Fqw1").child("LocationInfo");

        setContentView(R.layout.activity_maps_r);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        Log.i("3", "HEllo");

        LatLng yourLocation = new LatLng(18,73);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(yourLocation).title("Receiver Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocation,13));

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
              //  for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                 Locatn sloc = snapshot.getValue(Locatn.class);


                     recLocation = new LatLng(Double.parseDouble(sloc.getLat()),Double.parseDouble(sloc.getLont()));

                        mMap.addMarker(new MarkerOptions().position(recLocation).title("Sender Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(recLocation,13));

                    /* You could extract the values of object using the getter methods
     * and display it in your GUI.

     *  climate.getCity()
     *  climate.getTemperature()
     *  climate.getHumidity()
     *  climate.getPressure()
     **/

            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
       /*
        * You may print the error message.
               **/
            }
        });




      /*  Locatn locate = new Locatn("18","73","Receiver");
        FirebaseUser firebaseUser =Login.firebaseauth.getCurrentUser();
        databaseReference.child(firebaseUser.getUid()).child("LocationInfo").setValue(locate);*/



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("4","HEllo");


                LatLng yourLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(recLocation).title("Sender Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(recLocation,13));
                mMap.addMarker(new MarkerOptions().position(yourLocation).title("Receiver Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocation,13));


               Locatn locate = new Locatn(String.valueOf(yourLocation.latitude),String.valueOf(yourLocation.longitude),"Receiver");
                FirebaseUser firebaseUser =Login.firebaseauth.getCurrentUser();
                databaseReference.child(firebaseUser.getUid()).child("LocationInfo").setValue(locate);


                Intent directionsIntent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + yourLocation.latitude + "," + yourLocation.longitude + "&daddr=" + recLocation.latitude + "," + recLocation.longitude));
                startActivity(directionsIntent);







            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Log.i("5","HEllo");
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_location();


    }





    private void configure_location()
    {

        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.i("8","HEllo");
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        Log.i("7","HEllo");
        //noinspection MissingPermission
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);


    }



}






