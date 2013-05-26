/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side_java;

import android.os.AsyncTask;
import client_side_java.VKResponseClasses.Chat;
import client_side_java.VKResponseClasses.Dialog;
import client_side_java.VKResponseClasses.Group;
import client_side_java.VKResponseClasses.Message;
import client_side_java.VKResponseClasses.Person;
import client_side_java.VKResponseClasses.Response;
import client_side_java.VKResponseClasses.VKList;
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
    
    public void sendMessage(String accessToken, boolean isChatId, Integer id, String message, GetResponseCallback callback){//id = {chat_id: id} || {uid: id}
        String attrId = isChatId ? "chat_id" : "uid";
        String parametrs[] = {attrId, id.toString(), "message", message};
        Type responseType = new TypeToken<Response<Integer>>() {}.getType();
        new ApplyRequestClass<Integer>(accessToken, "messages.send", parametrs, callback, responseType);
    }
    
    public void getUsersInfo(String accessToken, String uids, String fields, final GetResponseCallback callback){ //callback = function(name){...}, uids: "123123,213123,345325", fields: "sex,photo"
        String parametrs[] = {"uids", uids, "fields", fields};        
        Type responseType = new TypeToken<Response<List<Person>>>() {}.getType();
        new ApplyRequestClass<List<Person>>(accessToken, "users.get", parametrs, callback, responseType);
    }
    
    public void getChatInfo(String accessToken, Integer chat_id, String fields, GetResponseCallback callback){
        String parametrs[] = {"chat_id", chat_id.toString(), "fields", fields};        
        Type responseType = new TypeToken<Response<Chat>>() {}.getType();
        new ApplyRequestClass<Chat>(accessToken, "messages.getChat", parametrs, callback, responseType);
    }

    public void getHistoryList(String accessToken, boolean isChatId, Integer id, Integer offset, Integer count, String fields, final GetResponseCallback callback){//id = {chat_id: id} || {uid: id}
        String attrId = isChatId ? "chat_id" : "uid";
        String parametrs[] = {attrId, id.toString(), "offset", offset.toString(), "count", count.toString(), "fields", fields};
        //parametrs["rev"] = 1;// возвращать сообщения в хронологическом порядке        
        Type responseType = new TypeToken<Response<JsonArray>>() {}.getType();
        new ApplyRequestClass<JsonArray>(accessToken, "messages.getHistory", parametrs, new GetResponseCallback<Response>() {
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
        new ApplyRequestClass<JsonArray>(accessToken, "messages.get", parametrs, new GetResponseCallback<Response>() {
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
        new ApplyRequestClass<List<Person>>(accessToken, "friends.get",parametrs, callback, responseType);
    }

    public void getDialogsList(String accessToken, Integer offset, Integer count, final GetResponseCallback callback){//callback = function(data)
        String parametrs[] = {"offset", offset.toString(), "count", count.toString()};
        Type responseType = new TypeToken<Response<JsonArray>>() {}.getType();
        new ApplyRequestClass<JsonArray>(accessToken, "messages.getDialogs", parametrs, new GetResponseCallback<Response>() {
            @Override
            public void callbackCall(Response data) {                
                Type itemType = new TypeToken<Dialog>() {}.getType();
                callback.callbackCall(createVKList((JsonArray) data.response, itemType));
            }
    }, responseType);
    }

    public void getLongPollServer(final String accessToken, final Object uiActivity, GetResponseCallback callback){
        String parametrs[] = {"use_ssl", "0"};
        Type responseType = null;
        new ApplyRequestClass<Integer>(accessToken, "messages.getLongPollServer", parametrs, new GetResponseCallback<Object>() {
            @Override
            public void callbackCall(Object data){
                new LongPollLooper().execute(accessToken, uiActivity);
            }
        }, responseType);
    }

    public void deleteFriend(String accessToken, Integer uid, GetResponseCallback callback){
        String parametrs[] = {"uid", uid.toString()};
        Type responseType = new TypeToken<Response<Integer>>() {}.getType();
        new ApplyRequestClass<Integer>(accessToken, "friends.delete", parametrs, callback, responseType);
    }

    public void getBadGroups(String accessToken, GetResponseCallback callback){
        String parametrs[] = {};
        Type responseType = new TypeToken<Response<List<Group>>>() {}.getType();
        new ApplyRequestClass<List<Group>>(accessToken, "execute.groups", parametrs, callback, responseType);
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

class ApplyRequestClass<T>{

    public ApplyRequestClass(String token, String method, String[] parametrs, GetResponseCallback callback, Type responseType){
          new ApplyRequestTask().execute(token, method, parametrs, callback, responseType);
    }

    class ApplyRequestTask extends AsyncTask<Object, Void, Response<T>> {
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
            try {
                URL request = new URL(url);
                Scanner scanner = new Scanner(request.openStream());
                String response = scanner.useDelimiter("\\Z").next();
                scanner.close();
                Gson gson = new Gson();
                Response<T> res = gson.fromJson(response, responseType);
                return res;
            } catch (Exception ex) {
                Logger.getLogger(VK.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

    }
}
    

