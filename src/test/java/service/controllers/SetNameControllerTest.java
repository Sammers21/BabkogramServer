package service.controllers;

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
import org.springframework.test.web.servlet.ResultMatcher;
import service.entity.Dialog;
import service.entity.User;
import service.objects.JSONInputNewName;

import service.repository.DialogRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SetNameControllerTest extends BaseControllerTest {

    @Test
    public void getNameOfUser() throws Exception {
        mockMvc.perform(get("/DanilsTOken/name/IliaGulkov"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dialog_name", is("IliaGulkov")));
    }

    @Test
    public void setNameOfUser() throws Exception {
        mockMvc.perform(post("/DanilsTOken/name/DanilKashin")
                .content(json(new JSONInputNewName("Danil Kashin")))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk());
        User danilKashin = userRepository.findByUsername("DanilKashin");

        assertTrue(userRepository.findByUsername("DanilKashin").getDisplayName().equals("Danil Kashin"));
    }

    @Test
    public void setNameOfDialog() throws Exception {
        Dialog iliaGulkovsDialog = dialogRepository.findByOwner("IliaGulkov").get(0);
        mockMvc.perform(post("/DanilsTOken/name/" + iliaGulkovsDialog.getDialogId())
                .content(json(new JSONInputNewName("New Name Of Dialog")))
                .contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk());

        assertTrue(dialogRepository.findByDialogId(iliaGulkovsDialog.getDialogId()).getDialogName().equals("New Name Of Dialog"));

        mockMvc.perform(get("/DanilsTOken/name/" + iliaGulkovsDialog.getDialogId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dialog_name", is("New Name Of Dialog")));

        mockMvc.perform(post("/DanilsTOken/name/IliaGulkov")
                .content(json(new JSONInputNewName("Ilias new name")))
                .contentType(contentType))
                .andExpect(status().isForbidden());
    }


    @Autowired
    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository) {
        super.setup(mockMvc, userRepository, tokenRepository, dialogRepository);
    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters).filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }


    private MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}