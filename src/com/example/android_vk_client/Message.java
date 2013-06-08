package com.example.android_vk_client;

import android.widget.*;
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
import client_side_java.VKResponseClasses.Dialog;
import client_side_java.VKResponseClasses.LoopMessage;
import client_side_java.VKResponseClasses.Messages;
import client_side_java.VKResponseClasses.Person;
import client_side_java.VKResponseClasses.Response;
import client_side_java.VKResponseClasses.VKList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.VkClient.R;

public class Message extends Activity implements VKHanlerInterface
{   
    private List<Map<String, ?>> items;
    private SimpleAdapter adapter;
    Context context = this;
    String userId;
    String userFreindId;
    String accessToken;
    String userName;
    String freindName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.massagelist);
        userId = getIntent().getExtras().getString("id");
        userFreindId = getIntent().getExtras().getString("idFreind");
        accessToken = getIntent().getExtras().getString("accessToken");

        connectLongPoll();
        getUserAndFriendName();
        getMessegeList();
       // ListView lv = (ListView)findViewById(R.id.list);
    }

    public void connectLongPoll(){
        VK vk = new VK();
        vk.connectLongPollServer(accessToken, this, null);
    }    
      public void message_send(View v){ 
            EditText mess = (EditText)findViewById(R.id.message);
            String message = mess.getText().toString();
            sendMess(message);
            //`adapter.notifyDataSetChanged();
        }

        public void sendMess(String mess) {
        VK vk = new VK();
        vk.sendMessage(accessToken, false, userFreindId, mess, new GetResponseCallback<Response>() {
            @Override
            public void callbackCall(Response data) {
                int a = 0;
            }
        });
    }

      public void getUserAndFriendName(){
        VK vk = new VK();
        vk.getUsersInfo(accessToken, userId.toString() + "," + userFreindId.toString(), "online", new GetResponseCallback<Response<List<Person>>>() {
            
            @Override
            public void callbackCall(Response<List<Person>> data) {  //возвращается лист, так как можно получить
                // данные о нескольких людях сразу, написав в аргументах вместо userId "166197615,13451435,13451345"   \
                String name = data.response.get(0).firstName;
                String lastname = data.response.get(0).lastName;
                userName = name + " " + lastname;
                if (data.response.size() > 1){
                    String name2 = data.response.get(1).firstName;
                    String lastname2 = data.response.get(1).lastName;
                    freindName = name2 + " " + lastname2;
                    TextView dialogName = (TextView)findViewById(R.id.MessName);
                    dialogName.setText(freindName);
                } else {
                    TextView dialogName = (TextView)findViewById(R.id.MessName);
                    dialogName.setText("Заметки");
                }
            }
            
        });
    }
      
private void getMessegeList() {
      VK vk = new VK();
        vk.getHistoryList(accessToken, false,userFreindId,0,0, "online", new GetResponseCallback<Response<VKList<Messages>>>() {
            @Override
            public void callbackCall(Response<VKList<Messages>> data) {
                items = new ArrayList<Map<String, ?>>();
                for(int i = data.response.items.size() - 1;  i >= 0; i--){
                 Map<String, Object> map = new HashMap<String, Object>();
                 Messages messages = data.response.items.get(i);
                 String IdAuth = "" + messages.uid;
                 if(IdAuth.equals(userId)){
                     map.put("content",userName);
                 }
                 else{
                     map.put("content",freindName);
                 }
                     map.put("idMess",IdAuth);
                     map.put("title",  messages.body);
                     map.put("data","" + messages.date);
                     items.add(map);
                }

                adapter = new SimpleAdapter(context, items, R.layout.message,
                        new String[] {"title", "content", "data", "idMess"},
                        new int[] {R.id.title, R.id.content, R.id.data});
            ListView lv = (ListView)findViewById(R.id.list);
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
	
	switch (item.getItemId()) {
	case R.id.freinds: Intent intent = new Intent(Message.this, Profile.class);
                    intent.putExtra("idFreind", userId.toString());
                    intent.putExtra("id", userId.toString());
                    intent.putExtra("accessToken", accessToken);

                    (new VK()).stopLongPollServer();
	                startActivity(intent);
		return true;
	case R.id.dialogs:Intent intent2 = new Intent(Message.this, Freinds.class);
                 
                    intent2.putExtra("id", userId.toString());
                    intent2.putExtra("accessToken", accessToken);
                    (new VK()).stopLongPollServer();
	                startActivity(intent2);
		return true;
	default:
		return super.onOptionsItemSelected(item);
	}
    }

    public void addOrNotMessageToList(LoopMessage msg, String name){
        if ((msg.fromId + "") == userFreindId){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("idMess", msg.fromId);
            map.put("title",  msg.body);
            map.put("content", name);
            map.put("data", msg.date.toString());
            items.add(map);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetIncomingMessage(LoopMessage msg) {
        addOrNotMessageToList(msg, userName);
    }

    @Override
    public void onGetOutgoingMessage(LoopMessage msg) {
        addOrNotMessageToList(msg, freindName);
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