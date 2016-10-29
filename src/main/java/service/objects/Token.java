package service.objects;

public class Token {

    private String token;

    public String getToken() {
        return token;
    }

    public Token() {
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }

    public Token(String token) {

        this.token = token;
    }
}
