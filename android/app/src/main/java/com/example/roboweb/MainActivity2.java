package com.example.roboweb;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity2 extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    WebView myWebView;
    EditText inputText;

    private String TAG = "TEST";
    private PermissionRequest mPermissionRequest;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ARROBOT";
    private static final String[] PERM_CAMERA =
            {Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        File file = new File(path + "/index.html");

        inputText = findViewById(R.id.inputText1);
        myWebView = findViewById(R.id.myWebView);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setAllowFileAccess(true);

        // Set a WebViewClient to open links in the same WebView
        myWebView.setWebViewClient(new WebViewClient());

        myWebView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.i(TAG, "onPermissionRequest");
                mPermissionRequest = request;
                final String[] requestedResources = request.getResources();
                for (String r : requestedResources) {
                    if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                        // In this sample, we only accept video capture request.
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity2.this)
                                .setTitle("Allow Permission to camera")
                                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mPermissionRequest.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE, PermissionRequest.RESOURCE_AUDIO_CAPTURE});

                                        Log.d(TAG, "Granted");
                                    }
                                })
                                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        mPermissionRequest.deny();
                                        Log.d(TAG, "Denied");
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        break;
                    }
                }
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);
                Toast.makeText(MainActivity2.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        });

        myWebView.addJavascriptInterface(new WebAppInterface(getApplicationContext()), "Android");
        myWebView.loadUrl("file:///android_asset/index.html");

        if (hasCameraPermission()) {
            Toast.makeText(MainActivity2.this, "load", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs access to your camera so you can take pictures.",
                    REQUEST_CAMERA_PERMISSION,
                    PERM_CAMERA);
        }
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(MainActivity2.this, PERM_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    }

    public void webContentChange(View view) {
        String a = inputText.getText().toString();
        myWebView.loadUrl(a);
    }
}
