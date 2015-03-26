package artlines.kz.reader;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.io.IOException;
import java.nio.charset.Charset;

public class ContentView extends Activity {

    WebView webView;
    String disp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_view);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");


        try {
            if(Globals.rowData.getResource().getData().length>0)

            disp=new String(Globals.rowData.getResource().getData());
            Log.d("dispString", disp);


                webView.loadData(disp, "text/html; charset=utf-8",null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}