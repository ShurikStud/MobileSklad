package com.omegaauto.shurik.mobilesklad;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;
import com.omegaauto.shurik.mobilesklad.utils.MySharedPref;
import com.omegaauto.shurik.mobilesklad.view.ProgressButton;

import java.util.List;

import static java.lang.Thread.sleep;

public class SettingsAdditionalFragment extends Fragment {

    SwitchCompat switchCompatCounter;
    EditText editTextCounter;
    EditText editTextTimeout;
    ProgressButton buttonReset;
    Spinner spinnerFontSize;

    SettingsAdditionalOnCheckedListener onCheckedListener;
    SettingsAdditionalOnClickListener onClickListener;
    SettingsAdditionalOnTouchListener onTouchListener;

    Boolean resetIsDown;

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
        buttonReset = (ProgressButton) view.findViewById(R.id.fragment_settings_additional_button_reset);
        spinnerFontSize = (Spinner) view.findViewById(R.id.fragment_settings_additional_spinner);

        updateSettingsView();

        onCheckedListener = new SettingsAdditionalOnCheckedListener();
        onClickListener = new SettingsAdditionalOnClickListener();
        onTouchListener = new SettingsAdditionalOnTouchListener();

        switchCompatCounter.setOnCheckedChangeListener(onCheckedListener);
        //buttonReset.setOnClickListener(onClickListener);
        buttonReset.setOnTouchListener(onTouchListener);
        //buttonReset.setOnTouchListener();

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

    private void settingsReset(){
        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
        mobileSkladSettings.initDefault();
        MySharedPref.saveMobileSkladSettings(getContext());

        ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
        containerPropertiesSettings.initDefault();
        MySharedPref.saveSettings(getContext());
    }

    private void updateSettingsView(){
        MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();

        switchCompatCounter.setChecked(mobileSkladSettings.getCounterEnable());
        editTextCounter.setText(String.valueOf(mobileSkladSettings.getCounter()));
        editTextTimeout.setText(String.valueOf(mobileSkladSettings.getTimeout()));

        List<Fragment> fragmentList = getActivity().getSupportFragmentManager().getFragments();
        for (Fragment fragment: fragmentList) {
            if (fragment.getClass().getSimpleName().equals("SettingsContainerFragment")){
                SettingsContainerFragment settingsContainerFragment = (SettingsContainerFragment) fragment;
                settingsContainerFragment.updateView();
            }
        }

    }

    public void updateView(){

    }

    class SettingsAdditionalOnCheckedListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            MobileSkladSettings mobileSkladSettings = MobileSkladSettings.getInstance();
            mobileSkladSettings.setCounterEnable(isChecked);
        }

    }

    class SettingsAdditionalOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.fragment_settings_additional_button_reset){
                //settingsReset();
//                buttonReset.setRatio(ratio);
//                ratio = ratio +0.3f;
//                if (ratio >= 1) ratio = 0;
            }

        }
    }

    class SettingsAdditionalOnTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.fragment_settings_additional_button_reset){
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        resetIsDown = true;
                        new ProgressTask().execute("");
                        break;
                    case MotionEvent.ACTION_UP:
                        resetIsDown = false;
                        break;
                    default:
                        break;
                }

            }
            return false;
        }
    }

    private class ProgressTask extends AsyncTask<String, Float, String > {

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            buttonReset.setRatio(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                Float ratio = 0.1f;
                publishProgress(ratio);

                while (ratio < 1) {
                    ratio = ratio +0.02f;
                    sleep(5);
                    publishProgress(ratio);
                    if (resetIsDown == false) {
                        publishProgress(0f);
                        break;
                    }
                }

            } catch (InterruptedException exInterrupter){
                exInterrupter.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            publishProgress(0f);

            if (resetIsDown){
                // TODO сброс настроек
                settingsReset();
                updateSettingsView();
            }

        }
    }



}
