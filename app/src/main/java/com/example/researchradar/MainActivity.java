package com.example.researchradar;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.researchradar.UtilsService.UtilService;
import com.example.researchradar.UtilsService.SharedPreferenceClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private Button login;
    private EditText  email_ET, password_ET;
    Button createAccount;
    //    String email, password;
    ProgressBar progressBar;
    private String email,password;
    UtilService utilService;
    SharedPreferenceClass sharedPreferences;
    String notificationToken;
    TextView credits;
    SharedPreferenceClass sharedPreferenceClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        login = (Button) findViewById(R.id.login);
        email_ET = (EditText) findViewById(R.id.username);
        password_ET = (EditText) findViewById(R.id.password);
//        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        utilService = new UtilService();
        createAccount = (Button) findViewById(R.id.register);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Signup.class));
//                Intent i = new Intent(getApplicationContext(),Signup.class);
//                startActivity(i);


            }

        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_ET.getText().toString();
                password = password_ET.getText().toString();

                Login(v);
            }
        });
    }
    private void Login(View view){
        String BaseURL = utilService.IPAddr();
//        String BaseURL = "http://192.0.0.2:4444/";
        String Route = "users/login";

        String apiKey = BaseURL+Route;
        Log.d("Arun","APIKEY "+apiKey);
//        Log.d("TokenTest","Before Hash"+notificationToken);
        final HashMap<String, String> params = new HashMap<>();
        params.put("password", password);
        params.put("email", email);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("LoginPage", "onResponse: MySightings response"+response.toString());

                try {
                    if(response.getBoolean("success")) {
//                        String AccountType = response.getString("accountType");
//                        String RegNo = response.getString("regno");
//                        String token = response.getString("msg");
                        String User = response.getString("user");
                        JSONObject jsonObject = response.getJSONObject("user");
//                                sharedPreferenceClass.setValue_string("dept", jsonObject.get("dept").toString());

                        sharedPreferenceClass.setValue_string("email", jsonObject.get("email").toString());
                        sharedPreferenceClass.setValue_string("name", jsonObject.get("name").toString());
                        sharedPreferenceClass.setValue_string("phone", jsonObject.get("phone").toString());
//                        startActivity(new Intent(MainActivity.this, Signup.Navigation.class));


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