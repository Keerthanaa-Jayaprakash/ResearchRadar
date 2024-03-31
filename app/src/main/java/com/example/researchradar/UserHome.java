package com.example.researchradar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.researchradar.PaperAdapter;
import com.example.researchradar.UtilsService.UtilService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHome extends Fragment {
    Button searchBT;
    EditText keywordsET;
    String keyword;
    UtilService utilService;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;

    RecyclerView recycler;
    List<Papers> modelList;
    RecyclerView.Adapter adapter;
    View view;
 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     view = inflater.inflate(R.layout.fragment_user_home, container, false);

     searchBT = view.findViewById(R.id.search);
     keywordsET = view.findViewById(R.id.searchrBar);
     utilService = new UtilService();

     refreshLayout = view.findViewById(R.id.refresh);
     refreshLayout.setOnRefreshListener(() -> {

     getFragmentManager().beginTransaction().detach(UserHome.this).attach(UserHome.this).commit();
     refreshLayout.setRefreshing(false);

     });
     searchBT.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             keyword = keywordsET.getText().toString();
             View viewK = (View) getView().getRootView().getWindowToken();

//             utilService.hideKeyboard(view, UserHome.this);

             searchPaper(v);
         }
     });

     recyclerView = view.findViewById(R.id.recyclerView);
//        layoutManager = new LinearLayoutManager(thiscontext);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);


     recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
     recyclerView.setHasFixedSize(true);

     modelList = new ArrayList<>();
     recycler = view.findViewById(R.id.recyclerView);
     recycler.setHasFixedSize(true);
     recycler.setLayoutManager(new LinearLayoutManager(getContext()));
     //call our items
     modelList.add(new Papers(R.drawable.acount_icon,"Mount Everest","Mount Everest is Earth's highest mountain above sea level, located in the Mahalangur Himal sub-range of the Himalayas. The China–Nepal border runs across its summit point. The current official elevation of 8,848 m (29,029 ft), recognised by China and Nepal, was established by a 1955 Indian survey and confirmed by a 1975 Chinese survey."));
     modelList.add(new Papers(R.drawable.acount_icon,"K2","K2, at 8,611 metres (28,251 ft) above sea level, is the second highest mountain in the world, after Mount Everest at 8,848 metres (29,029 ft). It is located on the China–Pakistan border between Baltistan in the Gilgit-Baltistan region of northern Pakistan, and Dafdar Township in Taxkorgan Tajik Autonomous County of Xinjiang, China."));
     modelList.add(new Papers(R.drawable.acount_icon,"Kangchenjunga","Kangchenjunga, also spelled Kanchenjunga, is the third highest mountain in the world. It rises with an elevation of 8,586 m (28,169 ft) in a section of the Himalayas called Kangchenjunga Himal delimited in the west by the Tamur River, in the north by the Lhonak Chu and Jongsang La, and in the east by the Teesta River."));
     modelList.add(new Papers(R.drawable.acount_icon,"Lhotse","Lhotse is the fourth highest mountain in the world at 8,516 metres (27,940 ft), after Mount Everest, K2, and Kangchenjunga. Part of the Everest massif, Lhotse is connected to the latter peak via the South Col. Lhotse means “South Peak” in Tibetan."));
     modelList.add(new Papers(R.drawable.acount_icon,"Makalu ","Makalu is the fifth highest mountain in the world at 8,485 metres (27,838 ft). It is located in the Mahalangur Himalayas 19 km (12 mi) southeast of Mount Everest, on the border between Nepal and Tibet Autonomous Region, China. One of the eight-thousanders, Makalu is an isolated peak whose shape is a four-sided pyramid."));
     modelList.add(new Papers(R.drawable.acount_icon,"Cho Oyu"," is the sixth-highest mountain in the world at 8,188 metres (26,864 ft) above sea level. Cho Oyu means \"Turquoise Goddess\" in Tibetan.The mountain is the westernmost major peak of the Khumbu sub-section of the Mahalangur Himalaya 20 km west of Mount Everest. The mountain stands on the China–Nepal border."));
     modelList.add(new Papers(R.drawable.acount_icon,"Dhaulagiri","The Dhaulagiri massif in Nepal extends 120 km (70 mi) from the Kaligandaki River west to the Bheri. This massif is bounded on the north and southwest by tributaries of the Bheri River and on the southeast by the Myagdi Khola. Dhaulagiri is the seventh highest mountain in the world at 8,167 metres (26,795 ft) above sea level, and the highest mountain within the borders of a single country (Nepal)."));
     //init the adapter with model list and context
     adapter = new PaperAdapter(modelList,getContext());
     //set the adapter into recyclerView
     recycler.setAdapter(adapter);


     return view;

//     return inflater.inflate(R.layout.fragment_user_home, container, false);
    }
    private void searchPaper(View v){
        String BaseURL = utilService.IPAddr();
        String Route = "search/searchPaper";
        String apiKey = BaseURL+Route;

        final HashMap<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                apiKey, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("¸", "onResponse: MySightings response"+response.toString());

                try {
                    if(response.getBoolean("success")) {
//                        String AccountType = response.getString("accountType");
//                        String RegNo = response.getString("regno");
//                        String token = response.getString("msg");
                        String User = response.getString("papers");
                        JSONObject jsonObject = response.getJSONObject("user");
//                                sharedPreferenceClass.setValue_string("dept", jsonObject.get("dept").toString());


//                        startActivity(new Intent(MainActivity.this, Navigation.class));


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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);




    }
}