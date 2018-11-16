package com.omegaauto.shurik.mobilesklad;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.omegaauto.shurik.mobilesklad.HTTPservices.LogisticHttpService;
import com.omegaauto.shurik.mobilesklad.HTTPservices.ParserHttpResponse;
import com.omegaauto.shurik.mobilesklad.cameraScanBarCode.AnyOrientationCaptureActivity;
import com.omegaauto.shurik.mobilesklad.container.NumberListLast;
import com.omegaauto.shurik.mobilesklad.controller.ControllerContainers;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;
import com.omegaauto.shurik.mobilesklad.utils.Const;
import com.omegaauto.shurik.mobilesklad.container.Container;
import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.omegaauto.shurik.mobilesklad.utils.MySharedPref;
import com.omegaauto.shurik.mobilesklad.view.ContainerPropertiesAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class ContainerActivity extends AppCompatActivity {

    private static final String LOG = "LOG_LOG";

    private static final int REQUEST_CONTAINER_NUMBER  = 100;
    private static final int REQUEST_RESULT_OK = 1;

    String lastBarcode = "";
    String scanBarcode = "";
    Boolean isCounterEnable = false;
    Context context;
    ListView listView;
    ProgressBar progressBarHTTPService;
    String errorMessage="";

    IntentIntegrator qrScan;

    Container container;

//
    Window window;
    TextView textViewContainer;
    TextView textViewCounter;
    EditText editTextContainer;
    LinearLayout linearLayoutCamera;

    MobileSkladSettings mobileSkladSettings;

    ControllerContainers controllerContainers;

    ContainerOnKeyListener containerOnKeyListener;
    ContainerOnClickListener containerOnClickListener;
    ContainerOnTouchListener containerOnTouchListener;

    ContainerPropertiesAdapter containerPropertiesAdapter;

    SwitchCompat onlineSwitch;
    SwitchCompat offlineSwitch;

    long lastTimeBackPressed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_container);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_container_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new ContainerOnNavigationItemSelectedListener());



        window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorBackgroundSeparatorCenter));
        }

        qrScan = new IntentIntegrator(this);
        qrScan.setCaptureActivity(AnyOrientationCaptureActivity.class);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        qrScan.setPrompt("Scan something");
        qrScan.setOrientationLocked(true);
        qrScan.setBeepEnabled(false);

        controllerContainers = new ControllerContainers();
        containerOnKeyListener = new ContainerOnKeyListener();
        containerOnClickListener = new ContainerOnClickListener();
        containerOnTouchListener = new ContainerOnTouchListener();

        View view = findViewById(R.id.activity_container_drawer_layout);

        listView = (ListView) findViewById(R.id.activity_container_listView);
        textViewContainer = (TextView) findViewById(R.id.activity_container_textview_container_value);
        textViewCounter = (TextView) findViewById(R.id.activity_container_textview_counter);
        editTextContainer = (EditText) findViewById(R.id.activity_container_edittext_container_value);
        progressBarHTTPService = (ProgressBar) findViewById(R.id.http_service_progress);
        linearLayoutCamera = (LinearLayout) findViewById(R.id.activity_container_layout_camera);

//TODO переделать. убрать методы depricated
        onlineSwitch = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_online));
        offlineSwitch = (SwitchCompat) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_offline));
