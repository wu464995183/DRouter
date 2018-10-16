package d.drouter1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

import d.drouter.RouterManager;
import d.drouter.RouterRequest;
import d.drouter.RouterResponse;
import d.router.DRouterPluginTransform;
import ime.annotation.UriAnnotation;
import ime.test2.WUdiTest;

@UriAnnotation(
        path = "login",
        scheme = "wudi",
        host = "d"
)
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                HashMap<String, RouterRequest> stringRouterRequestHashMap = RouterManager.getInstance().initRouter();



        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, WUdiTest.class));

            RouterResponse routerResponse = RouterManager.getInstance().startActivity(MainActivity.this, "wudi://d/login/test11");


//        try {
////            Log.e("debug", routerResponse.getErrorMassage());
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

            }
        });
    }
}
