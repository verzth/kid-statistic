package id.kiosku.statistic;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.concurrent.TimeUnit;

import id.kiosku.tcx.TCX;
import id.kiosku.tcx.TCXInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import id.kiosku.statistic.apis.StatisticAPI;
import id.kiosku.statistic.apis.receivers.GeneralApiReceiver;
import id.kiosku.statistic.apis.receivers.HitApiReceiver;
import id.kiosku.statistic.data.BaseData;
import id.kiosku.statistic.data.ContentData;
import id.kiosku.statistic.data.EventData;
import id.kiosku.statistic.data.HitData;

@SuppressWarnings("WeakerAccess")
public class StatisticManager {
    private static StatisticManager manager;
    private Context context;
    private TCX tcx;

    public static void init(Context context){
        init(context,null);
    }

    public static void init(Context context, TCX tcx) {
        StatisticManager.manager = new StatisticManager(context,tcx);
    }

    public static final int HIT = 0;
    public static final int HIT_CONTENT = 1;
    public static final int HIT_EVENT = 2;
    public static final int HIT_CONTENT_BULK = 4;
    public static final int HIT_EVENT_BULK = 5;
    private Retrofit apis;
    public interface EventListener{
        void onResponse(GeneralApiReceiver response);
        void onFailed();
    }

    public StatisticManager(Context context){
        this(context,null);
    }
    public StatisticManager(Context context, TCX tcx){
        this.context = context;
        this.tcx = tcx;
        String baseUrl = context.getApplicationContext().getClass().getAnnotation(StatisticConfig.class).url();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .callTimeout(3, TimeUnit.SECONDS)
                .addInterceptor(TCXInterceptor.create(tcx))
                .build();

        apis = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static StatisticManager with(Context context){
        return with(context,null);
    }
    public static StatisticManager with(Context context, TCX tcx){
        return new StatisticManager(context, tcx);
    }

    private EventListener listener;
    public StatisticManager setListener(EventListener listener){
        this.listener = listener;
        return this;
    }

    public static HitProvider createHit(){
        return new HitProvider(new HitData());
    }
    public static ContentProvider createContent(){
        return new ContentProvider(new ContentData());
    }
    public static EventProvider createEvent(){
        return new EventProvider(new EventData());
    }

    public static void triggerStart(BaseData data){
        data.logDatetimeStart = DateTime.now();
    }
    public static void triggerEnd(BaseData data){
        if(data.longTime==null)data.longTime=0;
        data.logDatetimeEnd = DateTime.now();
        try {
            data.longTime += Seconds.secondsBetween(data.logDatetimeStart,data.logDatetimeEnd).getSeconds();
        }catch (Exception e){}
    }
    public void send(BaseData data){
        send(data,HIT,null);
    }
    public void send(BaseData data, int flags){
        send(data,flags,null);
    }
    @SuppressWarnings("unchecked")
    public void send(final BaseData data, final int flags, final OnSend callback){
        if(callback!=null)callback.onPrepare(data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                StatisticDriver.with(context).attach(data);
                StatisticAPI statisticAPI = apis.create(StatisticAPI.class);
                Call<HitApiReceiver> call;
                switch (flags){
                    case HIT_CONTENT:{
                        call = statisticAPI.content((ContentData) data);
                    }break;
                    case HIT_CONTENT_BULK:{
                        call = statisticAPI.contentBulk((ContentData) data);
                    }break;
                    case HIT_EVENT:{
                        call = statisticAPI.event((EventData)data);
                    }break;
                    case HIT_EVENT_BULK:{
                        call = statisticAPI.eventBulk((EventData)data);
                    }break;
                    default: call = statisticAPI.hit(data);
                }
                call.enqueue(new Callback<HitApiReceiver>() {
                    @Override
                    public void onResponse(Call<HitApiReceiver> call, Response<HitApiReceiver> response) {
                        if(response.code()==200){
                            if(response.body()!=null){
                                if(listener!=null)listener.onResponse(response.body());
                            }else{
                                if(listener!=null)listener.onFailed();
                            }
                        }else {
                            if (listener != null) listener.onFailed();
                        }
                        if(callback!=null)callback.onSent(data);
                    }

                    @Override
                    public void onFailure(Call<HitApiReceiver> call, Throwable t) {
                        if(listener!=null)listener.onFailed();
                        if(callback!=null)callback.onFail(data);
                    }
                });
            }
        }).start();
    }

    private static String session;
    private static String screenView;
    private static String screenType;

    static{
        if(session==null)startSession();
        screenType = "mobile";
        screenView = "NoPage";
    }

    public static String getSession() {
        return session;
    }
    public static void startSession(){
        session = StatisticUtility.randomString(26)+ DateTime.now().toString("yMMddHHmmss");
    }

    public static void setSession(String session) {
        StatisticManager.session = session;
    }

    public static String getScreenView() {
        return screenView;
    }

    public static void setScreenView(String screenView) {
        StatisticManager.screenView = screenView;
    }

    public static String getScreenType() {
        return screenType;
    }

    public static void setScreenType(String screenType) {
        StatisticManager.screenType = screenType;
    }

    public static StatisticManager getInstance() {
        return manager;
    }

}
