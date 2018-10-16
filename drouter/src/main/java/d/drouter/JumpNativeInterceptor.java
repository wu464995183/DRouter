package d.drouter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

public class JumpNativeInterceptor implements Interceptor {

    @Override
    public RouterResponse intercept(RouterChain chain) {
        RouterRequest request = chain.getRequest();
        Context context = request.getContext();
        Bundle bundle = request.getBundle();
        List<Integer> list = request.getFlags();
        String activityName = request.getActivityName();
        RouterResponse.Builder builder = new RouterResponse.Builder();
        if (TextUtils.isEmpty(activityName)) {
            return builder.errorCode(100).errorMassage("uri不存在").build();
        }

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, activityName));


        if (bundle != null) {
            intent.putExtras(bundle);
        }

        if (list != null) {
            for (Integer f : list) {
                intent.setFlags(f);
            }
        }

        try {
            context.startActivity(intent);
            return new RouterResponse.Builder().errorCode(0).errorMassage("跳转成功").build();
        } catch (Exception e) {
            return new RouterResponse.Builder().errorCode(101).errorMassage("跳转的界面不存在").build();
        }
    }
}
