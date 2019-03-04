package id.kiosku.statistic.apis;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import id.kiosku.statistic.apis.receivers.HitApiReceiver;
import id.kiosku.statistic.data.BaseData;
import id.kiosku.statistic.data.ContentData;
import id.kiosku.statistic.data.EventData;

/**
 * Created by Dodi on 01/27/2017.
 */

public interface StatisticAPI {
    @POST("hit")
    Call<HitApiReceiver> hit(@Body BaseData params);
    @POST("content")
    Call<HitApiReceiver> content(@Body ContentData params);
    @POST("content/bulk")
    Call<HitApiReceiver> contentBulk(@Body ContentData params);
    @POST("event")
    Call<HitApiReceiver> event(@Body EventData params);
    @POST("event/bulk")
    Call<HitApiReceiver> eventBulk(@Body EventData params);
}
