package client_side_java.VKResponseClasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 26.05.13
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
public class LongPollInfo {
    @SerializedName("key")
    public String key;

    @SerializedName("server")
    public String server;

    @SerializedName("ts")
    public String ts;
}
