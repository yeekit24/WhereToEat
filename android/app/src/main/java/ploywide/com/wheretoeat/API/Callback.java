package ploywide.com.wheretoeat.API;

import org.json.JSONObject;

public abstract class Callback {
    public abstract void onSuccess(JSONObject response);

    public abstract void onFail(String errorResponse);
}
