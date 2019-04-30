package id.kiosku.statistic.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import id.kiosku.statistic.ContentProvider;
import id.kiosku.statistic.StatisticManager;

public class MainActivity extends AppCompatActivity {
    ContentProvider contentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },10);
        }

        contentProvider = StatisticManager.createContent().withTimeout();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticManager.setScreenType("android");
        StatisticManager.setScreenView("MainActivity");
        contentProvider.addData(
                StatisticManager.createContent()
                        .setId(1).setType("test").setCategory("test").setAction("test")
                        .putCustom("device_brand","Samsung")
                        .putCustom("device_type","S8Plus")
                        .putCustom("custom","1")
                        .build()
        );
        contentProvider.addData(
                StatisticManager.createContent()
                        .setId(2).setType("test").setCategory("test").setAction("test")
                        .putCustom("device_brand","Samsung")
                        .putCustom("device_type","S8Plus")
                        .putCustom("custom","2")
                        .build()
        );
        contentProvider.addData(
                StatisticManager.createContent()
                        .setId(3).setType("test").setCategory("test").setAction("test")
                        .putCustom("device_brand","Samsung")
                        .putCustom("device_type","S8Plus")
                        .putCustom("custom","3")
                        .build()
        );
        contentProvider.send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10){
            if(checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= 23){
                StatisticManager.createEvent()
                        .setType("application")
                        .setCategory("permission")
                        .setName("location_granted")
                        .setSuccess()
                        .putCustom("device_brand","Samsung")
                        .putCustom("device_type","S8Plus")
                        .send();
            }
        }
    }
}
