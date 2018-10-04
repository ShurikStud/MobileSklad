package com.omegaauto.shurik.mobilesklad.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;

import java.util.ArrayList;

public class SettingArrayAdapter extends ArrayAdapter<ContainerPropertiesSettings.Property> {
    private Context context;
    private ArrayList<ContainerPropertiesSettings.Property> properties;

    public SettingArrayAdapter(@NonNull Context context, int resource, Context context1) {
        super(context, resource);
        this.context = context1;
    }
}
