package com.appzone.shelcom.activities_fragments.activities.activity_other_services.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.activity_other_services.OtherActivity;
import com.appzone.shelcom.models.OrderIdModel;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Customer_Clearance extends Fragment {
    private ImageView arrow;
    private FrameLayout fl1,fl2,fl3,fl4,fl5;
    private ImageView icon1,icon2,icon3,icon4,icon5;
    private ImageView image1,image2,image3,image4,image5;
    private EditText edt_details;
    private Button btn_send;
    private UserModel userModel;
    private Preferences preferences;
    private String  current_language;
    private OtherActivity activity;

    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2,IMG_REQ3=3,IMG_REQ4=4,IMG_REQ5=5;
    private Uri imgUri1 = null, imgUri2 = null,imgUri3 = null,imgUri4 = null,imgUri5 = null;
    private int selectedType = 0;

    public static Fragment_Customer_Clearance newInstance() {
        return new Fragment_Customer_Clearance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer_clearance, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        activity = (OtherActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow = view.findViewById(R.id.arrow);
        if (current_language.equals("ar"))
        {
            arrow.setRotation(180.0f);
        }

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });


        icon1 = view.findViewById(R.id.icon1);
        icon2 = view.findViewById(R.id.icon2);
        icon3 = view.findViewById(R.id.icon3);
        icon4 = view.findViewById(R.id.icon4);
        icon5 = view.findViewById(R.id.icon5);
        fl1 = view.findViewById(R.id.fl1);
        fl2 = view.findViewById(R.id.fl2);
        fl3 = view.findViewById(R.id.fl3);
        fl4 = view.findViewById(R.id.fl4);
        fl5 = view.findViewById(R.id.fl5);
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        image3 = view.findViewById(R.id.image3);
        image4 = view.findViewById(R.id.image4);
        image5 = view.findViewById(R.id.image5);

        edt_details = view.findViewById(R.id.edt_details);
        btn_send = view.findViewById(R.id.btn_send);



        fl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateImageAlertDialog(IMG_REQ1);
            }
        });

        fl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateImageAlertDialog(IMG_REQ2);
            }
        });

        fl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateImageAlertDialog(IMG_REQ3);
            }
        });
        fl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateImageAlertDialog(IMG_REQ4);
            }
        });
        fl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateImageAlertDialog(IMG_REQ5);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }

    private void checkData() {

        String m_details = edt_details.getText().toString().trim();

        if (!TextUtils.isEmpty(m_details) &&
                imgUri1!=null&&
                imgUri2!=null&&
                imgUri3!=null&&
                imgUri4!=null&&
                imgUri5!=null

        )
        {
            edt_details.setError(null);
            Common.CloseKeyBoard(activity,edt_details);
            send(m_details);

        }else
        {
            if (TextUtils.isEmpty(m_details))
            {
                edt_details.setError(getString(R.string.field_req));

            }else
            {
                edt_details.setError(null);

            }


            if (imgUri1==null)
            {
                Toast.makeText(activity,getString(R.string.sel_m4_img), Toast.LENGTH_SHORT).show();
            }

            if (imgUri2==null)
            {
                Toast.makeText(activity, getString(R.string.sel_com_img), Toast.LENGTH_SHORT).show();
            }

            if (imgUri3==null)
            {
                Toast.makeText(activity, getString(R.string.sel_tx_img), Toast.LENGTH_SHORT).show();
            }

            if (imgUri4==null)
            {
                Toast.makeText(activity, getString(R.string.sel_imp_img), Toast.LENGTH_SHORT).show();
            }

            if (imgUri5==null)
            {
                Toast.makeText(activity, getString(R.string.sel_cus_img), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void send(String m_details) {

        if (userModel!=null)
        {
            final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
            dialog.show();
            int user_id;
            user_id = userModel.getUser().getId();

            /*if (userModel.getUser().getCompany_information()==null)
            {
                user_id = userModel.getUser().getId();
            }else
            {
                user_id = userModel.getUser().getCompany_information().getId();

            }*/
            RequestBody user_id_part =Common.getRequestBodyText(String.valueOf(user_id));
            RequestBody order_type_part =Common.getRequestBodyText("5");
            RequestBody description_part =Common.getRequestBodyText(m_details);

            MultipartBody.Part image1_part = Common.getMultiPart(activity,imgUri1,"modelFour");
            MultipartBody.Part image2_part = Common.getMultiPart(activity,imgUri2,"commercialRegister");

            MultipartBody.Part image3_part = Common.getMultiPart(activity,imgUri3,"multiplicationCard");
            MultipartBody.Part image4_part = Common.getMultiPart(activity,imgUri4,"importCard");

            MultipartBody.Part image5_part = Common.getMultiPart(activity,imgUri5,"soshibalCard");


            Api.getService(Tags.base_url)
                    .sendCustomerOrder(user_id_part,order_type_part,description_part,image1_part,image2_part,image3_part,image4_part,image5_part)
                    .enqueue(new Callback<OrderIdModel>() {
                        @Override
                        public void onResponse(Call<OrderIdModel> call, final Response<OrderIdModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                CreateAlertDialog(response.body().getOrder_details().getId()+"");

                            }else
                            {


                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                try {
                                    Log.e("Error_code",response.code()+""+response.errorBody().string());
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
                                Log.e("Error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });
        }else
            {
                Common.CreateSignAlertDialog(activity,getString(R.string.si_su));

            }


    }

    private void CreateAlertDialog(String order_id)
    {


        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(activity)
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

    private void CreateImageAlertDialog(final int img_req)
    {

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_image,null);
        Button btn_camera = view.findViewById(R.id.btn_camera);
        Button btn_gallery = view.findViewById(R.id.btn_gallery);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);



        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectedType = 2;
                Check_CameraPermission(img_req);

            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectedType = 1;
                CheckReadPermission(img_req);



            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }
    private void CheckReadPermission(int img_req)
    {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, img_req);
        } else {
            SelectImage(1,img_req);
        }
    }

    private void Check_CameraPermission(int img_req)
    {
        if (ContextCompat.checkSelfPermission(activity,camera_permission)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(activity,write_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{camera_permission,write_permission},img_req);
        }else
        {
            SelectImage(2,img_req);

        }

    }
    private void SelectImage(int type,int img_req) {

        Intent intent = new Intent();

        if (type == 1)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }else
            {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent,img_req);

        }else if (type ==2)
        {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,img_req);
            }catch (SecurityException e)
            {
                Toast.makeText(activity,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(activity,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG_REQ1) {

            if (selectedType ==1)
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ1);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }else
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ1);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == IMG_REQ2) {
            if (selectedType ==1)
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ2);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }else
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ2);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }

        else if (requestCode == IMG_REQ3) {

            if (selectedType ==1)
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ3);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }else
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ3);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }


        }
        else if (requestCode == IMG_REQ4) {
            if (selectedType ==1)
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ4);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }else
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ4);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == IMG_REQ5) {
            if (selectedType ==1)
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ5);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }else
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(selectedType,IMG_REQ5);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {
            if (selectedType == 1)
            {
                imgUri1 = data.getData();
                icon1.setVisibility(View.GONE);
                File file = new File(Common.getImagePath(activity, imgUri1));
                Picasso.with(activity).load(file).fit().into(image1);
            }else if (selectedType ==2)
            {
                icon1.setVisibility(View.GONE);

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                imgUri1 = getUriFromBitmap(bitmap);
                if (imgUri1 != null) {
                    String path = Common.getImagePath(activity, imgUri1);

                    if (path != null) {
                        Picasso.with(activity).load(new File(path)).fit().into(image1);

                    } else {
                        Picasso.with(activity).load(imgUri1).fit().into(image1);

                    }
                }
            }




        } else if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            if (selectedType == 1)
            {
                imgUri2 = data.getData();
                icon2.setVisibility(View.GONE);
                File file = new File(Common.getImagePath(activity, imgUri2));

                Picasso.with(activity).load(file).fit().into(image2);
            }else if (selectedType ==2)
            {

                icon2.setVisibility(View.GONE);

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                imgUri2 = getUriFromBitmap(bitmap);
                if (imgUri2 != null) {
                    String path = Common.getImagePath(activity, imgUri2);

                    if (path != null) {
                        Picasso.with(activity).load(new File(path)).fit().into(image2);

                    } else {
                        Picasso.with(activity).load(imgUri2).fit().into(image2);

                    }
                }
            }



        }
        else if (requestCode == IMG_REQ3 && resultCode == Activity.RESULT_OK && data != null) {

            if (selectedType == 1)
            {
                imgUri3 = data.getData();
                icon3.setVisibility(View.GONE);
                File file = new File(Common.getImagePath(activity, imgUri3));

                Picasso.with(activity).load(file).fit().into(image3);
            }else if (selectedType ==2)
            {

                icon3.setVisibility(View.GONE);

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                imgUri3 = getUriFromBitmap(bitmap);
                if (imgUri3 != null) {
                    String path = Common.getImagePath(activity, imgUri3);

                    if (path != null) {
                        Picasso.with(activity).load(new File(path)).fit().into(image3);

                    } else {
                        Picasso.with(activity).load(imgUri3).fit().into(image3);

                    }
                }



            }



        }
        else if (requestCode == IMG_REQ4 && resultCode == Activity.RESULT_OK && data != null) {

            if (selectedType == 1)
            {
                imgUri4 = data.getData();
                icon4.setVisibility(View.GONE);
                File file = new File(Common.getImagePath(activity, imgUri4));

                Picasso.with(activity).load(file).fit().into(image4);
            }else if (selectedType ==2)
            {

                icon4.setVisibility(View.GONE);

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                imgUri4 = getUriFromBitmap(bitmap);
                if (imgUri4 != null) {
                    String path = Common.getImagePath(activity, imgUri4);

                    if (path != null) {
                        Picasso.with(activity).load(new File(path)).fit().into(image4);

                    } else {
                        Picasso.with(activity).load(imgUri4).fit().into(image4);

                    }
                }


            }



        }

        else if (requestCode == IMG_REQ5 && resultCode == Activity.RESULT_OK && data != null) {

            if (selectedType == 1)
            {
                imgUri5 = data.getData();
                icon5.setVisibility(View.GONE);
                File file = new File(Common.getImagePath(activity, imgUri5));

                Picasso.with(activity).load(file).fit().into(image5);
            }else if (selectedType ==2)
            {

                icon5.setVisibility(View.GONE);

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                imgUri5 = getUriFromBitmap(bitmap);
                if (imgUri5 != null) {
                    String path = Common.getImagePath(activity, imgUri5);

                    if (path != null) {
                        Picasso.with(activity).load(new File(path)).fit().into(image5);

                    } else {
                        Picasso.with(activity).load(imgUri5).fit().into(image5);

                    }
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


}
