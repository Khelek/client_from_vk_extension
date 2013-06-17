package com.example.android_vk_client;

import android.widget.*;
import client_side_java.VKResponseClasses.*;
import com.example.android_vk_client.Profile;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import client_side_java.GetResponseCallback;
import client_side_java.VK;
import client_side_java.VKHanlerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.VkClient.R;

public class Message extends Activity implements VKHanlerInterface {
    private final int MY_INDEX = 0;
    private final int FRIEND_INDEX = 1;
    private List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
    private SimpleAdapter adapter;
    Context context = this;
    List<Person> talkers = new ArrayList<Person>();
    String accessToken;
    String chatId;
    boolean isChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.massagelist);
        String userId = getIntent().getExtras().getString("userId");
        accessToken = getIntent().getExtras().getString("accessToken");
        chatId = getIntent().getExtras().getString("idChat");
        String userFreindId = getIntent().getExtras().getString("idFriend");
        //userId = talkers.get(MY_INDEX).uid
        //friendId = talkers.get(FRIEND_INDEX).uid
        //userName = talkers.get(MY_INDEX).getFullName()
        //friendName - аналогично
        //сделал для единообразия с беседами

        connectLongPoll();
        if (chatId != null){
            getChatInfo(chatId, userId);
            getMessagesList(chatId, true);
            isChat = true;
        }  else {
            getUserAndFriendName(userId, userFreindId);
            getMessagesList(userFreindId, false);
            isChat = false;
        }
        // ListView lv = (ListView)findViewById(R.id.list);
    }

    public void connectLongPoll() {
        VK vk = new VK();
        vk.connectLongPollServer(accessToken, this, null);
    }

    public void message_send(View v) {
        EditText mess = (EditText) findViewById(R.id.message);
        String message = mess.getText().toString();
        if (isChat) {
            sendMess(message, chatId, true);
        } else {
            sendMess(message, talkers.get(FRIEND_INDEX).uid.toString(), false);
        }
        //очищать поле ввода
    }

    public void sendMess(String mess, String destinationId, boolean isChat) {
        VK vk = new VK();
        vk.sendMessage(accessToken, isChat, destinationId, mess, new GetResponseCallback<Response>() {
            @Override
            public void callbackCall(Response data) {
                int a = 0;
            }
        });
    }

    private void getChatInfo(String chatId, final String userId){
        VK vk = new VK();
        vk.getChatInfo(accessToken, chatId, "online", new GetResponseCallback<Response<Chat>>() {

            @Override
            public void callbackCall(Response<Chat> data) {
                TextView dialogName = (TextView) findViewById(R.id.MessName);
                dialogName.setText(data.response.title);

                List<Person> users = data.response.users;
                for (int i = 0; i < users.size(); i++) {
                    if (i == MY_INDEX) {
                        talkers.add(null);
                    }
                    if (userId.equals(users.get(i).uid.toString())) {
                        talkers.set(MY_INDEX, users.get(i));
                    } else {
                        talkers.add(data.response.users.get(i));
                    }
                }
            }

        });
    }

    public void getUserAndFriendName(String userId, String userFriendId) {
        VK vk = new VK();
        vk.getUsersInfo(accessToken, userId + "," + userFriendId, "online", new GetResponseCallback<Response<List<Person>>>() {

            @Override
            public void callbackCall(Response<List<Person>> data) {  //возвращается лист, так как можно получить
                // данные о нескольких людях сразу, написав в аргументах вместо userId "166197615,13451435,13451345"   \
                talkers.add(data.response.get(MY_INDEX));
                if (data.response.size() > 1) {
                    talkers.add(data.response.get(FRIEND_INDEX));
                    TextView dialogName = (TextView) findViewById(R.id.MessName);
                    dialogName.setText(talkers.get(FRIEND_INDEX).getFullName());
                } else {
                    TextView dialogName = (TextView) findViewById(R.id.MessName);
                    dialogName.setText("Заметки");
                }
            }

        });
    }

    private Person getPersonWithId(int uid){
        for (int i = 0; i < talkers.size(); i++) {
            Person talker = talkers.get(i);
            if (talker.uid == uid) {
                return talker;
            }
        }
        return new Person();
    }

    private void getMessagesList(String conversationId, boolean isChat) {
        VK vk = new VK();
        vk.getHistoryList(accessToken, isChat, conversationId, 0, 0, "online", new GetResponseCallback<Response<VKList<Messages>>>() {
            @Override
            public void callbackCall(Response<VKList<Messages>> data) {
                for (int i = data.response.items.size() - 1; i >= 0; i--) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    Messages messages = data.response.items.get(i);
                    map.put("content", getPersonWithId(messages.uid).getFullName());
                    map.put("idMess", messages.uid);
                    map.put("title", messages.body);
                    map.put("data", "" + messages.date);
                    items.add(map);
                }

                adapter = new SimpleAdapter(context, items, R.layout.message,
                        new String[]{"title", "content", "data", "idMess"},
                        new int[]{R.id.title, R.id.content, R.id.data});
                ListView lv = (ListView) findViewById(R.id.list);
                lv.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String userId =  talkers.get(MY_INDEX).uid.toString();
        switch (item.getItemId()) {
            case R.id.freinds:
                Intent intent = new Intent(Message.this, Profile.class);
                intent.putExtra("idFreind", userId);
                intent.putExtra("id", userId);
                intent.putExtra("accessToken", accessToken);

                (new VK()).stopLongPollServer();
                startActivity(intent);
                return true;
            case R.id.dialogs:
                Intent intent2 = new Intent(Message.this, Freinds.class);

                intent2.putExtra("id", userId);
                intent2.putExtra("accessToken", accessToken);
                (new VK()).stopLongPollServer();
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getAuthorIdAndAddMessageToList(final LoopMessage msg){
        VK vk = new VK();
        vk.getMessagesById(accessToken, msg.messageId + "", new GetResponseCallback<Response<VKList<Messages>>>() {
            @Override
            public void callbackCall(Response<VKList<Messages>> data) {
                addMessageToList(msg, getPersonWithId(data.response.items.get(0).uid).getFullName());
            }
        });
    }

    public void addMessageToList(LoopMessage msg, String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("idMess", msg.fromId);
        map.put("title", msg.body);
        map.put("content", name);
        map.put("data", msg.date.toString());
        items.add(map);
        adapter.notifyDataSetChanged();
    }

    private boolean inCurrentDialog(LoopMessage msg){
        if (msg.fromId > 2000000000) {
            msg.fromId = msg.fromId - 2000000000;
        }
        return (msg.fromId + "").equals(isChat ? chatId : talkers.get(FRIEND_INDEX).uid.toString());
    }

    @Override
    public void onGetIncomingMessage(LoopMessage msg) {
        if (inCurrentDialog(msg)) {
            if (isChat) {
                getAuthorIdAndAddMessageToList(msg);
            } else {
                addMessageToList(msg, talkers.get(FRIEND_INDEX).getFullName());
            }
        } else {
            //оповещение о новом сообщении
        }
    }

    @Override
    public void onGetOutgoingMessage(LoopMessage msg) {
        if (inCurrentDialog(msg)) {
            addMessageToList(msg, talkers.get(MY_INDEX).getFullName());
        }
    }

    @Override
    public void onTypingMessageInDialog(int userId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTypingMessageInChat(int userId, int chatId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onMessageDelete(int messageId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onMessageFlagsReplace(int messageId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onMessageFlagsSet() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onMessageFlagsReset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onChatEdit(int chatId, boolean selfEdit) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFriendOnline(int userId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFriendOffline(int userId, boolean away) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCall(int userId, int chatId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}