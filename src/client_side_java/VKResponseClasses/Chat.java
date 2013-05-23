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
public class Chat {
    @SerializedName("type")
    public String type;
    
    @SerializedName("chat_id")
    public int chatId;
    
    @SerializedName("title")
    public String title;
    
    @SerializedName("admin_id")
    public String adminId;
    
    @SerializedName("users")
    public List<Person> users;
    
}
