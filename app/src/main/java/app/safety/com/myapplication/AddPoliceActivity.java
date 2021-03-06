package app.safety.com.myapplication;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.json.JSONException;

import app.safety.com.R;
import core.*;

public class AddPoliceActivity extends FragmentActivity implements LocationListener {
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
    private TextView txtLocation;
    private TextView txtContact;
    private LinearLayout btnCall;

    private double latlat;
    private double lnglng;

    Api api = null;
    Bundle b;

    boolean EDITABLE = false;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_police);
        api = new Api(Static.API_KEY);



        markerText = (TextView) findViewById(R.id.locationMarkertext);
        Address = (TextView) findViewById(R.id.textLocation);
        txtNum = (TextView) findViewById(R.id.textKontak);
        markerLayout = (LinearLayout) findViewById(R.id.locationMarker);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtContact = (TextView) findViewById(R.id.txtContact);
        btnCall = (LinearLayout) findViewById(R.id.call);
        final Button btnSave = (Button) findViewById(R.id.btn_save);

        b = getIntent().getExtras();
        if(!b.isEmpty()){
            EDITABLE = true;
            //menu.findItem(R.id.edit_button).setVisible(true);

            /*edit text invisible*/
            Address.setVisibility(View.INVISIBLE);
            txtNum.setVisibility(View.INVISIBLE);

            txtLocation.setVisibility(View.VISIBLE);
            txtContact.setVisibility(View.VISIBLE);

            txtLocation.setText(b.getString("address"));
            txtContact.setText(b.getString("number"));
        }

        /*btnCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String num = ((TextView) v.findViewById(R.id.txtContact)).getText().toString();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + num));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });*/

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

        //button click
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.Police police = new Api.Police();
                police.address = Address.getText().toString();
                police.num = txtNum.getText().toString();
                police.lat = latlat;
                police.lng = lnglng;

                btnSave.setText("Menyimpan...");

                if(!EDITABLE)
                    api.pushPolice(police, new Api.Callback<Api.Police>() {
                        @Override
                        public Void success(Api.Police params) throws JSONException {
                            btnSave.setText("Berhasil");
                            Intent i = new Intent(getApplicationContext(),PoliceAcitvity.class);
                            startActivity(i);
                            return null;
                        }

                        @Override
                        public Void failed(String msg) {
                            Toast.makeText(getApplicationContext(), "ERROR : " + msg , Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    });
                else {
                    police.id = b.getString("id");
                    api.updatePolice(police, new Api.Callback<Api.Police>() {
                        @Override
                        public Void success(Api.Police params) throws JSONException {
                            btnSave.setText("Berhasil");
                            Intent i = new Intent(getApplicationContext(), PoliceAcitvity.class);
                            startActivity(i);
                            return null;
                        }

                        @Override
                        public Void failed(String msg) {
                            Toast.makeText(getApplicationContext(), "ERROR : " + msg, Toast.LENGTH_SHORT).show();
                            return null;
                        }

                    });
                }
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
            if( EDITABLE )latLong = new LatLng(Double.parseDouble(b.getString("lat")),Double.parseDouble(b.getString("lng")));
            else latLong = new LatLng(-7.257472, 112.752088);
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

            if(!EDITABLE) mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            mGoogleMap.clear();

            mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition arg0) {
                    // TODO Auto-generated method stub
                    center = mGoogleMap.getCameraPosition().target;

                    markerText.setText(" Lokasi Polisi ");
                    mGoogleMap.clear();
                    markerLayout.setVisibility(View.VISIBLE);

                    try {
                        new GetLocationAsync(center.latitude, center.longitude)
                                .execute();

                    } catch (Exception e) {
                    }
                }
            });

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
                geocoder = new Geocoder(AddPoliceActivity.this, Locale.ENGLISH);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_detail_police, menu);

        menu.findItem(R.id.edit_button).setVisible(false);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_button:
                /*edit text true*/
                /*txtNum.setVisibility(View.VISIBLE);
                Address.setVisibility(View.VISIBLE);
                txtNum.setText(b.getString("number"));
                Address.setText(b.getString("address"));

                *//*text view false*//*
                txtLocation.setVisibility(View.INVISIBLE);
                txtContact.setVisibility(View.INVISIBLE);*/

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}