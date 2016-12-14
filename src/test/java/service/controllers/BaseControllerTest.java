package service.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import service.repository.DialogRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

public class BaseControllerTest {

    protected MockMvc mockMvc;

    protected UserRepository userRepository;

    public BaseControllerTest() {
    }

    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository) {

        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.dialogRepository = dialogRepository;
    }

    protected TokenRepository tokenRepository;
    protected DialogRepository dialogRepository;
}
