package d.drouter;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class RouterManager {
    private static final RouterManager ourInstance = new RouterManager();
    private HashMap<String, RouterRequest> mRouterData = new HashMap<>();

    public static RouterManager getInstance() {
        return ourInstance;
    }

    private RouterManager() {
    }

    public HashMap<String, RouterRequest> initRouter() {
        if (mRouterData.size() != 0) {
            throw new IllegalArgumentException("重复初始化");
        } else {
            try {
                init();
            } catch (Exception e) {
                throw new IllegalArgumentException("初始化失败" + e);
            }
        }

        return mRouterData;
    }

    public RouterResponse startActivity(Context context, String uri) {
        return startActivity(context, uri, null, (int[]) null);
    }

    public RouterResponse startActivity(Context context, String uri, Bundle bundle) {
        return startActivity(context, uri, bundle, (int[]) null);
    }

    public RouterResponse startActivity(Context context, String uri, Bundle bundle, int... flags) {
        RouterRequest routerRequest = mRouterData.get(uri);
        RouterRequest newRequest = RouterRequest.newBuilder(routerRequest).context(context).bundle(bundle).flags(flags).build();

        ArrayList<Interceptor> list = new ArrayList<>();
        if (newRequest.getInterceptors() != null) {
            list.addAll(newRequest.getInterceptors());
        }

        list.add(new JumpNativeInterceptor());

        RouterChain chain = new RouterChain(list, null, 0);
        return chain.process(newRequest);
    }

    private void init() throws Exception {
    }

    private void addTargetClassPath(String path) throws Exception {
        String targetPath = path.replace("/", ".").replace(".class", "");
        Class clazz = Class.forName(targetPath);
        UriHandler uriHandler = (UriHandler) clazz.newInstance();
        uriHandler.init();
        HashMap<String, RouterRequest> data = uriHandler.getData();
        mRouterData.putAll(data);
    }
}
