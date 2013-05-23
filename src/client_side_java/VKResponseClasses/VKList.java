/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side_java.VKResponseClasses;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author haukot
 */
public class VKList<T> {
    
    public int countAllItems;
    
    public List<T> items;
    
    public VKList(int count, List<T> arr){
        countAllItems = count;
        items = arr;
    }
}
