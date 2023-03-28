package com.example.weatherpodcastapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.weatherpodcastapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String SearchText="";
    //private ActivityMapsBinding binding;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String Latitude="";
    private String Longitude="";
    private String City="";
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.SearchBtn);
        Button button2 = (Button) findViewById(R.id.ClearBtn);
        EditText editText = (EditText) findViewById(R.id.EditSeach);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchText = String.valueOf(editText.getText());
                Log.d("tag", SearchText);
                getData(SearchText);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
            }
        });
    }

    private void getData(String SearchText) {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        String key = "Your Api Key";
        String url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+SearchText+"/tomorrow?unitGroup=metric&key="+key+"&contentType=json";
        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(), "Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                Log.i(TAG, "Response :" + response.toString());
                try {
                    JSONObject JObject = new JSONObject(response);
                    Latitude = JObject.getString("latitude");
                    Longitude = JObject.getString("longitude");
                    City = JObject.getString("address");
                    JSONArray jArray = JObject.getJSONArray("days");
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    LatLng latLng = new LatLng(Double.parseDouble(Latitude), Double.parseDouble(Longitude));

                    // on below line we are adding marker to that position.
                    mMap.addMarker(new MarkerOptions().position(latLng).title(City));

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}