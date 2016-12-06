package service.objects;

import service.entity.Message;


public class DialogMessageInResponse {

    private String dialog_id;

    private Message last_message;

    public DialogMessageInResponse() {
    }

    public DialogMessageInResponse(String dialog_id, Message last_message) {

        this.dialog_id = dialog_id;
        this.last_message = last_message;
    }

    public Message getLast_message() {

        return last_message;
    }

    @Override
    public String toString() {
        return "{" +
                "dialog_id='" + dialog_id + '\'' +
                ", last_message=" + last_message +
                '}';
    }

    public void setLast_message(Message last_message) {
        this.last_message = last_message;
    }

    public String getDialog_id() {

        return dialog_id;
    }

    public void setDialog_id(String dialog_id) {
        this.dialog_id = dialog_id;
    }
}
