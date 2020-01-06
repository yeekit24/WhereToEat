package ploywide.com.wheretoeat.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ploywide.com.wheretoeat.API.APIHelper;
import ploywide.com.wheretoeat.API.Callback;
import ploywide.com.wheretoeat.R;
import ploywide.com.wheretoeat.adapter.ChoicesAdapater;
import ploywide.com.wheretoeat.app.AppController;
import ploywide.com.wheretoeat.model.ChoicesItem;

public class AddFoodChoiceActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Context myContext;

    Button randomButton,nearbyButton;
    FloatingActionButton addButton;
    RecyclerView choicesRecyclerView;
    EditText addItemField;

    ChoicesAdapater choicesAdapater;
    List<ChoicesItem> choicesItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_choices);

        myContext = this;
        toolbar = findViewById(R.id.toolbar);
        initializeToolbar();

        addButton = findViewById(R.id.addButton);
        nearbyButton = findViewById(R.id.nearbyButton);
        randomButton = findViewById(R.id.randomButton);
        nearbyButton.setOnClickListener(this);
        randomButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        addItemField = findViewById(R.id.addItemField);

        /* RecyclerView Stuff**/
        choicesRecyclerView = findViewById(R.id.choicesRecyclerView);
        final LinearLayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext());
        choicesRecyclerView.setLayoutManager(myLayoutManager);
        choicesAdapater = new ChoicesAdapater(this,choicesItems);
        choicesRecyclerView.setAdapter(choicesAdapater);

        /*Get the array list from Main Activity*/
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        ArrayList<ChoicesItem> arraylist = bundle.getParcelableArrayList("choiceArray");
        assert arraylist != null;
        Log.d("lol 123",arraylist.toString());

        insertDataToChoiceArray(arraylist);

        addButton.setEnabled(false);
        addItemField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addButton.setEnabled(true);
            }
        });

    }

    private void insertDataToChoiceArray(ArrayList<ChoicesItem> arraylist) {
        choicesItems.addAll(arraylist);
        choicesAdapater.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nearbyButton:
                requestFineLocation();
                break;
            case R.id.randomButton:
                onBackPressed();
                break;
            case R.id.addButton:
                addChoicesToList();
                break;
        }
    }

    private void requestFineLocation() {
        Dexter.withActivity(AddFoodChoiceActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            getLocationXandY();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

    }

    double userLatitude,userLongitude;
    private static final int SELECT_NEARBY_ITEM = 222;
    @SuppressLint("MissingPermission")
    private void getLocationXandY() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(myContext);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(AddFoodChoiceActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    userLatitude = location.getLatitude();
                    userLongitude = location.getLongitude();

                    getNearbyRestaurantList(userLatitude,userLongitude);

                    Log.d("UPLOAD", String.valueOf(userLatitude));
                    Log.d("UPLOAD", String.valueOf(userLongitude));
                } else {
                    Log.d("CreateActivity LOC", "LOCATION NULL");
                }

            }
        });
    }

    private void getNearbyRestaurantList(double userLatitude, double userLongitude) {
        Intent intent = new Intent(AddFoodChoiceActivity.this,SuggestNearbyListActivity.class);
        intent.putExtra("userLatitude",userLatitude);
        intent.putExtra("userLongitude",userLongitude);
        startActivityForResult(intent,SELECT_NEARBY_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_NEARBY_ITEM){
            Bundle bundle;
            if (data != null) {
                bundle = data.getExtras();
                ArrayList<ChoicesItem> intentData = null;
                if (bundle != null) {
                    intentData = bundle.getParcelableArrayList("choiceArray");
                }
                if (intentData != null) {
                    Log.d("lol 123",intentData.toString());
                    updateChoiceList(intentData);

                }
            }
        }

    }

    private void updateChoiceList(ArrayList<ChoicesItem> intentData) {
        choicesItems.addAll(0,intentData);
        choicesAdapater.notifyDataSetChanged();
    }

    private void addChoicesToList() {
        final String choice = addItemField.getText().toString().trim();

        if(!choice.equals("")){
            APIHelper.create_choice(getApplicationContext(), AppController.user_id, choice, "", new Callback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        ChoicesItem item = new ChoicesItem(choice,0, response.getLong("choice_id"));
                        choicesItems.add(0,item);
                        choicesAdapater.notifyDataSetChanged();
                        addItemField.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(String error) {
                    ChoicesItem item = new ChoicesItem(choice,0, -1);
                    choicesItems.add(0,item);
                }
            });
        }
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

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodChoiceActivity.this);
        builder.setTitle(getResources().getString(R.string.need_permission));
        builder.setMessage(getResources().getString(R.string.location_permission_needed));
        builder.setPositiveButton(getResources().getString(R.string.go_to_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

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
        String shareBodyText = "Hey there,I using WhereToEat Android app now." + " " +"Join me?"
                +System.getProperty("line.separator") + System.getProperty("line.separator")
                + "Download WhereToEat today!" + System.getProperty("line.separator")
                + "https://play.google.com/store/apps/details?id=ploywide.com.wheretoeat" +System.getProperty("line.separator")+"Powered by PloyWide";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }


}
