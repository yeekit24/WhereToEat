package ploywide.com.wheretoeat.Helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ploywide.com.wheretoeat.Interface.PlacesCallback;
import ploywide.com.wheretoeat.app.AppController;
import ploywide.com.wheretoeat.model.PlacesItem;

public class ApiHelper {

    public void retrieveNearbyPlacesListFromGoogleMap(String userLatitude, String userLongitude, String radius, String placeType, String googleMapApiKey, final PlacesCallback placesCallback) {

        //String URL1= "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=4.5286853,101.1260501&radius=1500&type=restaurant&key=AIzaSyBqCQN-YgOtB8R7X0KqvA8tD7RQXCY1Dzs";

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + userLatitude + "," + userLongitude + "&radius=" + radius + "&type=" + placeType + "&key=" + googleMapApiKey;

        Log.d("Place URL", url);
        Log.d("Place URL", "calling");

            StringRequest jsonReq = new StringRequest(Request.Method.GET,
                url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);

                            new Parser().parsePlacesList(jObj, new PlacesCallback() {
                                        @Override
                                        public void success(List<PlacesItem> placesItems) {

                                            placesCallback.success(placesItems);
                                        }

                                        @Override
                                        public void showError(String error) {
                                            placesCallback.showError(error);
                                        }
                                    }
                            );


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(AppController.TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

}
