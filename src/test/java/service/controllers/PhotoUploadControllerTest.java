package service.controllers;

import org.codehaus.groovy.control.io.ReaderSource;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import service.ResourceStorage;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PhotoUploadControllerTest extends BaseControllerForAllTests {

    @Autowired
    private ResourceStorage storageService;

    @Test
    public void shouldListAllFiles() throws Exception {
        Stream<Path> pathStream = this.storageService.loadAll();

        List<Path> collect = pathStream.collect(Collectors.toList());
        assertTrue(collect.size() > 0);

        mockMvc.perform(get("/kek23/avatar/pavell"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void handleUserFileUpload() throws Exception {
        InputStream inputStream = new FileInputStream(new File("src/test/resources/rkmu1i.png"));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", inputStream);


        mockMvc.perform(fileUpload("/kek23/avatar").
                file(mockMultipartFile).
                contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }



    @Autowired
    public void setup(MockMvc mockMvc, UserRepository userRepository, TokenRepository tokenRepository, DialogRepository dialogRepository, MessageRepository messageRepository) {
        super.setup(mockMvc, userRepository, tokenRepository, dialogRepository, messageRepository);
    }
}