package d.drouter;

import java.util.ArrayList;

public class RouterChain implements Chain{

    private ArrayList<Interceptor> mInterceptors;
    private RouterRequest mRequest;
    private int mIndex;

    public RouterChain(ArrayList<Interceptor> list, RouterRequest request, int index) {
        mInterceptors = list;
        mRequest = request;
        mIndex = index;
    }

    public RouterRequest getRequest() {
        return mRequest;
    }

    @Override
    public RouterResponse process(RouterRequest request) {
        Interceptor interceptor = mInterceptors.get(mIndex);

        RouterChain chain = new RouterChain(mInterceptors, request, ++mIndex);

        return interceptor.intercept(chain);
    }
}
