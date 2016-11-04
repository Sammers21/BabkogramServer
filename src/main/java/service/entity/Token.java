package service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import service.objects.JSONToken;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Random;

@Entity
public class Token {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String token;

    @JsonIgnore
    private String username;

    @JsonIgnore
    public String getUsername() {
        return username;
    }

    public JSONToken getJSONObject() {
        return new JSONToken(getToken());
    }

    public Token(String token, String username) {
        this.token = token;
        this.username = username;
    }

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

    private static final Random rnd = new Random();

    public static Token generate(TokenRepository tokenRepository, String username) {
        StringBuilder stringBuilder;

        do {
            stringBuilder = new StringBuilder("");
            String pattern = "ABCDEFGHIKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

            for (int i = 0; i < 32; i++) {
                stringBuilder.append(pattern.charAt(rnd.nextInt(pattern.length())));
            }

        } while (tokenRepository.findByToken(stringBuilder.toString()) != null);

        return new Token(stringBuilder.toString(), username);

    }
}
