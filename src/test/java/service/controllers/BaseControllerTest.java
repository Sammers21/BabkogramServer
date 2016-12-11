package service.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import service.repository.DialogRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

public class BaseControllerTest {

    private MockMvc mockMvc;

    private UserRepository userRepository;

    private TokenRepository tokenRepository;

    private DialogRepository dialogRepository;
}
