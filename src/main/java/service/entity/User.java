package service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "\"user\"")
public class User implements Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = ALL, mappedBy = "user")
    @ElementCollection(targetClass = TimeStatistics.class)
    private Set<TimeStatistics> statistics = new HashSet<TimeStatistics>();


    public Set<TimeStatistics> getStatistics() {
        return statistics;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "username")
    private String username;

    private String password;

    @Column(columnDefinition = "TEXT")
    private String dialogList = "";

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

    public boolean contains(String dialogName) {
        return getDialogList().stream()
                .filter(s -> s.equals(dialogName)).count() == 1;
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

    public void addTimeStatistics(TimeStatistics timeStatistics) {
        timeStatistics.setUser(this);
        statistics.add(timeStatistics);
    }

    public void addExist(TimeStatistics timeStatistics) {
        statistics = statistics.stream().filter(s -> !s.equals(timeStatistics)).collect(Collectors.toSet());
        statistics.add(timeStatistics);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
