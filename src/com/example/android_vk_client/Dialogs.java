package com.example.android_vk_client;

import android.widget.*;
import com.example.android_vk_client.Freinds;
import com.example.android_vk_client.Message;
import com.example.android_vk_client.Profile;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import client_side_java.GetResponseCallback;
import client_side_java.VK;
import client_side_java.VKHanlerInterface;
import client_side_java.VKResponseClasses.Dialog;
import client_side_java.VKResponseClasses.LoopMessage;
import client_side_java.VKResponseClasses.Person;
import client_side_java.VKResponseClasses.Response;
import client_side_java.VKResponseClasses.VKList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.VkClient.R;

public class Dialogs extends Activity//  implements VKHanlerInterface
{
    private List<Map<String, ?>> items;
    String userId;
    String accessToken;
    SimpleAdapter adapter;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        userId = getIntent().getExtras().getString("id");
        accessToken = getIntent().getExtras().getString("accessToken");

        ListView lv = (ListView) findViewById(R.id.list);
        setDialogs();
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Map<String, ?> dialog = items.get(position);
                dialog.get("uid");
                String title = (String) dialog.get("title");
                String idFreind = (String) dialog.get("idFreind");
                String idChat = (String) dialog.get("idChat");
                Intent intentProfile = new Intent(Dialogs.this, Message.class);
                intentProfile.putExtra("idChat", idChat);
                intentProfile.putExtra("idFriend", idFreind);
                intentProfile.putExtra("userId", userId);
                intentProfile.putExtra("accessToken", accessToken);
                startActivity(intentProfile);
            }
        };
        lv.setOnItemClickListener(itemListener);
        //  connectLongPoll();

    }

    public void connectLongPoll() {
        VK vk = new VK();
        // vk.connectLongPollServer(accessToken, this, null);
    }

    public void setDialogs() {
        VK vk = new VK();
        vk.getDialogsList(accessToken, 0, "online", new GetResponseCallback<Response<VKList<Dialog>>>() {
            @Override
            public void callbackCall(Response<VKList<Dialog>> data) {
                items = new ArrayList<Map<String, ?>>();
                for (int i = 0; i < data.response.items.size(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    Dialog dialog = data.response.items.get(i);
                    Person lastMessagingUser = data.response.items.get(i).user;
                    String empty = " ... ";
                    if (dialog.title.equalsIgnoreCase(empty)) {
                        map.put("title", "" + lastMessagingUser.firstName + " " + lastMessagingUser.lastName);
                    } else {
                        map.put("title", dialog.title);
                    }
                    if (dialog.chatId != 0) {
                        map.put("data", "" + lastMessagingUser.firstName + " " + lastMessagingUser.lastName);
                    }
                    map.put("content", dialog.body);

                    if (dialog.chatId != 0) {
                        map.put("idChat", "" +dialog.chatId);
                    }
                    // map.put("readState", "" + dialog.readState);
                    map.put("idFreind", "" + dialog.uid);
                    items.add(map);
                }
                adapter = new SimpleAdapter(context, items, R.layout.dialoglist,
                        new String[]{"title", "content", "data", "idChat", "readState", "uid"},
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

        switch (item.getItemId()) {

            case R.id.freinds:
                Intent intent = new Intent(Dialogs.this, Profile.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("idFreind", userId.toString());
                intent.putExtra("accessToken", accessToken);
                startActivity(intent);
                return true;
            case R.id.dialogs:
                Intent intent2 = new Intent(Dialogs.this, Freinds.class);
                intent2.putExtra("id", userId.toString());
                intent2.putExtra("accessToken", accessToken);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