//        onlineSwitch = (SwitchCompat) MenuItemCompat.getActionProvider(navigationView.getMenu().
//                findItem(R.id.nav_online)).onCreateActionView();
//
//        offlineSwitch = (SwitchCompat) MenuItemCompat.getActionProvider(navigationView.getMenu().
//                findItem(R.id.nav_offline)).onCreateActionView();

        onlineSwitch.setChecked(true);
        onlineSwitch.setEnabled(false);

        offlineSwitch.setChecked(false);
        offlineSwitch.setEnabled(false);

        editTextContainer.setOnKeyListener(containerOnKeyListener);
        textViewContainer.setOnClickListener(containerOnClickListener);
        linearLayoutCamera.setOnTouchListener(containerOnTouchListener);

        lastBarcode = MySharedPref.loadBarcode(context);
        container = MySharedPref.loadContainer(context);
        if (container == null){
            container = new Container();
            container.setNoData();
        }

        scanBarcode = "";
        mobileSkladSettings = MobileSkladSettings.getInstance();
        updateView(null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextContainer.setFocusableInTouchMode(true);
        editTextContainer.requestFocus();

//        if (containerPropertiesAdapter != null){
//            showContainer();
//        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        MySharedPref.saveBarcode(context, lastBarcode);
        MySharedPref.saveContainer(context, container);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CONTAINER_NUMBER) {
            if (resultCode == REQUEST_RESULT_OK) {
                String barcode = data.getStringExtra("Barcode");
                updateView(barcode);
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, R.string.scan_not_found, Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    //textBarCode.setText(result.getContents());

                    String barcode = result.getContents();
                    updateView(barcode);

                   //startSearch(result.getContents(), true);
                }
            }

        }
    }

    @Override
    public void onBackPressed() {

        long currentTimeMillis = System.currentTimeMillis();

        if ( (currentTimeMillis - 1000) > lastTimeBackPressed) {
            lastTimeBackPressed = currentTimeMillis;
            Toast.makeText(context,R.string.scan_exit_message, Toast.LENGTH_SHORT).show();
            return;
        }

        finish();
        System.exit(0);

        //super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                startScan();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void updateView(String barcode){

        if (barcode == null) {
            showContainer();
            return;
        }

        NumberListLast.getInstance().setCurrentNumber(barcode);

        if (! barcode.equals(lastBarcode)) {
            showProgress(true);
            lastBarcode = barcode.toString();
            setHeaderContainer();
            new ProgressTask().execute(barcode);
        } else {
            setHeaderContainer();
            showContainer();
        }
    }

    private void showContainer(){

        ContainerPropertiesSettings containerPropertiesSettings = ContainerPropertiesSettings.getInstance();
        containerPropertiesAdapter = new ContainerPropertiesAdapter(context, container, containerPropertiesSettings);
        listView.setAdapter(new ContainerPropertiesAdapter(context, container, containerPropertiesSettings));

        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (isCounterEnable && mobileSkladSettings.getCounterEnable()) {
            new CounterTask().execute("");
        }

        showProgress(false);

    }

    private void setHeaderContainer(){
        textViewContainer.setText(lastBarcode);
    }

    private class ProgressTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String jsonText;
            try {
                jsonText = getResponse(strings[0]);
            } catch (IOException ex){
                jsonText = ex.getMessage();
            }

            return jsonText;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == "") {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                container = new Container();
                container.setNoData();
                lastBarcode = "";
            } else {
                ParserHttpResponse parser = new ParserHttpResponse();
                parser.setContainerInfoString(result);
                container = parser.getContainerInfo();
                String errorJson = parser.getErrorString();
                if (errorJson != "") {
                    Toast.makeText(context, errorJson, Toast.LENGTH_LONG).show();
                    container.setNoData();
                    lastBarcode = "";
                }
            }

            showContainer();
         }

        private String getResponse(String barcode) throws IOException{

//            LogisticHttpService logisticHttpService = new LogisticHttpService();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//
//            return logisticHttpService.getContainerJson(barcode);

            String result = "";

            BufferedReader bufferedReader = null;
            String errorMessage = "";



            try {
                URL url = new URL(Const.URL_HTTP_SERVICE_1C_LOGISTIC + barcode);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setReadTimeout(mobileSkladSettings.getTimeout() * 1000); // 2 секунды
                connection.connect();

                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                result = stringBuffer.toString();
            } catch (SocketTimeoutException exTimeout) {
                errorMessage = "Превышено время ожидания...";
            } catch (UnknownHostException exUnknownHostException){
                errorMessage = "Проверьте подключение к Интернет";
            } catch (IOException ex) {
                errorMessage = ex.getMessage();
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }

            return result;


//            BufferedReader bufferedReader = null;
//            errorMessage = "";
//
//            try {
//                URL url = new URL(Const.URL_HTTP_SERVICE_1C_LOGISTIC + barcode);
//
////                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                connection.setRequestMethod("GET");
//                connection.setReadTimeout(mobileSkladSettings.getTimeout() * 1000); // 2 секунды
//                connection.connect();
//
//                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuffer stringBuffer = new StringBuffer();
//                String line = null;
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(line);
//                }
//                return stringBuffer.toString();
//            } catch (SocketTimeoutException exTimeout) {
//                errorMessage = "Превышено время ожидания...";
//                return "";
//            } catch (UnknownHostException exUnknownHostException){
//                errorMessage = "Проверьте подключение к Интернет";
//                return "";
//            } catch (IOException ex) {
//                errorMessage = ex.getMessage();
//                return "";
//            } finally {
//                if (bufferedReader != null) {
//                    bufferedReader.close();
//                }
//            }

        }

    }

    private class CounterTask extends AsyncTask<String, Integer, String >{

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            textViewCounter.setText(String.valueOf(values[0]));
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                int countertime = mobileSkladSettings.getCounter();
                publishProgress(countertime);

                while (countertime > 0) {

                    sleep(1000);
                    countertime -= 1;
                    publishProgress(countertime);
                }
            } catch (InterruptedException exInterrupter){
                exInterrupter.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            finish();
            //Toast.makeText(context, "finish", Toast.LENGTH_SHORT).show();

        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            listView.setVisibility(show ? View.GONE : View.VISIBLE);
            listView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    listView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBarHTTPService.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBarHTTPService.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBarHTTPService.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBarHTTPService.setVisibility(show ? View.VISIBLE : View.GONE);
            listView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void startScan(){
        qrScan.initiateScan();
    }

    class ContainerOnKeyListener implements View.OnKeyListener{
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (v.getId() == R.id.activity_container_edittext_container_value){
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    if (editTextContainer.length() > 1){
                        //lastBarcode = editTextContainer.getText().toString();
                        String newBarcode = editTextContainer.getText().toString();
                        editTextContainer.setText("");
//                        updateView(lastBarcode);
                        updateView(newBarcode);
                        if (getCurrentFocus() != null) {
                            InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    } else {
                        editTextContainer.setText("");
                    }
                }
            }
            return false;
        }
    }

    class ContainerOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(context, NumberContainerActivity.class), REQUEST_CONTAINER_NUMBER);
        }
    }

    class ContainerOnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (view.getId() == R.id.activity_container_layout_camera && motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                startScan();
                return false;
            }
            return false;
        }
    }

    class ContainerOnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_settings){
//                startActivity(new Intent(this, FamilyActivity.class));
                Intent intent = new Intent(context, SettingsPagersActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_online) {

            } else if (id == R.id.nav_offline) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_container_drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;

        }
    }

}
