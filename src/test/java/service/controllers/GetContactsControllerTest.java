package service.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import service.entity.Message;
import service.entity.Token;
import service.entity.User;
import service.objects.JSONInputRequestMessage;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GetContactsControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        tokenRepository.deleteAll();
        userRepository.deleteAll();
        messageRepository.deleteAll();

        User user1 = new User("pavel", "123");
        User user2 = new User("ilia", "123");
        User user3 = new User("danil", "123");
        User user4 = new User("BigBoss", "123");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        Token token = new Token("kek1", "pavel");
        Token token2 = new Token("kek2", "ilia");
        Token token3 = new Token("kek3", "danil");
        Token token4 = new Token("kek4", "BigBoss");
        tokenRepository.save(token);
        tokenRepository.save(token2);
        tokenRepository.save(token3);
        tokenRepository.save(token4);
        Message helloFromDanil = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "danil",
                "pavel",
                messageRepository);
        Message helloFromIlia = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "ilia",
                "pavel",
                messageRepository);
        Thread.sleep(2000);
        Message helloFromIlia2 = Message.getFromJSONinput(new JSONInputRequestMessage("text", "helloAgain"),
                "ilia",
                "pavel",
                messageRepository);
        messageRepository.save(helloFromDanil);
        messageRepository.save(helloFromIlia);
        messageRepository.save(helloFromIlia2);
    }

    @Test
    public void getFromNoUser() throws Exception {
        Message helloFROMBOSS = Message.getFromJSONinput(new JSONInputRequestMessage("text", "helloDanil"),
                "BigBoss",
                "danil",
                messageRepository);
        messageRepository.save(helloFROMBOSS);
        mockMvc.perform(get("/kek4/contacts/offset/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dialogs[0].dialog_id", is("danil")))
                .andExpect(jsonPath("$.dialogs[0].last_message.content", is("helloDanil")));
    }


    @Test
    public void getCustomCountOfContacts() throws Exception {
        mockMvc.perform(get("/kek1/contacts/offset/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dialogs[0].dialog_id", is("danil")))
                .andExpect(jsonPath("$.dialogs[0].last_message.content", is("hello")));
    }

    @Test
    public void getDefaultCountOfContacts() throws Exception {
        mockMvc.perform(get("/kek1/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dialogs[0].dialog_id", is("ilia")))
                .andExpect(jsonPath("$.dialogs[1].dialog_id", is("danil")))
                .andExpect(jsonPath("$.dialogs[1].last_message.content", is("hello")))
                .andExpect(jsonPath("$.dialogs[0].last_message.content", is("helloAgain")));

    }


}