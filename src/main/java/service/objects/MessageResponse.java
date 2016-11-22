package service.objects;


import java.util.ArrayList;
import java.util.List;

public class MessageResponse {



    public MessageResponse() {
    }

    public MessageResponse(List<MessageInResponse> messages) {
        this.messages = messages;
    }

    public List<MessageInResponse> getMessages() {

        return messages;
    }

    public void setMessages(List<MessageInResponse> messages) {
        this.messages = messages;
    }

    private List<MessageInResponse> messages=new ArrayList<>();


}
