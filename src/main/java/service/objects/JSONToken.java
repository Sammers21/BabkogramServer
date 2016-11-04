package service.objects;


public class JSONToken {

    private String token;

    public JSONToken() {
    }

    public void setToken(String token) {

        this.token = token;
    }

    public String getToken() {

        return token;
    }

    @Override
    public String toString() {
        return "{" +
                "token='" + token + '\'' +
                '}';
    }

    public JSONToken(String token) {

        this.token = token;
    }
}
