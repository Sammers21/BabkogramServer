package service.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;
    private String dialog_id;

    public Dialog(String dialog_id, List<String> userList) {
        this.dialog_id = dialog_id;
        this.userList = userList;
    }

    public String getDialog_id() {

        return dialog_id;
    }

    public void setDialog_id(String dialog_id) {
        this.dialog_id = dialog_id;
    }

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
