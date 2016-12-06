package service.entity;


import java.util.ArrayList;
import java.util.List;

public class Dialog {
    private List<String> userList = new ArrayList<>();

    public Dialog() {
    }

    public Dialog(List<String> userList) {

        this.userList = userList;
    }

    public List<String> getUserList() {

        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}
