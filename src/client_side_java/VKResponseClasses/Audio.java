package client_side_java.VKResponseClasses;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 29.05.13
 * Time: 8:48
 * To change this template use File | Settings | File Templates.
 */
public class Audio {

    @SerializedName("aid")
    public int aid;

    @SerializedName("owner_id")
    public int ownerId;

    @SerializedName("duration")
    public int duration;

    @SerializedName("artist")
    public String artist;

    @SerializedName("title")
    public String title;

    @SerializedName("url")
    public String url;

    @SerializedName("performer")
    public String performer;

    @SerializedName("lyrics_id")
    public String lyricsId;



}
