package service.entity;


import javax.persistence.*;

@Entity
public class TimeStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)


    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TimeStatistics() {

    }
}
