package com.example.shurik.mobilesklad.view;

import android.content.ClipData;
import android.view.View;
import android.widget.AdapterView;

import com.example.shurik.mobilesklad.container.ContainerPropertiesSettings;

public class MyOnItemLongClickListener implements AdapterView.OnItemLongClickListener{

    ContainerPropertiesSettings containerPropertiesSettings;

    public MyOnItemLongClickListener(){
        containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (containerPropertiesSettings.getSeparator() != position) {

            ContainerPropertiesSettings.Property property = (ContainerPropertiesSettings.Property) (parent.getItemAtPosition(position));

            PassObject passObj = new PassObject(view, property);

            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, passObj, 0);
        }
        return true;
    }
}
