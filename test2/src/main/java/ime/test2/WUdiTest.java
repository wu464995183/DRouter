package ime.test2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import d.drouter.RouterManager;
import d.drouter.RouterResponse;
import ime.annotation.UriAnnotation;

@UriAnnotation(
        path = "login/test11",
        scheme = "wudi",
        host = "d"
)
public class WUdiTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("打开的肯定肯定2222");
        setContentView(textView);

        textView.setTextColor(Color.YELLOW);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterResponse routerResponse = RouterManager.getInstance().startActivity(WUdiTest.this, "wudi1://d/login");
                Log.e("debug", routerResponse.getErrorMassage());
            }
        });

    }
}
