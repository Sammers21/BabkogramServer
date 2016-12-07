package service.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.entity.Dialog;
import service.entity.Token;
import service.entity.User;
import service.repository.DialogRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConferenceControllerTest {
    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
        User user1 = new User("pavel", "123");
        userRepository.save(user1);
        Token token = new Token("kek1", "pavel");
        tokenRepository.save(token);
    }

    @Test
    public void createDialog() throws Exception {
        mockMvc.perform(get("/kek1/conferences/create"))
                .andExpect(status().isOk());
        List<Dialog> s = dialogRepository.findByOwner("pavel");
     //   int s2=s.get(0).getUserList().size();
        assertEquals(dialogRepository.findByOwner("pavel").size(), 1);

    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private DialogRepository dialogRepository;
}