package service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    private String guid;

    private String type;

    private String content;

    private String fromUsername;

    private String toUsername;

    public Message(String guid, String type, String content, String fromUsername, String toUsername) {
        this.guid = guid;
        this.type = type;
        this.content = content;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public void setId(long id) {

        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {

        return type;
    }

    public Message(String text) {
        this.type = text;
    }

    public String getText() {

        return type;
    }

    public Message() {

    }

    public long getId() {

        return id;
    }
}
