package service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.expression.ExpressionException;
import service.objects.JSONInputRequestMessage;
import service.repository.MessageRepository;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import static service.controllers.SendMessageController.genereteGuid;

@Entity
public class Message implements Comparable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    private String guid;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String content;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private String sender;


    @JsonIgnore
    private String toUsername;


    @Override
    public String toString() {
        return "{" +
                "guid='" + guid + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", toUsername='" + toUsername + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    private long timestamp;

    public Message(long timestamp, String guid, String type, String content, String sender, String toUsername) {
        this.guid = guid;
        this.type = type;
        this.content = content;
        this.sender = sender;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (getId() != message.getId()) return false;
        if (getTimestamp() != message.getTimestamp()) return false;
        if (getGuid() != null ? !getGuid().equals(message.getGuid()) : message.getGuid() != null) return false;
        if (getType() != null ? !getType().equals(message.getType()) : message.getType() != null) return false;
        if (getContent() != null ? !getContent().equals(message.getContent()) : message.getContent() != null)
            return false;
        if (getSender() != null ? !getSender().equals(message.getSender()) : message.getSender() != null) return false;
        return getToUsername() != null ? getToUsername().equals(message.getToUsername()) : message.getToUsername() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getGuid() != null ? getGuid().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        result = 31 * result + (getSender() != null ? getSender().hashCode() : 0);
        result = 31 * result + (getToUsername() != null ? getToUsername().hashCode() : 0);
        result = 31 * result + (int) (getTimestamp() ^ (getTimestamp() >>> 32));
        return result;
    }

    public Message() {

    }

    public long getId() {

        return id;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Message) {
            if (((Message) o).timestamp < this.timestamp)
                return 1;
            else if (((Message) o).timestamp == this.timestamp) {
                return 0;
            } else return -1;
        }
        throw new ExpressionException("invalid compare");
    }
}
