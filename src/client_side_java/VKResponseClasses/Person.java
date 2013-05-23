/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side_java.VKResponseClasses;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author haukot
 */

public class Person {
    @SerializedName("uid")
    public int uid;
     
    @SerializedName("first_name")
    public String firstName;
    
    @SerializedName("last_name")
    public String lastName;
     
    @SerializedName("photo_50")
    public String photo50;     
    
    @SerializedName("online")
    public int online;  
    
    @SerializedName("invited_by")
    public String invitedBy;    // messages.getChat
    
    @SerializedName("lists")
    public List<Integer> lists;  
}




