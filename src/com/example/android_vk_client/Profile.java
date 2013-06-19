/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.android_vk_client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import client_side_java.GetResponseCallback;
import client_side_java.VK;
import client_side_java.VKHanlerInterface;
import client_side_java.VKResponseClasses.City;
import client_side_java.VKResponseClasses.LoopMessage;
import client_side_java.VKResponseClasses.Person;
import client_side_java.VKResponseClasses.Response;
import client_side_java.VKResponseClasses.Status;
import com.example.VkClient.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @author Omni
 */
public class Profile extends Activity {

    public String accessToken;
    public String userId;
    public String userIdFriend;
    public String nameFriend;
    public String image;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.profile);

        userId = getIntent().getExtras().getString("id");
        userIdFriend = getIntent().getExtras().getString("idFriend");
        accessToken = getIntent().getExtras().getString("accessToken");
        //setUserProfile();
        getStatusAndRelationAndSexAndCityAndPhoto();

    }

    public void sendMess(View v) {

        Intent intent = new Intent(Profile.this, Message.class);
        intent.putExtra("id", userId);
        intent.putExtra("idFriend", userIdFriend);
        intent.putExtra("accessToken", accessToken);
        startActivity(intent);
    }

    public void setUserProfile() {

        final VK vk = new VK();
        vk.getUsersInfo(accessToken, userIdFriend, "online", new GetResponseCallback<Response<List<Person>>>() {

            @Override
            public void callbackCall(Response<List<Person>> data) {
                TextView Id = (TextView) findViewById(R.id.idProfile);
                Id.setText("" + userId);
                String name = data.response.get(0).firstName;
                String family = data.response.get(0).lastName;
                TextView userName = (TextView) findViewById(R.id.nameProfile);
                userName.setText(name + " " + family);

            }

        });

    }

    public void getStatusAndRelationAndSexAndCityAndPhoto() {
        final VK vk = new VK();
        vk.getUsersInfo(accessToken, userIdFriend, "online,photo_200,sex,relation,city,status,online", new GetResponseCallback<Response<List<Person>>>() {
            @Override
            public void callbackCall(Response<List<Person>> data) {  //возвращается лист, так как можно получить
                TextView Id = (TextView) findViewById(R.id.idProfile);
                Id.setText("" + userId);
                String name = data.response.get(0).firstName;
                String family = data.response.get(0).lastName;
                nameFriend = name + " " + family;
                TextView userName = (TextView) findViewById(R.id.nameProfile);
                userName.setText(nameFriend);
                // данные о нескольких людях сразу, написав в аргументах вместо userId "166197615,13451435,13451345"
                Person hum = data.response.get(0);
                String relation = hum.getRelationDescription();   //семейное положение
                //Person partner = hum.relationPartner; //возвращается id, first name, last name
                TextView userRelation = (TextView) findViewById(R.id.relation);
                userRelation.setText(relation);
                //String sex = hum.getSex();
                int sexId = hum.sexId;
                String birth = hum.birthDate;
                TextView birthday = (TextView) findViewById(R.id.textView7);
                birthday.setText(birth);
                ImageView userSex = (ImageView) findViewById(R.id.sex);
                if (sexId == 1) {
                    userSex.setImageResource(R.drawable.female);
                } else if (sexId == 2) {
                    userSex.setImageResource(R.drawable.male);
                }
                Status status = hum.status;
                TextView userStatus = (TextView) findViewById(R.id.statusProfile);
                userStatus.setText(status.text);
                City city = hum.city;
                TextView userCity = (TextView) findViewById(R.id.city);
                if (city != null) {
                    userCity.setText(city.name);
                }

                hum.getBitmapPhoto200(new GetResponseCallback<Bitmap>() {
                    @Override
                    public void callbackCall(Bitmap data) {
                        ImageView avatar = (ImageView) findViewById(R.id.imageView1);
                        avatar.setImageBitmap(data);
                    }
                });
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

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.newMessage:
                int a = 0;
                return true;
            case R.id.freinds:
                Intent intent = new Intent(Profile.this, Friends.class);
                intent.putExtra("id", userId.toString());
                intent.putExtra("accessToken", accessToken);
                startActivity(intent);
                return true;
            case R.id.dialogs:
                Intent intent2 = new Intent(Profile.this, Dialogs.class);
                intent2.putExtra("id", userId.toString());
                intent2.putExtra("accessToken", accessToken);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}