package d.drouter;

import java.util.HashMap;

public abstract class UriHandler {

    private HashMap<String, RouterRequest> mRouterData = new HashMap<>();

    public abstract void init();

    protected void regist(String scheme, String host, String path, String activityName, boolean exported, Interceptor... interceptors) {
        String key = String.format("%s://%s/%s", scheme.toLowerCase(), host.toLowerCase(), path.toLowerCase());
        RouterRequest build = new RouterRequest.Builder().exported(exported).activityName(activityName).interceptor(interceptors).build();
        mRouterData.put(key, build);
    }

    public HashMap<String, RouterRequest> getData() {
        return mRouterData;
    }
}
