package id.kiosku.statistic.demo;

import android.app.Application;

import id.kiosku.statistic.StatisticConfig;
import id.kiosku.statistic.StatisticManager;
import id.kiosku.tcx.TCX;
import id.kiosku.utils.LocationDriver;

@StatisticConfig(
    url = "https://server_url"
)
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocationDriver.init(this);
        StatisticManager.init(
            this,
            new TCX(
                "https://server_url/",
                "APP_ID",
                "APP_KEY",
                "APP_MASTER_TOKEN_KEY"
            )
        );
    }
}
