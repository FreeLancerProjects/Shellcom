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
import com.appzone.shelcom.models.ContainerTypeModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Spinner_Other_Container_Type_Adapter extends BaseAdapter {
    private List<ContainerTypeModel> containerTypeModelList;
    private LayoutInflater inflater;
    private String current_language;

    public Spinner_Other_Container_Type_Adapter(Context context, List<ContainerTypeModel> containerTypeModelList) {
        this.containerTypeModelList = containerTypeModelList;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public int getCount() {
        return containerTypeModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return containerTypeModelList.get(position);
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

        ContainerTypeModel containerTypeModel = containerTypeModelList.get(position);
        if (current_language.equals("ar")||current_language.equals("ur")) {
            tv_name.setText(containerTypeModel.getAr_type());
        } else {
            tv_name.setText(containerTypeModel.getEn_type());

        }
        return convertView;
    }
}
