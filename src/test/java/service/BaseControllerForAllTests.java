package service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.nio.charset.Charset;

public class BaseControllerForAllTests {

    protected MockMvc mockMvc;

    protected UserRepository userRepository;
    protected MessageRepository messageRepository;

    public BaseControllerForAllTests() {
    }

    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository, MessageRepository messageRepository) {

        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.dialogRepository = dialogRepository;
        this.messageRepository = messageRepository;
    }

    protected TokenRepository tokenRepository;
    protected DialogRepository dialogRepository;

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

}
