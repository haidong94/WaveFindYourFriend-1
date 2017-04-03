package vinsoft.com.wavefindyourfriend.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.untils.MethodUntil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private static final int PLACE_PICKER_REQUEST = 3;

    //private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    //private static final long MIN_TIME_BW_UPDATES = 0;

    TextView tvNameLocation;
    Spinner spnTransMode;

    private GoogleMap mMap;
    PlaceAutocompleteFragment autocompleteFragment;

    LocationManager locationManager;
    Location locationNow;

    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;

    List<Marker> markerList;
    List<Polyline> polylineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initWidget();

        initPermission();

        markerList = new ArrayList<>();
        polylineList = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        initServiceProvider();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //mMap.setMyLocationEnabled(true);

        setEvent();

        try {
            LatLng latLngNow = new LatLng(locationNow.getLatitude(), locationNow.getLongitude());
            getInfoLocation(latLngNow);
        } catch (Exception ex) {
            Log.d("Location", ex.toString());
        }

        //loadPlacePicker();

    }


    @Override
    public void onLocationChanged(Location location) {
        LatLng latLngNow = new LatLng(location.getLatitude(), location.getLongitude());
        //getInfoLocation(latLngNow);

        Marker marker = markerList.get(0);
        marker.setPosition(latLngNow);

        markerList.set(0, marker);

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


    public void initWidget() {
        tvNameLocation = (TextView) findViewById(R.id.tv_name_location);
        spnTransMode = (Spinner) findViewById(R.id.spn_transport_mode);

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        String transMode[] = {TransportMode.WALKING.toUpperCase(), TransportMode.DRIVING.toUpperCase()};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MapsActivity.this,
                android.R.layout.simple_spinner_item,
                transMode
        );
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spnTransMode.setAdapter(adapter);

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
            boolean b = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
            //checkNetwork=true;

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(this, R.string.txt_error_network_gps, Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                /**/

                if (checkGPS) {
                    if (locationNow == null) {
                        try {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                            if (locationManager != null) {
                                locationNow = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                                if (locationNow == null) {
                                    locationNow = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                                }
                            }
                        } catch (SecurityException e) {
                            Log.d("Error", e.toString());
                        }
                    }
                }

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
            }
        } catch (Exception ex) {
            Log.d("Error", ex.toString());
        }
    }

    public void getInfoLocation(LatLng latLngNow) {
        if (markerList.size() >= 2) {
            markerList.get(1).remove();
            markerList.remove(1);
        }
        try {
            Marker marker = null;
            if (markerList.size() == 0) {
                marker = mMap.addMarker(new MarkerOptions().position(latLngNow));
                marker.setTitle("Me");
                marker.showInfoWindow();
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            } else {
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
                List<Address> address = geoCoder.getFromLocation(latLngNow.latitude, latLngNow.longitude, 1);
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

    public void drawPath(String transportMode) {
        if (markerList.size() == 2) {
            if (polylineList.size() > 0) {
                polylineList.get(0).remove();
                polylineList.clear();
            }

            GoogleDirection.withServerKey(String.valueOf(R.string.google_maps_key))
                    .from(markerList.get(0).getPosition())
                    .to(markerList.get(1).getPosition())
                    .transportMode(transportMode)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();

                            DisplayMetrics displaymetrics = new DisplayMetrics();
                            PolylineOptions polylineOptions = null;
                            if (displaymetrics.densityDpi < 350) {
                                polylineOptions = DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 3, Color.RED);
                            } else {
                                polylineOptions = DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.RED);
                            }
                            Polyline polyline = mMap.addPolyline(polylineOptions);
                            polylineList.add(polyline);
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            Log.d("DIRECTERROR", "2");
                        }
                    });
        }
    }

    public void setEvent() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                getInfoLocation(latLng);
                drawPath(spnTransMode.getSelectedItem().toString().toLowerCase());
                autocompleteFragment.setText("");
            }
        });

        spnTransMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drawPath(spnTransMode.getAdapter().getItem(position).toString().toLowerCase());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                getInfoLocation(place.getLatLng());
                drawPath(spnTransMode.getSelectedItem().toString().toLowerCase());
            }

            @Override
            public void onError(Status status) {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                MethodUntil.makeToast(MapsActivity.this, String.valueOf(R.string.txt_permission_grant),0);
            } else {

                MethodUntil.makeToast(MapsActivity.this, String.valueOf(R.string.txt_permission_deny),0);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    | checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    | checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
    }


}
