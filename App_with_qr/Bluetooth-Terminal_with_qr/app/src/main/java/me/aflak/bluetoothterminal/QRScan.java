package me.aflak.bluetoothterminal;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.blikoon.qrcodescanner.QrCodeActivity;
import android.app.Activity;
import android.util.Log;
import android.content.DialogInterface;
import android.app.AlertDialog;


public class QRScan extends AppCompatActivity {
    private Button button;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "ScanYourQR";
    int code = 1375890685;
    String result;

    public void invalidScan() {
        AlertDialog alertDialog = new AlertDialog.Builder(QRScan.this).create();
        alertDialog.setTitle("Invalid Scan");
        alertDialog.setMessage(result);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscan);
        button = (Button) findViewById(R.id.scanButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QRScan.this,QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != Activity.RESULT_OK)
        {
            Log.d(LOGTAG,"COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(QRScan.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            int resultInt = 0;
            boolean validInt = true;
            if(data==null)
                return;
            //Getting the passed result
            result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d(LOGTAG,"Have scan result in your app activity :"+ result);
            try {
                resultInt = Integer.parseInt(result);
            } catch (NumberFormatException | NullPointerException nfe) {
                validInt = false;
               invalidScan();
            }
            if (validInt) {
                if (resultInt != code) {
                    invalidScan();
                } else {
                    Intent i = new Intent(QRScan.this, Select.class);
                    startActivity(i);
                }
            }




        }
    }
}