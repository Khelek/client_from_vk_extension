/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side_java.VKResponseClasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * @author haukot
 */
public class Dialog implements Serializable {
    @SerializedName("mid")
    public int mid;
    
    @SerializedName("date")
    public int date;
    
    @SerializedName("out")
    public int out;

    @SerializedName("uid")
    public int uid; //ради совместимости
    
    @SerializedName("user")
    public Person user;
    
    @SerializedName("read_state")
    public int readState;
    
    @SerializedName("body")
    public String body;
    
    @SerializedName("chat_id")
    public int chatId;
    
    @SerializedName("title")
    public String title;
    
    @SerializedName("admin_id")
    public String adminId;
    
    @SerializedName("chat_active")
    public String chatActive;
    
    @SerializedName("users_count")
    public int usersCount;
}
