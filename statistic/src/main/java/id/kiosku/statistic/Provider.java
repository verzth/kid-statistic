package id.kiosku.statistic;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import id.kiosku.statistic.data.BaseData;

/**
 * Created by Dodi on 09/19/2017.
 */
@SuppressWarnings("unchecked")
public abstract class Provider<A extends BaseData, B extends Provider> {
    protected A data;
    private Handler handler;
    private static final int TOTAL_TIMEOUT = 10000;
    private boolean isTimeout, isTotal;
    private int timeout=5000,total=10;

    public Provider(A data) {
        this.data = data;
        handler = new Handler(Looper.getMainLooper());
    }

    public B setCallforward(String callforward) {
        data.callforward = callforward;
        return (B)this;
    }

    public B putCustom(@NonNull String name, String value) {
        data.names.add(name);
        data.values.add(value);
        return (B) this;
    }

    public B putCustom(@NonNull String name, int value) {
        return putCustom(name, String.valueOf(value));
    }

    public B putCustom(@NonNull String name, long value) {
        return putCustom(name, String.valueOf(value));
    }

    public B putCustom(@NonNull String name, short value) {
        return putCustom(name, String.valueOf(value));
    }

    public B putCustom(@NonNull String name, float value) {
        return putCustom(name, String.valueOf(value));
    }

    public B putCustom(@NonNull String name, double value) {
        return putCustom(name, String.valueOf(value));
    }

    public B putCustom(@NonNull String name, char value) {
        return putCustom(name, String.valueOf(value));
    }

    public B clearCustom() {
        data.names.clear();
        data.values.clear();
        return (B) this;
    }

    public B addData(A data){
        this.data.data.add(data);
        if(isTotal && this.data.data.size()>=total){
            send();
        }else if(isTotal){
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    send();
                }
            },TOTAL_TIMEOUT);
        }
        if(isTimeout){
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    send();
                }
            },timeout);
        }
        return (B)this;
    }

    public B clearData(){
        handler.removeCallbacksAndMessages(null);
        data.data.clear();
        return (B)this;
    }

    public B withTimeout(){
        isTimeout = true;
        isTotal = false;
        return (B)this;
    }

    public B withTimeout(int milliseconds){
        isTimeout = true;
        isTotal = false;
        timeout = milliseconds;
        return (B)this;
    }

    public B withTotal(){
        isTotal = true;
        isTimeout = false;
        return (B)this;
    }

    public B withTotal(int total){
        isTotal = true;
        isTimeout = false;
        this.total = total;
        return (B)this;
    }

    public abstract void send();

    public A build() {
        return data;
    }
}
