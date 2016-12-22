package service.controllers;

import org.apache.log4j.Logger;
import org.junit.Assert;
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
import service.BaseControllerForAllTests;
import service.entity.Dialog;
import service.objects.JSONInputRequestMessage;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SendMessageControllerTest extends BaseControllerForAllTests {
    private static final Logger log = Logger.getLogger(LogoutAllTokensController.class.getName());

    @Autowired
    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository, MessageRepository messageRepository) {
        super.setup(mockMvc, userRepository, tokenRepository, dialogRepository, messageRepository);
    }

    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Test
    public void basicMessageSend() throws Exception {
        mockMvc.perform(post("/bestToken20/messages/send/ilia2")
                .content(this.json(new JSONInputRequestMessage("text", "hello ilia2")))
                .contentType(contentType))
                .andExpect(status().isOk());
        assertTrue(messageRepository.findByContent("hello ilia2").size() == 1);
    }

    @Test
    public void notFoudTest() throws Exception {
        mockMvc.perform(get("/bestToken2/messages/send/ilia/somemethod"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void longMessageTest() throws Exception {
        StringBuilder bigText = new StringBuilder("");
        for (int i = 0; i < 100; i++) {
            bigText.append("veryVeryBigTExt");
        }
        mockMvc.perform(post("/bestToken20/messages/send/ilia2")
                .content(this.json(new JSONInputRequestMessage("text", bigText.toString())))
                .contentType(contentType))
                .andExpect(status().isOk());

        Thread.sleep(10000);


    }

    @Test
    public void tsetDialogSendingMessage() throws Exception {
        mockMvc.perform(get("/BATTOKEN5/conferences/create"))
                .andExpect(status().isOk());

        assertTrue(dialogRepository.findByOwner("BATYA5").size() == 1);
        List<Dialog> batyaConfs = dialogRepository.findByOwner("BATYA5");


        Thread.sleep(2000);

        mockMvc.perform(get("/BATTOKEN5/conferences/" + batyaConfs
                .get(0).getDialogId() + "/invite/" + "BATYA6"))
                .andExpect(status().isOk());

        batyaConfs = dialogRepository.findByOwner("BATYA5");
        assertTrue(batyaConfs.get(0).getUserNameList().contains("BATYA6"));

        Thread.sleep(2000);
        mockMvc.perform(post("/BATTOKEN5/messages/send/" + batyaConfs
                .get(0).getDialogId())
                .content(this.json(new JSONInputRequestMessage("text", "hello at all")))
                .contentType(contentType))
                .andExpect(status().isOk());


        Thread.sleep(2000);
        mockMvc.perform(get("/BATTOKEN6/messages/"+ batyaConfs.get(0).getDialogId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].content",is("invited|BATYA6")))
                .andExpect(jsonPath("$.messages[1].content",is("hello at all")));


        Thread.sleep(2000);

        mockMvc.perform(get("/BATTOKEN5/messages/"+ batyaConfs.get(0).getDialogId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages[0].content",is("created")))
                .andExpect(jsonPath("$.messages[1].content",is("invited|BATYA6")))
                .andExpect(jsonPath("$.messages[2].content",is("hello at all")))
                .andDo(print());



        mockMvc.perform(get("/BATTOKEN6/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dialogs[0].dialog_id",is(batyaConfs.get(0).getDialogId())))
                .andExpect(jsonPath("$.dialogs[0].last_message.content",is("hello at all")));
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