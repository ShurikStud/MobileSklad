package com.example.shurik.mobilesklad.view;

import android.view.DragEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shurik.mobilesklad.container.ContainerPropertiesSettings;

public class MyItemOnDragListener implements View.OnDragListener{

    ContainerPropertiesSettings.Property property;

    int resumeColor;
    private static final int shadowColor = 0x30000000;

    MyItemOnDragListener(ContainerPropertiesSettings.Property property, int resumeColor){
        this.property = property;
        this.resumeColor = resumeColor;
    }


    @Override
    public boolean onDrag(View v, DragEvent event) {

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(shadowColor);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundColor(resumeColor);
                break;
            case DragEvent.ACTION_DROP:
                PassObject passObj = (PassObject)event.getLocalState();
                View view = passObj.view;
                ContainerPropertiesSettings.Property passedProperty = passObj.property;
                ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
                ListView oldParent = (ListView)view.getParent();
                SettingsAdapter srcAdapter = (SettingsAdapter) (oldParent.getAdapter());

                int removeLocation = containerPropertiesSettings.getIndex(passedProperty);
                int insertLocation = containerPropertiesSettings.getIndex(property);

                /*
                 * If drag and drop on the same list, same position,
                 * ignore
                 */
                if(removeLocation != insertLocation){

                    containerPropertiesSettings.movePropertyTo(insertLocation, passedProperty);
                    srcAdapter.notifyDataSetChanged();
                }

                v.setBackgroundColor(resumeColor);

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                v.setBackgroundColor(resumeColor);
            default:
                break;
        }

        return true;

    }

}
