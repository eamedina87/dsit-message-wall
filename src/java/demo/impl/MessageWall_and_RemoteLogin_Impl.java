package demo.impl;

import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageWall_and_RemoteLogin_Impl implements RemoteLogin, MessageWall {

    private List<Message> messages;
    private Map<String, String> users_and_passwords = new HashMap<String, String>(){
        {
            put("erick", "erick");
            put("adolfo", "adolfo");
            put("jose","jose");
            put("luis","luis");
        }
        
    };
    

    public MessageWall_and_RemoteLogin_Impl() {
        messages = new ArrayList<Message>();
    }
    
    @Override
    public UserAccess connect(String usr, String passwd) {
        if (users_and_passwords.containsKey(usr) && users_and_passwords.get(usr).equals(passwd)){
            return new UserAccess_Impl(this, usr);
        }
        return null;
    }

    @Override
    public void put(String user, String msg) {
        Message mMessage = new Message_Impl(user, msg);
        messages.add(messages.size(),mMessage);
    }

    @Override
    public boolean delete(String user, int index) {
        return messages.remove(index)!=null;
    }

    @Override
    public Message getLast() {
        
        return messages==null||messages.isEmpty()?new Message_Impl("No user", "No message"):messages.get(messages.size()-1);
    }

    @Override
    public int getNumber() {
        return messages.size();
    }

    @Override
    public List<Message> getAllMessages() {
        return messages;
    }

    
}
