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
    ) {
        Token token = tokenRepository.findByToken(auth_token);
        if (checkToken(auth_token, token)) {
            log.error("invalid token");
            return new ResponseEntity<>(new ErrorResponseObject("invalid token"), HttpStatus.FORBIDDEN);
        }
        User sender = userRepository.findByUsername(token.getUsername());
        if (checkUser(tokenRepository, auth_token, token, sender)) {
            log.error("Destination user does not exist");
            return new ResponseEntity<>(new ErrorResponseObject("Destination user does not exist"), HttpStatus.FORBIDDEN);
        }
        TimeStampResponse tsr = new TimeStampResponse(Instant.now().getEpochSecond() + "");
        if (dialog_id.charAt(0) == '+') {
            log.info("message was sent into dialog");
            Dialog dialogFromDataBase;
            try {
                dialogFromDataBase = getDialogFromDataBase(auth_token, dialog_id);
            } catch (IllegalArgumentException e) {
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
           // sendMesaageToDialogMembers(message, dialogFromDataBase);

        } else {
            log.info("message was sent to user");
            User receiver = userRepository.findByUsername(dialog_id);
            if (checkAndSend(jsonInputRequestMessage, sender, receiver)) {
                log.error("no such receiver");
                return new ResponseEntity<>(new ErrorResponseObject("no such receiver"), HttpStatus.FORBIDDEN);
            }


        }

        return new ResponseEntity<>(tsr, HttpStatus.OK);
    }


    /**
     * operation to send message
     *
     * @param jsonInputRequestMessage message to sent
     * @param sender                  user who send
     * @param receiver                user who receive
     * @return status
     */
    private boolean checkAndSend(
            JSONInputRequestMessage jsonInputRequestMessage,
            User sender,
            User receiver
    ) {
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
            log.debug("message with text" + message.getContent()
                    + " from " + sender.getUsername() +
                    " to " + receiver.getUsername() +
                    " was send");
        }
        return false;
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
    public SendMessageController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }


}
