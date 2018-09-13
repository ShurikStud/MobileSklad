package com.example.shurik.mobilesklad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.shurik.mobilesklad.container.Container;
import com.example.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.example.shurik.mobilesklad.view.ContainerPropertiesAdapter;

public class ContainerActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Container container = new Container();

        container.setAmount_goods(10);
        container.setDriver_name("Driver 007 фамилия имя отчество");
        container.setInvoice_numbers("38320");
        container.setNn(1);
        container.setNumber(12);
        container.setPartner_name("Contragent 001 фирма далеко от сюда");
        container.setZayavkaTEP_highway_number("ХВ-00012345");
        container.setZayavkaTEP_number("ХВ-00088991");
        container.setTrip_number("324134");
        container.setVehicle_name("АХ 0777 ВР");

        ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();

        listView = (ListView) findViewById(R.id.activity_container_listView);

        listView.setAdapter(new ContainerPropertiesAdapter(this, container, containerPropertiesSettings));



    }
}
