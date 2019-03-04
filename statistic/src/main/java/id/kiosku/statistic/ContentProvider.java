package id.kiosku.statistic;


import id.kiosku.statistic.data.ContentData;

/**
 * Created by Dodi on 09/19/2017.
 */
public class ContentProvider extends Provider<ContentData, ContentProvider> {
    public ContentProvider(ContentData data) {
        super(data);
    }

    public ContentProvider setInteraction() {
        setInteraction(true);
        return this;
    }

    public ContentProvider setInteraction(boolean interaction) {
        data.isInteraction = interaction;
        return this;
    }

    public ContentProvider setId(int id) {
        data.ID = String.valueOf(id);
        return this;
    }

    public ContentProvider setId(short id) {
        data.ID = String.valueOf(id);
        return this;
    }

    public ContentProvider setId(long id) {
        data.ID = String.valueOf(id);
        return this;
    }

    public ContentProvider setId(String id) {
        data.ID = id;
        return this;
    }

    public ContentProvider setUserId(int id) {
        data.userId = String.valueOf(id);
        return this;
    }

    public ContentProvider setUserId(short id) {
        data.userId = String.valueOf(id);
        return this;
    }

    public ContentProvider setUserId(long id) {
        data.userId = String.valueOf(id);
        return this;
    }

    public ContentProvider setUserId(String id) {
        data.userId = id;
        return this;
    }

    public ContentProvider setType(String type) {
        data.type = type;
        return this;
    }

    public ContentProvider setCategory(String category) {
        data.category = category;
        return this;
    }

    public ContentProvider setAction(String action) {
        data.action = action;
        return this;
    }

    @Override
    public void send() {
        if(data.data.size()==0) StatisticManager.getInstance().send(data, StatisticManager.HIT_CONTENT);
        else{
            StatisticManager.getInstance().send(data, StatisticManager.HIT_CONTENT_BULK, new OnSend<ContentData>(){
                @Override
                public void onPrepare(ContentData data) {

                }

                @Override
                public void onFail(ContentData data) {

                }

                @Override
                public void onSent(ContentData data) {
                    clearData();
                }
            });
        }
    }
}
