package service.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.entity.Message;
import service.entity.Token;
import service.entity.User;
import service.objects.JSONInputRequestMessage;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GetMessagesControllerTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
        messageRepository.deleteAll();
        User user1 = new User("pavel", "123");
        User user2 = new User("ilia", "123");
        User user3 = new User("danil", "123");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        Token token = new Token("kek1", "pavel");
        Token token2 = new Token("kek2", "ilia");
        Token token3 = new Token("kek3", "danil");
        tokenRepository.save(token);
        tokenRepository.save(token2);
        tokenRepository.save(token3);
        Message helloFromDanil = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "danil",
                "pavel",
                messageRepository);
        Thread.sleep(1000);
        Message helloFromIlia = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "ilia",
                "pavel",
                messageRepository);
        Thread.sleep(1000);
        Message helloFromPavel = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "pavel",
                "ilia",
                messageRepository);
        Thread.sleep(1000);
        messageRepository.save(helloFromDanil);
        messageRepository.save(helloFromIlia);
        messageRepository.save(helloFromPavel);
    }

    @Test
    public void getCustomMessageAfterSomeTime() throws Exception {

        mockMvc.perform(get("/kek1/messages/ilia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].content", is("hello")))
                .andExpect(jsonPath("$.messages[0].sender", is("ilia")))
                .andExpect(jsonPath("$.messages[1].sender", is("pavel")));
    }




    @Test
    public void getCustomLimitAndSkip() throws Exception {
        mockMvc.perform(get("/kek1/messages/ilia/limit/1/skip/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].content", is("hello")))
                .andExpect(jsonPath("$.messages[0].sender", is("ilia")));

    }


}