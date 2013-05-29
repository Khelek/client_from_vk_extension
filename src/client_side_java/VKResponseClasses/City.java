package client_side_java.VKResponseClasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 29.05.13
 * Time: 6:39
 * To change this template use File | Settings | File Templates.
 */
public class City {
    @SerializedName("cid")
    public int cid;

    @SerializedName("name")
    public String name;
}
