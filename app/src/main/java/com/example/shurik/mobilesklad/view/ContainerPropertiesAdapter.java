package com.example.shurik.mobilesklad.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shurik.mobilesklad.R;
import com.example.shurik.mobilesklad.container.Container;
import com.example.shurik.mobilesklad.container.ContainerPropertiesSettings;

import java.util.ArrayList;
import java.util.List;

public class ContainerPropertiesAdapter extends BaseAdapter {

    Context context;
    List<ContainerProperty> properties;

    public ContainerPropertiesAdapter(Context context, Container container, ContainerPropertiesSettings containerPropertiesSettings) {
        this.context = context;
        properties = new ArrayList<ContainerProperty>();

        for (ContainerPropertiesSettings.Property property: containerPropertiesSettings.getVisibleProperties()) {
            properties.add(new ContainerProperty(property.getDescription(), container.getPropertyValueString(property.getName())));
        }

    }

    @Override
    public int getCount() {
        return properties.size();
    }

    @Override
    public Object getItem(int position) {
        return properties.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContainerProperty containerProperty = properties.get(position);

        View row = convertView;
        ViewHolder viewHolder = new ViewHolder();

        if (row == null){
            // если элемент еще не отображался, то необходимо его создать

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(R.layout.activity_container_item, parent, false);

            viewHolder.textValue = (TextView) row.findViewById(R.id.activity_container_item_text_value);
            viewHolder.textName = (TextView) row.findViewById(R.id.activity_container_item_text_name);

            row.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) row.getTag();
        }


        viewHolder.textValue.setText(containerProperty.getValue());
        viewHolder.textName.setText(containerProperty.getName());

        return row;
    }

    class ViewHolder{
        TextView textValue;
        TextView textName;
    }

}
