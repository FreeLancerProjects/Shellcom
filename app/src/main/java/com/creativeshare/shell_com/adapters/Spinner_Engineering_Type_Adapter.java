package com.creativeshare.shell_com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.models.Engineering_Type_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Spinner_Engineering_Type_Adapter extends BaseAdapter {
    private List<Engineering_Type_Model> engineering_type_modelList;
    private LayoutInflater inflater;
    private String current_language;

    public Spinner_Engineering_Type_Adapter(Context context, List<Engineering_Type_Model> engineering_type_modelList) {
        this.engineering_type_modelList = engineering_type_modelList;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public int getCount() {
        return engineering_type_modelList.size();
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

        Engineering_Type_Model engineering_type_model = engineering_type_modelList.get(position);
        if (current_language.equals("ar")||current_language.equals("ur")) {
            tv_name.setText(engineering_type_model.getAr_type());
        } else {
            tv_name.setText(engineering_type_model.getEn_type());

        }
        return convertView;
    }
}
