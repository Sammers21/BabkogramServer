package service.controllers;

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
import service.objects.PairOfNewAndOldPassword;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;


import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChangePassWordControllerTest extends BaseControllerForAllTests {

    @Test
    public void changePassword() throws Exception {
        mockMvc.perform(post("/kek23").
                contentType(MediaType.APPLICATION_JSON)
                .content(json(new PairOfNewAndOldPassword("123", "321"))))
                .andExpect(status().isOk());
        assertTrue(userRepository.findByUsername("pavell").getPassword().equals("321"));

        mockMvc.perform(post("/kek23").
                contentType(MediaType.APPLICATION_JSON)
                .content(json(new PairOfNewAndOldPassword("123", "someNew"))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error",is("invalid password")));

        assertTrue(userRepository.findByUsername("pavell").getPassword().equals("321"));

        mockMvc.perform(post("/kek23").
                contentType(MediaType.APPLICATION_JSON)
                .content(json(new PairOfNewAndOldPassword("321", "123"))))
                .andExpect(status().isOk());

        assertTrue(userRepository.findByUsername("pavell").getPassword().equals("123"));

    }

    @Autowired
    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository, MessageRepository messageRepository) {
        super.setup(mockMvc, userRepository, tokenRepository, dialogRepository, messageRepository);
    }

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters).filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}