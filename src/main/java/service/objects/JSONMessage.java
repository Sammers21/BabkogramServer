package service.objects;


public class JSONMessage

{
    private String message;

    public JSONMessage() {
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getMessage() {

        return message;
    }

    public JSONMessage(String message) {

        this.message = message;
    }
}
