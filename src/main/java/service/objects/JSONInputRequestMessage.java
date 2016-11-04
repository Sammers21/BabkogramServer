package service.objects;


public class JSONInputRequestMessage {
    private String type;

    private String content;


    public void setType(String type) {

        this.type = type;
    }

    public String getType() {

        return type;
    }

    public String getContent() {
        return content;
    }

    public JSONInputRequestMessage() {

    }

    public JSONInputRequestMessage(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public void setContent(String content) {

        this.content = content;
    }
}
