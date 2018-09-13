package com.example.shurik.mobilesklad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.example.shurik.mobilesklad.view.ContainerPropertiesAdapter;
import com.example.shurik.mobilesklad.view.ContainerProperty;
import com.example.shurik.mobilesklad.view.MyOnItemLongClickListener;
import com.example.shurik.mobilesklad.view.SettingsAdapter;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    ListView listView;
    LinearLayout linearLayout;

    MyOnDragListener myOnDragListener;
    MyOnItemLongClickListener myOnItemLongClickListener;
    //MyItemOnDragListener myItemOnDragListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        myOnDragListener = new MyOnDragListener();
        myOnItemLongClickListener = new MyOnItemLongClickListener();

        linearLayout = (LinearLayout) findViewById(R.id.activity_settings_layout_list_view);
        listView = (ListView) findViewById(R.id.activity_settings_list_view);

        SettingsAdapter settingsAdapter = new SettingsAdapter(this);


        listView.setAdapter(settingsAdapter);
        listView.setOnDragListener(myOnDragListener);
        listView.setOnItemLongClickListener(myOnItemLongClickListener);

    }



 /*   //objects passed in Drag and Drop operation
    class PassObject{
        View view;
        ContainerPropertiesSettings.Property property;
        List<ContainerPropertiesSettings.Property> srcList;

        PassObject(View view, ContainerPropertiesSettings.Property property, List<ContainerPropertiesSettings.Property> s){
            this.view = view;
            this.property = property;
            srcList = s;
        }
    }
*/
    class MyOnDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {

           /* switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //prompt.append("ACTION_DRAG_STARTED: " + area  + "\n");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //prompt.append("ACTION_DRAG_ENTERED: " + area  + "\n");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //prompt.append("ACTION_DRAG_EXITED: " + area  + "\n");
                    break;
                case DragEvent.ACTION_DROP:
                    //prompt.append("ACTION_DROP: " + area  + "\n");

                    PassObject passObj = (PassObject) event.getLocalState();
                    View view = passObj.view;
                    ContainerPropertiesSettings.Property passedItem = passObj.property;
                    List<ContainerPropertiesSettings.Property> srcList = passObj.srcList;
                    ListView oldParent = (ListView)view.getParent();
                    ContainerPropertiesAdapter srcAdapter = (ContainerPropertiesAdapter)(oldParent.getAdapter());

                    LinearLayout newParent = (LinearLayout) v;
                    ContainerPropertiesAdapter destAdapter = (ContainerPropertiesAdapter)(newParent.listView.getAdapter());
                    List<ContainerPropertiesSettings.Property> destList = destAdapter.getList();

                    if(removeItemToList(srcList, passedItem)){
                        addItemToList(destList, passedItem);
                    }

                    srcAdapter.notifyDataSetChanged();
                    destAdapter.notifyDataSetChanged();

                    //smooth scroll to bottom
                    newParent.listView.smoothScrollToPosition(destAdapter.getCount()-1);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //prompt.append("ACTION_DRAG_ENDED: " + area  + "\n");
                default:
                    break;
            }
*/
            return true;
        }
    }

/*
    class MyItemOnDragListener implements View.OnDragListener{

        ContainerPropertiesSettings.Property property;

        MyItemOnDragListener(ContainerPropertiesSettings.Property property){
            this.property = property;
        }


        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //prompt.append("Item ACTION_DRAG_STARTED: " + "\n");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //prompt.append("Item ACTION_DRAG_ENTERED: " + "\n");
                    v.setBackgroundColor(0x30000000);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //prompt.append("Item ACTION_DRAG_EXITED: " + "\n");
                    v.setBackgroundColor(resumeColor);
                    break;
                case DragEvent.ACTION_DROP:
                    //prompt.append("Item ACTION_DROP: " + "\n");

                    PassObject passObj = (PassObject)event.getLocalState();
                    View view = passObj.view;
                    Item passedItem = passObj.item;
                    List<Item> srcList = passObj.srcList;
                    ListView oldParent = (ListView)view.getParent();
                    ItemsListAdapter srcAdapter = (ItemsListAdapter)(oldParent.getAdapter());

                    ListView newParent = (ListView)v.getParent();
                    ItemsListAdapter destAdapter = (ItemsListAdapter)(newParent.getAdapter());
                    List<Item> destList = destAdapter.getList();

                    int removeLocation = srcList.indexOf(passedItem);
                    int insertLocation = destList.indexOf(me);
                    */
/*
                     * If drag and drop on the same list, same position,
                     * ignore
                     *//*

                    if(srcList != destList || removeLocation != insertLocation){
                        if(removeItemToList(srcList, passedItem)){
                            destList.add(insertLocation, passedItem);
                        }

                        srcAdapter.notifyDataSetChanged();
                        destAdapter.notifyDataSetChanged();
                    }

                    v.setBackgroundColor(resumeColor);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //prompt.append("Item ACTION_DRAG_ENDED: "  + "\n");
                    v.setBackgroundColor(resumeColor);
                default:
                    break;
            }

            return true;

        }
    }
*/



}
