package service.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.entity.Dialog;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.time.Instant;
import java.util.List;


import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConferenceControllerTest extends BaseControllerForAllTests {


    @Test
    public void inviteUser() throws Exception {
        mockMvc.perform(get("/BATTOKEN/conferences/create"))
                .andExpect(status().isOk());

        assertTrue(dialogRepository.findByOwner("BATYA").size() == 1);
        List<Dialog> batyaConfs = dialogRepository.findByOwner("BATYA");

        mockMvc.perform(get("/BATTOKEN/conferences/" + batyaConfs
                .get(0).getDialogId() + "/invite/" + "BATYAUNKOWN"))
                .andExpect(status().isForbidden());
        assertTrue(!batyaConfs.get(0).getUserNameList().contains("BATYAUNJOWN"));

        mockMvc.perform(get("/BATTOKEN/conferences/" + batyaConfs
                .get(0).getDialogId() + "/invite/" + "BATYA2"))
                .andExpect(status().isOk());
        batyaConfs = dialogRepository.findByOwner("BATYA");
        assertTrue(batyaConfs.get(0).getUserNameList().contains("BATYA2"));
    }

    @Test
    public void kickUser() throws Exception {
        mockMvc.perform(get("/BATTOKEN2/conferences/create"))
                .andExpect(status().isOk());

        assertTrue(dialogRepository.findByOwner("BATYA2").size() == 1);
        List<Dialog> batyaConfs = dialogRepository.findByOwner("BATYA2");

        mockMvc.perform(get("/BATTOKEN2/conferences/" + batyaConfs
                .get(0).getDialogId() + "/invite/" + "BATYA"))
                .andExpect(status().isOk());

        batyaConfs = dialogRepository.findByOwner("BATYA2");
        assertTrue(batyaConfs.get(0).getUserNameList().contains("BATYA"));

        mockMvc.perform(get("/BATTOKEN2/conferences/" + batyaConfs
                .get(0).getDialogId() + "/kick/" + "BATYA"))
                .andExpect(status().isOk());

        batyaConfs = dialogRepository.findByOwner("BATYA2");
        assertTrue(!batyaConfs.get(0).getUserNameList().contains("BATYA"));
    }

    @Test
    public void leave() throws Exception {
        mockMvc.perform(get("/BATTOKEN3/conferences/create"))
                .andExpect(status().isOk());

        assertTrue(dialogRepository.findByOwner("BATYA3").size() == 1);
        List<Dialog> batyaConfs = dialogRepository.findByOwner("BATYA3");

        mockMvc.perform(get("/BATTOKEN3/conferences/" + batyaConfs
                .get(0).getDialogId() + "/invite/" + "BATYA"))
                .andExpect(status().isOk());

        batyaConfs = dialogRepository.findByOwner("BATYA3");
        assertTrue(batyaConfs.get(0).getUserNameList().contains("BATYA"));

        mockMvc.perform(get("/BATTOKEN/conferences/" + batyaConfs
                .get(0).getDialogId() + "/leave"))
                .andExpect(status().isOk());

        batyaConfs = dialogRepository.findByOwner("BATYA3");
        assertTrue(!batyaConfs.get(0).getUserNameList().contains("BATYA"));
    }

    @Test
    public void user_list() throws Exception {
        mockMvc.perform(get("/BATTOKEN4/conferences/create"))
                .andExpect(status().isOk());

        assertTrue(dialogRepository.findByOwner("BATYA4").size() == 1);
        List<Dialog> batyaConfs = dialogRepository.findByOwner("BATYA4");

        mockMvc.perform(get("/BATTOKEN4/conferences/" + batyaConfs
                .get(0).getDialogId() + "/invite/" + "BATYA"))
                .andExpect(status().isOk());

        batyaConfs = dialogRepository.findByOwner("BATYA4");
        assertTrue(batyaConfs.get(0).getUserNameList().contains("BATYA"));

        mockMvc.perform(get("/BATTOKEN4/conferences/" + batyaConfs
                .get(0).getDialogId() + "/user_list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originator", is("BATYA4")))
                .andExpect(jsonPath("$.users[0].user_id", is("BATYA")))
                .andDo(print());
            }


    @Test
    public void createDialog() throws Exception {
        mockMvc.perform(get("/kek23/conferences/create"))
                .andExpect(status().isOk());

        assertTrue(dialogRepository.findByOwner("pavell").size() == 1);

    }

    @Autowired
    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository, MessageRepository messageRepository) {
        super.setup(mockMvc, userRepository, tokenRepository, dialogRepository, messageRepository);
    }


}