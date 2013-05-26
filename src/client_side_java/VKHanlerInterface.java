package client_side_java;

import client_side_java.VKResponseClasses.LoopMessage;

/**
 * Created with IntelliJ IDEA.
 * User: haukot
 * Date: 24.05.13
 * Time: 8:05
 * To change this template use File | Settings | File Templates.
 */
public interface VKHanlerInterface {
    public void onGetIncomingMessage(LoopMessage msg);
    public void onGetOutgoingMessage(LoopMessage msg);
    public void onTypingMessageInDialog(int userId);     //приходит примерно раз в пять секунд
    public void onTypingMessageInChat(int userId, int chatId);
    public void onMessageDelete(int messageId);

    //--------    нечто странное и не реализованное пока  --------------------
    public void onMessageFlagsReplace(int messageId);
    public void onMessageFlagsSet();
    public void onMessageFlagsReset();
    //-------------------------------------------------

    public void onChatEdit(int chatId, boolean selfEdit);

    public void onFriendOnline(int userId);
    public void onFriendOffline(int userId, boolean away );   //away - оффлайн по таймауту
    public void onCall(int userId, int chatId);      //не реализовано
}
