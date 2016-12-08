package service.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import service.repository.DialogRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity
public class Dialog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;
    private String dialogId;
    private String dialogName;
    private String owner;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "dialogs", cascade = CascadeType.ALL)
    private Set<User> userList = new HashSet<>();

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
        dialog.getUserList().add(owner);
        dialogRepository.save(dialog);
        owner.getDialogs().add(dialog);
        return dialog;
    }

    public Dialog(String owner) {
        this.owner = owner;
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

    public Set<User> getUserList() {
        return userList;
    }

    public void setUserList(Set<User> userList) {
        this.userList = userList;
    }

    public Dialog(String dialogId, Set<User> userList) {

        this.dialogId = dialogId;
        this.userList = userList;
    }

    private static final Random rnd = new Random();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dialog)) return false;

        Dialog dialog = (Dialog) o;

        if (!getDialogId().equals(dialog.getDialogId())) return false;
        return owner.equals(dialog.owner);

    }

    @Override
    public int hashCode() {
        int result = getDialogId().hashCode();
        result = 31 * result + owner.hashCode();
        return result;
    }
}
