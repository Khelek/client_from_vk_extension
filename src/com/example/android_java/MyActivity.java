package com.example.android_java;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import client_side_java.GetResponseCallback;
import client_side_java.LongPollLooper;
import client_side_java.VK;
import client_side_java.VKHanlerInterface;
import client_side_java.VKResponseClasses.*;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;

public class MyActivity extends Activity implements VKHanlerInterface {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getUserIdAndAccessToken();

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
        getStatusAndRelationAndSexAndCityAndPhoto();


        connectLongPoll(); //подписка на получение обновлений
        // для остановки используй vk.stopLongPollServer()
    }

    public void getStatusAndRelationAndSexAndCityAndPhoto(){
        final VK vk = new VK();
        vk.getUsersInfo(accessToken, "5127441", "online,photo_50,sex,relation,city,status", new GetResponseCallback<Response<List<Person>>>() {
            @Override
            public void callbackCall(Response<List<Person>> data) {  //возвращается лист, так как можно получить
                // данные о нескольких людях сразу, написав в аргументах вместо userId "166197615,13451435,13451345"
                Person hum = data.response.get(0);
                String name = hum.firstName;
                String relation = hum.getRelationDescription();   //семейное положение
                Person partner = hum.relationPartner; //возвращается id, first name, last name
                String sex = hum.getSex();
                int sexId = hum.sexId;
                Status status = hum.status;
                City city = hum.city;

                hum.getBitmapPhoto50(new GetResponseCallback<Bitmap>() {
                    @Override
                    public void callbackCall(Bitmap data) {
                        ImageView imgView = (ImageView) findViewById(R.id.imageView);
                        imgView.setImageBitmap(data);
                    }
                });
            }
        });
    }

    public void connectLongPoll(){
           VK vk = new VK();
           vk.connectLongPollServer(accessToken, this, null);
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

    @Override
    public void onGetIncomingMessage(LoopMessage msg) {
        int a = 0;
    }

    @Override
    public void onGetOutgoingMessage(LoopMessage msg) {
        int a = 0;
    }

    @Override
    public void onTypingMessageInDialog(int userId) {
        int a = 0;
    }

    @Override
    public void onTypingMessageInChat(int userId, int chatId) {

    }

    @Override
    public void onMessageDelete(int messageId) {
        int a = 0;
    }

    @Override
    public void onMessageFlagsReplace(int messageId) {
    }

    @Override
    public void onMessageFlagsSet() {
    }

    @Override
    public void onMessageFlagsReset() {
    }

    @Override
    public void onChatEdit(int chatId, boolean selfEdit) {
    }

    @Override
    public void onFriendOnline(int userId) {
        int a = 0;
    }

    @Override
    public void onFriendOffline(int userId, boolean away) {
            int a = 0;
    }

    @Override
    public void onCall(int userId, int chatId) {
    }
}
