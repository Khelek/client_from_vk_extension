package com.example.android_java;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import client_side_java.GetResponseCallback;
import client_side_java.LongPollLooper;
import client_side_java.VK;
import client_side_java.VKHanlerInterface;
import client_side_java.VKResponseClasses.Person;
import client_side_java.VKResponseClasses.Response;

import java.io.EOFException;
import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getUserIdAndAccessToken();

      //   VK vk = new VK();
     /*   vk.getLongPollServer("token", this, new GetResponseCallback() {
            @Override
            public void callbackCall(Object data) {
                int a = 0;
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });*/
    }

    public String accessToken;
    public String userId;

    public void getUserIdAndAccessToken(){
        final String address = "http://oauth.vk.com/authorize?client_id=3456670&scope=messages,friends&redirect_uri=http://oauth.vk.com/blank.html&display=popup&response_type=token";
        WebView wv = (WebView)findViewById(R.id.webView);
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                try {
                    accessToken = getAttributeFor("access_token", url);
                    userId = getAttributeFor("user_id", url);
                    Process();
                } catch (Exception e)
                {

                }
                return true;
            }
        });
        wv.loadUrl(address);
    }

    public void Process(){
       getUserName();
        //sendMess(accessToken);
    }

    public void getUserName(){
        VK vk = new VK();
        vk.getUsersInfo(accessToken, userId, "online", new GetResponseCallback<List<Person>>() {
            @Override
            public void callbackCall(List<Person> data) {  //возвращается лист, так как можно получить
                // данные о нескольких людях сразу, написав в аргументах вместо userId "166197615,13451435,13451345"
                String name = data.get(0).firstName;
            }
        });
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
        vk.sendMessage(token, false, 5127441, "труруру", new GetResponseCallback<Response>() {
            @Override
            public void callbackCall(Response data) {
                int a = 0;
            }
        });
    }

    public void onGetMessage(String message){
        String testM = message;//делай с ним что хочешь
    }

}
