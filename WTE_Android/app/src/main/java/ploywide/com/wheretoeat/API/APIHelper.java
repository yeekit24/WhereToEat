package ploywide.com.wheretoeat.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ploywide.com.wheretoeat.app.AppController;


@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class APIHelper {

    private static void callToAPIServer(final Context mContext, String URL, int method, JSONObject request, final Callback callback) {
        JsonObjectRequest jsonReq = new JsonObjectRequest(method, URL , request,
                response -> responseHandler(response, callback), error -> VolleyLog.d(AppController.TAG, "Error: " + error.getMessage())) {
            //adding header to authenticate
            @Override
            public Map<String, String> getHeaders() {
                if (method != 3) {
                    Map<String,String> headers = new HashMap<>();

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String apiKey = preferences.getString("apiKey","");
                    headers.put("Content-Type", "application/json");
                    headers.put("mime-type", "application/json");
                    //headers.put("authorization",apiKey);

                    //todo :: remove this dummy authorization later
                    //headers.put("authorization","6b266dd38bc829648cc6248d16670786");
                    return  headers;
                }
                else {
                    return new HashMap<>();
                }
            }
        };

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private static void responseHandler(JSONObject response, Callback mCallback) {
        try {
            int errorCode = response.getInt("error_code");

            if (errorCode == 0) {

                JSONObject jObj = response.getJSONObject("return_data");
                mCallback.onSuccess(jObj);

            }else{
                String errorMessage = response.getString("error_message");
                mCallback.onFail("Error Code "+ errorCode+ ": " +errorMessage);
            }
        } catch (JSONException e) {
            mCallback.onFail(e.toString());
            e.printStackTrace();
        }
    }

    public static void init_connection(final Context mContext, String android_id, int loginDeviceType, final Callback mCallback) {
        Map<String, java.io.Serializable> request = new HashMap<>();
        request.put("device_guid", android_id);
        request.put("login_device_type_id", loginDeviceType);

        callToAPIServer(mContext, AppConfig.URL_INIT_CONNECTION, Request.Method.POST, new JSONObject(request), mCallback);
    }

    public static void get_choices(final Context mContext, String user_id, final Callback mCallback) {

        callToAPIServer(mContext, AppConfig.URL_GET_CHOICES+"?user_id="+user_id, Request.Method.GET, null, mCallback);
    }

    public static void spin(final Context mContext, String user_id, long selected_choice_id) {
        Map<String, java.io.Serializable> request = new HashMap<String, java.io.Serializable>();
        request.put("user_id", user_id);
        request.put("selected_choice_id", selected_choice_id);
        callToAPIServer(mContext, AppConfig.URL_SPIN, Request.Method.POST, new JSONObject(request), new Callback() {
            @Override
            public void onSuccess(JSONObject response) {
            }

            @Override
            public void onFail(String error) {
            }
        });
    }

    public static void create_choice(final Context mContext, String user_id, String choice_name, String choice_desc, final Callback mCallback) {
        Map<String, String> request = new HashMap<>();
        request.put("user_id", user_id);
        request.put("choice_name", choice_name);
        request.put("choice_desc", choice_desc);
        callToAPIServer(mContext, AppConfig.URL_CREATE_CHOICES, Request.Method.POST, new JSONObject(request), mCallback);
    }

    public static void delete_choice(final Context mContext, long choice_id, String user_id, final Callback mCallback) {
        Map request = new HashMap();
        request.put("user_id", user_id);
        callToAPIServer(mContext, AppConfig.URL_DELETE_CHOICE+choice_id, Request.Method.DELETE, null, mCallback);
    }

    public static void get_choice_profile(final Context mContext, String user_id, final Callback mCallback) {
        Map request = new HashMap();
        request.put("user_id", user_id);
        callToAPIServer(mContext, AppConfig.URL_GET_CHOICE_PROFILE, Request.Method.GET, new JSONObject(request), mCallback);
    }

    public static void create_choice_profile(final Context mContext, String user_id, String choice_profile_name, String choice_profile_desc, String longtitude, String latitude, final Callback mCallback) {
        Map<String, String> request = new HashMap<>();
        request.put("user_id", user_id);
        request.put("choice_profile_name", choice_profile_name);
        request.put("choice_profile_desc", choice_profile_desc);
        request.put("longtitude", longtitude);
        request.put("latitude", latitude);
        callToAPIServer(mContext, AppConfig.URL_CREATE_CHOICE_PROFILE, Request.Method.POST, new JSONObject(request), mCallback);
    }
}
