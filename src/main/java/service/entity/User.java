package service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class User implements Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;

    private String password;

    private String dialogList = "";

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonIgnore
    private String displayName;

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        setDisplayName(username);
    }


    public boolean contains(String userName) {
        return dialogList.contains(userName);
    }

    public void addDialog(String dialog) {
        dialogList = add(dialogList, dialog);
    }

    public void deleteDialog(String dialog) {
        dialogList = delete(dialogList, dialog);
    }

    public List<String> getDialogList() {
        return getList(dialogList);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
