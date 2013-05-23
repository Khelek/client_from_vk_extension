package com.example.android_java;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import client_side_java.GetResponseCallback;
import client_side_java.VK;
import client_side_java.VKResponseClasses.Response;

import java.io.EOFException;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final String address = "http://oauth.vk.com/authorize?client_id=3456670&scope=messages,friends&redirect_uri=http://oauth.vk.com/blank.html&display=popup&response_type=token";

        WebView wv = (WebView)findViewById(R.id.webView);

        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                try {
                    sendMess(getAttributeFor("access_token", url));
                } catch (Exception e) {
                    //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                return true;
            }
        });
        wv.loadUrl(address);
    }

    public static String getAttributeFor(String attrName, String url) throws Exception{
        String _access_token = "";
        int index = url.indexOf(attrName);
        if (index != -1) {
            index = index + attrName.length() + 1;//"acess_token=", 1 == "=".length()
            for (char c = url.charAt(index); index <= url.length() - 1 ; index++) {
                c = url.charAt(index);
                if (c == '&') break;
                _access_token += c;
            }
        } else {
            System.out.println("Что-то пошло не так, попробуйте еще разок.");
            throw new EOFException("Отсутствует " + attrName);
        }
        return _access_token;
    }

    public void sendMess(String token) {
        VK vk = new VK();
        vk.sendMessage(token, false, 166197615, "труруру", new GetResponseCallback<Response>() {
            @Override
            public void callbackCall(Response data) {
                int a = 0;
                //сделать чтото с тем, что пришло в ответ(т.е. с data);
            }
        });
    }

}
