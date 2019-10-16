package com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.activity.ShipmentActivity;
import com.appzone.shelcom.share.Common;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Fragment_Shipment_Load_Description extends Fragment {

    private ImageView image1,image2,icon1,icon2;
    private EditText edt_description;
  //  private EditText edt_value,edt_weight;
    private FrameLayout fl1,fl2;
    private ShipmentActivity activity;

    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null, imgUri2 = null;
    private int selectedType = 0;


    public static Fragment_Shipment_Load_Description newInstance()
    {
        return new Fragment_Shipment_Load_Description();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipment_load_description,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (ShipmentActivity) getActivity();

        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        icon1 = view.findViewById(R.id.icon1);
        icon2 = view.findViewById(R.id.icon2);
        edt_description = view.findViewById(R.id.edt_description);
        //edt_value = view.findViewById(R.id.edt_value);
        //edt_weight = view.findViewById(R.id.edt_weight);
        fl1 = view.findViewById(R.id.fl1);
        fl2 = view.findViewById(R.id.fl2);

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
    }

    public boolean isDataOk()
    {
        String m_description = edt_description.getText().toString();
       // String m_value = edt_value.getText().toString().trim();
        //String m_weight = edt_weight.getText().toString().trim();

        if (!TextUtils.isEmpty(m_description)&&
          //      !TextUtils.isEmpty(m_value)&&
            //    !TextUtils.isEmpty(m_weight)&&
              //  Integer.parseInt(m_weight)<=35&&
                imgUri1!=null&&
                imgUri2!=null
        )
        {
            edt_description.setError(null);
          //  edt_value.setError(null);
            //edt_weight.setError(null);
            Common.CloseKeyBoard(activity,edt_description);
            activity.saveLoadDescriptionData(m_description,imgUri1,imgUri2);
            return true;
        }else
            {
                if (TextUtils.isEmpty(m_description))
                {
                    edt_description.setError(getString(R.string.field_req));

                }else
                    {
                        edt_description.setError(null);

                    }
/*
                if (TextUtils.isEmpty(m_value))
                {
                    edt_value.setError(getString(R.string.field_req));

                }else
                {
                    edt_value.setError(null);

                }

                if (TextUtils.isEmpty(m_weight))
                {
                    edt_weight.setError(getString(R.string.field_req));

                }else
                {
                    edt_weight.setError(null);

                }*/

                if (imgUri1==null)
                {
                    Toast.makeText(activity,getString(R.string.ch_img), Toast.LENGTH_SHORT).show();
                }
             /*   if (Integer.parseInt(m_weight)>35)
                {
                    Common.CreateSignAlertDialog(activity,getString(R.string.weight_of_load));
                }*/

                return false;
            }
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
