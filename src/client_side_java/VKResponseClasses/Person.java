/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side_java.VKResponseClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Pair;
import client_side_java.GetResponseCallback;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
    private String photo50Url;
    
    @SerializedName("online")
    public int online;  
    
    @SerializedName("invited_by")
    public String invitedBy;    // messages.getChat
    
    @SerializedName("lists")
    public List<Integer> lists;

    public Bitmap photo50;

    /**
     *
     * @param callback  GetResponseCallback<Bitmap>
     * @throws IOException
     */
    public void getBitmapPhoto50(final GetResponseCallback callback) {
        new AsyncImagesLoader().execute(new GetResponseCallback<Bitmap>() {
            @Override
            public void callbackCall(Bitmap img) {
                //if (img == null) ;
                photo50 = img;
                callback.callbackCall(img);
            }
        }, photo50Url);
    }

    private class AsyncImagesLoader extends AsyncTask<Object, Void, Bitmap> {

        private GetResponseCallback callback;
        /**
         *
         * @param params [GetResponseCallback<Bitmap>, String url]
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params){
            Bitmap image = null;
            callback = (GetResponseCallback)params[0];
            String url = (String)params[1];
            try {
                image = getImageByUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap resImage) {
            callback.callbackCall(resImage);
        }

        private Bitmap getImageByUrl(String url) throws IOException,   MalformedURLException {
            Bitmap image = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            return image;
        }
}
}






