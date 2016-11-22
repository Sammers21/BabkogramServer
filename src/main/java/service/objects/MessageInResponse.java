package service.objects;

import service.entity.Message;

public class MessageInResponse{

    private String dialog_id;

    private Message message;

    public MessageInResponse(String dialog_id, Message message) {
        this.dialog_id = dialog_id;
        this.message = message;
    }

    public String getDialog_id() {

        return dialog_id;
    }

    public void setDialog_id(String dialog_id) {
        this.dialog_id = dialog_id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
