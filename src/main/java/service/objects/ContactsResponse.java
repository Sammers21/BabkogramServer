package service.objects;


import java.util.ArrayList;

public class ContactsResponse {

    public ContactsResponse() {
    }

    public ContactsResponse(ArrayList<DialogMessageInResponse> dialogs) {

        this.dialogs = dialogs;
    }

    public ArrayList<DialogMessageInResponse> getDialogs() {
        return dialogs;
    }

    public void setDialogs(ArrayList<DialogMessageInResponse> dialogs) {
        this.dialogs = dialogs;
    }

    private  ArrayList<DialogMessageInResponse> dialogs=new ArrayList<>();

    @Override
    public String toString() {
        return "{" +
                "dialogs=" + dialogs +
                '}';
    }
}
