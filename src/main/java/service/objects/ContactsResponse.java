package service.objects;


import java.util.ArrayList;

public class ContactsResponse {

    public ContactsResponse() {
    }

    public ContactsResponse(ArrayList<Dialogs> dialogs) {

        this.dialogs = dialogs;
    }

    public ArrayList<Dialogs> getDialogs() {
        return dialogs;
    }

    public void setDialogs(ArrayList<Dialogs> dialogs) {
        this.dialogs = dialogs;
    }

    private  ArrayList<Dialogs> dialogs=new ArrayList<>();

    @Override
    public String toString() {
        return "{" +
                "dialogs=" + dialogs +
                '}';
    }
}
