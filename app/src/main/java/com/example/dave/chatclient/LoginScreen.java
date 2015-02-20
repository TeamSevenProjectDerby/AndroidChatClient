package com.example.dave.chatclient;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class LoginScreen extends ActionBarActivity {

    GlobalResources global;
    EditText nameField;
    TextView gpsMsg;
    double tempLat = 0.0;
    double tempLong = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        gpsMsg = (TextView)findViewById(R.id.waitingForGPSview);

        LocationManager locatM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener locatL = new locatL();
        locatM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locatL);

        global = (GlobalResources)getApplicationContext();
        initializeControls();
    }

    class locatL implements LocationListener{

        //Called whenever the users location changes, updates their longitude
        //and latitude
        @Override
        public void onLocationChanged(Location location) {
            if (location != null)
            {
                tempLat = location.getLatitude();
                tempLong = location.getLongitude();
            }
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
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

    //Links all activity controls to variables
    public void initializeControls() {
        nameField = (EditText)findViewById(R.id.editUserName);
    }

    //Creates a socket with the server and sends the login name before starting the chatroom activity
    public void login(View view) {
        String name = nameField.getText().toString();
        while(tempLat == 0 && tempLong == 0)
        {
            gpsMsg.setText("Waiting for GPS to find your location...");
        }
        double longitude = tempLong;  //Get the longitude value
        double latitude = tempLat; //Get the Latitude value
        if (nameField.getText() != null) {
            global.setUserName(name);
            global.setLongitude(longitude);
            global.setLatitude(latitude);
            global.initSocket();
            Intent intent = new Intent(this, ChatRoom.class);
            startActivity(intent);
        }
    }
}
