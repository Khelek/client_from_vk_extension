package client_side_java;

import android.os.AsyncTask;
import android.os.Message;
import client_side_java.VKResponseClasses.*;
import com.example.android_vk_client.Authorization;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

import android.os.Handler;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 24.05.13
 * Time: 2:50
 * To change this template use File | Settings | File Templates.
 */
public class LongPollLooper {
    VKHanlerInterface activityHandler;
    Handler threadHandler;
    boolean failed;
    String key;
    String server;
    String ts;

    public LongPollLooper(String _accessToken, String _key, String _server, String _ts, VKHanlerInterface _handler){
        key = _key;
        server = _server;
        ts = _ts;
        failed = false;
        activityHandler = _handler;
        threadHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                processingLongPoll((LongPollUpdates)msg.obj);
            };
        };
        doInBackground(_accessToken);
    }

    private void doInBackground(final String accessToken){
        Thread t = new Thread(new Runnable() {
            public void run() {
                while(true){
                    if (!failed){
                        // if (isCancelled()) return null;
                        LongPollUpdates updates = getUpdates(key, server, ts);
                        if (updates.failed) {
                            failed = true; //если истек key
                            connectLongPoll(accessToken);
                        }
                        ts = updates.ts;
                        Message msg = new Message();
                        msg.obj = updates;
                        threadHandler.sendMessage(msg);
                    }
                }
            }
        });
        t.start();
    }

    public void connectLongPoll(String accessToken){
        VK vk = new VK();
        vk.connectLongPollServer(accessToken, null, new GetResponseCallback<Response<LongPollInfo>>() {
            @Override
            public void callbackCall(Response<LongPollInfo> data) {
                key = data.response.key;
                server = data.response.server;
                ts = data.response.ts;
                failed = false;
            }
        });
    }

    public LongPollUpdates getUpdates(String key, String server, String ts){
        String url = "http://" + server + "?act=a_check&key=" + key + "&ts=" + ts + "&wait=25&mode=0";
        LongPollUpdates updates = getJSON(url, LongPollUpdates.class);
        return updates;
    }

    private <T> T getJSON(String url, Type responseType) {
        Scanner scanner;
        try {
            URL request = new URL(url);
            scanner = new Scanner(request.openStream());
        } catch (Exception ex) {
            //Logger.getLogger(VK.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        String response = scanner.useDelimiter("\\Z").next();
        scanner.close();
        Gson gson = new Gson();
        T res = gson.fromJson(response, responseType);
        return res;
    }

    public void processingLongPoll(LongPollUpdates data){
        try{
            for (int i = 0; i < data.updates.size(); i++){
                JsonArray update = data.updates.get(i);
                int code = new Gson().fromJson(update.get(i),int.class);
                switch(code){
                    case 0:
                        deleteMessage(update);
                        //удаление сообщения с указанным local_id
                        break;
                    case 1:
                        //замена флагов сообщения
                        break;
                    case 2:
                        //установка флагов сообщения
                        break;
                    case 3:
                        //сброс флагов сообщения
                        break;
                    case 4:
                        messageHandler(update);
                        //добавление нового сообщения
                        break;
                    case 8:
                        friendOnline(update);
                        //друг $user_id стал онлайн
                        break;
                    case 9:
                        friendOffline(update);
                        //друг $user_id стал оффлайн
                        break;
                    case 51:
                        chatEdit(update);
                        //изменен параметр беседы $chat_id
                        break;
                    case 61:
                        typeMessageInDialog(update);
                        //набор текста в диалоге
                        break;
                    case 62:
                        typeMessageInChat(update);
                        //набор текста в беседе(chat_id)
                        break;
                    case 70:
                        //звонок
                        break;
                }
            }
        }catch (Exception ex){
        }
    }

    public void deleteMessage(JsonArray info){
        int messageId = new Gson().fromJson(info.get(1),int.class);
        activityHandler.onMessageDelete(messageId);
    }

    public void  friendOnline(JsonArray info){
        int userId = new Gson().fromJson(info.get(1),int.class);
        activityHandler.onFriendOnline(userId);
    }

    public void  friendOffline(JsonArray info){
        int userId = new Gson().fromJson(info.get(1),int.class);
        boolean away = (new Gson().fromJson(info.get(2),int.class) == 1) ? true : false;
        activityHandler.onFriendOffline(userId, away);
    }

    public void chatEdit(JsonArray info){
        int chatId = new Gson().fromJson(info.get(1),int.class);
        boolean selfEdit = (info.size() > 2) ? true : false;//возможно нужно проверять третий элемент на истину
        activityHandler.onChatEdit(chatId, selfEdit);
    }

    public void typeMessageInDialog(JsonArray info){
        int userId = new Gson().fromJson(info.get(1),int.class);
        activityHandler.onTypingMessageInDialog(userId);
    }

    public void typeMessageInChat(JsonArray info){
        int userId = new Gson().fromJson(info.get(1),int.class);
        int chatId = new Gson().fromJson(info.get(2),int.class);
        activityHandler.onTypingMessageInChat(userId, chatId);
    }

    public void messageHandler(JsonArray mess){
        LoopMessage loopMessage = new LoopMessage(mess);
       if (loopMessage.out) {
           activityHandler.onGetOutgoingMessage(loopMessage);
       }   else {
           activityHandler.onGetIncomingMessage(loopMessage);
       }
    }


}


