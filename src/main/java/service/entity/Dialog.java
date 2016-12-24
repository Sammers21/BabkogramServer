package service.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import service.repository.DialogRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Dialog implements Serializable, Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;
    private String dialogId;
    private String dialogName;

    private String owner;

    @Column(columnDefinition = "TEXT")
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
        dialog.addUser(owner.getUsername());
        dialogRepository.save(dialog);
        return dialog;
    }

    public boolean contains(String userName) {
        return getUserNameList().stream()
                .filter(s -> s.equals(userName)).count() == 1;
    }

    public void addUser(String userName) {
        userList = add(userList, userName);
    }

    public void deleteUser(String userName) {
        userList = delete(userList, userName);
    }

    public List<String> getUserNameList() {
        return getList(userList);
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

    public String getOwner() {
        return owner;
    }
}
