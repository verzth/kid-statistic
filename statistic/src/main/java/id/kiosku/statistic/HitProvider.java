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
        StatisticManager.getInstance().send(data, StatisticManager.HIT);
    }
}
