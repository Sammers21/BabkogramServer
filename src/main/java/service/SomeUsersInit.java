package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import service.entity.Dialog;
import service.entity.Message;
import service.entity.Token;
import service.entity.User;
import service.objects.JSONInputRequestMessage;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;
import sun.tools.jar.CommandLine;

import javax.annotation.PostConstruct;

@Component
@Profile("DEV")
public class SomeUsersInit {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private DialogRepository dialogRepository;

    public SomeUsersInit() {

    }

    @PostConstruct
    public void initTestSet() throws InterruptedException {
        User user = new User("pavel", "bestprogrammer");
        User user3 = new User("danil", "123");
        User user4 = new User("BigBoss", "123");
        User user2 = new User("ilia", "bestprogrammer");
        user2.setDisplayName("ILIAGULK");
        User user5 = new User("ilia2", "bestprogrammer");
        User user6 = new User("pavel2", "bestprogrammer");
        userRepository.save(user6);
        userRepository.save(user2);
        userRepository.save(user5);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user);
        Token token9 = new Token("bestToken20", "pavel2");
        Token token = new Token("bestToken", "pavel");
        Token token1 = new Token("badToken", "ilia");
        Token token2 = new Token("bestToken2", "pavel");
        Token token3 = new Token("bestToken3", "pavel");
        Token token4 = new Token("bestToken4", "pavel");
        Token token5 = new Token("kek1", "pavel");
        Token token6 = new Token("kek2", "ilia");
        Token token7 = new Token("kek3", "danil");
        Token token8 = new Token("kek4", "BigBoss");
        tokenRepository.save(token);
        tokenRepository.save(token9);
        tokenRepository.save(token1);
        tokenRepository.save(token2);
        tokenRepository.save(token3);
        tokenRepository.save(token4);
        tokenRepository.save(token5);
        tokenRepository.save(token6);
        tokenRepository.save(token7);
        tokenRepository.save(token8);
        Message helloFromDanil = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "danil",
                "pavel",
                messageRepository);
        Thread.sleep(1002);
        Message helloFromIlia = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "ilia",
                "pavel",
                messageRepository);
        Thread.sleep(1002);
        Message helloFromIlia2 = Message.getFromJSONinput(new JSONInputRequestMessage("text", "helloAgain"),
                "ilia",
                "pavel",
                messageRepository);
        Thread.sleep(1002);
        Message helloFromPavel = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "pavel",
                "ilia",
                messageRepository);
        Thread.sleep(1002);
        Message helloFROMBOSS = Message.getFromJSONinput(new JSONInputRequestMessage("text", "helloDanil"),
                "BigBoss",
                "danil",
                messageRepository);

        messageRepository.save(helloFromIlia2);
        messageRepository.save(helloFROMBOSS);
        messageRepository.save(helloFromDanil);
        messageRepository.save(helloFromIlia);
        messageRepository.save(helloFromPavel);

    }

    @PostConstruct
    public void initNewUsers() {


        User bat = new User("BATYA", "BATYATHEBESTMESSA");
        User bat2 = new User("BATYA2", "BATYATHEBESTMESSA");
        User bat3 = new User("BATYA3", "BATYATHEBESTMESSA");
        User bat4 = new User("BATYA4", "BATYATHEBESTMESSA");
        User bat5 = new User("BATYA5", "BATYATHEBESTMESSA");
        User bat6 = new User("BATYA6", "BATYATHEBESTMESSA");
        Token tokenBat = new Token("BATTOKEN", "BATYA");
        Token tokenBat2 = new Token("BATTOKEN2", "BATYA2");
        Token tokenBat3 = new Token("BATTOKEN3", "BATYA3");
        Token tokenBat4 = new Token("BATTOKEN4", "BATYA4");
        Token tokenBat5 = new Token("BATTOKEN5", "BATYA5");
        Token tokenBat6 = new Token("BATTOKEN6", "BATYA6");
        tokenRepository.save(tokenBat);
        tokenRepository.save(tokenBat2);
        tokenRepository.save(tokenBat4);
        tokenRepository.save(tokenBat6);
        tokenRepository.save(tokenBat5);
        userRepository.save(bat6);
        userRepository.save(bat5);
        userRepository.save(bat2);
        userRepository.save(bat);
        userRepository.save(bat4);
        tokenRepository.save(tokenBat3);
        userRepository.save(bat3);

        User user1 = new User("pavell", "123");
        Token token2 = new Token("kek23", "pavell");
        User d = new User("DanilKashin", "1234567890");
        User i = new User("IliaGulkov", "1234567890");
        Message helloFromDanil = Message.getFromJSONinput(new JSONInputRequestMessage("text", "hello"),
                "DanilKashin",
                "IliaGulkov",
                messageRepository);
        Dialog dialog = Dialog.generate(dialogRepository, i);
        i.addDialog(dialog.getDialogId());
        d.addDialog(dialog.getDialogId());
        dialog.addUser(d.getUsername());
        Message helloTOCONF = Message.getFromJSONinput(new JSONInputRequestMessage("text", "helloDanil"),
                "IlilaGulkov",
                dialog.getDialogId(),
                messageRepository);
        Token token = new Token("DanilsTOken", "DanilKashin");
        tokenRepository.save(token);
        userRepository.save(d);
        userRepository.save(i);
        messageRepository.save(helloFromDanil);
        messageRepository.save(helloTOCONF);
        tokenRepository.save(token2);
        userRepository.save(user1);
        dialogRepository.save(dialog);
    }
}
