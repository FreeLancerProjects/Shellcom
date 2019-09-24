package com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.appzone.shelcom.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.appzone.shelcom.adapters.Spinner_Adapter;
import com.appzone.shelcom.adapters.Spinner_Container_Size_Adapter;
import com.appzone.shelcom.models.CityModel;
import com.appzone.shelcom.models.ContainerSizeModel;
import com.appzone.shelcom.models.OrderIdModel;
import com.appzone.shelcom.models.PlaceGeocodeData;
import com.appzone.shelcom.models.PlaceMapDetailsData;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
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

public class Fragment_Water_Delivery extends Fragment implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, LocationListener , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private List<ContainerSizeModel> containerSizeModelList;
    private ImageView arrow,image_search;
    private Spinner spinner_city,spinner_size;
    private LinearLayout ll_time,ll_date;
    private TextView tv_time,tv_date;
    private EditText edt_address;
    private Button btn_send;
    private List<CityModel> cityModelList;
    private Spinner_Adapter city_adapter;
    private Spinner_Container_Size_Adapter spinner_container_size_adapter;
    private Home_Activity activity;
    private String current_language;
    private Marker marker;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private double lat=0.0,lng=0.0;
    private float zoom = 15.6f;
    private String address="";
    private int container_size_id=-1;
    private long time=0,date=0;
    private String city_id="";
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private UserModel userModel;
    private Preferences preferences;
    private Calendar order_time_calender;
    private Runnable runnable;
    private Handler handler;



    public static Fragment_Water_Delivery newInstance() {
        return new Fragment_Water_Delivery();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_water_delivery, container, false);
        initView(view);

        updateUI();

        return view;
    }

    private void initView(final View view) {
        order_time_calender = Calendar.getInstance();
        cityModelList = new ArrayList<>();
        containerSizeModelList = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        arrow = view.findViewById(R.id.arrow);
        if (current_language.equals("ar")||current_language.equals("ur"))
        {
            arrow.setRotation(180.0f);
        }

        image_search = view.findViewById(R.id.image_search);
        ll_time = view.findViewById(R.id.ll_time);
        ll_date = view.findViewById(R.id.ll_date);
        tv_time = view.findViewById(R.id.tv_time);
        tv_date = view.findViewById(R.id.tv_date);

        edt_address = view.findViewById(R.id.edt_address);
        btn_send = view.findViewById(R.id.btn_send);

        spinner_size = view.findViewById(R.id.spinner_size);
        spinner_container_size_adapter = new Spinner_Container_Size_Adapter(activity,containerSizeModelList);
        spinner_size.setAdapter(spinner_container_size_adapter);

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

        spinner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0)
                {
                    container_size_id =-1;
                }else
                {
                    container_size_id = containerSizeModelList.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel!=null)
                {
                    CheckData();

                }else
                    {
                        Common.CreateUserNotSignInAlertDialog(activity);
                    }
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        createDatePickerDialog();
        createTimePickerDialog();

        CheckPermission();
        getCities();
        getContainerSize();





    }

    private void CheckData() {
        if (!TextUtils.isEmpty(city_id)&&
                time!=0&&
                date!=0&&
                container_size_id!=-1&&
                !TextUtils.isEmpty(address)
        )
        {
            tv_time.setError(null);
            tv_date.setError(null);
            edt_address.setError(null);
            Common.CloseKeyBoard(activity,edt_address);
            send(city_id,container_size_id,address,lat,lng);
        }else
            {
                if (TextUtils.isEmpty(city_id))
                {
                    Toast.makeText(activity, getString(R.string.ch_city), Toast.LENGTH_SHORT).show();
                }

                if (time ==0)
                {
                    tv_time.setError(getString(R.string.field_req));
                }

                if (date ==0)
                {
                    tv_date.setError(getString(R.string.field_req));
                }

                if (container_size_id ==-1)
                {
                    Toast.makeText(activity, getString(R.string.ch_cont_size), Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(address))
                {
                    edt_address.setError(getString(R.string.field_req));
                }else
                    {
                        edt_address.setError(null);
                    }
            }
    }

    private void send(String city_id, int container_size_id, String address, double lat, double lng) {

        if (userModel!=null)
        {
            final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
            dialog.show();
            String user_id ;
            user_id = String.valueOf(userModel.getUser().getId());

            /*if (userModel.getUser().getCompany_information()==null)
            {
                user_id = String.valueOf(userModel.getUser().getId());
            }else
            {
                user_id = String.valueOf(userModel.getUser().getCompany_information().getId());

            }*/
            Api.getService(Tags.base_url)
                    .sendDeliveryWaterOrder(user_id,"1",(order_time_calender.getTimeInMillis()/1000),lat,lng,city_id,address,String.valueOf(container_size_id))
                    .enqueue(new Callback<OrderIdModel>() {
                        @Override
                        public void onResponse(Call<OrderIdModel> call, Response<OrderIdModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getOrder_details()!=null)
                            {
                                CreateAlertDialog(response.body().getOrder_details().getId()+"");
                            }else
                            {
                                Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();

                                try {
                                    Log.e("Error_code", response.code() + "" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderIdModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    });
        }else
            {
                Common.CreateSignAlertDialog(activity,getString(R.string.si_su));
            }


    }

    private void CreateAlertDialog(String order_id)
    {


        final AlertDialog dialog = new android.app.AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_order_id,null);
        Button doneBtn = view.findViewById(R.id.doneBtn);

        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(getString(R.string.order_sent_successfully_order_number_is)+" "+order_id);


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.Back();

            }
        });

        dialog.getWindow().getAttributes().windowAnimations= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
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


    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (fragment!=null)
        {
            fragment.getMapAsync(this);

        }


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
    private void getContainerSize() {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getContainerSize()
                .enqueue(new Callback<List<ContainerSizeModel>>() {
                    @Override
                    public void onResponse(Call<List<ContainerSizeModel>> call, Response<List<ContainerSizeModel>> response) {
                        dialog.dismiss();

                        if (response.isSuccessful())
                        {
                            if (response.body()!=null)
                            {
                                containerSizeModelList.clear();
                                containerSizeModelList.add(new ContainerSizeModel("إختر","Choose"));
                                containerSizeModelList.addAll(response.body());
                                spinner_container_size_adapter.notifyDataSetChanged();
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
                    public void onFailure(Call<List<ContainerSizeModel>> call, Throwable t) {
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

        Log.e("location",location.getLatitude()+"__");
        AddMarker(lat,lng);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient!=null)
        {
            if (locationCallback!=null)
            {
                LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);

            }
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
}
