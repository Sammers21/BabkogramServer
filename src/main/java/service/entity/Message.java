package service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import service.objects.JSONInputRequestMessage;
import service.repository.MessageRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

import static service.controllers.SendMessageController.genereteGuid;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    private String guid;

    private String type;

    private String content;

    @JsonIgnore
    private String fromUsername;

    @JsonIgnore
    private String toUsername;


    @Override
    public String toString() {
        return "{" +
                "guid='" + guid + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", fromUsername='" + fromUsername + '\'' +
                ", toUsername='" + toUsername + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    private long timestamp;

    public Message(long timestamp, String guid, String type, String content, String fromUsername, String toUsername) {
        this.guid = guid;
        this.type = type;
        this.content = content;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.timestamp = timestamp;
    }

    public static Message getFromJSONinput(JSONInputRequestMessage js, String from, String to, MessageRepository messageRepository) {
        return new Message(
                Instant.now().getEpochSecond(),
                genereteGuid(messageRepository),
                js.getType(),
                js.getContent(),
                from,
                to
        );
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
