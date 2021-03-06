package service.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.entity.Dialog;
import service.entity.Message;
import service.entity.Token;
import service.entity.User;
import service.objects.ErrorResponseObject;
import service.objects.JSONInputRequestMessage;
import service.objects.TimeStampResponse;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;
import service.speller.YandexSpellService;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/{auth_token}/messages/send/{dialog_id}")
public class SendMessageController extends BaseController {

    private static final Logger log = Logger.getLogger(SendMessageController.class.getName());

    /**
     * method which is responsible for sending messages
     *
     * @param auth_token              user's token
     * @param dialog_id               dialod who will receive
     * @param jsonInputRequestMessage message to sent
     * @return status
     */
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> sendMessage(
            @PathVariable String auth_token,
            @PathVariable String dialog_id,
            @RequestBody JSONInputRequestMessage jsonInputRequestMessage
    ) throws InterruptedException {
        //validate token
        Token token = tokenRepository.findByToken(auth_token);
        if (checkToken(auth_token, token)) {
            log.error("invalid token");
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);
        }
        //validate user
        User sender = userRepository.findByUsername(token.getUsername());
        if (checkUser(tokenRepository, auth_token, token, sender)) {
            log.error("Destination user does not exist");
            return new ResponseEntity<>(new ErrorResponseObject("Destination user does not exist"), HttpStatus.FORBIDDEN);
        }
        //current timestamp
        TimeStampResponse tsr = new TimeStampResponse(Instant.now().getEpochSecond() + "");
        //if receiver is dialog
        if (dialog_id.charAt(0) == '+') {
            log.info("message was sent into dialog");
            Dialog dialogFromDataBase;
            try {
                dialogFromDataBase = getDialogFromDataBase(auth_token, dialog_id);
            } catch (FileNotFoundException e) {
                log.error(e.toString());
                return new ResponseEntity<>(tsr, HttpStatus.FORBIDDEN);
            }
            Message message = new Message(
                    Instant.now().getEpochSecond(),
                    genereteGuid(messageRepository),
                    jsonInputRequestMessage.getType(),
                    jsonInputRequestMessage.getContent(),
                    sender.getUsername(),
                    dialogFromDataBase.getDialogId()
            );

            messageRepository.save(message);
            yandexSpellService.asyncTask(message);
            // sendMesaageToDialogMembers(message, dialogFromDataBase);
            //if receiver is person
        } else {
            log.info("message was sent to user");
            User receiver = userRepository.findByUsername(dialog_id);
            if (receiver == null) {
                log.error("no such receiver");
                return new ResponseEntity<>(new ErrorResponseObject("no such receiver"), HttpStatus.FORBIDDEN);
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
                yandexSpellService.asyncTask(message);
                log.debug("message with text" + message.getContent()
                        + " from " + sender.getUsername() +
                        " to " + receiver.getUsername() +
                        " was send");
            }
        }

        return new ResponseEntity<>(tsr, HttpStatus.OK);
    }


    /**
     * check some token for existence
     *
     * @param auth_token auth_token to check
     * @param token      token obj
     * @return status
     */
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

    @Autowired
    private YandexSpellService yandexSpellService;

    @Autowired
    public SendMessageController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }


}
