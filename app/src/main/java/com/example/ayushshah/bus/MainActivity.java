package com.example.ayushshah.bus;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MapsActivity.class.getSimpleName();

    List<Address> geocodeMatches = null;
    String Address1;
    String Address2;
    String State;
    String Zipcode;
    String Country;
    Double sLat, sLng, dLat, dLng;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        setContentView(R.layout.activity_maps);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void go(View view){
        EditText dst = (EditText)findViewById(R.id.dstText);
        String destination = dst.getText().toString();
        Geocoder coder = new Geocoder(this);
        Toast.makeText(getApplicationContext(), "Alive", Toast.LENGTH_LONG).show();
        try {
            ArrayList<Address> addresses = (ArrayList<Address>) coder
                    .getFromLocationName(destination, 100);
            for (Address add : addresses) {
                dLat = add.getLatitude();
                dLng = add.getLongitude();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent;
        intent = new Intent(this, MapsActivity.class);
      /*  Bundle b= new Bundle();

        b.putDouble("srcLat", sLat);
        b.putDouble("srcLng", sLng);
        b.putDouble("dstLat", dLat);
        b.putDouble("dstLng", dLng);
        intent.putExtras(b);
      */
        intent.putExtra("srcLat", sLat.toString());
        intent.putExtra("srcLng", sLng.toString());
        intent.putExtra("dstLat", dLat.toString());
        intent.putExtra("dstLng", dLng.toString());
     /*   arr.add(sLng.toString());
        arr.add(dLat.toString());
        arr.add(dLng.toString());*/

        Toast.makeText(getApplicationContext(), "S " + sLat.toString() + " " + sLng.toString() + "D " + dLat.toString() + " " + dLng.toString(), Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        sLat = location.getLatitude();
        sLng = location.getLongitude();
        try {
            geocodeMatches =
                    new Geocoder(this).getFromLocation(sLat, sLng, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!geocodeMatches.isEmpty())
        {
            Address1 = geocodeMatches.get(0).getAddressLine(0);
            Address2 = geocodeMatches.get(0).getAddressLine(1);
            State = geocodeMatches.get(0).getAdminArea();
            Zipcode = geocodeMatches.get(0).getPostalCode();
            Country = geocodeMatches.get(0).getCountryName();
        }
        TextView source = (TextView)findViewById(R.id.srcText);
        source.setText(Address1+" "+Address2);
        //Toast.makeText(getApplicationContext(), Address1 + Address2 + State, Toast.LENGTH_LONG).show();
        if (location == null) {
            // Blank for a moment...
        }
        else {
            handleNewLocation(location);
        };
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
