package vinsoft.com.wavefindyourfriend.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

import vinsoft.com.wavefindyourfriend.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

   // private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    //private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;

    TextView tvNameLocation;

    private GoogleMap mMap;
    LocationManager locationManager;
    Location locationNow;

    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;

    List<Marker> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initWidget();

        markerList=new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        initServiceProvider();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setEvent();

        LatLng latLngNow = new LatLng(locationNow.getLatitude(), locationNow.getLongitude());
        getInfoLocation(latLngNow);


    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng latLngNow = new LatLng(location.getLatitude(), location.getLongitude());
        //getInfoLocation(latLngNow);

        Marker marker= markerList.get(0);
        marker.setPosition(latLngNow);

        markerList.set(0,marker);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    public void initWidget(){
        tvNameLocation= (TextView) findViewById(R.id.tv_name_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void initServiceProvider() {
        try {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            // getting GPS status
            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            //checkNetwork=true;

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(this, R.string.txt_error_network_gps, Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;

                if (checkNetwork) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            locationNow = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (locationNow == null) {
                                locationNow = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                            }
                        }
                    } catch (SecurityException e) {
                        Log.d("Error", e.toString());

                    }
                }

                if (checkGPS) {
                    if (locationNow == null) {
                        try {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                            if (locationManager != null) {
                                locationNow = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (locationNow == null) {
                                    locationNow = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                                }
                            }
                        } catch (SecurityException e) {
                            Log.d("Error", e.toString());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.d("Error", ex.toString());
        }
    }

    public void getInfoLocation( LatLng latLngNow){
        if (markerList.size()>=2){
            markerList.get(1).remove();
            markerList.remove(1);
        }
        try {
            Marker marker=null;
            if (markerList.size()==0) {
                marker = mMap.addMarker(new MarkerOptions().position(latLngNow));
                marker.setTitle("Me");
                marker.showInfoWindow();
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            }

            else {
                marker = mMap.addMarker(new MarkerOptions().position(latLngNow));
                marker.setTitle("Dong");
                marker.showInfoWindow();
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }

            markerList.add(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngNow, 17));

            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
            StringBuilder builder = new StringBuilder();
            try {
                List<Address> address = geoCoder.getFromLocation(latLngNow.latitude,latLngNow.longitude,1);
                int maxLines = address.get(0).getMaxAddressLineIndex();
                for (int i = 0; i < maxLines; i++) {
                    String addressStr = address.get(0).getAddressLine(i);
                    builder.append(addressStr);
                    builder.append(" ");
                }

                tvNameLocation.setText(builder.toString());

            } catch (IOException e) {
                Log.d("AddressError", e.toString());
            }

        } catch (Exception ex) {
            Log.d("Error1", "Not find location");
        }

    }

    public void setEvent(){
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                getInfoLocation(latLng);
            }
        });
    }


}
