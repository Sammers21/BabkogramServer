package service.objects;


public class TimeStampResponse {
    private String timestamp;

    public TimeStampResponse() {
    }

    public TimeStampResponse(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
