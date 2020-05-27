package com.yourcompany.covid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String URL_RESULT = "https://api.covid19api.com/summary";
    TextView total_confirmed, total_death, total_recovered, new_confirmed, new_death, new_recovered;
    private List<Modal_RecyclerView> modal_recyclerViewList;
    private RecyclerView recyclerView;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveResult();

        modal_recyclerViewList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycle_manage);



    }

    private void retrieveResult() {

         StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_RESULT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                              //  String success = jsonObject.getString("Global");
                                JSONObject obj = jsonObject.getJSONObject("Global");
                                String total_confirm = obj.getString("TotalConfirmed");
                                String total_death1 = obj.getString("TotalDeaths");
                                String total_recovered1 = obj.getString("TotalRecovered");
                                String new_confirmed1 = obj.getString("NewConfirmed");
                                String new_death1 = obj.getString("NewDeaths");
                                String new_recovered1 = obj.getString("NewRecovered");

                                total_confirmed = findViewById(R.id.total_confirmed);
                                total_confirmed.setText(total_confirm);
                                total_death = findViewById(R.id.total_death);
                                total_death.setText(total_death1);
                                total_recovered = findViewById(R.id.total_recovered);
                                total_recovered.setText(total_recovered1);
                                new_confirmed = findViewById(R.id.new_confirm);
                                new_confirmed.setText(new_confirmed1);
                                new_death = findViewById(R.id.new_death);
                                new_death.setText(new_death1);
                                new_recovered = findViewById(R.id.new_recovered);
                                new_recovered.setText(new_recovered1);

                               // JSONObject obj1 = jsonObject.getJSONObject("User");

                                JSONArray jsonArray = jsonObject.getJSONArray("Countries");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        Modal_RecyclerView modal_recyclerView = new Modal_RecyclerView();
                                        modal_recyclerView.setCountry(object.getString("Country").trim());
                                        modal_recyclerView.setTotal_confirmed(object.getString("TotalConfirmed").trim());
                                        modal_recyclerView.setTotal_death(object.getString("TotalDeaths").trim());
                                        modal_recyclerView.setTotal_recovered(object.getString("TotalRecovered").trim());
                                        modal_recyclerView.setNew_confirmed(object.getString("NewConfirmed").trim());
                                        modal_recyclerView.setNew_death(object.getString("NewDeaths").trim());
                                        modal_recyclerView.setNew_recovered(object.getString("NewRecovered").trim());

                                        modal_recyclerViewList.add(modal_recyclerView);





                                    }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                //   loading.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "error" + e.toString(), Toast.LENGTH_SHORT).show();
                            }

                            setuprecyclerview(modal_recyclerViewList);



                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //  loading.setVisibility(View.GONE);
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();

                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof NetworkError) {
                        Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        Toast.makeText(getApplicationContext(), "Parsing error! Please try again after some time!", Toast.LENGTH_SHORT).show();
                    }


                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                  //  params.put("sno", sno);
                    return params;
                }
            };



            RequestQueue requestQueue = Volley.newRequestQueue(this);

            requestQueue.add(stringRequest);
        }

    private void setuprecyclerview(List<Modal_RecyclerView> modal_recyclerViewList) {
        Adapter_recylerview adapter_recylerview = new Adapter_recylerview(this, modal_recyclerViewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter_recylerview);

    }
    }
