package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import service.entity.Message;
import service.entity.User;
import service.objects.JSONInputRequestMessage;
import service.repository.MessageRepository;
import service.repository.UserRepository;
import sun.tools.jar.CommandLine;

import javax.annotation.PostConstruct;

@Component
public class SomeUsersInit {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public SomeUsersInit() {

    }
    @PostConstruct
    public void initNewUsers(){
        User d=new User("DanilKashin","1234567890");
        User i=new User("IliaGulkov","1234567890");
        Message helloFromDanil = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "DanilKashin",
                "IliaGulkov",
                messageRepository);
        userRepository.save(d);
        userRepository.save(i);
        messageRepository.save(helloFromDanil);
    }
}
