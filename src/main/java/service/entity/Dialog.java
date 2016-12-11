package service.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import service.repository.DialogRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Dialog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;
    private String dialogId;
    private String dialogName;
    private String owner;
    private String userList = "";

    public static Dialog generate(DialogRepository dialogRepository, User owner) {
        Dialog dialog = new Dialog();
        StringBuilder stringBuilder;

        do {
            stringBuilder = new StringBuilder("");
            String pattern = "ABCDEFGHIKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

            for (int i = 0; i < 31; i++) {
                stringBuilder.append(pattern.charAt(rnd.nextInt(pattern.length())));
            }

        } while (dialogRepository.findByDialogId("+" + stringBuilder.toString()) != null);
        dialog.setDialogId("+" + stringBuilder.toString());
        dialog.setOwner(owner.getUsername());
        dialog.setDialogName(dialog.getDialogId());
        dialogRepository.save(dialog);
        return dialog;
    }

    public boolean contains(String userName) {
        return userList.contains(userName);
    }

    public void addUser(String userName) {
        if (userList.equals("")) {
            userList += userName;
        } else if (!userList.contains(";")) {
            if (!userList.equals(userName)) {
                userList += ";" + userName;
            }
        } else {
            List<String> s2 = Arrays.stream(userList.split(";"))
                    .filter(s -> s.equals(userName))
                    .collect(Collectors.toList());
            if (s2.size() == 0) {
                userList += ";" + userName;
            }

        }
    }

    public void deleteUser(String userName) {
        if (userList.contains(userName)) {
            if (!userList.contains(";")) {
                userList = "";
            } else {
                List<String> s2 = Arrays.stream(userList.split(";"))
                        .filter(s -> !s.equals(userName))
                        .collect(Collectors.toList());
                if (s2.size() == 1) {
                    userList = s2.get(0);
                } else {
                    userList = s2.get(0);
                    for (int i = 1; i < s2.size(); i++) {
                        userList += ";" + s2.get(i);
                    }
                }
            }
        }

    }

    public List<String> getUserNameList() {
        if (userList.equals(""))
            return new ArrayList<String>();
        if (!userList.contains(";"))
            return Arrays.asList(userList);
        return Arrays.stream(userList.split(";")).collect(Collectors.toList());
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Dialog() {
    }

    public String getDialogName() {
        return dialogName;
    }

    public void setDialogName(String dialogName) {
        this.dialogName = dialogName;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    private static final Random rnd = new Random();
}
