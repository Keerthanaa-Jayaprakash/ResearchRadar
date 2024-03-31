package com.example.researchradar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.researchradar.UtilsService.UtilService;
import com.example.researchradar.UtilsService.SharedPreferenceClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserProfile extends Fragment {
    Button test,logout;
    String email;
    UtilService utilService;
    SharedPreferenceClass sharedPreferences;
    RelativeLayout progress;
    Context thiscontext;
    Bitmap qrBits;
    ImageView qr;
    TextView username, name, phone;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_user_profile, container, false);
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        thiscontext = container.getContext();
//        FirebaseMessaging.getInstance().subscribeToTopic("user");


        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.clear();
//                startActivity(new Intent(getActivity(), LoginPage.class));
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("finish", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });


        utilService = new UtilService();
        sharedPreferences = new SharedPreferenceClass(getContext());
        progress = view.findViewById(R.id.progress);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.phone);
        email = sharedPreferences.getValue_string("email");
        Log.d("Dashboard", "Shared Preference Value"+email);
        progress.setVisibility(View.VISIBLE);
        dashboard(view);
        return view;

    }
    private void dashboard (View view){
        String BaseURL = utilService.IPAddr();
        String Route = "users/userProfile";
        String apiKey = BaseURL+Route;

        final HashMap<String, String> params = new HashMap<>();
        params.put("email", email);


//        String IP = utilService.IPAddr().toString();
//        Log.d("AccounT", "IP Address"+IP.toString());
//        Toast.makeText(getContext(), "IP "+IP, Toast.LENGTH_SHORT).show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(JSONObject response) {
                Log.d("LoginPage", "onResponse: MySightings response"+response.toString());

//
//                try {
//                    Log.d("LoginPage", "onResponse: User"+response.get("user").get);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


                try {
                    if(response.getBoolean("success")) {
//                        String IP = utilService.IPAddr();
//                        Toast.makeText(getContext(), "IP "+IP, Toast.LENGTH_SHORT).show();

                        String User = response.getString("user");
//                        JSONArray jsonArray = response.getJSONArray("user");
                        JSONObject jsonObject = response.getJSONObject("user");
                        Log.d("LoginPage", "onResponse: User"+jsonObject.get("email"));
//                        String namee = (String) jsonObject.get("username");
                        name.setText((String) jsonObject.get("name"));
                        phone.setText(String.valueOf(jsonObject.get("phone")));
                        username.setText((String) jsonObject.get("email"));

                        progress.setVisibility(View.GONE);






                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Dashboard", "onResponse: MySightings Error"+error.toString());

//                Toast.makeText(LoginPage.this, error.networkResponse.toString(), Toast.LENGTH_SHORT).show();
                NetworkResponse response = error.networkResponse;
                if(error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers,  "utf-8"));

                        JSONObject obj = new JSONObject(res);
//                        Toast.makeText(LoginPage.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                        utilService.showSnackBar(view, obj.getString("msg"));

                    } catch (JSONException | UnsupportedEncodingException je) {
                        je.printStackTrace();

                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return params;
            }
        };

        // set retry policy
        int socketTime = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTime,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // request add
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

}