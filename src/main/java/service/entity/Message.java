package service.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String text;

    public Message(String text) {
        this.text = text;
    }

    public String getText() {

        return text;
    }

    public Message() {

    }

    public long getId() {

        return id;
    }
}
