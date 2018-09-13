package com.example.shurik.mobilesklad;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.shurik.mobilesklad.CameraScanBarCode.CameraPreview;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.SourceData;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public class ScanActivity extends AppCompatActivity {

    Context context;

    IntentIntegrator qrScan;

/*
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
*/

    ScanListener scanListener;
    ScanOnKeyListener scanOnKeyListener;

    EditText textBarCode;
    Button buttonSearch;
    Button buttonScan;

/*
    static {
        System.loadLibrary("iconv");
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scanListener = new ScanListener();
        scanOnKeyListener = new ScanOnKeyListener();

        textBarCode = (EditText) findViewById(R.id.activity_scan_edit_text_barcode);
        buttonSearch = (Button) findViewById(R.id.activity_scan_button_search);
        buttonScan = (Button) findViewById(R.id.activity_scan_button_scan);

        buttonSearch.setOnClickListener(scanListener);
        buttonScan.setOnClickListener(scanListener);

        textBarCode.setOnKeyListener(scanOnKeyListener);

        qrScan = new IntentIntegrator(this);
        //qrScan.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);


  /*      autoFocusHandler = new Handler();

        mCamera = getCameraInstance();

        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3); //почему именно эти параметры нигде не указано
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
*/

    }

    /*private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {

        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    textBarCode.setText("barcode result " + sym.getData());
                    barcodeScanned = true;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                textBarCode.setText(result.getContents());
                startActivity(new Intent(context, ContainerActivity.class));
                //qrScan.initiateScan();
            }
        }
    }

    public void startScan() {
        qrScan.initiateScan();
/*
        if (barcodeScanned) {
            barcodeScanned = false;
            textBarCode.setText("Scanning...");
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            previewing = true;
            mCamera.autoFocus(autoFocusCB);
        }
*/
    }

/*    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }*/

    class ScanListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.activity_scan_button_scan){
                startScan();
                return;
            }
            if (v.getId() == R.id.activity_scan_button_search){
                startActivity(new Intent(context, ContainerActivity.class));
            }
        }
    }

    class ScanOnKeyListener implements View.OnKeyListener{
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Toast.makeText(context, keyCode, Toast.LENGTH_SHORT).show();
            if (v.getId() == R.id.activity_scan_edit_text_barcode){
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    Toast.makeText(context,keyCode, Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }
    }


}
