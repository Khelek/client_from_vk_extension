package client_side_java;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 24.05.13
 * Time: 0:33
 * To change this template use File | Settings | File Templates.
 */
public interface GetResponseCallback<T> {
    void callbackCall(T data);
}
