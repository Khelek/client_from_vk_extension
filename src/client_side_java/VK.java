/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side_java;

import android.os.AsyncTask;
import client_side_java.VKResponseClasses.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author haukot
 */
 /*callback = new GetResponseCallback() {
      @Override
      public void callbackCall() {
          //execution code
      }
  };*/


public class VK {
    public LongPollLooper longPollLooper;

    public void sendMessage(String accessToken, boolean isChatId, Integer id, String message, GetResponseCallback callback){//id = {chat_id: id} || {uid: id}
        String attrId = isChatId ? "chat_id" : "uid";
        String parametrs[] = {attrId, id.toString(), "message", message};
        Type responseType = new TypeToken<Response<Integer>>() {}.getType();
        new ApplyRequestTask<Integer>().execute(accessToken, "messages.send", parametrs, callback, responseType);
    }

    public void getUsersInfo(String accessToken, String uids, String fields, final GetResponseCallback callback){ //callback = function(name){...}, uids: "123123,213123,345325", fields: "sex,photo"
        String parametrs[] = {"uids", uids, "fields", fields};
        Type responseType = new TypeToken<Response<List<Person>>>() {}.getType();
        new ApplyRequestTask<List<Person>>().execute(accessToken, "execute.getUsersInfo", parametrs, callback, responseType);
    }

    /**
     * Получает текст статуса пользователя или сообщества.
     * @param accessToken
     * @param uid для сообщества должен быть отрицательным (-123213)
     * @param callback GetResponseCallback <Response<Status>>
     */
    public void getStatus(String accessToken, Integer uid, final GetResponseCallback callback){ //callback = function(name){...}, uids: "123123,213123,345325", fields: "sex,photo"
        String parametrs[] = {"uid", uid.toString()};
        Type responseType = new TypeToken<Response<Status>>() {}.getType();
        new ApplyRequestTask<List<Person>>().execute(accessToken, "execute.getUsersInfo", parametrs, callback, responseType);
    }

    public void getChatInfo(String accessToken, Integer chat_id, String fields, GetResponseCallback callback){
        String parametrs[] = {"chat_id", chat_id.toString(), "fields", fields};
        Type responseType = new TypeToken<Response<Chat>>() {}.getType();
        new ApplyRequestTask<Chat>().execute(accessToken, "messages.getChat", parametrs, callback, responseType);
    }

    public void getHistoryList(String accessToken, boolean isChatId, Integer id, Integer offset, Integer count, String fields, final GetResponseCallback callback){//id = {chat_id: id} || {uid: id}
        String attrId = isChatId ? "chat_id" : "uid";
        String parametrs[] = {attrId, id.toString(), "offset", offset.toString(), "count", count.toString(), "fields", fields};
        //parametrs["rev"] = 1;// возвращать сообщения в хронологическом порядке        
        Type responseType = new TypeToken<Response<JsonArray>>() {}.getType();
        new ApplyRequestTask<JsonArray>().execute(accessToken, "messages.getHistory", parametrs, new GetResponseCallback<Response>() {
            @Override
            public void callbackCall(Response data) {
                Type itemType = new TypeToken<Message>() {}.getType();
                callback.callbackCall(createVKList((JsonArray) data.response, itemType));
            }
        }, responseType);
    }

    public void getUnreadMessages(String accessToken, Integer count, final GetResponseCallback callback){//count = 0 for all
        String parametrs[] = {"out", "0", "count", count.toString(), "filters", "1"};
        Type responseType = new TypeToken<Response<JsonArray>>() {}.getType();
        new ApplyRequestTask<JsonArray>().execute(accessToken, "messages.get", parametrs, new GetResponseCallback<Response>() {
            @Override
            public void callbackCall(Response data) {
                Type itemType = new TypeToken<Message>() {}.getType();
                callback.callbackCall(createVKList((JsonArray) data.response, itemType));
            }
        }, responseType);
    }

    public void getFriendsList(String accessToken, Integer offset, Integer count, String order, GetResponseCallback callback){//callback = function(data), order = "hints"
        String parametrs[] = {"offset", offset.toString(), "count", count.toString(),
                "order", order, "name_case", "nom", "fields", "photo_50,online"};
        Type responseType = new TypeToken<Response<List<Person>>>() {}.getType();
        new ApplyRequestTask<List<Person>>().execute(accessToken, "friends.get",parametrs, callback, responseType);
    }

    public void getDialogsList(String accessToken, Integer offset, Integer count, final GetResponseCallback callback){//callback = function(data)
        String parametrs[] = {"offset", offset.toString(), "count", count.toString()};
        Type responseType = new TypeToken<Response<JsonArray>>() {}.getType();
        new ApplyRequestTask<JsonArray>().execute(accessToken, "messages.getDialogs", parametrs, new GetResponseCallback<Response>() {
            @Override
            public void callbackCall(Response data) {
                Type itemType = new TypeToken<Dialog>() {}.getType();
                callback.callbackCall(createVKList((JsonArray) data.response, itemType));
            }
        }, responseType);
    }

