package com.yourcompany.covid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CDATASection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private String URL_RESULT = "https://api.covid19api.com/summary";
    TextView total_confirmed, total_death, total_recovered, new_confirmed, new_death, new_recovered;
    private List<Modal_RecyclerView> modal_recyclerViewList;
    private RecyclerView recyclerView;
    private ProgressBar loading;
    private LocationManager locationManager;
    String newGeoLocation = "";
    String newGeoLocationlat = "";
    String newGeoLocationlong = "";
    private FusedLocationProviderClient mFusedLocationClient;
    private String[] PERMISSIONS_REQUIRED = new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final String TAG = "MyActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private String provider;
    private TextView filter;
    final Context context = this;
    String[] users = {"Total Case", "Total Death", "Total Recovered"};

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Mumbai");

        retrieveResult();

        modal_recyclerViewList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycle_manage);
        total_confirmed = findViewById(R.id.total_confirmed);
        filter = findViewById(R.id.filter);


        ImageView sort = findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Collections.sort(modal_recyclerViewList, Modal_RecyclerView.BY_TOTAL_CASE);





            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.filter);
                final Button submit = (Button) dialog.findViewById(R.id.save);
                ImageView close = (ImageView) dialog.findViewById(R.id.imageView_close);
                Spinner spin = (Spinner) dialog.findViewById(R.id.spinner1);
                ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, users);
                adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter_state);
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(), "Selected User: " + users[position], Toast.LENGTH_SHORT).show();
                        final String result = users[position];
                        //  names.add("others");
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final LinearLayout total_case, total_death, total_recovered;
                                final TextView reset = findViewById(R.id.reset);
                                total_case = findViewById(R.id.total_case2);
                                total_death = findViewById(R.id.total_death2);
                                total_recovered = findViewById(R.id.total_recovered2);
                                total_case.setVisibility(View.VISIBLE);
                                total_death.setVisibility(View.VISIBLE);
                                total_recovered.setVisibility(View.VISIBLE);

                                if (result.equals("Total Case")) {
                                    total_death.setVisibility(GONE);
                                    total_recovered.setVisibility(GONE);
                                    dialog.cancel();
                                    reset.setVisibility(View.VISIBLE);
                                    reset.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            total_case.setVisibility(View.VISIBLE);
                                            total_death.setVisibility(View.VISIBLE);
                                            total_recovered.setVisibility(View.VISIBLE);
                                            reset.setVisibility(GONE);
                                        }
                                    });
                                    Toast.makeText(getApplicationContext(), "Selected User: " + result, Toast.LENGTH_SHORT).show();

                                } else if (result.equals("Total Death")) {
                                    total_case.setVisibility(GONE);
                                    total_recovered.setVisibility(GONE);
                                    dialog.cancel();
                                    reset.setVisibility(View.VISIBLE);
                                    reset.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            total_case.setVisibility(View.VISIBLE);
                                            total_death.setVisibility(View.VISIBLE);
                                            total_recovered.setVisibility(View.VISIBLE);
                                            reset.setVisibility(GONE);
                                        }
                                    });
                                    Toast.makeText(getApplicationContext(), "Selected User: " + result, Toast.LENGTH_SHORT).show();

                                } else if (result.equals("Total Recovered")) {
                                    total_death.setVisibility(GONE);
                                    total_case.setVisibility(GONE);
                                    dialog.cancel();
                                    reset.setVisibility(View.VISIBLE);
                                    reset.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            total_case.setVisibility(View.VISIBLE);
                                            total_death.setVisibility(View.VISIBLE);
                                            total_recovered.setVisibility(View.VISIBLE);
                                            reset.setVisibility(GONE);

                                        }
                                    });
                                    Toast.makeText(getApplicationContext(), "Selected User: " + result, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // names.add("others");

                    }
                });
                dialog.show();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

            }

        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);


        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Toast.makeText(getApplicationContext(), "Location not available", Toast.LENGTH_SHORT).show();
            //  latituteField.setText("Location not available");
            // longitudeField.setText("Location not available");
        }
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


                            total_confirmed.setText(total_confirm);
                            total_death = findViewById(R.id.total_death);
                            total_death.setText(total_death1);
                            total_recovered = findViewById(R.id.total_recovered);
                            total_recovered.setText(total_recovered1);
//                            new_confirmed = findViewById(R.id.new_confirm);
//                            new_confirmed.setText(new_confirmed1);
//                            new_death = findViewById(R.id.new_death);
//                            new_death.setText(new_death1);
//                            new_recovered = findViewById(R.id.new_recovered);
//                            new_recovered.setText(new_recovered1);

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



//        Collections.sort(modal_recyclerViewList, new Comparator<Modal_RecyclerView>() {
//            @Override
//            public int compare(Modal_RecyclerView lhs, Modal_RecyclerView rhs) {
//                return lhs.getTotal_confirmed().compareTo(rhs.getTotal_confirmed());
//            }
//        });



    }



    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = builder.toString(); //This is the complete address.

          //  latituteField.setText(String.valueOf(lat));
          //  longitudeField.setText(String.valueOf(lng));
          //  addressField.setText(fnialAddress); //This will display the final address.
            setTitle(fnialAddress);
            Toast.makeText(getApplicationContext(), "Location" +fnialAddress, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }


    //https://stackoverflow.com/questions/18221614/how-i-can-get-the-city-name-of-my-current-position

    }

