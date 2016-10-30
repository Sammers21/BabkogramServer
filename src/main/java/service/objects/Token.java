package service.objects;

import service.repository.UserRepository;

import java.util.Random;

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

    private static final Random rnd = new Random();

    public static Token generate(UserRepository userRepository) {
        StringBuilder stringBuilder;

        do {
            stringBuilder = new StringBuilder("");
            String pattern = "ABCDEFGHIKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

            for (int i = 0; i < 32; i++) {
                stringBuilder.append(pattern.charAt(rnd.nextInt(pattern.length())));
            }

        } while (userRepository.findByToken(stringBuilder.toString()) != null);

        return new Token(stringBuilder.toString());

    }
}
