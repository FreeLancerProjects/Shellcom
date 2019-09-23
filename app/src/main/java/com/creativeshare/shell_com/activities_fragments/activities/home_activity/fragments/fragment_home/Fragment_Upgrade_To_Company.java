package com.creativeshare.shell_com.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.shell_com.adapters.ServicesAdapter;
import com.creativeshare.shell_com.adapters.Spinner_Adapter;
import com.creativeshare.shell_com.adapters.Spinner_Services_Adapter;
import com.creativeshare.shell_com.models.CityModel;
import com.creativeshare.shell_com.models.SelectedLocation;
import com.creativeshare.shell_com.models.ServicesModel;
import com.creativeshare.shell_com.models.UserModel;
import com.creativeshare.shell_com.preferences.Preferences;
import com.creativeshare.shell_com.remote.Api;
import com.creativeshare.shell_com.share.Common;
import com.creativeshare.shell_com.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Upgrade_To_Company extends Fragment {

    private ImageView back, image, image_icon_upload, image_map_arrow;
    private TextView tv_address;
    private EditText edt_name, edt_email, edt_info;
    private Spinner spinner_city, spinner_services;
    private FrameLayout fl_select_location;
    private Button btn_send;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private ServicesAdapter adapter;
    private String current_language;
    private Home_Activity activity;
    private final String read_perm = Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE, camera_permission = Manifest.permission.CAMERA;
    private final int IMG1 = 1, IMG2 = 2;
    private Uri uri;
    private UserModel userModel;
    private Preferences preferences;
    private SelectedLocation selectedLocation = null;
    private List<CityModel> cityModelList;
    private Spinner_Adapter city_adapter;
    private Spinner_Services_Adapter spinner_services_adapter;
    private String city_id = "";
    private List<ServicesModel> servicesModelList, selected_servicesModelList;
    private List<Integer> selected_ids_list;


    public static Fragment_Upgrade_To_Company newInstance() {
        return new Fragment_Upgrade_To_Company();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade_to_company, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        selected_servicesModelList = new ArrayList<>();
        servicesModelList = new ArrayList<>();
        selected_ids_list = new ArrayList<>();
        cityModelList = new ArrayList<>();

        preferences = Preferences.getInstance();
        activity = (Home_Activity) getActivity();
        userModel = preferences.getUserData(activity);

        Paper.init(activity);
        current_language = Paper.book().read("language", Locale.getDefault().getLanguage());
        back = view.findViewById(R.id.back);
        image_map_arrow = view.findViewById(R.id.image_map_arrow);

        if (current_language.equals("ar") || current_language.equals("ur")) {
            back.setRotation(180.0f);
            image_map_arrow.setRotation(180.0f);

        }


        image = view.findViewById(R.id.image);
        image_icon_upload = view.findViewById(R.id.image_icon_upload);
        tv_address = view.findViewById(R.id.tv_address);
        fl_select_location = view.findViewById(R.id.fl_select_location);
        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_info = view.findViewById(R.id.edt_info);
        spinner_city = view.findViewById(R.id.spinner_city);
        btn_send = view.findViewById(R.id.btn_send);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recView.setLayoutManager(manager);
        adapter = new ServicesAdapter(selected_servicesModelList, activity, this);
        recView.setAdapter(adapter);

        spinner_services = view.findViewById(R.id.spinner_services);
        spinner_services_adapter = new Spinner_Services_Adapter(activity, servicesModelList);
        spinner_services.setAdapter(spinner_services_adapter);


        city_adapter = new Spinner_Adapter(activity, cityModelList);
        spinner_city.setAdapter(city_adapter);

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    city_id = "";
                } else {
                    city_id = cityModelList.get(position).getId_city();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    ServicesModel servicesModel = servicesModelList.get(position);
                    if (!isSelectedServiceIdHas_ServiceId(servicesModel)) {
                        selected_ids_list.add(servicesModel.getId());
                        selected_servicesModelList.add(servicesModel);
                        adapter.notifyDataSetChanged();

                        recView.setVisibility(View.VISIBLE);


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateImageAlertDialog();
            }
        });
        fl_select_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentMap("fragment_upgrade_to_company");
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        getCities();
        getServices();

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
                                cityModelList.clear();
                                cityModelList.add(new CityModel("إختر", "Choose"));
                                cityModelList.addAll(response.body());
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

    private void getServices() {

        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getServices()
                .enqueue(new Callback<List<ServicesModel>>() {
                    @Override
                    public void onResponse(Call<List<ServicesModel>> call, Response<List<ServicesModel>> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                servicesModelList.clear();
                                servicesModelList.add(new ServicesModel("إختر", "Choose"));
                                servicesModelList.addAll(response.body());
                                spinner_services_adapter.notifyDataSetChanged();
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
                    public void onFailure(Call<List<ServicesModel>> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private boolean isSelectedServiceIdHas_ServiceId(ServicesModel servicesModel) {
        boolean isHas = false;
        for (int i = 0; i < selected_ids_list.size(); i++) {
            if (servicesModel.getId() == selected_ids_list.get(i)) {
                isHas = true;
            }
        }
        return isHas;
    }

    private void checkData() {

        String m_name = edt_name.getText().toString().trim();
        String m_email = edt_email.getText().toString().trim();
        String m_info = edt_info.getText().toString().trim();


        if (!TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_email) &&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches() &&
                !TextUtils.isEmpty(m_info) &&
                selectedLocation != null &&
                uri != null &&
                !TextUtils.isEmpty(city_id) &&
                selected_ids_list.size() > 0
        ) {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_info.setError(null);

            Common.CloseKeyBoard(activity, edt_name);
            Send(m_name, m_email, m_info, uri, selectedLocation);

        } else {

            if (TextUtils.isEmpty(m_name)) {
                edt_name.setError(getString(R.string.field_req));
            } else {
                edt_name.setError(null);

            }

            if (TextUtils.isEmpty(m_email)) {
                edt_email.setError(getString(R.string.field_req));
            } else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches()) {
                edt_email.setError(getString(R.string.inv_email));
            } else {
                edt_email.setError(null);

            }

            if (TextUtils.isEmpty(m_info)) {
                edt_info.setError(getString(R.string.field_req));
            } else {
                edt_info.setError(null);

            }

            if (uri == null) {
                Toast.makeText(activity, getString(R.string.ch_comp_image), Toast.LENGTH_SHORT).show();

            }

            if (TextUtils.isEmpty(city_id)) {
                Toast.makeText(activity, getString(R.string.ch_city), Toast.LENGTH_SHORT).show();
            }

            if (selected_ids_list.size() == 0) {
                Toast.makeText(activity, getString(R.string.ch_service), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void Send(String m_name, String m_email, String m_info, Uri uri, SelectedLocation selectedLocation) {

        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.show();
        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(userModel.getUser().getId()));
        RequestBody lat_part = Common.getRequestBodyText(String.valueOf(selectedLocation.getLat()));
        RequestBody lng_part = Common.getRequestBodyText(String.valueOf(selectedLocation.getLng()));

        RequestBody name_part = Common.getRequestBodyText(m_name);
        RequestBody email_part = Common.getRequestBodyText(m_email);
        RequestBody info_part = Common.getRequestBodyText(m_info);

        RequestBody address_part = Common.getRequestBodyText(selectedLocation.getAddress());
        RequestBody city_id_part = Common.getRequestBodyText(city_id);
        List<RequestBody> requestBodyList_Ids = getRequestBodyList(selected_ids_list);

        MultipartBody.Part image_part = Common.getMultiPart(activity, uri, "commercial_register_image");


        Api.getService(Tags.base_url)
                .upgradeToCompany(email_part, name_part, info_part, lat_part, lng_part, city_id_part, address_part, user_id_part, requestBodyList_Ids, image_part)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            CreateSuccessAlertDialog(getString(R.string.req_sent_admin_reply));

                        } else {
                            if (response.code() == 401) {
                                Common.CreateSignAlertDialog(activity, getString(R.string.req_pend));
                            } else {
                                Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();

                            }
                            try {
                                Log.e("Error_code", response.code() + "" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }


    private void CreateSuccessAlertDialog(String msg) {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_sign, null);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.Back();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private List<RequestBody> getRequestBodyList(List<Integer> selected_ids_list) {
        List<RequestBody> requestBodyList = new ArrayList<>();
        for (int id : selected_ids_list) {
            RequestBody requestBody = Common.getRequestBodyText(String.valueOf(id));
            requestBodyList.add(requestBody);
        }

        return requestBodyList;
    }

    public void setLocation(SelectedLocation location) {
        this.selectedLocation = location;
        tv_address.setText(location.getAddress());

    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_image, null);
        Button btn_camera = view.findViewById(R.id.btn_camera);
        Button btn_gallery = view.findViewById(R.id.btn_gallery);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);


        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Check_CameraPermission();

            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Check_ReadPermission();


            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }

    private void Check_ReadPermission() {
        if (ContextCompat.checkSelfPermission(activity, read_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{read_perm}, IMG1);
        } else {
            select_photo(1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(activity, WRITE_PERM) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, WRITE_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{camera_permission, WRITE_PERM}, IMG2);
        } else {
            select_photo(2);

        }

    }

    private void select_photo(int type) {

        Intent intent = new Intent();

        if (type == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, IMG1);

        } else if (type == 2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, IMG2);
            } catch (SecurityException e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data != null) {
            image_icon_upload.setVisibility(View.GONE);
            uri = data.getData();

            String path = Common.getImagePath(activity, uri);
            if (path != null) {
                Picasso.with(activity).load(new File(path)).fit().into(image);

            } else {
                Picasso.with(activity).load(uri).fit().into(image);

            }
        } else if (requestCode == IMG2 && resultCode == Activity.RESULT_OK && data != null) {
            image_icon_upload.setVisibility(View.GONE);
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            uri = getUriFromBitmap(bitmap);
            if (uri != null) {
                String path = Common.getImagePath(activity, uri);

                if (path != null) {
                    Picasso.with(activity).load(new File(path)).fit().into(image);

                } else {
                    Picasso.with(activity).load(uri).fit().into(image);

                }
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(IMG1);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == IMG2) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(2);

                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    public void setItemForDelete(int position) {

        selected_ids_list.remove(position);
        selected_servicesModelList.remove(position);
        adapter.notifyDataSetChanged();

        if (selected_servicesModelList.size() == 0) {
            recView.setVisibility(View.GONE);
        }

    }
}
