package d.drouter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RouterRequest {
    private boolean mExported;
    private String mActivityName;
    private ArrayList<Interceptor> mInterceptors;
    private Bundle mBundle;
    private List mFlags;
    private Context mContext;

    RouterRequest(Builder builder) {
        mExported = builder.mExported;
        mActivityName = builder.mActivityName;
        mInterceptors = builder.mInterceptors;
        mBundle = builder.mBundle;
        mFlags = builder.mFlags;
        mContext = builder.mContext;
    }

    public ArrayList<Interceptor> getInterceptors() {
        return mInterceptors;
    }

    public Context getContext() {
        return mContext;
    }

    public boolean getExported() {
        return mExported;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public List getFlags() {
        return mFlags;
    }

    public String getActivityName() {
        return mActivityName;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static Builder newBuilder(RouterRequest request) {
        return new Builder(request);
    }

    public static class Builder {

        private boolean mExported;
        private HashMap<String, Object> mParamsMap;
        private String mActivityName;
        private ArrayList<Interceptor> mInterceptors;
        private String mUrl;
        private Bundle mBundle;
        private List mFlags;
        private Context mContext;

        public Builder() {
        }

        Builder(RouterRequest request) {
            if (request != null) {
                mExported = request.mExported;
                mActivityName = request.mActivityName;
                mInterceptors = request.mInterceptors;
                mBundle = request.mBundle;
                mFlags = request.mFlags;
                mContext = request.mContext;
            }
        }

        public Builder context(Context context) {
            mContext = context;
            return this;
        }

        public Builder activityName(String name) {
            mActivityName = name;
            return this;
        }

        public Builder exported(boolean b) {
            mExported = b;
            return this;
        }

        public Builder interceptor(Interceptor... interceptors) {
            if (interceptors != null && interceptors.length > 0) {
                mInterceptors = (ArrayList<Interceptor>) Arrays.asList(interceptors);
            }

            return this;
        }

        /**
         * @param uri router://com.test/te?p1=xxx&p2=xx
         * @return
         */
        public Builder uri(String uri) {
            if (TextUtils.isEmpty(uri)) {
                String[] split = uri.split("\\?");
                if (split.length == 2) {
                    mUrl = split[0];
                    mParamsMap = getParams(split[1]);
                }
            }
            return this;
        }

        public Builder bundle(Bundle bundle) {
            mBundle = bundle;
            return this;
        }

        public Builder flags(int... flags) {
            if (flags != null && flags.length > 0) {
                mFlags = Arrays.asList(flags);
            }
            return this;
        }

        public RouterRequest build() {
            return new RouterRequest(this);
        }

        private HashMap<String, Object> getParams(String path) {
            //  p1=xxxxxx&p2=xxxxx&p3=xxxxx
            HashMap<String, Object> map = new HashMap<>();
            if (TextUtils.isEmpty(path)) {
                String[] param = path.split("&");
                for (int i = 0; i < param.length; i++) {
                    String[] kv = param[i].split("=");
                    if (kv.length != 2) {
                        continue;
                    }
                    map.put(kv[0], kv[1]);
                }
            }

            return map;
        }
    }
}
