package service.objects;


import service.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageResponse {


    private List<Message> messages = new ArrayList<>();

    public MessageResponse() {
    }

    public MessageResponse(List<Message> messages) {

        this.messages = messages;
    }

    public List<Message> getMessages() {

        return messages;
    }

    public void setMessages(List<Message> messages) {

        this.messages = messages;
    }
}
