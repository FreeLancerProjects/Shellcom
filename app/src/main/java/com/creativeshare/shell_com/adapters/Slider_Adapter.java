package com.creativeshare.shell_com.adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.models.SliderDataModel;
import com.creativeshare.shell_com.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Slider_Adapter extends PagerAdapter {


    private List<SliderDataModel.SliderModel> sliderModelList;
    private Context context;


    public Slider_Adapter(Context context, List<SliderDataModel.SliderModel> sliderModelList) {
        this.context = context;
        this.sliderModelList = sliderModelList;
    }


    @Override
    public int getCount() {
        return sliderModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.slider_row,container,false);

        SliderDataModel.SliderModel  sliderModel = sliderModelList.get(position);
        final ImageView image = view.findViewById(R.id.image);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_SLIDER_URL+sliderModel.getUrl())).fit().placeholder(R.drawable.logo_512).into(image);

        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
