package service.objects;


import java.util.ArrayList;
import java.util.List;

public class ConferenceUserResponse {
    public ConferenceUserResponse(String owner) {
        originator = owner;
    }

    public ArrayList<UserInConferenceList> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserInConferenceList> users) {
        this.users = users;
    }

    public ConferenceUserResponse() {

    }

    private ArrayList<UserInConferenceList> users = new ArrayList<>();

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    private String originator;
}