    /**
     * помечает пользователя как онлайн на 15 минут
     * @param accessToken
     * @param callback возвращает 1 в случае успеха
     */
    public void setOnline(String accessToken, GetResponseCallback callback){
        Type responseType = new TypeToken<Response<Integer>>() {}.getType();
        new ApplyRequestTask<List<Person>>().execute(accessToken, "account.setOnline", new String[]{}, callback, responseType);
    }

    /**
     * значения ненулевых счетчиков пользователя(непрочитанные сообщение, добавления в друзья и тд)
     * @param accessToken
     * @param callback
     */
    public void getCounters(String accessToken, GetResponseCallback callback){
        String parametrs[] = {"filter", "friends, messages, photos, videos, notes, gifts, events, groups"};
        Type responseType = new TypeToken<Response<Counters>>() {}.getType();
        new ApplyRequestTask<List<Person>>().execute(accessToken, "account.getCounters",parametrs, callback, responseType);
    }

    /**
     * Получает город по id
     * @param accessToken
     * @param cids   "1,43,54"
     * @param callback
     */
    public void getCitiesById(String accessToken, String cids, GetResponseCallback callback){
        String parametrs[] = {"cids", cids};
        Type responseType = new TypeToken<Response<List<City>>>() {}.getType();
        new ApplyRequestTask<Integer>().execute(accessToken, "places.getCityById", parametrs, callback, responseType);
    }

    public void connectLongPollServer(final String accessToken, final VKHanlerInterface handler, GetResponseCallback callback){     //handler = this например  , callback =  null
        String parametrs[] = {"use_ssl", "0"};
        Type responseType = new TypeToken<Response<LongPollInfo>>() {}.getType();
        if (callback == null){
            callback = new GetResponseCallback<Response<LongPollInfo>>() {
                @Override
                public void callbackCall(Response<LongPollInfo> data) {
                       if (data.error == null){
                           if (longPollLooper != null) longPollLooper.cancel(false);
                           longPollLooper = new LongPollLooper();
                           longPollLooper.execute(accessToken, data.response.key, data.response.server, data.response.ts, handler);
                       }  else {
                           //errror
                       }
                }
            };
        }
        new ApplyRequestTask<Integer>().execute(accessToken, "messages.getLongPollServer", parametrs, callback, responseType);
    }

    public void stopLongPollServer(){
        longPollLooper.cancel(false);
    }

    public void deleteFriend(String accessToken, Integer uid, GetResponseCallback callback){
        String parametrs[] = {"uid", uid.toString()};
        Type responseType = new TypeToken<Response<Integer>>() {}.getType();
        new ApplyRequestTask<Integer>().execute(accessToken, "friends.delete", parametrs, callback, responseType);
    }

    public void getBadGroups(String accessToken, GetResponseCallback callback){
        String parametrs[] = {};
        Type responseType = new TypeToken<Response<List<Group>>>() {}.getType();
        new ApplyRequestTask<List<Group>>().execute(accessToken, "execute.groups", parametrs, callback, responseType);
    }

    public <T> Response<VKList<T>> createVKList(JsonArray arr, Type itemType){
        Gson gson = new Gson();
        int count = gson.fromJson(arr.get(0), int.class);
        List<T> ls = new ArrayList<T>();
        for (int i = 1; i < arr.size(); i++){
            ls.add((T) gson.fromJson(arr.get(i), itemType));
        }
        return new Response<VKList<T>>(new VKList<T>(count, ls));
    }
}

class ApplyRequestTask<T> extends AsyncTask<Object, Void, Response<T>> {
    private GetResponseCallback callback;

    @Override
    protected Response<T> doInBackground(Object... args){
        callback = (GetResponseCallback)args[3];
        Response<T> res;
        try{
            res = baseFunction((String)args[0], (String)args[1], (String[])args[2], (Type)args[4]);
        } catch (Exception ex){
            res = null;
        }
        return res;
    }

    @Override
    protected void onPostExecute(Response<T> result) {
        callback.callbackCall(result);
    }

    public String  getParametrsString(String[] parametrs){
        StringBuilder parametrsString = new StringBuilder();
        for(int i = 0; i < parametrs.length; i++) {
            if (i != 0) {
                if (i % 2 == 0) {
                    parametrsString.append("&");
                } else {
                    parametrsString.append("=");
                }
            }
            try {
                parametrsString.append(URLEncoder.encode(parametrs[i], "UTF-8"));
            } catch (Exception ex){
                int a = 0;
            }
        }
        parametrsString.append('&');
        return parametrsString.toString();
    }

    private <T> Response<T>  baseFunction(String accessToken, String methodName, String[] parametrs, Type responseType) {//parametrs = {offset: 200, property: "value"}  , callback = function(data)
        String url = "https://api.vk.com/method/"+ methodName+"?"+getParametrsString(parametrs)+"access_token="+accessToken;
        Response<T> result = getJSON(url, responseType);
        return result;
    }

    private <T> Response<T> getJSON(String url, Type responseType) {
        Scanner scanner;
        try {
            URL request = new URL(url);
            scanner = new Scanner(request.openStream());
        } catch (Exception ex) {
            Logger.getLogger(VK.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
            String response = scanner.useDelimiter("\\Z").next();
            scanner.close();
            Gson gson = new Gson();
            Response<T> res = gson.fromJson(response, responseType);
            return res;

    }

}