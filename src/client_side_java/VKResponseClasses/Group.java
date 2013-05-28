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
public class Group {
    @SerializedName("lid")
    public int lid;
     
    @SerializedName("name")
    public String name;

    @SerializedName("friends")
    public List<Person> friends;     
     
}
