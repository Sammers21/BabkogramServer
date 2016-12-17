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
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GetMessagesControllerTest extends BaseControllerForAllTests {
     @Autowired
    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository, MessageRepository messageRepository) {
        super.setup(mockMvc, userRepository, tokenRepository, dialogRepository, messageRepository);
    }

    @Test
    public void getCustomMessageAfterSomeTime() throws Exception {
        mockMvc.perform(get("/kek1/messages/ilia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].content", is("hello")))
                .andExpect(jsonPath("$.messages[0].sender", is("ilia")))
                .andExpect(jsonPath("$.messages[1].sender", is("ilia")))
                .andExpect(jsonPath("$.messages[2].sender", is("pavel")));
    }




    @Test
    public void getCustomLimitAndSkip() throws Exception {
        mockMvc.perform(get("/kek1/messages/ilia/limit/1/skip/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].content", is("helloAgain")))
                .andExpect(jsonPath("$.messages[0].sender", is("ilia")))
                .andDo(print());

    }


}