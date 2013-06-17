package client_side_java.VKResponseClasses;

import client_side_java.GetResponseCallback;
import client_side_java.VK;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 24.05.13
 * Time: 8:39
 * To change this template use File | Settings | File Templates.
 */
public class LoopMessage {
    public int messageId;
    public int fromId;
    public int authorUid;
    public int chatId;
    public Date date;
    public String subject;
    public String body;
    public boolean out = false;               //исходящее
    public boolean readState = true;          //прочитано
    public boolean containsMedia = false;     //содержит медиаконтент
    public boolean verifyOnSpam = false;      //проверено пользователем на спам
    public boolean markedAsSpam = false;      //помечено как спам
    public boolean messageIsDeleted = false;  //сообщение удалено
    public boolean messageSendFriend = false; //сообщение послано другом
    public boolean messageSendByChat = false; //сообщение послано через чат
    public boolean markedMessage = false;     //помеченное сообщение
    public boolean thereAnswer = false;       //на сообщение создан ответ
    public List<Object> attachments;          //приложения(реализация когда-нибудь поменяется)

    public LoopMessage(JsonArray mess){
        Gson gson = new Gson();
        this.messageId = gson.fromJson(mess.get(1), int.class);
        int flags = gson.fromJson(mess.get(2), int.class);
        this.fromId = gson.fromJson(mess.get(3), int.class);
        int unixTime = gson.fromJson(mess.get(4), int.class) + 3600*4;  //часовая зона
        date = new Date((long)unixTime*1000);
        this.subject = gson.fromJson(mess.get(5), String.class);
        this.body = gson.fromJson(mess.get(6), String.class);
        Type responseType = new TypeToken<List<Object>>() {}.getType();
        if (mess.size() > 7) {
            this.attachments = gson.fromJson(mess.get(7), responseType);
        }
        processFlags(flags);
    }

    private void processFlags(int flag){ //HOW TO: переделать в цикл
        if (flag == 0) return;
        if (flag - 512 >= 0){
            this.containsMedia = true;
            processFlags(flag - 512);
        } else if (flag - 256 >= 0){
            this.verifyOnSpam = true;
            processFlags(flag - 256);
        } else if (flag - 128 >= 0){
            this.messageIsDeleted = true;
            processFlags(flag - 128);
        } else if (flag - 64 >= 0){
            this.markedAsSpam = true;
            processFlags(flag - 64);
        } else if (flag - 32 >= 0){
            this.messageSendFriend = true;
            processFlags(flag - 32);
        } else if (flag - 16 >= 0){
            this.messageSendByChat = true;
            processFlags(flag - 16);
        } else if (flag - 8 >= 0){
            this.markedMessage = true;
            processFlags(flag - 8);
        } else if (flag - 4 >= 0){
            this.thereAnswer = true;
            processFlags(flag - 4);
        } else if (flag - 2 >= 0){
            this.out = true;
            if (flag - 2 == 0){
                return;
            }  else  {
                processFlags(flag - 2);
            }
        } else {
            if (flag - 1 >= 0){
                readState = false;
                processFlags(flag - 1);
            }
            return;
        }
    }
}
