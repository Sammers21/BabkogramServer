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
import service.repository.DialogRepository;
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
public class GetContactsControllerTest extends BaseControllerForAllTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository, MessageRepository messageRepository) {
        super.setup(mockMvc, userRepository, tokenRepository, dialogRepository, messageRepository);
    }


    @Test
    public void getFromNoUser() throws Exception {

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