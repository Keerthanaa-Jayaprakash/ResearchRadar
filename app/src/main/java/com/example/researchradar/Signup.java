package com.example.researchradar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.toolbox.JsonObjectRequest;

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
import com.android.volley.toolbox.Volley;
//import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.researchradar.UtilsService.UtilService;
import com.example.researchradar.UtilsService.SharedPreferenceClass;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class Signup extends AppCompatActivity {
    Button login;
    EditText name_ET, email_ET, password_ET, phone_ET;
    private String name, email, password, phone;
    Button signup;
    UtilService utilService;
    SharedPreferenceClass sharedPreferenceClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, MainActivity.class));

            }
        });

        name_ET = (EditText) findViewById(R.id.name);
        email_ET = (EditText) findViewById(R.id.username);
        password_ET = (EditText) findViewById(R.id.password);
        phone_ET = (EditText) findViewById(R.id.phonenum);

        signup = (Button) findViewById(R.id.register);
        Log.d("Debug", "Out Onclick");
        utilService = new UtilService();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", "In Onclick");
                name = name_ET.getText().toString();
                email = email_ET.getText().toString();
                phone = phone_ET.getText().toString();
                password = password_ET.getText().toString();
                Log.d("Debug", "In Onclick2");

//                if(validate(v))
                utilService.hideKeyboard(v, Signup.this);

                signupUser(v);

            }
        });
    }
    private void signupUser(View view){
        String BaseURL = utilService.IPAddr();
//        String BaseURL = "http://192.0.0.2:4444/";
        String Route = "users/signup";

        String apiKey = BaseURL+Route;

//        Log.d("TokenTest","Before Hash"+notificationToken);
        final HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("password", password);
        params.put("email", email);
        params.put("phone", phone);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("LoginPage", "onResponse: MySightings response"+response.toString());

                try {
                    if(response.getBoolean("success")) {
                        sharedPreferenceClass.setValue_string("email", email);
                        sharedPreferenceClass.setValue_string("name", name);
                        sharedPreferenceClass.setValue_string("phone", phone);
                        startActivity(new Intent(Signup.this, MainActivity.class));


                    }
//                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
//                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if(error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers,  "utf-8"));

                        JSONObject obj = new JSONObject(res);
                        utilService.showSnackBar(view, obj.getString("msg"));

//                        progressBarar.setVisibility(View.GONE);
                    } catch (JSONException | UnsupportedEncodingException je) {
                        je.printStackTrace();
//                        progressBararsBar.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }

}