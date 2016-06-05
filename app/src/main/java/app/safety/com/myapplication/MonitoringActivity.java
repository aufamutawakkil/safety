package app.safety.com.myapplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;

import app.safety.com.R;
import core.*;

public class MonitoringActivity extends FragmentActivity implements LocationListener {
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    GoogleMap mGoogleMap;


    public static String ShopLat;
    public static String ShopPlaceId;
    public static String ShopLong;
    // Stores the current instantiation of the location client in this object
    //private LocationClient mLocationClient;
    boolean mUpdatesRequested = false;
    private TextView markerText;
    private LatLng center;
    private LinearLayout markerLayout;
    private Geocoder geocoder;
    private List<Address> addresses;
    private TextView Address;
    private TextView txtNum;
    private ArrayList<LatLng> points;
    private Polyline line;

    private double latlat;
    private double lnglng;

    private SmsUtils smsUtils;

    Api api = null;
    int index = 1;
    int lastIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_add_police);
            api = new Api(Static.API_KEY);
            points = new ArrayList<>();
            markerText = (TextView) findViewById(R.id.locationMarkertext);
            Address = (TextView) findViewById(R.id.textLocation);
            txtNum = (TextView) findViewById(R.id.num);
            markerLayout = (LinearLayout) findViewById(R.id.locationMarker);

            smsUtils = new SmsUtils(getApplicationContext());

            //refresh point polilyne
            PublicData.points = new ArrayList<>();

            // Getting Google Play availability status
            int status = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(getBaseContext());

            if (status != ConnectionResult.SUCCESS) { // Google Play Services are
                // not available

                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                        requestCode);
                dialog.show();

            } else { // Google Play Services are available*/

                try {
                    // Loading map
                    stupMap();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Getting reference to the SupportMapFragment
                // Create a new global location parameters object
                mLocationRequest = LocationRequest.create();


                //* Set the update interval
                mLocationRequest.setInterval(GData.UPDATE_INTERVAL_IN_MILLISECONDS);

                // Use high accuracy
                mLocationRequest
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                // Set the interval ceiling to one minute
                mLocationRequest
                        .setFastestInterval(GData.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

                // Note that location updates are off until the user turns them on
                mUpdatesRequested = false;
                stupMap();

                //  Create a new location client, using the enclosing class to handle
                // callbacks.

                //mLocationClient = new LocationClient(this, this, this);
                //mLocationClient.connect();
            }



        Button btnRefresh = (Button) findViewById(R.id.btn_save);
        points.add(new LatLng(-7.312079, 112.731018));
        /*points.add(new LatLng(-7.313665, 112.729688));*/
       /* points.add(new LatLng(-7.313593, 112.728273));
        points.add(new LatLng(-7.312840, 112.728528));
        points.add(new LatLng(-7.311976, 112.726241));
        points.add(new LatLng(-7.310481, 112.726772));*/

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE);
        mGoogleMap.addPolyline(options.add(points.get(0)));



        //button click
        btnRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PolylineOptions options = new PolylineOptions().width(3).color(Color.BLUE);
                    /*for (int i = 0; i < points.size(); i++) {
                        //LatLng point = points.get(i);
                        options.add(points.get(i));
                    }*/

                mGoogleMap.addPolyline(options.addAll(PublicData.points));

            }
        });


    }


    private void stupMap() {
        try {
            LatLng latLong;
            // TODO Auto-generated method stub
            mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // Enabling MyLocation in Google Map
            //mGoogleMap.setMyLocationEnabled(true);
           /* if (mLocationClient.getLastLocation() != null) {
                latLong = new LatLng(mLocationClient.getLastLocation()
                        .getLatitude(), mLocationClient.getLastLocation()
                        .getLongitude());
                ShopLat = mLocationClient.getLastLocation().getLatitude() + "";
                ShopLong = mLocationClient.getLastLocation().getLongitude()
                        + "";


            } else {*/
            latLong = new LatLng(-7.313430, 112.728283);
            //}
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(16f).build();

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            mGoogleMap.clear();

            mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition arg0) {
                    // TODO Auto-generated method stub
                    center = mGoogleMap.getCameraPosition().target;

                    markerText.setText(" Lokasi Anda ");
                    mGoogleMap.clear();
                    markerLayout.setVisibility(View.VISIBLE);

                    try {
                        new GetLocationAsync(center.latitude, center.longitude)
                                .execute();

                    } catch (Exception e) {
                    }
                }
            });

            new Thread(new Runnable() {
                public void run() {

                    //update googlemap
                    PolylineOptions options = new PolylineOptions().width(3).color(Color.BLUE);
                    if( PublicData.points.size() > 0 )
                        mGoogleMap.addPolyline(options.addAll(PublicData.points));

                    /*char  charat;

                    ArrayList<Long> id;
                    ArrayList number;
                    ArrayList content;

                    String numberSms = "";

                    ArrayList sms = smsUtils.read();
                    id = ((ArrayList) sms.get(0));
                    number = ((ArrayList) sms.get(1));
                    content = ((ArrayList) sms.get(2));


                    for(int i=0;i<id.size();i++){
                        charat = number.get(i).toString().charAt(0);
                        if( charat == '0'  ){
                            numberSms = number.get(i).toString();
                        }else if( charat == '+' ){
                            numberSms = "0" + number.get(i).toString().substring(2);
                        }

                        if( PublicData.noHardware.equals(numberSms) ){
                            String body[] = content.get(i).toString().split("|");
                            if(body[0] == "monitoring"){
                                PublicData.points.add( new LatLng(Double.parseDouble(body[1]),Double.parseDouble(body[2])));

                                //update googlemap
                                PolylineOptions options = new PolylineOptions().width(3).color(Color.BLUE);
                                mGoogleMap.addPolyline(options.addAll(PublicData.points));

                                //delete sms
                                smsUtils.delete(getApplicationContext(),id.get(i));
                            }
                        }

                    }*/
                }
            }).start();
            /*markerLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try {

                        LatLng latLng1 = new LatLng(center.latitude,
                                center.longitude);

                        Marker m = mGoogleMap.addMarker(new MarkerOptions()
                                .position(latLng1)
                                .title(" Lokasi Anda ")
                                .snippet("")
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.location2)));
                        m.setDraggable(true);

                        markerLayout.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }

                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub



        redrawLine();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    private void redrawLine(){

        //mGoogleMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE);
        for (int i = 0; i < points.size(); i++) {
            //LatLng point = points.get(i);
            options.add(points.get(i));
        }

        line = mGoogleMap.addPolyline(options); //add Polyline
    }

    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;

            latlat = latitude;
            lnglng = longitude;
        }

        @Override
        protected void onPreExecute() {
            Address.setText(" Mendapatkan Lokasi... ");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(MonitoringActivity.this, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();


                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");

                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Address.setText(addresses.get(0).getAddressLine(0)
                        + addresses.get(0).getAddressLine(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}