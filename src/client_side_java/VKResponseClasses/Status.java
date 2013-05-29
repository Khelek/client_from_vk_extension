package client_side_java.VKResponseClasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 29.05.13
 * Time: 8:23
 * To change this template use File | Settings | File Templates.
 */
public class Status {
    @SerializedName("text")
    public String text;

    @SerializedName("audio")
    public Audio audio;

}
