package ploywide.com.wheretoeat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import ploywide.com.wheretoeat.API.APIHelper;
import ploywide.com.wheretoeat.API.Callback;
import ploywide.com.wheretoeat.R;
import ploywide.com.wheretoeat.app.AppController;

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar mProgressBar;

    private void getRegisterDetails() {
        //get the android id
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("androidId", android_id);

        int loginDeviceType = 1;

        APIHelper.init_connection(getApplicationContext(), android_id, loginDeviceType, new Callback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    AppController.user_id = response.getString("user_id");
                    AppController.user_device_id = response.getString("user_device_id");
                    mProgressBar.setProgress(60);
                    startApp();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String error) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //MobileAds.initialize(this, getString(R.string.admob_app_id));

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setProgress(30);
        getRegisterDetails();
    }

    private void startApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
