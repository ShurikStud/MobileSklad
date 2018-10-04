package com.omegaauto.shurik.mobilesklad;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;

public class SettingsAdditionalFragment extends Fragment {

    SwitchCompat switchCompatCounter;
    EditText editTextCounter;
    EditText editTextTimeout;

    MyOnCheckedListener onCheckedListener;

    public static SettingsAdditionalFragment getInstance(){
        SettingsAdditionalFragment settingsAdditionalFragment = new SettingsAdditionalFragment();
        return settingsAdditionalFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_additional, null);

        switchCompatCounter = (SwitchCompat) view.findViewById(R.id.fragment_settings_additional_switch_counter);
        editTextCounter = (EditText) view.findViewById(R.id.fragment_settings_additional_editText_counter);
        editTextTimeout = (EditText) view.findViewById(R.id.fragment_settings_additional_editText_timeout);

        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();

        switchCompatCounter.setChecked(mobileSkladSettings.getCounterEnable());
        editTextCounter.setText(String.valueOf(mobileSkladSettings.getCounter()));
        editTextTimeout.setText(String.valueOf(mobileSkladSettings.getTimeout()));

        onCheckedListener = new MyOnCheckedListener();

        switchCompatCounter.setOnCheckedChangeListener(onCheckedListener);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        String counter = editTextCounter.getText().toString();
        String timeout = editTextTimeout.getText().toString();

        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
        mobileSkladSettings.setCounter(Integer.valueOf(counter));
        mobileSkladSettings.setTimeout(Integer.valueOf(timeout));

    }

    class MyOnCheckedListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
            mobileSkladSettings.setCounterEnable(isChecked);
        }

    }

}
