package ploywide.com.wheretoeat.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ploywide.com.wheretoeat.API.APIHelper;
import ploywide.com.wheretoeat.API.Callback;
import ploywide.com.wheretoeat.Helper.ApiHelper;
import ploywide.com.wheretoeat.Interface.PlacesCallback;
import ploywide.com.wheretoeat.R;
import ploywide.com.wheretoeat.adapter.PlacesAdapter;
import ploywide.com.wheretoeat.app.AppController;
import ploywide.com.wheretoeat.model.ChoicesItem;
import ploywide.com.wheretoeat.model.PlacesItem;

public class SuggestNearbyListActivity extends AppCompatActivity {

    PlacesAdapter placesAdapter;
    List<PlacesItem> placesItems = new ArrayList<>();
    List<ChoicesItem> choicesItems = new ArrayList<>();
    RecyclerView placesRecyclerView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_nearby_list);

        double userLatitude = getIntent().getDoubleExtra("userLatitude",0.0);
        double userLongitude = getIntent().getDoubleExtra("userLongitude",0.0);

        Log.d("Location 123",String.valueOf(userLatitude));
        Log.d("Location 123",String.valueOf(userLongitude));

        toolbar = findViewById(R.id.toolbar);
        initializeToolbar();

        /* RecyclerView Stuff**/
        placesRecyclerView = findViewById(R.id.placesRecyclerView);
        final LinearLayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext());
        placesRecyclerView.setLayoutManager(myLayoutManager);
        placesAdapter = new PlacesAdapter(this, placesItems, new PlacesAdapter.OnPlaceAddedListener() {
            @Override
            public void onPlaceAdded(String placeName) {
                saveItemToChoiceArray(placeName);
            }
        });
        placesRecyclerView.setAdapter(placesAdapter);

        getNearbyRestaurantFromGoogleMap(userLatitude,userLongitude);

    }

    private void saveItemToChoiceArray(String placeName) {
        APIHelper.create_choice(getApplicationContext(), AppController.user_id, placeName, "", new Callback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    ChoicesItem item = new ChoicesItem(placeName,0, response.getLong("choice_id"));
                    choicesItems.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                };
            }

            @Override
            public void onFail(String error) {
                ChoicesItem item = new ChoicesItem(placeName,0, -1);
                choicesItems.add(item);
            }
        });
    }

    private void getNearbyRestaurantFromGoogleMap(double userLatitude, double userLongitude) {
        String apiKey = getResources().getString(R.string.google_map_api_key);
        String radiusString = String.valueOf(2000);
        String userLatitudeString = String.valueOf(userLatitude);
        String userLongitudeString = String.valueOf(userLongitude);

        new ApiHelper().retrieveNearbyPlacesListFromGoogleMap(userLatitudeString, userLongitudeString, radiusString, "restaurant", apiKey, new PlacesCallback() {
            @Override
            public void success(List<PlacesItem> placesItemsList) {
                updatePlaceListToUI(placesItemsList);
            }

            @Override
            public void showError(String error) {
                Toast.makeText(SuggestNearbyListActivity.this,error,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updatePlaceListToUI(List<PlacesItem> placesItemsList) {
        placesItems.addAll(placesItemsList);
        placesAdapter.notifyDataSetChanged();
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Pick your food");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(toolbar.getNavigationIcon() != null)  {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("choiceArray", (ArrayList<? extends Parcelable>) choicesItems);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu_white,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                shareLinkToOtherApp();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareLinkToOtherApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //String shareSubText = "Today I will eat at";
        String shareBodyText = "Hey there,I using WhereToEat Android app now." + " " + "Join me?"
                +System.getProperty("line.separator") + System.getProperty("line.separator")
                + "Download WhereToEat today!" + System.getProperty("line.separator")
                + "https://play.google.com/store/apps/details?id=ploywide.com.wheretoeat"+System.getProperty("line.separator")+"Powered by PloyWide";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }
}
