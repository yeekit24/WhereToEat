package ploywide.com.wheretoeat.Helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ploywide.com.wheretoeat.Interface.PlacesCallback;
import ploywide.com.wheretoeat.model.PlacesItem;

class Parser {
    private List<PlacesItem> placesItems = new ArrayList<>();

    void parsePlacesList(JSONObject response, PlacesCallback placesCallback){

        try {
            JSONArray results = response.getJSONArray("results");
            String name,vicinity;
            for(int i= 0;i < results.length();i++) {
                JSONObject details = results.getJSONObject(i);
                name = details.getString("name");
                vicinity = details.getString("vicinity");

                setAllDetailsToPlacesItem(name,vicinity);
            }
            placesCallback.success(placesItems);

        } catch (JSONException e) {
            placesCallback.showError("Unknown error");
            e.printStackTrace();
        }
    }

    private void setAllDetailsToPlacesItem(String name, String vicinity) {
        PlacesItem item = new PlacesItem();
        item.setPlaceName(name);
        item.setPlaceAddress(vicinity);
        placesItems.add(item);
    }
}
