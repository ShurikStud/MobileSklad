package com.omegaauto.shurik.mobilesklad.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.widget.ListView;

import com.omegaauto.shurik.mobilesklad.R;
import com.omegaauto.shurik.mobilesklad.utils.Const;
import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;

public class MyItemOnDragListener implements View.OnDragListener{

    ContainerPropertiesSettings.Property property;

    Drawable drawable_visible;
    Drawable drawable_invisible;
    Drawable drawable_separator;

    Context context;
    private static final int shadowColor = 0x30000000;

    MyItemOnDragListener(ContainerPropertiesSettings.Property property, Context context){
        this.property = property;
        this.context = context;

        drawable_visible = context.getDrawable(R.drawable.shape_row_visible);
        drawable_invisible = context.getDrawable(R.drawable.shape_row_invisible);
        drawable_separator = context.getDrawable(R.drawable.shape_row_separator);

    }


    @Override
    public boolean onDrag(View v, DragEvent event) {

        ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();

        switch (event.getAction()) {

            case DragEvent.ACTION_DRAG_STARTED:
                break;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(Const.colorBackgroundShadow);
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                int index_exited = containerPropertiesSettings.getIndex(property);

                if (index_exited == containerPropertiesSettings.getSeparator()) {
//                    v.setBackgroundColor(Const.colorBackgroundSeparator);
                    v.setBackground(drawable_separator);
                } else if (index_exited < containerPropertiesSettings.getSeparator()) {
//                    v.setBackgroundColor(Const.colorBackgroundVisible);
                    v.setBackground(drawable_visible);
                } else {
//                    v.setBackgroundColor(Const.colorBackgroundInvisible);
                    v.setBackground(drawable_invisible);
                }
                break;

            case DragEvent.ACTION_DROP:
                PassObject passObj = (PassObject) event.getLocalState();
                View view = passObj.view;
                ContainerPropertiesSettings.Property passedProperty = passObj.property;
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
                    v.getParent();
                }

                int index_drop = containerPropertiesSettings.getIndex(property);

                if (index_drop == containerPropertiesSettings.getSeparator()) {
                    //v.setBackgroundColor(Const.colorBackgroundSeparator);
                    v.setBackground(drawable_separator);
                } else if (index_drop < containerPropertiesSettings.getSeparator()) {
                    //v.setBackgroundColor(Const.colorBackgroundVisible);
                    v.setBackground(drawable_visible);
                } else {
                    //v.setBackgroundColor(Const.colorBackgroundInvisible);
                    v.setBackground(drawable_invisible);
                }
                //v.setBackgroundColor(resumeColor);

                break;

            case DragEvent.ACTION_DRAG_ENDED:

                int index_ended = containerPropertiesSettings.getIndex(property);

                if (index_ended == containerPropertiesSettings.getSeparator()) {
                    //v.setBackgroundColor(Const.colorBackgroundSeparator);
                    v.setBackground(drawable_separator);
                } else if (index_ended < containerPropertiesSettings.getSeparator()) {
                    //v.setBackgroundColor(Const.colorBackgroundVisible);
                    v.setBackground(drawable_visible);
                } else {
                    //v.setBackgroundColor(Const.colorBackgroundInvisible);
                    v.setBackground(drawable_invisible);
                }

                break;

            default:
                break;
        }

        return true;

    }

}
