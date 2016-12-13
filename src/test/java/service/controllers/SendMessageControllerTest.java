package service.controllers;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.entity.Token;
import service.entity.User;
import service.objects.JSONInputRequestMessage;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SendMessageControllerTest {
    private static final Logger log = Logger.getLogger(LogoutAllTokensController.class.getName());


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MessageRepository messageRepository;


    @Autowired
    private MockMvc mvc;

    @Before
    public void clean() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();

        User user = new User("pavel", "bestprogrammer");
        User user2 = new User("ilia", "bestprogrammer");
        Token token = new Token("bestToken", "pavel");
        Token token1 = new Token("badToken", "ilia");


        userRepository.save(user);
        userRepository.save(user2);
        tokenRepository.save(token);
    }

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Test
    public void basicMessageSend() throws Exception {
        mvc.perform(post("/bestToken/messages/send/ilia")
                .content(this.json(new JSONInputRequestMessage("text", "hello ilia")))
                .contentType(contentType))
                .andExpect(status().isOk());
        assertTrue(messageRepository.findByContent("hello ilia").size() == 1);
    }

    @Test
    public void notFoudTest() throws Exception {
        mvc.perform(get("/bestToken/messages/send/ilia/somemethod"))
                .andExpect(status().isNotFound());
        assertTrue(messageRepository.findByContent("hello ilia").size() == 1);
    }

    @Test
    public void longMessageTest() throws Exception {
        StringBuilder bigText = new StringBuilder("");
        for (int i = 0; i < 1000000; i++) {
            bigText.append("veryVeryBigTExt");
        }
        mvc.perform(post("/bestToken/messages/send/ilia")
                .content(this.json(new JSONInputRequestMessage("text", bigText.toString())))
                .contentType(contentType))
                .andExpect(status().isOk());
    }


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters).filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }


    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}