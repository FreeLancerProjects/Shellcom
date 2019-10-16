package com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.activity.ShipmentActivity;
import com.appzone.shelcom.adapters.Spinner_Adapter;
import com.appzone.shelcom.models.CityModel;
import com.appzone.shelcom.models.PlaceGeocodeData;
import com.appzone.shelcom.models.PlaceMapDetailsData;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.tags.Tags;
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
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;
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

public class Fragment_Shipment_Delivery_Information extends Fragment implements OnCountryPickerListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView tv_code,tv_time,tv_date;
    private ImageView image_phone_code,image_search;
    private EditText edt_phone,edt_responsible_name,edt_address;
  //  private EditText edt_company_name,edt_email;
    private Spinner spinner_city;
    private List<CityModel> cityModelList;
    private Spinner_Adapter city_adapter;
    private LinearLayout ll_time,ll_date;
    private ShipmentActivity activity;
    private String current_language;
    private CountryPicker picker;
    private String code = "";
    private String city_id="";
    private String address ="";
    private double lat=0.0,lng=0.0;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.6f;

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private Calendar order_time_calender;
    private long date=0,time=0;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private Runnable runnable;
    private Handler handler;

    public static Fragment_Shipment_Delivery_Information newInstance()
    {
        return new Fragment_Shipment_Delivery_Information();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipment_delivery_information,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        order_time_calender = Calendar.getInstance();
        cityModelList = new ArrayList<>();
        activity = (ShipmentActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        image_phone_code = view.findViewById(R.id.image_phone_code);

        if (current_language.equals("ar"))
        {
            image_phone_code.setRotation(180.0f);
        }

        image_search = view.findViewById(R.id.image_search);

        tv_code = view.findViewById(R.id.tv_code);
        edt_address = view.findViewById(R.id.edt_address);
        tv_time = view.findViewById(R.id.tv_time);
        tv_date = view.findViewById(R.id.tv_date);
        edt_phone = view.findViewById(R.id.edt_phone);
     //   edt_company_name = view.findViewById(R.id.edt_company_name);
        edt_responsible_name = view.findViewById(R.id.edt_responsible_name);
       // edt_email = view.findViewById(R.id.edt_email);
        ll_time = view.findViewById(R.id.ll_time);
        ll_date = view.findViewById(R.id.ll_date);

        spinner_city = view.findViewById(R.id.spinner_city);

        city_adapter = new Spinner_Adapter(activity,cityModelList);
        spinner_city.setAdapter(city_adapter);

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0)
                {
                    city_id ="";
                }else
                {
                    city_id = cityModelList.get(position).getId_city();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        image_phone_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.showDialog(activity);
            }
        });

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = edt_address.getText().toString().trim();
                if (!TextUtils.isEmpty(query))
                {
                    edt_address.setError(null);
                    Common.CloseKeyBoard(activity,edt_address);
                    Search(query);
                }else
                {
                    edt_address.setError(getString(R.string.field_req));
                }
            }
        });

        ll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show(activity.getFragmentManager(),"");
            }
        });

        ll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(activity.getFragmentManager(),"");
            }
        });


        createDatePickerDialog();
        createTimePickerDialog();
        CreateCountryDialog();
        initMap();

        runnable = new Runnable() {
            @Override
            public void run() {
                CheckPermission();
                getCities();

            }
        };
        handler = new Handler();
        handler.postDelayed(runnable,2000);

    }

    public boolean isDataOk()
    {
        String m_phone = edt_phone.getText().toString().trim();
       // String m_company_name = edt_company_name.getText().toString();
        String m_responsible_name = edt_responsible_name.getText().toString();
        //String m_email = edt_email.getText().toString();

        if (!TextUtils.isEmpty(m_phone)&&
                !TextUtils.isEmpty(code)&&
          //      !TextUtils.isEmpty(m_company_name)&&
                !TextUtils.isEmpty(m_responsible_name)&&
            //    !TextUtils.isEmpty(m_email)&&
              //  Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&
                !TextUtils.isEmpty(address)&&
                !TextUtils.isEmpty(city_id)&&
                lat!=0&&
                lng!=0&&
                time!=0&&
                date!=0
        )
        {
            tv_code.setError(null);
            edt_phone.setError(null);
            //edt_company_name.setError(null);
            edt_responsible_name.setError(null);
            tv_time.setError(null);
            tv_date.setError(null);
            edt_address.setError(null);
            Common.CloseKeyBoard(activity,edt_phone);
            activity.SaveFromShipmentDeliveryData(code,m_phone,m_responsible_name,address,city_id,lat,lng,order_time_calender.getTimeInMillis());
            return true;
        }else
        {
            if (TextUtils.isEmpty(code))
            {
                tv_code.setError(getString(R.string.field_req));
            }else
            {
                tv_code.setError(null);

            }
            if (TextUtils.isEmpty(m_phone))
            {
                edt_phone.setError(getString(R.string.field_req));
            }else
            {
                edt_phone.setError(null);

            }
/*
            if (TextUtils.isEmpty(m_company_name))
            {
                edt_company_name.setError(getString(R.string.field_req));
            }else
            {
                edt_company_name.setError(null);

            }*/

            if (TextUtils.isEmpty(m_responsible_name))
            {
                edt_responsible_name.setError(getString(R.string.field_req));
            }else
            {
                edt_responsible_name.setError(null);

            }

         /*   if (TextUtils.isEmpty(m_email))
            {
                edt_email.setError(getString(R.string.field_req));
            }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches()){
                edt_email.setError(getString(R.string.inv_email));

            }else
            {
                edt_email.setError(null);

            }
*/


            if (TextUtils.isEmpty(address))
            {
                edt_address.setError(getString(R.string.field_req));
            }else
            {
                edt_address.setError(null);

            }

            if (time==0)
            {
                tv_time.setError(getString(R.string.field_req));
            }

            if (date==0)
            {
                tv_date.setError(getString(R.string.field_req));
            }

            if (TextUtils.isEmpty(city_id))
            {
                Toast.makeText(activity, getString(R.string.ch_city), Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }


    private void initMap() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

    }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(activity,R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(activity,R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(activity,R.color.colorPrimary));
        datePickerDialog.setOkText(getString(R.string.select));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(current_language));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }

    private void createTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,5);
        timePickerDialog = TimePickerDialog.newInstance(this,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(ActivityCompat.getColor(activity,R.color.colorPrimary));
        timePickerDialog.setCancelColor(ActivityCompat.getColor(activity,R.color.gray4));
        timePickerDialog.setOkColor(ActivityCompat.getColor(activity,R.color.colorPrimary));
        timePickerDialog.setOkText(getString(R.string.select));
        timePickerDialog.setCancelText(getString(R.string.cancel));
        timePickerDialog.setLocale(new Locale(current_language));
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setMinTime(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));


    }

    private void CreateCountryDialog() {
        CountryPicker.Builder builder = new CountryPicker.Builder()
                .canSearch(true)
                .with(activity)
                .listener(this);
        picker = builder.build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);


        if (picker.getCountryFromSIM() != null) {
            updateUi(picker.getCountryFromSIM());

        } else if (telephonyManager != null && picker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updateUi(picker.getCountryByISO(telephonyManager.getNetworkCountryIso()));


        } else if (picker.getCountryByLocale(Locale.getDefault()) != null) {
            updateUi(picker.getCountryByLocale(Locale.getDefault()));

        } else {
            tv_code.setText("+966");
            code = "+966";
        }


    }

    private void getCities()
    {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getCities()
                .enqueue(new Callback<List<CityModel>>() {
                    @Override
                    public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {
                        dialog.dismiss();

                        if (response.isSuccessful())
                        {
                            if (response.body()!=null)
                            {
                                cityModelList.clear();
                                cityModelList.add(new CityModel("إختر","Choose"));
                                cityModelList.addAll(response.body());
                                city_adapter.notifyDataSetChanged();
                            }
                        }else
                        {
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

    @Override
    public void onSelectCountry(Country country) {
        updateUi(country);
    }

    private void updateUi(Country country) {

        tv_code.setText(country.getDialCode());
        code = country.getDialCode();


    }


    private void CheckPermission()
    {
        if (ActivityCompat.checkSelfPermission(activity,fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            getGeoData(lat, lng);

            AddMarker(lat, lng);


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    marker.setPosition(latLng);
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    getGeoData(lat,lng);
                }
            });

        }
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

    private void getGeoData(final double lat, double lng) {

        String location = lat + "," + lng;
        Api.getService("https://maps.googleapis.com/maps/api/")
                .getGeoData(location, current_language, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceGeocodeData>() {
                    @Override
                    public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getResults().size() > 0) {
                                address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                edt_address.setText(address);
                                Log.e("address",address);
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

        String fields = "id,place_id,name,geometry,formatted_address";
        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, current_language, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getCandidates().size() > 0) {
                                address = response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", "");
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
                            status.startResolutionForResult(activity,100);
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
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate()
    {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(activity)
                .requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        AddMarker(lat,lng);
        getGeoData(lat,lng);

        if (googleApiClient!=null)
        {
            LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == loc_req)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initGoogleApi();
            }else
            {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);


        order_time_calender.set(Calendar.YEAR,year);
        order_time_calender.set(Calendar.MONTH,monthOfYear);
        order_time_calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);


        tv_date.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
        date = calendar.getTimeInMillis();


    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);

        order_time_calender.set(Calendar.HOUR_OF_DAY,hourOfDay);
        order_time_calender.set(Calendar.MINUTE,minute);
        order_time_calender.set(Calendar.SECOND,second);


        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",Locale.ENGLISH);
        String t = dateFormat.format(new Date(calendar.getTimeInMillis()));
        tv_time.setText(t);

        time = calendar.getTimeInMillis();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler!=null&&runnable!=null)
        {
            handler.removeCallbacks(runnable);

        }
        if (googleApiClient!=null)
        {
            if (locationCallback!=null)
            {
                LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
                googleApiClient.disconnect();
                googleApiClient = null;
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
