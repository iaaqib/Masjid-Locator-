package com.kodesnippets.aaqib.locator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class MapsActivity extends Activity implements LocationListener {
    //instance variables for Marker icon drawable resources
    int userIcon, masjidIcon, drinkIcon, shopIcon, otherIcon;

    //the map
    //the map
    private GoogleMap theMap;
    private Button start;

    //location manager
    private LocationManager locMan;

    //user marker
    private Marker userMarker;

    //places of interest
    private Marker[] placeMarkers;
    //max
    private final int MAX_PLACES = 20;//most returned from google
    //marker options
    private MarkerOptions[] places;

    private boolean updateFinished = true;

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SalahTimings salahTimings = new SalahTimings();
        locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        salahTimings.checkIfGPSIsOn(locMan,this);
        //find out if we already have it
        if(theMap==null){
            //get the map
            theMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
            //check in case map/ Google Play services not available
            if(theMap!=null){
                //ok - proceed
                theMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //create marker array
                placeMarkers = new Marker[MAX_PLACES];
                //update location
             //   locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
            }
        }
    }

    //location listener functions

    @Override
    public void onLocationChanged(Location location) {
        Log.v("MyMapActivity", "location changed");
        Log.v("Test", "location change block");
        updatePlaces();
    }
    @Override
    public void onProviderDisabled(String provider){
        Log.v("MyMapActivity", "provider disabled");
    }
    @Override
    public void onProviderEnabled(String provider) {
        Log.v("MyMapActivity", "provider enabled");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v("MyMapActivity", "status changed");
    }

    /*
     * update the place markers
     */
    private void updatePlaces(){
        //get location manager
        //get last location
        Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double lat = lastLoc.getLatitude();
        double lng = lastLoc.getLongitude();
        //create LatLng
        LatLng lastLatLng = new LatLng(lat, lng);

        //remove any existing marker
        if(userMarker!=null) userMarker.remove();
        //create and set marker properties
        userMarker = theMap.addMarker(new MarkerOptions()
                .position(lastLatLng)
                .title("You are here")
                .snippet("Your last recorded location"));
        //move to location
        theMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,15));

        //build places query string
        String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location="+lat+","+lng+
                "&radius=1000&sensor=true" +
                "&types=mosque"+
                "&key=AIzaSyBaHi5-1UjGZSafHat45t_w0EkVVCmn8RU";//ADD KEY

        //execute query
        new GetPlaces().execute(placesSearchStr);
    }

    private class GetPlaces extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch places
            updateFinished = false;
            StringBuilder placesBuilder = new StringBuilder();
            for (String placeSearchURL : placesURL) {
                try {

                    URL requestUrl = new URL(placeSearchURL);
                    HttpURLConnection connection = (HttpURLConnection)requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        BufferedReader reader = null;

                        InputStream inputStream = connection.getInputStream();
                        if (inputStream == null) {
                            return "";
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = reader.readLine()) != null) {

                            placesBuilder.append(line + "\n");
                        }

                        if (placesBuilder.length() == 0) {
                            return "";
                        }

                        Log.d("test", placesBuilder.toString());
                    }
                    else {
                        Log.i("test", "Unsuccessful HTTP Response Code: " + responseCode);
                    }
                } catch (MalformedURLException e) {
                    Log.e("test", "Error processing Places API URL", e);
                } catch (IOException e) {
                    Log.e("test", "Error connecting to Places API", e);
                }
            }
            return placesBuilder.toString();
        }

        //process data retrieved from doInBackground
        protected void onPostExecute(String result) {
            //parse place data returned from Google Places
            //remove existing markers
            if (placeMarkers != null) {
                for (int pm = 0; pm < placeMarkers.length; pm++) {
                    if (placeMarkers[pm] != null)
                        placeMarkers[pm].remove();
                }
            }
            try {
                //parse JSON

                //create JSONObject, pass stinrg returned from doInBackground
                JSONObject resultObject = new JSONObject(result);
                //get "results" array
                JSONArray placesArray = resultObject.getJSONArray("results");
                //marker options for each place returned
                places = new MarkerOptions[placesArray.length()];
                //loop through places

                Log.d("test", "The placesArray length is " + placesArray.length() + "...............");

                for (int p = 0; p < placesArray.length(); p++) {
                    //parse each place
                    //if any values are missing we won't show the marker
                    boolean missingValue = false;
                    LatLng placeLL = null;
                    String placeName = "";
                    String vicinity = "";
                    int currIcon = otherIcon;
                    try {
                        //attempt to retrieve place data values
                        missingValue = false;
                        //get place at this index
                        JSONObject placeObject = placesArray.getJSONObject(p);
                        //get location section
                        JSONObject loc = placeObject.getJSONObject("geometry")
                                .getJSONObject("location");
                        //read lat lng
                        placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
                                Double.valueOf(loc.getString("lng")));
                        //get types
                        JSONArray types = placeObject.getJSONArray("types");
                        //loop through types
                        for(int t=0; t<types.length(); t++){
                            //what type is it
                            String thisType=types.get(t).toString();
                            //check for particular types - set icons
                            if(thisType.contains("mosque")){
                                //			currIcon = masjidIcon;
                                break;
                            }
                            else if(thisType.contains("health")){
                                //	currIcon = drinkIcon;
                                break;
                            }
                            else if(thisType.contains("doctor")){
                                //	currIcon = shopIcon;
                                break;
                            }
                        }
                        //vicinity
                        vicinity = placeObject.getString("vicinity");
                        //name
                        placeName = placeObject.getString("name");
                    } catch (JSONException jse) {
                        Log.v("PLACES", "missing value");
                        missingValue = true;
                        jse.printStackTrace();
                    }
                    //if values missing we don't display
                    if (missingValue) places[p] = null;
                    else
                        places[p] = new MarkerOptions()
                                .position(placeLL)
                                .title(placeName)
                                .snippet(vicinity).icon(BitmapDescriptorFactory.fromResource(R.drawable.mosque));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (places != null && placeMarkers != null) {
                Log.d("test", "The placeMarkers length is " + placeMarkers.length + "...............");

                for (int p = 0; p < places.length && p < placeMarkers.length; p++) {
                    //will be null if a value was missing

                    if (places[p] != null) {

                        placeMarkers[p] = theMap.addMarker(places[p]);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(theMap!=null){
            locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(theMap!=null){
            locMan.removeUpdates(this);
        }
    }
}