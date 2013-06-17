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
    private List<Map<String, ?>> items;

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


    }

    ;


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
                List<ListView> groups = new ArrayList<ListView>();
                Algorithm al = new Algorithm();
                List<List<Person>> friends = al.clustering(data.response);
                for (int j = 0; j < friends.size(); j++) {

                    ListView group = null;
                    if (j > 3) break;
                    switch (j) {
                        case 0:
                            group = (ListView) findViewById(R.id.list1);
                            break;
                        case 1:
                            group = (ListView) findViewById(R.id.list2);

                            break;
                        case 2:
                            group = (ListView) findViewById(R.id.list3);

                            break;
                        case 3:
                            group = (ListView) findViewById(R.id.list4);

                            break;
                    }
                    group.setPadding(0, 20, 0, 0);
                    groups.add(group);


                    items = new ArrayList<Map<String, ?>>();
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
                        map.put("idFreind", id);
                        items.add(map);
                    }
                    final List<SimpleAdapter> adapters = new ArrayList<SimpleAdapter>();
                    final SimpleAdapter adapter = new SimpleAdapter(context, items, R.layout.freindlist,
                            new String[]{"title", "status", "idFreind"},
                            new int[]{R.id.title, R.id.status});
                    adapters.add(adapter);
                    groups.get(j).setAdapter(adapter);
                    itemListener = new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            Toast.makeText(getApplicationContext(),
                                    " удалён.",
                                    Toast.LENGTH_SHORT).show();
                            Map<String, ?> itemAtPosition = (Map<String, ?>) parent.getItemAtPosition(position);
                            Map<String, ?> some = items.get(position);

                            String title = (String) some.get("title");
                            String idFreind = (String) some.get("idFreind");
                            Intent intentProfile = new Intent(Group.this, Profile.class);
                            intentProfile.putExtra("id", id);
                            intentProfile.putExtra("idFreind", idFreind);
                            intentProfile.putExtra("accessToken", accessToken);
                            startActivity(intentProfile);
                        }
                    };
                    groups.get(j).setOnItemClickListener(itemListener);
                    final int finalJ = j;
                    itemLongListener = new OnItemLongClickListener() {

                        @Override
                        public boolean onItemLongClick(final AdapterView<?> parent, View v,
                                                       final int position, long id) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Удалить из списка друзей? ");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            int id = items.indexOf(parent.getItemAtPosition(position));
                                            Map<String, ?> selectItem = items.get(id);
                                            String title = (String) selectItem.get("title");
                                            items.remove(parent.getItemAtPosition(position));
                                            adapters.get(finalJ).notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(),
                                                    title + " удалён.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            builder.setNegativeButton("Нет",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.cancel();
                                        }
                                    });

                            builder.show();

                            return true;
                        }

                    };
                    groups.get(j).setOnItemLongClickListener(itemLongListener);
                }
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
                Intent intentProfile = new Intent(Group.this, Profile.class);
                intentProfile.putExtra("id", id);
                intentProfile.putExtra("idFreind", id);
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
