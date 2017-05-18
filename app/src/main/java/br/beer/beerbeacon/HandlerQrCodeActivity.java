package br.beer.beerbeacon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.Serializable;

/**
 * Created by ronanlima on 17/05/17.
 */

public class HandlerQrCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentIntegrator intentIntegrator = new IntentIntegrator(HandlerQrCodeActivity.this);
        intentIntegrator.setCaptureActivity(QrCodeActivity.class);
        intentIntegrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("TAG", "Deu ruim, não conseguiu ler a mesa");
                finish();
            } else {
                String resultQrCode = result.getContents();
                if (resultQrCode != null && !resultQrCode.isEmpty()) {
                    Log.d("TAG", resultQrCode);
                }
            }
        }
    }

    interface ReaderQrcode extends Serializable{
        void callListener(String qrcode);
    }
}
