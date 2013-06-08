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

public class Freinds extends Activity
{   
    private final Context context = this;
    private List<Map<String, ?>> items;
    SimpleAdapter adapter;
    public String userId;
    public String accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        Button group = new Button(this);
        group.setText("группировать друзей");
        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(Freinds.this, Group.class);
                intentProfile.putExtra("id", userId);
                intentProfile.putExtra("accessToken", accessToken);
                startActivity(intentProfile);

            }
        };

        // присвоим обработчик кнопке OK (btnOk)
        group.setOnClickListener(oclBtnOk);
        userId = getIntent().getExtras().getString("id");
        accessToken = getIntent().getExtras().getString("accessToken");
        ListView lv = (ListView)findViewById(R.id.list);
       RelativeLayout setButton = (RelativeLayout)findViewById(R.id.setButt);
        setButton.addView(group);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.list_layout_controller);
         getFrendsList();
        lv.setLayoutAnimation(controller);
        OnItemClickListener itemListener;
        itemListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {
             //    Toast.makeText(getApplicationContext(),
             //                                " удалён.",
             //                               Toast.LENGTH_SHORT).show();
                        Map<String, ?> itemAtPosition = (Map<String, ?>)parent.getItemAtPosition(position);
                        Map<String, ?> some = items.get(position);

                        String title = (String)some.get("title");
                        String idFreind = (String)some.get("idFreind");
                        Intent intentProfile = new Intent(Freinds.this, Profile.class);
                        intentProfile.putExtra("id", userId);
                        intentProfile.putExtra("idFreind", idFreind);
                        intentProfile.putExtra("accessToken", accessToken);
                        startActivity(intentProfile);
            }
        };
        lv.setOnItemClickListener(itemListener);
        OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {

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
                                        adapter.notifyDataSetChanged();
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
        
		lv.setOnItemLongClickListener(itemLongListener);
    }    

    protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		this.onListItemClick(l, v, position, id);
                
			int idstrMonth =  items.indexOf(l.getItemAtPosition(position));
	}
    
    private void getFrendsList(){
        final VK vk = new VK();
          vk.getFriendsList(accessToken, userId, 0, 0, "photo_50,online", "hints", new GetResponseCallback<Response<List<Person>>>() {
        @Override
        public void callbackCall(Response<List<Person>> data) {
            items = new ArrayList<Map<String, ?>>();
            for(int i = 0; i < data.response.size(); i++){
                Map<String, Object> map = new HashMap<String, Object>();
                Person freind = data.response.get(i);
                String name =  freind.lastName + " " + freind.firstName;
                map.put("title",name);
                if (freind.online == 1){
                map.put("status", R.drawable.online);
                }
                int a = freind.uid;
                String id = "" + a;
                map.put("idFreind", id);
                items.add(map);
            }
             adapter = new SimpleAdapter(context, items, R.layout.freindlist,
                            new String[] {"title", "status", "idFreind"},
                            new int[] {R.id.title, R.id.status});
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
          
	case R.id.freinds:Intent intentProfile = new Intent(Freinds.this, Profile.class);
                                intentProfile.putExtra("id", userId);
                                intentProfile.putExtra("idFreind", userId);
                                intentProfile.putExtra("accessToken", accessToken);
                                startActivity(intentProfile);
		return true;
	case R.id.dialogs:Intent intentDialog = new Intent(Freinds.this, Dialogs.class);
                intentDialog.putExtra("id", userId);
                intentDialog.putExtra("accessToken", accessToken);
	        startActivity(intentDialog);
		return true;
	default:
		return super.onOptionsItemSelected(item);
	}
    }

   
   
}
