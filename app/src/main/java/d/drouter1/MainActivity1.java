package d.drouter1;

import android.app.Activity;
import android.os.Bundle;

import d.drouter.RouterManager;
import d.drouter.RouterResponse;
import ime.annotation.UriAnnotation;

@UriAnnotation(
        path = "login",
        scheme = "wudi1",
        host = "d"
)
public class MainActivity1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        try {
//            RouterManager.getInstance().init();
            RouterResponse routerResponse = RouterManager.getInstance().startActivity(this, "wudi://d/login1");
////            Log.e("debug", routerResponse.getErrorMassage());
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }
}
