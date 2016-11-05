package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.entity.Message;
import service.entity.Token;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.objects.JSONInputRequestMessage;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/{auth_token}/messages/send/{dialog_id}")
public class SendMessageController {

    private static final Logger log = Logger.getLogger(SendMessageController.class.getName());

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private MessageRepository messageRepository;

    @Autowired
    public SendMessageController(UserRepository userRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.messageRepository = messageRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> sendMessage(@PathVariable String auth_token,
                                  @PathVariable String dialog_id,
                                  @RequestBody JSONInputRequestMessage jsonInputRequestMessage) {
        Token token = tokenRepository.findByToken(auth_token);
        if (checkToken(auth_token, token))
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);

        User sender = userRepository.findByUsername(token.getUsername());
        if (checkUser(tokenRepository, auth_token, token, sender))
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);

        if (dialog_id.charAt(0) == '+') {
            log.debug("deal with dialog");
            //TODO
            //DIALOG INIT
        } else {
            log.debug("deal with User");
            User receiver = userRepository.findByUsername(dialog_id);
            if (checkAndSend(jsonInputRequestMessage, sender, receiver))
                return new ResponseEntity<>(new ErrorResponseObject("no such receiver"), HttpStatus.FORBIDDEN);


        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public boolean checkAndSend(@RequestBody JSONInputRequestMessage jsonInputRequestMessage, User sender, User receiver) {
        if (receiver == null) {
            return true;
        } else {

            Message message = new Message(
                    Instant.now().getEpochSecond(),
                    genereteGuid(messageRepository),
                    jsonInputRequestMessage.getType(),
                    jsonInputRequestMessage.getContent(),
                    sender.getUsername(),
                    receiver.getUsername()
            );
            messageRepository.save(message);
            log.debug("message has been sent");
        }
        return false;
    }

    static boolean checkToken(String auth_token, Token token) {
        if (token == null) {
            log.info("invalid token " + auth_token);
            return true;
        }
        return false;
    }

    static boolean checkUser(TokenRepository tokenRepository, String auth_token, Token token, User sender) {
        if (sender == null) {
            log.info("no such users with token " + auth_token);
            log.debug("delete token " + auth_token);
            tokenRepository.delete(token);
            return true;
        }
        return false;
    }

    public static String genereteGuid(MessageRepository messageRepository) {
        UUID randomUUID;

        do {
            randomUUID = UUID.randomUUID();
        } while (messageRepository.findByGuid(randomUUID.toString()) != null);

        return randomUUID.toString();

    }


}
