package client_side_java.VKResponseClasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 29.05.13
 * Time: 7:07
 * To change this template use File | Settings | File Templates.
 */
public class Counters {
    @SerializedName("friends")
    public int friends;

    @SerializedName("messages")
    public int messages;

    @SerializedName("photos")
    public int photos;

    @SerializedName("videos")
    public int videos;

    @SerializedName("notes")
    public int notes;

    @SerializedName("gifts")
    public int gifts;

    @SerializedName("events")
    public int events;

    @SerializedName("groups")
    public int groups;
}
