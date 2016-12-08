package service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    private String displayName;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "USER_DIALOG",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "DIALOG_ID", referencedColumnName = "id")

    )
    private Set<Dialog> dialogs = new HashSet<>();

    public Set<Dialog> getDialogs() {
        return dialogs;
    }

    public void setDialogs(Set<Dialog> dialogs) {
        this.dialogs = dialogs;
    }

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
        this.displayName = username;
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
