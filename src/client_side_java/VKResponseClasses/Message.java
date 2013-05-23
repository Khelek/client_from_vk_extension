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
public class Message {
    @SerializedName("body")
    public String body;
    
    @SerializedName("mid")
    public int mid;
    
    @SerializedName("uid")
    public int uid;
    
    @SerializedName("from_id")
    public int fromId;
    
    @SerializedName("date")
    public int date;
   
    @SerializedName("read_state")
    public int readState;
    
    @SerializedName("out")
    public int out;
}
