package com.example.shurik.mobilesklad.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shurik.mobilesklad.R;
import com.example.shurik.mobilesklad.container.Container;
import com.example.shurik.mobilesklad.container.ContainerPropertiesSettings;

import java.util.List;

public class SettingsAdapter extends BaseAdapter {

    private static final int colorSeparator = 0x64089E;
    private static final int colorSettingVisible = 0x82E4F3;
    private static final int colorSettingInvisible = 0xF4D179;

    private Context context;
    private ContainerPropertiesSettings containerPropertiesSettings;

    public SettingsAdapter(Context context){
        this.context = context;
        containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
    }
    @Override
    public int getCount() {
        return containerPropertiesSettings.getSize();
    }

    @Override
    public Object getItem(int position) {
        return containerPropertiesSettings.getProperty(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        ViewHolder viewHolder = null;
        //
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            if (position == containerPropertiesSettings.getSeparator()) {
                rowView = inflater.inflate(R.layout.activity_settings_item_separator, null);
            } else {
                rowView = inflater.inflate(R.layout.activity_settings_item, null);
            }

            viewHolder = new ViewHolder();
            //viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
            if (position == containerPropertiesSettings.getSeparator()){
                viewHolder.text = (TextView) rowView.findViewById(R.id.settings_item_text_view_separator);
            } else {
                viewHolder.text = (TextView) rowView.findViewById(R.id.settings_item_text_view);
            }
            rowView.setTag(viewHolder);

            //rowView.setOnDragListener(new MyItemOnDragListener(containerPropertiesSettings.getProperty(position), context.getResources().getColor(android.R.color.background_light), prompt));
            if (position < containerPropertiesSettings.getSeparator()) {
                rowView.setOnDragListener(new MyItemOnDragListener(containerPropertiesSettings.getProperty(position), colorSettingVisible));
            } else if (position == containerPropertiesSettings.getSeparator()) {
                rowView.setOnDragListener(new MyItemOnDragListener(containerPropertiesSettings.getProperty(position), colorSeparator));
            } else {
                rowView.setOnDragListener(new MyItemOnDragListener(containerPropertiesSettings.getProperty(position), colorSettingInvisible));
            }


        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }


        //viewHolder.icon.setImageDrawable(list.get(position).ItemDrawable);
        viewHolder.text.setText(containerPropertiesSettings.getProperty(position).getDescription());


/*
        //rowView.setOnDragListener(new MyItemOnDragListener(containerPropertiesSettings.getProperty(position), context.getResources().getColor(android.R.color.background_light), prompt));
        if (position < containerPropertiesSettings.getSeparator()) {
            rowView.setOnDragListener(new MyItemOnDragListener(containerPropertiesSettings.getProperty(position), colorSettingVisible));
        } else if (position == containerPropertiesSettings.getSeparator()) {
            rowView.setOnDragListener(new MyItemOnDragListener(containerPropertiesSettings.getProperty(position), colorSeparator));
        } else {
            rowView.setOnDragListener(new MyItemOnDragListener(containerPropertiesSettings.getProperty(position), colorSettingInvisible));
        }
*/

        return rowView;

    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
    }




}

//objects passed in Drag and Drop operation





