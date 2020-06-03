package com.viaexpressa.scannerbarcode.classes;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class LocationService extends Service {

    FusedLocationProviderClient mFusedLocationProviderClient;
    LocationCallback mLocationCallback;
    Usuario usuario;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.i("meulog", "Lat e Long é: " + locationResult.getLastLocation().getLatitude() + ","
                        + locationResult.getLastLocation().getLongitude() + " nome: " + usuario.getNomeUsuario());

                // organizando a localização para enviar ao web service.
                formatLocation(locationResult.getLastLocation());

            }
        };
    }

    private void formatLocation(Location location) {
        ArrayList<String> lista = new ArrayList<String>();
        lista.add(0, String.valueOf(location.getLatitude()));
        lista.add(1, String.valueOf(location.getLongitude()));
        lista.add(2, String.valueOf(usuario.getIdUsuario()));
        HTTPService request = new HTTPService(this);
        request.sendLocation(lista);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestLocation() {
        HTTPService request = new HTTPService(getApplicationContext());
        final ArrayList<Long> interval = request.getTimeoutLocation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval((interval != null) ? interval.get(0) : 90000);
                mLocationRequest.setFastestInterval((interval != null) ? interval.get(1) : 90000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        },  3000);



    }
}
