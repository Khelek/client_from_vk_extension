package client_side_java.VKResponseClasses;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 26.05.13
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */
public class LongPollUpdates {
    @SerializedName("ts")
    public String ts;

    @SerializedName("failed")
    public boolean failed;

    @SerializedName("updates")
    public List<JsonArray> updates;
}
