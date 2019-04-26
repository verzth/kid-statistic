package id.kiosku.statistic;

import id.kiosku.statistic.data.HitData;

/**
 * Created by Dodi on 09/19/2017.
 */
public class HitProvider extends Provider<HitData, HitProvider> {
    public HitProvider(HitData data) {
        super(data);
    }
    @Override
    public void send() {
        if(data.data.size()==0) StatisticManager.getInstance().send(data, StatisticManager.HIT);
        else{
            StatisticManager.getInstance().send(data, StatisticManager.HIT_BULK, new OnSend<HitData>(){
                @Override
                public void onPrepare(HitData data) {

                }

                @Override
                public void onFail(HitData data) {
                    clearData();
                }

                @Override
                public void onSent(HitData data) {
                    clearData();
                }
            });
        }
    }
}
