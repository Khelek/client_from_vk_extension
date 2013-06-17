/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side_java.VKResponseClasses;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author haukot
 */
public class Messages {
    @SerializedName("body")
    public String body;
    
    @SerializedName("mid")
    public int mid;
    
    @SerializedName("uid")
    public Integer uid;
    
    @SerializedName("from_id")
    public int fromId;
    
    @SerializedName("date")
    public int date;
   
    @SerializedName("read_state")
    public int readState;
    
    @SerializedName("out")
    public Integer out;

    @SerializedName("title")
    public String title;

    @SerializedName("chat_id")
    public Integer chatId;

    //chat_active: [86030925, 55827129],
    //users_count: 3,
    //admin_id: 5127441
}
