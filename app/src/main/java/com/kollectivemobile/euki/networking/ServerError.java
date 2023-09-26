package com.kollectivemobile.euki.networking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ServerError {

    private int status;
    private boolean success;
    private String message;

    public ServerError(String json) {
        message = "";
        if (json != null) {
            parseError(json);
        }
    }

    public ServerError() {
        this(null);
    }

    public void parseError(String json) {
        try {
            JSONObject jsonError = new JSONObject(json);
            if (jsonError.has("success")) {
                success = jsonError.getBoolean("success");
            }
            if (jsonError.has("status")) {
                status = jsonError.getInt("status");
            }
            if (jsonError.has("error")) {
                message = jsonError.getString("error");
            } else if (jsonError.has("errors")){
                JSONObject errors = jsonError.getJSONObject("errors");
                if (errors.has("message")) {
                    message += errors.getString("message");
                } else {
                    Iterator<String> it = errors.keys();
                    while (it.hasNext()) {
                        String key = it.next();
                        JSONArray errorArray = errors.getJSONArray(key);
                        message += key + " : " + errorArray.getString(0) + "\n";
                    }
                }
            }
        } catch (JSONException e) {
            message = "Could not parse error";
        }
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
