package client_side_java;

import android.os.AsyncTask;
import client_side_java.VKResponseClasses.LoopMessage;
import client_side_java.VKResponseClasses.Message;
import client_side_java.VKResponseClasses.Response;
import com.example.android_java.MyActivity;
import com.google.gson.JsonArray;

import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 24.05.13
 * Time: 2:50
 * To change this template use File | Settings | File Templates.
 */
public class LongPollLooper extends AsyncTask<Object, Object, Object> {
    VKHanlerInterface handler;

    @Override
    protected Response doInBackground(Object... args){
        publishProgress(args);
        return null;
    }

    @Override
    protected void onProgressUpdate(Object ... progress) {
        handler = (VKHanlerInterface)progress[1];
    }

    @Override
    protected void onPostExecute(Object result) {

    }

   /* public void processingLongPoll(data){
        try{
            for (index in data.updates){        //JsonArray
                var update = data.updates[index];
                switch(update[0]){
                    case 0:
                        //удаление сообщения с указанным local_id
                        break;
                    case 1:
                        //замена флагов сообщения
                        break;
                    case 2:
                        //установка флагов сообщения
                        break;
                    case 3:
                        //сброс флагов сообщения
                        break;
                    case 4:
                        messageHandler(update);
                        //добавление нового сообщения
                        break;
                    case 8:
                        freindStatusOnline(update);
                        //друг $user_id стал онлайн
                        break;
                    case 9:
                        freindStatusOffline(update);
                        //друг $user_id стал оффлайн
                        break;
                    case 51:
                        //изменен параметр беседы $chat_id
                        break;
                    case 61:
                        writtingHandler(update);
                        //набор текста в диалоге
                        break;
                    case 62:
                        writtingHandler(update);
                        //набор текста в беседе(chat_id)
                        break;
                    case 70:
                        //звонок
                        break;
                }
            }
        }catch (Exception ex){
        }
    }

    public void messageHandler(JsonArray mess){
        LoopMessage loopMessage = new LoopMessage(mess);
       if (loopMessage.out) {
           handler.onGetOutgoingMessage(loopMessage);
       }   else {
           handler.onGetIncomingMessage(loopMessage);
       }
    }
             */

}


