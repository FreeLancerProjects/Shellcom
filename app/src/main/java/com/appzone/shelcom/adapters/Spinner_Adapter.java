package com.appzone.shelcom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appzone.shelcom.R;
import com.appzone.shelcom.models.CityModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Spinner_Adapter extends BaseAdapter {
    private List<CityModel> cityModelList;
    private LayoutInflater inflater;
    private String current_language;

    public Spinner_Adapter(Context context,List<CityModel> cityModelList) {
        this.cityModelList = cityModelList;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public int getCount() {
        return cityModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);

        }
        TextView tv_name = convertView.findViewById(R.id.tv_name);

        CityModel cityModel = cityModelList.get(position);
        if (current_language.equals("ar")||current_language.equals("ur")) {
            tv_name.setText(cityModel.getAr_city_title());
        } else {
            tv_name.setText(cityModel.getEn_city_title());

        }
        return convertView;
    }
}
