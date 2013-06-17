package com.example.android_vk_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import client_side_java.GetResponseCallback;
//import client_side_java.LongPollLooper;
import client_side_java.VK;
//dimport client_side_java.VKHanlerInterface;
import client_side_java.VKResponseClasses.Person;
import client_side_java.VKResponseClasses.Response;
import com.example.VkClient.R;

import java.io.EOFException;
import java.util.List;

public class Authorization extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getUserIdAndAccessToken();
        //getUserName();

    }

    public String accessToken;
    public String userId;

    public void getUserIdAndAccessToken() {
        final String address = "http://oauth.vk.com/authorize?client_id=3456670&scope=messages,friends&redirect_uri=http://oauth.vk.com/blank.html&display=popup&response_type=token";
        WebView wv = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                try {
                    accessToken = getAttributeFor("access_token", url);
                    userId = getAttributeFor("user_id", url);
                    newActivity();
                    //getUserName();

                } catch (Exception e) {

                }
                return true;
            }
        });

        wv.loadUrl(address);

    }

    public void getUserName() {
        VK vk = new VK();
        vk.getUsersInfo(accessToken, userId.toString(), "online", new GetResponseCallback<Response<List<Person>>>() {

            @Override
            public void callbackCall(Response<List<Person>> data) {  //возвращается лист, так как можно получить
                // данные о нескольких людях сразу, написав в аргументах вместо userId "166197615,13451435,13451345"
                String name = data.response.get(0).firstName;
                // TextView myName = (TextView)findViewById(R.id.text);
                //myName.setText(name);
                newActivity();
            }

        });

    }

    public static String getAttributeFor(String attrName, String url) throws Exception {
        String _access_token = "";
        int index = url.indexOf(attrName);
        if (index != -1) {
            index = index + attrName.length() + 1;//"acess_token=", 1 == "=".length()
            for (char c = url.charAt(index); index <= url.length() - 1; index++) {
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


    public void onGetMessage(String message) {
        String testM = message;//делай с ним что хочешь
    }

    public void newActivity() {
        Intent intent = new Intent(Authorization.this, Profile.class);
        intent.putExtra("id", userId.toString());
        intent.putExtra("idFreind", userId.toString());
        intent.putExtra("accessToken", accessToken);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
