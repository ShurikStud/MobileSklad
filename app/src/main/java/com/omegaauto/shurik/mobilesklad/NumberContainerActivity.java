package com.omegaauto.shurik.mobilesklad;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.omegaauto.shurik.mobilesklad.container.Container;
//import com.omegaauto.shurik.mobilesklad.container.ContainerListLast;
//import com.omegaauto.shurik.mobilesklad.view.ContainerListLastAdapter;
import com.omegaauto.shurik.mobilesklad.container.NumberListLast;
import com.omegaauto.shurik.mobilesklad.view.NumberListAdapter;

public class NumberContainerActivity extends AppCompatActivity {

    private static final int REQUEST_RESULT_OK = 1;

    ImageButton buttonSearch;
    EditText editTextContainer;
    //RecyclerView recyclerView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_container);

        editTextContainer = (EditText) findViewById(R.id.activity_number_container_edit_text);
        buttonSearch = (ImageButton) findViewById(R.id.activity_number_container_button_search);
        //recyclerView = (RecyclerView) findViewById(R.id.activity_number_container_recycler_view);

        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        ContainerListLastAdapter containerListLastAdapter = new ContainerListLastAdapter();
//        recyclerView.setAdapter(containerListLastAdapter);
        NumberListAdapter numberListAdapter = new NumberListAdapter(this);
        //recyclerView.setAdapter(numberListAdapter);

        listView = findViewById(R.id.activity_number_container_list_view);
        listView.setAdapter(numberListAdapter);

        ScanOnClickListener scanOnClickListener = new ScanOnClickListener();
        buttonSearch.setOnClickListener(scanOnClickListener);

        editTextContainer.setText(NumberListLast.getInstance().getCurrentNumber());
        editTextContainer.selectAll();
    }

    @Override
    protected void onStart() {
        super.onStart();
        editTextContainer.setFocusable(true);
        editTextContainer.requestFocus();
        editTextContainer.selectAll();

    }

    protected void startSearch(String barcode){
        Intent intent = new Intent();
        intent.putExtra("Barcode", barcode);
        setResult(REQUEST_RESULT_OK, intent);
        finish();
   }

    class ScanOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.activity_number_container_button_search){
                if (editTextContainer.length() > 0) {
                    startSearch(editTextContainer.getText().toString());
                }
                return;
            }
        }
    }

}
