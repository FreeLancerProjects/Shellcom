package com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.appzone.shelcom.adapters.Spinner_Other_Container_Size_Adapter;
import com.appzone.shelcom.adapters.Spinner_Other_Container_Type_Adapter;
import com.appzone.shelcom.models.ContainerTypeModel;
import com.appzone.shelcom.models.OrderIdModel;
import com.appzone.shelcom.models.OtherServiceContainerSizeModel;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Container extends Fragment implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, LocationListener {

    private ImageView arrow,image_search;
    private Spinner spinner_size,spinner_type;
    private Spinner_Other_Container_Size_Adapter adapter_size;
    private Spinner_Other_Container_Type_Adapter adapter_type;
    private List<ContainerTypeModel> containerTypeList;
    private List<OtherServiceContainerSizeModel.Size> containerSizeList;
    private EditText edt_search;
    private Button btn_send;
    private String current_language;
    private Home_Activity activity;

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
    private int container_size_id, container_type_id;
    private UserModel userModel;
    private Preferences preferences;



    public static Fragment_Container newInstance() {
        return new Fragment_Container();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_container, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        containerSizeList = new ArrayList<>();
        containerTypeList = new ArrayList<>();
        containerTypeList.add(new ContainerTypeModel("إختر","Choose"));
        containerSizeList.add(new OtherServiceContainerSizeModel.Size("إختر","Choose"));

        preferences = Preferences.getInstance();
        activity = (Home_Activity) getActivity();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow = view.findViewById(R.id.arrow);

        if (current_language.equals("ar")||current_language.equals("ur"))
        {
            arrow.setRotation(180.0f);
        }

        image_search = view.findViewById(R.id.image_search);
        spinner_size = view.findViewById(R.id.spinner_size);
        spinner_type = view.findViewById(R.id.spinner_type);
        edt_search = view.findViewById(R.id.edt_search);
        btn_send = view.findViewById(R.id.btn_send);

        adapter_size = new Spinner_Other_Container_Size_Adapter(activity,containerSizeList);
        spinner_size.setAdapter(adapter_size);

        adapter_type = new Spinner_Other_Container_Type_Adapter(activity, containerTypeList);
        spinner_type.setAdapter(adapter_type);


        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position ==0)
                {
                    container_size_id=-1;
                    container_type_id=-1;
                }else
                    {
                        container_size_id = -1;
                        container_type_id = containerTypeList.get(position).getId();
                        getContainerSize(container_type_id);
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
                    container_size_id = -1;
                }else
                    {
                        container_size_id = containerSizeList.get(position).getId();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = edt_search.getText().toString().trim();
                if (!TextUtils.isEmpty(query))
                {
                    Search(query);
                }
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    String query = edt_search.getText().toString().trim();
                    if (!TextUtils.isEmpty(query))
                    {
                        Search(query);
                    }
                }
                return false;
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();

            }
        });
        updateUI();
        CheckPermission();
        getContainerType();

    }

    private void checkData() {
        if (container_size_id!=-1&&
                container_type_id!=-1&&
                !TextUtils.isEmpty(address)
        )
        {
            edt_search.setError(null);
            Common.CloseKeyBoard(activity,edt_search);
            SendOrder(container_size_id,container_type_id,address);
        }else
            {
                if (container_size_id==-1)
                {
                    Toast.makeText(activity, getString(R.string.ch_cont_size), Toast.LENGTH_SHORT).show();
                }


                if (container_type_id==-1)
                {
                    Toast.makeText(activity,getString(R.string.ch_con_type), Toast.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(address))
                {
                    edt_search.setError(getString(R.string.field_req));
                }else
                    {
                        edt_search.setError(null);
                    }

            }
    }


    private void getContainerType()
    {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        Api.getService(Tags.base_url)
                .getContainerType()
                .enqueue(new Callback<List<ContainerTypeModel>>() {
                    @Override
                    public void onResponse(Call<List<ContainerTypeModel>> call, Response<List<ContainerTypeModel>> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            containerTypeList.clear();
                            containerTypeList.add(new ContainerTypeModel("إختر","Choose"));
                            containerTypeList.addAll(response.body());
                            adapter_type.notifyDataSetChanged();

                        }else
                        {


                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ContainerTypeModel>> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    private void getContainerSize(int container_type_id)
    {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        Api.getService(Tags.base_url)
                .getOtherContainerSize(container_type_id)
                .enqueue(new Callback<OtherServiceContainerSizeModel>() {
                    @Override
                    public void onResponse(Call<OtherServiceContainerSizeModel> call, Response<OtherServiceContainerSizeModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getAll_containers_sizes()!=null)
                        {
                            containerSizeList.clear();
                            containerSizeList.add(new OtherServiceContainerSizeModel.Size("إختر","Choose"));
                            containerSizeList.addAll(response.body().getAll_containers_sizes());
                            adapter_size.notifyDataSetChanged();
                        }else
                        {


                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OtherServiceContainerSizeModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void SendOrder(int container_size_id, int container_type_id, String address) {

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
                    .sendContainerOrder(user_id,"4",lat,lng,address,container_type_id,container_size_id)
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


        final AlertDialog dialog = new AlertDialog.Builder(activity)
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
        fragment.getMapAsync(this);


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
                                edt_search.setText(address);
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
        LocationServices.getFusedLocationProviderClient(activity)
                .removeLocationUpdates(locationCallback);
        googleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100&&resultCode== Activity.RESULT_OK)
        {

            startLocationUpdate();
        }
    }

}
