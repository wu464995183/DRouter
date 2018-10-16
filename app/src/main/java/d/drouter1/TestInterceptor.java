package d.drouter1;

import d.drouter.Interceptor;
import d.drouter.RouterChain;
import d.drouter.RouterResponse;

public class TestInterceptor implements Interceptor {
    @Override
    public RouterResponse intercept(RouterChain chain) {
        return null;
    }
}
