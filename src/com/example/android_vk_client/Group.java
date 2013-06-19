package com.example.android_vk_client;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import client_side_java.Algorithm;
import client_side_java.GetResponseCallback;
import client_side_java.VK;
import client_side_java.VKHanlerInterface;
import client_side_java.VKResponseClasses.LoopMessage;
import client_side_java.VKResponseClasses.Person;
import client_side_java.VKResponseClasses.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.VkClient.R;

public class Group extends Activity {
    private final Context context = this;
    private List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

    public String id;
    public String accessToken;
    OnItemClickListener itemListener;
    OnItemLongClickListener itemLongListener;
    LayoutAnimationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grouplist);
        id = getIntent().getExtras().getString("id");
        accessToken = getIntent().getExtras().getString("accessToken");
        // ListView lv = (ListView)findViewById(R.id.list);
        controller = AnimationUtils.loadLayoutAnimation(this, R.anim.list_layout_controller);
        getFrendsList();
        // lv.setLayoutAnimation(controller);


    };

    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        this.onListItemClick(l, v, position, id);

        int idstrMonth = items.indexOf(l.getItemAtPosition(position));
    }

    private void getFrendsList() {
        final VK vk = new VK();

        vk.getFriendsList(accessToken, id, 0, 0, "photo_50,online", "hints", new GetResponseCallback<Response<List<Person>>>() {
            @Override
            public void callbackCall(Response<List<Person>> data) {
                ListView groups = (ListView) findViewById(R.id.listGroup);
                Algorithm al = new Algorithm();
                List<List<Person>> friends = al.clustering(data.response);
                for (int j = 0; j < friends.size(); j++) {
                    Map<String, Object> map0 = new HashMap<String, Object>();
                    map0.put("title", null);
                    map0.put("idFriend", null);
                    items.add(map0);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("title", "Группа №" + (j + 1));
                    map2.put("idFriend", null);
                    items.add(map2);
                    for (int i = 0; i < friends.get(j).size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        Person friend = friends.get(j).get(i);
                        String name = friend.lastName + " " + friend.firstName;
                        map.put("title", name);
                        if (friend.online == 1) {
                            map.put("status", R.drawable.online);
                        }
                        int a = friend.uid;
                        String id = "" + a;
                        map.put("idFriend", id);
                        items.add(map);
                    }



                }
                final SimpleAdapter adapter = new SimpleAdapter(context, items, R.layout.freindlist,
                        new String[]{"title", "status", "idFriend"},
                        new int[]{R.id.title, R.id.status});
                groups.setAdapter(adapter);
                itemListener = new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long idItem) {

                        Map<String, ?> some = items.get(position);
                        String idFriend = (String) some.get("idFriend");
                        if (idFriend != null) {
                            Intent intentProfile = new Intent(Group.this, Profile.class);
                            intentProfile.putExtra("id", id);
                            intentProfile.putExtra("idFriend", idFriend);
                            intentProfile.putExtra("accessToken", accessToken);
                            startActivity(intentProfile);
                        }
                    }
                };
                groups.setOnItemClickListener(itemListener);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.newMessage);
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.newMessage:
                int a = 0;
                return true;
            case R.id.freinds:
                Intent intentProfile = new Intent(Group.this, Profile.class);
                intentProfile.putExtra("id", id);
                intentProfile.putExtra("idFriend", id);
                intentProfile.putExtra("accessToken", accessToken);
                startActivity(intentProfile);
                return true;
            case R.id.dialogs:
                Intent intentDialog = new Intent(Group.this, Dialogs.class);
                intentDialog.putExtra("id", id);
                intentDialog.putExtra("accessToken", accessToken);
                startActivity(intentDialog);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
