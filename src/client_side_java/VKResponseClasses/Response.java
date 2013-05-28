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
public class Response <T> {
    @SerializedName("response")
    public T response;
    
    @SerializedName("error")
    public Object error;

    public Response(T resp){
        response = resp;
    }

    public Response() {
    }
}
