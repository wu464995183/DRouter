package ime.test1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import d.drouter.RouterManager;
import d.drouter.RouterResponse;
import ime.annotation.UriAnnotation;


@UriAnnotation(
        path = "test1",
        scheme = "wudi",
        host = "d"
)
public class TestAct1 extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("打开的肯定肯定");
        setContentView(textView);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterResponse routerResponse = RouterManager.getInstance().startActivity(TestAct1.this, "wudi://d/login");
                Log.e("debug", routerResponse.getErrorMassage());
            }
        });

    }
}
