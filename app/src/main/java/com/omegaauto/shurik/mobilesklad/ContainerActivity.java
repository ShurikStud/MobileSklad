package com.omegaauto.shurik.mobilesklad;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.omegaauto.shurik.mobilesklad.HTTPservices.HTTPservice1CLogistic;
import com.omegaauto.shurik.mobilesklad.settings.MobileSkladSettings;
import com.omegaauto.shurik.mobilesklad.utils.Const;
import com.omegaauto.shurik.mobilesklad.container.Container;
import com.omegaauto.shurik.mobilesklad.container.ContainerPropertiesSettings;
import com.omegaauto.shurik.mobilesklad.utils.MySharedPref;
import com.omegaauto.shurik.mobilesklad.view.ContainerPropertiesAdapter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import static java.lang.Thread.sleep;

public class ContainerActivity extends AppCompatActivity {

    String lastBarcode = "";
    String scanBarcode = "";
    Boolean isCounterEnable = false;
    Context context;
    ListView listView;
    ProgressBar progressBarHTTPService;
    String errorMessage="";

    Container container;

//
    Window window;
    TextView textViewContainer;
    TextView textViewCounter;
    EditText editTextContainer;

    MobileSkladSettings mobileSkladSettings;

    ContainerOnKeyListener containerOnKeyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_container);

        window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.colorBackgroundSeparatorCenter));
        }

        containerOnKeyListener = new ContainerOnKeyListener();

        View view = findViewById(R.id.activity_container_view);

        listView = (ListView) findViewById(R.id.activity_container_listView);
        textViewContainer = (TextView) findViewById(R.id.activity_container_textview_container_value);
        textViewCounter = (TextView) findViewById(R.id.activity_container_textview_counter);
        editTextContainer = (EditText) findViewById(R.id.activity_container_edittext_container_value);
        progressBarHTTPService = (ProgressBar) findViewById(R.id.http_service_progress);

        editTextContainer.setOnKeyListener(containerOnKeyListener);
//        editTextContainer.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);

        //editTextContainer.setShowSoftInputOnFocus(false);

        lastBarcode = MySharedPref.loadBarcode(context);
        container = MySharedPref.loadContainer(context);
        scanBarcode = "";

        mobileSkladSettings = MobileSkladSettings.getInstance();

        String barcode = getIntent().getStringExtra("Barcode");
        scanBarcode = "";

        //if (getIntent().hasExtra("CounterEnable"))
        isCounterEnable = getIntent().getBooleanExtra("CounterEnable", false);

        if (isCounterEnable && mobileSkladSettings.getCounterEnable()) {
            textViewCounter.setVisibility(View.VISIBLE);
        }else {
            textViewCounter.setVisibility(View.INVISIBLE);
        }

        updateView(barcode);

    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextContainer.setFocusableInTouchMode(true);
        editTextContainer.requestFocus();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MySharedPref.saveBarcode(context, lastBarcode);
        MySharedPref.saveContainer(context, container);
    }


    private void updateView(String barcode){
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
        listView.setAdapter(new ContainerPropertiesAdapter(context, container, containerPropertiesSettings));
        showProgress(false);

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (isCounterEnable && mobileSkladSettings.getCounterEnable())
        new CounterTask().execute("");
    }

//    private View createHeader(){
//        View view = getLayoutInflater().inflate(R.layout.container_header, null);
//        textViewContainer = ((TextView)view.findViewById(R.id.container_header_textview_container_value));
//        setHeaderContainer();
//        return view;
//    }

    private void setHeaderContainer(){
        textViewContainer.setText(lastBarcode);
    }

    private class ProgressTask extends AsyncTask<String, Void, String> {

//        private String readStream(InputStream is) {
//            try {
//                ByteArrayOutputStream bo = new ByteArrayOutputStream();
//                int i = is.read();
//                while(i != -1) {
//                    bo.write(i);
//                    i = is.read();
//                }
//                return bo.toString();
//            } catch (IOException e) {
//                return "";
//            }
//        }

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
                HTTPservice1CLogistic httpService1CLogistic = new HTTPservice1CLogistic();
                container = httpService1CLogistic.getContainerInfo(result);
                String errorJson = httpService1CLogistic.getErrorString();
                if (errorJson != "") {
                    Toast.makeText(context, errorJson, Toast.LENGTH_LONG).show();
                    container.setNoData();
                    lastBarcode = "";
                }
            }

            showContainer();

         }

        private String getResponse(String barcode) throws IOException{

            BufferedReader bufferedReader = null;
            errorMessage = "";

            try {
                URL url = new URL(Const.URL_HTTP_SERVICE_1C_LOGISTIC + barcode);

//                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
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
                return stringBuffer.toString();
            } catch (SocketTimeoutException exTimeout) {
                errorMessage = "Превышено время ожидания...";
                return "";
            } catch (UnknownHostException exUnknownHostException){
                errorMessage = "Проверьте подключение к Интернет";
                return "";
            } catch (IOException ex) {
                errorMessage = ex.getMessage();
                return "";
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }

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

    class ContainerOnKeyListener implements View.OnKeyListener{
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (v.getId() == R.id.activity_container_edittext_container_value){
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    if (editTextContainer.length() > 1){
                        lastBarcode = editTextContainer.getText().toString();
                        editTextContainer.setText("");
                        updateView(lastBarcode);
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

}
