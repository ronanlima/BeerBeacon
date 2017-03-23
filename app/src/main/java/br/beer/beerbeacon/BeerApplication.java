package br.beer.beerbeacon;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;
import com.estimote.sdk.SecureRegion;

import java.util.List;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Ronan.lima on 21/02/17.
 */

public class BeerApplication extends Application {
    public static final String TAG = BeerApplication.class.getCanonicalName().toUpperCase();

    public static BeerApplication instance = null;

    public static BeerApplication getInstance() {
        return instance;
    }

    private BeaconManager beaconManager;
    private String idConsumoFBase;

    @Override
    public void onCreate() {
        super.onCreate();

        if (getInstance() == null) {
            instance = this;
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build());

            EstimoteSDK.initialize(getApplicationContext(), "beerbeacon-lju", "a97dcc9e7ad472e291b61cc8680297dc");
            EstimoteSDK.enableDebugLogging(true);

            beaconManager = new BeaconManager(getApplicationContext());

            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    beaconManager.startMonitoring(new SecureRegion(
                            "regiao monitorada",
                            UUID.fromString("44FEBBAD-3A05-B196-516B-01DF2CD8EA15"),
                            5653, 53208
                    ));
                }
            });

            beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                @Override
                public void onEnteredRegion(Region region, List<Beacon> list) {
                    showNotification("Vamos beber", "Entre no estabelecimento à sua frente para " +
                            "experimentar os diversos sabores de chopp disponíveis");
                }

                @Override
                public void onExitedRegion(Region region) {
                    Log.i(TAG, "Saiu do range do beacon " + region.getIdentifier());
                }
            });
        }
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, notification);
    }

    public String getIdConsumoFBase() {
        return idConsumoFBase;
    }

    public void setIdConsumoFBase(String idConsumoFBase) {
        this.idConsumoFBase = idConsumoFBase;
    }
}
