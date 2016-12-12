package service.objects;


import java.util.ArrayList;
import java.util.List;

public class ConferenceUserResponse {
    public ArrayList<UserInConferenceList> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserInConferenceList> users) {
        this.users = users;
    }

    public ConferenceUserResponse() {

    }

    private ArrayList<UserInConferenceList> users=new ArrayList<>();
}
