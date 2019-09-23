package com.creativeshare.shell_com.activities_fragments.activities.home_activity.fragments.fragment_home;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.shell_com.adapters.Spinner_Adapter;
import com.creativeshare.shell_com.models.CityModel;
import com.creativeshare.shell_com.models.Equipment_Model;
import com.creativeshare.shell_com.models.PlaceGeocodeData;
import com.creativeshare.shell_com.models.PlaceMapDetailsData;
import com.creativeshare.shell_com.models.Rental_equipment_Model;
import com.creativeshare.shell_com.models.UserModel;
import com.creativeshare.shell_com.preferences.Preferences;
import com.creativeshare.shell_com.remote.Api;
import com.creativeshare.shell_com.share.Common;
import com.creativeshare.shell_com.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/// Ahmed saad

public class Fragment_Rental_Of_Equipment extends Fragment implements OnMapReadyCallback, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    private ImageView date, clock, back, search;
    private TextView txtdate, txtclock;
    private EditText address, timeneeds, equipnum;
    private Button order;
    final static private String Tag = "lat";
    final static private String Tag2 = "lng";
    final static private String Tag3 = "id";
    private boolean stop = false;

    private Marker marker;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private double lat = 0.0, lng = 0.0;
    private float zoom = 15.6f;

    private Home_Activity activity;
    private Preferences preferences;
    private String current_language, timeclock = "", formatedaddress;
    private ArrayAdapter<String> wideadapter;
    private List<String> citiesname, equipmentsize;
    private Spinner cities, equipmentsizesp;
    private Spinner_Adapter city_adapter;
    private List<CityModel> cities_models;
    private Equipment_Model equipment_model;
    private int years, months, days, hour, minute, second;
    private int id;
    private UserModel userModel;
    private String city_id = "";
    private int size_id=-1;


    public static Fragment_Rental_Of_Equipment newInstance(int id) {
        Fragment_Rental_Of_Equipment fragment_rental_of_equipment = new Fragment_Rental_Of_Equipment();
        Bundle bundle = new Bundle();
        bundle.putInt(Tag3, id);
        fragment_rental_of_equipment.setArguments(bundle);
        return fragment_rental_of_equipment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rental_of_equipment, container, false);
        updateUI();
        initView(view);
        getCities();
        getequipmentsize(id);
        CheckPermission();
        return view;
    }

    private void initView(final View view) {

        cities_models = new ArrayList<>();
        cities_models.add(new CityModel("إختر", "Choose"));
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        citiesname = new ArrayList<>();
        equipmentsize = new ArrayList<>();
        timeneeds = view.findViewById(R.id.editText_timeneed);
        address = view.findViewById(R.id.editText_address);
        cities = view.findViewById(R.id.sp_city);
        city_adapter = new Spinner_Adapter(activity, cities_models);
        cities.setAdapter(city_adapter);


        equipmentsizesp = view.findViewById(R.id.sp_equipment_size);
        equipnum = view.findViewById(R.id.edittext_equipment_num);
        txtdate = view.findViewById(R.id.txtview_date);
        txtclock = view.findViewById(R.id.txtview_clock);
        date = view.findViewById(R.id.imageview_date);
        clock = view.findViewById(R.id.imageview_clock);
        back = view.findViewById(R.id.back);
        search = view.findViewById(R.id.imageview_search);
        order = view.findViewById(R.id.bt_send);
        lat = getArguments().getDouble(Tag);
        lng = getArguments().getDouble(Tag2);
        id = getArguments().getInt(Tag3);
        citiesname.add(getResources().getString(R.string.choose));
        equipmentsize.add(getResources().getString(R.string.choose));
        wideadapter = new ArrayAdapter<>(activity,R.layout.spinner_row, equipmentsize);

        equipmentsizesp.setAdapter(wideadapter);
        if (current_language.equals("en")) {
            back.setRotation(180f);
        }
        address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = address.getText().toString();

                    if (!TextUtils.isEmpty(query)) {
                        Search(query);
                        Common.CloseKeyBoard(activity, view);
                        return true;
                    }
                }

                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = address.getText().toString();

                if (!TextUtils.isEmpty(query)) {
                    Search(query);
                    Common.CloseKeyBoard(activity, view);
                }
            }
        });
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                now.set(Calendar.HOUR_OF_DAY, 5);
                TimePickerDialog dpd = TimePickerDialog.newInstance(Fragment_Rental_Of_Equipment.this, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);
                dpd.setAccentColor(getResources().getColor(R.color.black));
                dpd.setMaxTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
                dpd.setVersion(TimePickerDialog.Version.VERSION_2);
                dpd.show(activity.getFragmentManager(), "Timepickerdialog");

            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Fragment_Rental_Of_Equipment.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.setAccentColor(getResources().getColor(R.color.black));
                dpd.setMinDate(now);
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);

                dpd.show(activity.getFragmentManager(), "Datepickerdialog");
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel == null) {
                    Common.CreateUserNotSignInAlertDialog(activity);
                } else {
                    MakewaterOrder();

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    city_id = "";
                } else {
                    city_id = cities_models.get(position).getId_city();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        equipmentsizesp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    size_id = -1;
                } else {
                    size_id = equipment_model.getAll_equipment_sizes().get(position-1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(activity, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void MakewaterOrder() {
        String numofequipment, timeneed;
        numofequipment = equipnum.getText().toString();
        timeneed = timeneeds.getText().toString();
        if (cities.getSelectedItemPosition() == 0 || size_id == -1 || txtdate.getText().toString().isEmpty() || txtclock.getText().toString().isEmpty() || (lat == 0.0 && lng == 0.0) || timeneed.isEmpty() || numofequipment.isEmpty()) {
            if (TextUtils.isEmpty(city_id)) {
                Toast.makeText(activity, getResources().getString(R.string.ch_city), Toast.LENGTH_LONG).show();
            }
            if (equipmentsizesp.getSelectedItemPosition() == 0) {
                Toast.makeText(activity, getString(R.string.ch_size), Toast.LENGTH_LONG).show();
            }
            if (txtdate.getText().toString().isEmpty()) {
                txtdate.setError(getString(R.string.field_req));
            }
            if (txtclock.getText().toString().isEmpty()) {
                txtclock.setError(getString(R.string.field_req));
            }
            if (lat == 0.0 && lng == 0.0) {
                address.setError(getString(R.string.field_req));
            }
            if (numofequipment.isEmpty()) {
                equipnum.setError("");
            }
            if (timeneed.isEmpty()) {
                timeneeds.setError("");
            }
        } else {

            if (userModel != null) {
                final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
                dialog.show();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, years);
                calendar.set(Calendar.MONTH, months);
                calendar.set(Calendar.DAY_OF_MONTH, days);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, second);

                int user_id;
                user_id = userModel.getUser().getId();

                /*if (userModel.getUser().getCompany_information()==null)
                {
                    user_id = userModel.getUser().getId();
                }else
                {
                    user_id = userModel.getUser().getCompany_information().getId();

                }*/
                // Log.e("err", lat + " " + lng + " " + city_id + " " + wide_id + " " + time / 1000 + " " + (int) (time / 1000));
                Api.getService(Tags.base_url).equipmentorder(2, user_id, id, size_id, lat, lng, city_id, formatedaddress, (calendar.getTimeInMillis() / 1000), timeneed, Integer.parseInt(numofequipment)).enqueue(new Callback<Rental_equipment_Model>() {
                    @Override
                    public void onResponse(Call<Rental_equipment_Model> call, Response<Rental_equipment_Model> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_LONG).show();
                            activity.Back();
                        } else {
                            dialog.dismiss();

                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_LONG).show();

                            try {
                                Log.e("error", response.code() + "" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Rental_equipment_Model> call, Throwable t) {
                        dialog.dismiss();

                        try {
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();

                            Log.e("suss", t.getMessage() + "");
                        } catch (Exception e) {

                        }

                    }
                });
            } else {
                Common.CreateSignAlertDialog(activity, getString(R.string.si_su));

            }


        }
    }

    private void getCities() {

        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getCities()
                .enqueue(new Callback<List<CityModel>>() {
                    @Override
                    public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                cities_models.clear();
                                cities_models.add(new CityModel("إختر", "Choose"));
                                cities_models.addAll(response.body());
                                city_adapter.notifyDataSetChanged();
                            }
                        } else {
                            try {
                                Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();
                                Log.e("Error_code", response.code() + "" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CityModel>> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void getequipmentsize(int id) {
        Api.getService(Tags.base_url).getequipsize(id).enqueue(new Callback<Equipment_Model>() {
            @Override
            public void onResponse(Call<Equipment_Model> call, Response<Equipment_Model> response) {
                if (response.isSuccessful()) {
                    //  Common.setCities_models(response.body());
                    equipment_model = response.body();
                    for (int i = 0; i < response.body().getAll_equipment_sizes().size(); i++) {
                        if (current_language.equals("ar") || current_language.equals("ur")) {
                            equipmentsize.add(response.body().getAll_equipment_sizes().get(i).getAr_title());
                        } else {
                            equipmentsize.add(response.body().getAll_equipment_sizes().get(i).getEn_title());

                        }
                    }
                } else {
                    Log.e("error", response.code() + "");
                }

            }

            @Override
            public void onFailure(Call<Equipment_Model> call, Throwable t) {

            }
        });
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        fragment.getMapAsync(this);

    }


    private void AddMarker(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;

        if (marker == null) {
            IconGenerator iconGenerator = new IconGenerator(activity);
            iconGenerator.setBackground(null);
            View view = LayoutInflater.from(activity).inflate(R.layout.search_map_icon, null);
            iconGenerator.setContentView(view);
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV()).draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));

        } else {
            marker.setPosition(new LatLng(lat, lng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


        }


    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.years = year;
        this.months = monthOfYear + 1;
        this.days = dayOfMonth;
        if (current_language.equals("ar") || current_language.equals("ur")) {
            txtdate.setText(year + "/" + months + "/" + dayOfMonth);

        } else {
            txtdate.setText(dayOfMonth + "/" + months + "/" + year);

        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        this.hour = hourOfDay;
        this.minute = minute;
        this.second = second;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        String d = dateFormat.format(new Date(calendar.getTimeInMillis()));

        txtclock.setText(d);
    }

    private void getGeoData(final double lat, final double lng) {

        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, current_language, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getResults().size() > 0) {
                                formatedaddress = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                address.setText(formatedaddress);
                                AddMarker(lat, lng);
                                //place_id = response.body().getCandidates().get(0).getPlace_id();
                            }
                        } else {

                            try {
                                Log.e("error_code", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                        try {


                            // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void Search(String query) {

        // image_pin.setVisibility(View.GONE);
        //progBar.setVisibility(View.VISIBLE);
        String fields = "id,place_id,name,geometry,formatted_address";
        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, current_language, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            /*image_pin.setVisibility(View.VISIBLE);
                            progBar.setVisibility(View.GONE);
*/

                            if (response.body().getCandidates().size() > 0) {
                                formatedaddress = response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                //place_id = response.body().getCandidates().get(0).getPlace_id();
                                lat = response.body().getCandidates().get(0).getGeometry().getLocation().getLat();
                                lng = response.body().getCandidates().get(0).getGeometry().getLocation().getLng();
                                AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng());
                            }
                        } else {


                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceMapDetailsData> call, Throwable t) {
                        try {


                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    getGeoData(lat, lng);

                }
            });

        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity, 100);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(activity)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        getGeoData(lat, lng);
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }

        LocationServices.getFusedLocationProviderClient(activity)
                .removeLocationUpdates(locationCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null) {
            if (locationCallback!=null)
            {
                LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
                googleApiClient.disconnect();
                googleApiClient = null;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
                Log.e("ssss","sssss");
            } else {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100&&resultCode== Activity.RESULT_OK)
        {

            startLocationUpdate();
        }
    }
}
